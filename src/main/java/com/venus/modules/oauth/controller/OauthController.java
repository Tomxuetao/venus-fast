package com.venus.modules.oauth.controller;

import com.venus.common.constant.Constant;
import com.venus.common.exception.VenusException;
import com.venus.common.utils.HttpContextUtils;
import com.venus.common.utils.Result;
import com.venus.common.validator.ValidatorUtils;
import com.venus.common.validator.group.DefaultGroup;
import com.venus.modules.login.entity.SysUserTokenEntity;
import com.venus.modules.login.service.ShiroService;
import com.venus.modules.oauth.dto.AuthTokenDTO;
import com.venus.modules.oauth.dto.GenerateAuthTokenDTO;
import com.venus.modules.oauth.entity.AuthCodeEntity;
import com.venus.modules.oauth.entity.AuthTokenEntity;
import com.venus.modules.oauth.service.AuthCodeService;
import com.venus.modules.oauth.service.AuthTokenService;
import com.venus.modules.sys.entity.SysClientEntity;
import com.venus.modules.sys.entity.SysUserEntity;
import com.venus.modules.sys.service.SysClientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/open-api/oauth")
@Api(tags = "Oauth2")
public class OauthController {
    @Autowired
    private ShiroService shiroService;

    @Autowired
    private AuthCodeService authCodeService;

    @Autowired
    private AuthTokenService authTokenService;

    @Autowired
    private SysClientService sysClientService;


    @GetMapping("authorize")
    @ApiOperation("授权端点")
    @ApiImplicitParams({@ApiImplicitParam(name = "client_id", value = "客户端ID", paramType = "query", dataType = "String", dataTypeClass = String.class), @ApiImplicitParam(name = "redirect_uri", value = "回调地址", paramType = "query", dataType = "String", dataTypeClass = String.class), @ApiImplicitParam(name = "response_type", value = "响应类型", paramType = "query", dataType = "String", dataTypeClass = String.class), @ApiImplicitParam(name = Constant.TOKEN_HEADER, value = "验证登录用户", paramType = "query", dataType = "String", dataTypeClass = String.class)})
    public void authorize(@ApiIgnore @RequestParam Map<String, String> dataForm, HttpServletResponse response, HttpServletRequest request) throws IOException {
        String token = HttpContextUtils.getRequestToken(request);
        String oauth2LoginUrl = "http://127.0.0.1:5173/venus-admin/login" + "?redirect_uri=" + dataForm.get("redirect_uri") + "&client_id=" + dataForm.get("client_id");
        if(StringUtils.isBlank(token)) {
            response.sendRedirect(oauth2LoginUrl);
        } else {
            SysUserTokenEntity userTokenEntity = shiroService.getByToken(token);
            if(userTokenEntity == null) {
                response.sendRedirect(oauth2LoginUrl);
            } else {
                String code = authCodeService.generateCode(userTokenEntity.getUserId(), dataForm.get("client_id"));
                response.sendRedirect(dataForm.get("redirect_uri") + "?code=" + code);
            }
        }
    }

    @PostMapping("access_token")
    @ApiOperation("令牌端点")
    public Result<AuthTokenDTO> token(@RequestBody GenerateAuthTokenDTO dto) {
        // 校验参数
        ValidatorUtils.validateEntity(dto, DefaultGroup.class);

        // 生成令牌
        AuthTokenEntity authTokenEntity = createAccessToken(dto.getCode(), dto.getClientId(), dto.getClientSecret());

        AuthTokenDTO authTokenDTO = new AuthTokenDTO();
        authTokenDTO.setAccessToken(authTokenEntity.getToken());
        authTokenDTO.setExpireDate(authTokenEntity.getExpireDate());
        authTokenDTO.setRefreshToken(authTokenEntity.getRefreshToken());
        return new Result<AuthTokenDTO>().ok(authTokenDTO);
    }

    @PostMapping("access_token2")
    @ApiOperation("令牌端点")
    public ResponseEntity token2(@RequestParam("code") String code, @RequestParam("redirect_uri") String redirectUri, @RequestParam("client_id") String clientId, @RequestParam("client_secret") String clientSecret) {
        // 生成令牌
        AuthTokenEntity authTokenEntity = createAccessToken(code, clientId, clientSecret);

        Map<String, String> result = new HashMap<>();
        result.put("scope", "user");
        result.put("token_type", "bearer");
        result.put("access_token", authTokenEntity.getToken());
        result.put("refresh_token", authTokenEntity.getRefreshToken());
        result.put("expires_in", authTokenEntity.getExpireDate().getTime() / 1000 + "");
        return ResponseEntity.ok(result);
    }

    @GetMapping("user")
    @ApiOperation("用户信息")
    public Result<SysUserEntity> user(HttpServletRequest request) {
        return new Result<SysUserEntity>().ok(getUser(request));
    }

    @GetMapping("user2")
    @ApiOperation("用户信息")
    public ResponseEntity user2(HttpServletRequest request) {
        SysUserEntity user = getUser(request);
        Map<String, String> result = new HashMap<>();
        result.put("type", "Admin");
        result.put("login", user.getUsername());
        result.put("id", user.getId().toString());
        return ResponseEntity.ok(result);
    }

    @GetMapping("check_token")
    @ApiOperation("检查令牌")
    public void checkToken() {

    }

    @GetMapping("logout")
    @ApiOperation("退出")
    public Result<String> logout(HttpServletRequest request) {
        String token = HttpContextUtils.getRequestToken(request);
        if(StringUtils.isBlank(token)) {
            return new Result<String>().error("退出失败，token为空");
        }
        authTokenService.logout(token);
        return new Result<String>().ok("退出成功");
    }

    /**
     * http请求获取用户信息
     * @param request - 请求对象
     */
    private SysUserEntity getUser(HttpServletRequest request) {
        String token = HttpContextUtils.getRequestToken(request);

        if(StringUtils.isBlank(token)) {
            throw new VenusException("token不能为空");
        }
        token = StringUtils.replace(token, "Bearer ", "");

        AuthTokenEntity authTokenEntity = authTokenService.validateToken(token);
        if(authTokenEntity == null) {
            throw new VenusException("token验证失败，请检查token");
        }
        SysUserEntity user = shiroService.getUser(authTokenEntity.getUserId());
        if(user == null) {
            throw new VenusException("token验证失败，请检查token");
        } else if(user.getStatus() == Constant.UserStatus.DISABLE.getValue()) {
            throw new VenusException("账号已被禁用，请联系管理员");
        }
        return user;
    }

    /**
     * 生成令牌
     * @param code         - 授权码
     * @param clientId     - 客户端ID
     * @param clientSecret - 客户端密钥
     */
    private AuthTokenEntity createAccessToken(String code, String clientId, String clientSecret) {
        // 验证授权码
        AuthCodeEntity authCodeEntity = authCodeService.validateCode(code, clientId);
        // 验证客户端
        SysClientEntity sysClientEntity = sysClientService.validateClient(clientId, clientSecret);
        if(sysClientEntity == null) {
            throw new VenusException("客户端验证失败，请检查client_id或者client_secret");
        }
        if(authCodeEntity == null) {
            throw new VenusException("授权码验证失败，请检查code");
        }
        if(authCodeEntity.getExpireDate().getTime() < System.currentTimeMillis()) {
            throw new VenusException("授权码已过期，请重新获取");
        }
        // 生成令牌
        return authTokenService.generateToken(authCodeEntity.getUserId(), clientId);
    }
}
