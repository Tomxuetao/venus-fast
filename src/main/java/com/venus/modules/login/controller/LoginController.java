package com.venus.modules.login.controller;

import com.venus.common.constant.Constant;
import com.venus.common.exception.ErrorCode;
import com.venus.common.exception.VenusException;
import com.venus.common.utils.IpUtils;
import com.venus.common.utils.Result;
import com.venus.common.utils.SpringContextUtils;
import com.venus.common.utils.StrUtils;
import com.venus.common.validator.AssertUtils;
import com.venus.common.validator.ValidatorUtils;
import com.venus.modules.log.entity.SysLogLoginEntity;
import com.venus.modules.log.enums.LoginOperateEnum;
import com.venus.modules.log.enums.LoginStatusEnum;
import com.venus.modules.log.service.SysLogLoginService;
import com.venus.modules.login.dto.CodeDTO;
import com.venus.modules.login.dto.LoginDTO;
import com.venus.modules.login.entity.SysUserTokenEntity;
import com.venus.modules.login.password.PasswordUtils;
import com.venus.modules.login.service.CaptchaService;
import com.venus.modules.login.service.ShiroService;
import com.venus.modules.login.service.SysUserTokenService;
import com.venus.modules.login.user.SecurityUser;
import com.venus.modules.login.user.UserDetail;
import com.venus.modules.msg.cloud.MailMsgFactory;
import com.venus.modules.msg.entity.SysMsgMailEntity;
import com.venus.modules.msg.enums.SendTypeEnum;
import com.venus.modules.msg.service.SysMsgMailService;
import com.venus.modules.sys.dto.SysUserDTO;
import com.venus.modules.sys.enums.LoginTypeEnum;
import com.venus.modules.sys.enums.UserStatusEnum;
import com.venus.modules.sys.service.SysOnlineService;
import com.venus.modules.sys.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@RestController
@Api(tags = "登录管理")
public class LoginController {
    @Autowired
    private ShiroService shiroService;
    @Autowired
    private CaptchaService captchaService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysMsgMailService sysMsgMailService;
    @Autowired
    private SysLogLoginService sysLogLoginService;
    @Autowired
    private SysUserTokenService sysUserTokenService;

    @GetMapping("captcha")
    @ApiOperation(value = "验证码", produces = "application/octet-stream")
    @ApiImplicitParam(paramType = "query", dataType = "string", name = "uuid", required = true, dataTypeClass = String.class)
    public void captcha(HttpServletResponse response, String uuid) throws IOException {
        //uuid不能为空
        AssertUtils.isBlank(uuid, ErrorCode.IDENTIFIER_NOT_NULL);

        //生成验证码
        captchaService.create(response, uuid);
    }

    /**
     * 保存登录日志
     */
    private void saveLoginLog(HttpServletRequest request, SysUserDTO user, LoginDTO login, int loginType) {
        SysLogLoginEntity log = new SysLogLoginEntity();
        log.setOperation(LoginOperateEnum.LOGIN.value());
        log.setCreateDate(new Date());
        log.setIp(IpUtils.getIpAddr(request));
        log.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
        log.setIp(IpUtils.getIpAddr(request));

        //用户不存在
        if (user == null) {
            log.setStatus(LoginStatusEnum.FAIL.value());
            log.setCreatorName(login.getAccount());
            sysLogLoginService.save(log);

            throw new VenusException(ErrorCode.ACCOUNT_PASSWORD_ERROR);
        }

        if (loginType == LoginTypeEnum.USERNAME.value()) {
            //密码错误
            if (!PasswordUtils.matches(login.getSecretKey(), user.getPassword())) {
                log.setStatus(LoginStatusEnum.FAIL.value());
                log.setCreator(user.getId());
                log.setCreatorName(user.getUsername());
                sysLogLoginService.save(log);

                throw new VenusException(ErrorCode.ACCOUNT_PASSWORD_ERROR);
            }
        }

        //账号停用
        if (user.getStatus() == UserStatusEnum.DISABLE.value()) {
            log.setStatus(LoginStatusEnum.LOCK.value());
            log.setCreator(user.getId());
            log.setCreatorName(user.getUsername());
            sysLogLoginService.save(log);

            throw new VenusException(ErrorCode.ACCOUNT_DISABLE);
        }

        //登录成功
        log.setStatus(LoginStatusEnum.SUCCESS.value());
        log.setCreator(user.getId());
        log.setCreatorName(user.getUsername());
        sysLogLoginService.save(log);
    }

    @PostMapping("login")
    @ApiOperation(value = "登录")
    public Result login(HttpServletRequest request, @RequestBody LoginDTO login) {
        //效验数据
        ValidatorUtils.validateEntity(login);

        //验证码是否正确
        boolean flag = captchaService.validate(login.getUuid(), login.getCaptcha());
        if (!flag) {
            return new Result().error(ErrorCode.CAPTCHA_ERROR);
        }

        //用户信息
        SysUserDTO user = sysUserService.getByUsername(login.getAccount());

        this.saveLoginLog(request, user, login, LoginTypeEnum.USERNAME.value());
        return sysUserTokenService.createToken(user.getId());
    }

    @PostMapping("codeLogin")
    @ApiOperation(value = "验证码登录")
    public Result codeLogin(HttpServletRequest request, @RequestBody LoginDTO login) {
        //效验数据
        ValidatorUtils.validateEntity(login);

        //图片验证码是否正确
        boolean flag = captchaService.validate(login.getUuid(), login.getCaptcha());
        if (!flag) {
            return new Result().error(ErrorCode.CAPTCHA_ERROR);
        }

        String uuidKey = login.getAccount() + ":" + login.getCaptcha() + ":" + login.getUuid();
        // 短信或邮件验证码是否正确
        boolean codeFlag = captchaService.validate(uuidKey, login.getSecretKey());
        if (!codeFlag) {
            return new Result().error(login.getAccount().contains("@") ? ErrorCode.EMAIL_CODE_ERROR : ErrorCode.SMS_CODE_ERROR);
        }

        // 用户信息
        SysUserDTO user = sysUserService.getByAccount(login.getAccount());
        this.saveLoginLog(request, user, login, LoginTypeEnum.Account.value());

        return sysUserTokenService.createToken(user.getId());
    }

    @PostMapping("code")
    @ApiOperation(value = "获取登录验证码")
    public Result msgCode(HttpServletRequest request, @RequestBody CodeDTO login) {
        //效验数据
        ValidatorUtils.validateEntity(login);
        //验证码是否正确
        boolean flag = captchaService.validate(login.getUuid(), login.getCaptcha());

        if (!flag) {
            return new Result().error(ErrorCode.CAPTCHA_ERROR);
        }

        // 重新设置验证码，登录时会再次使用，所以需要重新设置
        captchaService.setCache(login.getUuid(), login.getCaptcha());

        String code = StrUtils.randomCode(6);
        SysMsgMailEntity sysMsgMailEntity = new SysMsgMailEntity();
        sysMsgMailEntity.setRecipient(login.getAccount());
        sysMsgMailEntity.setCreatorName("系统发送");
        sysMsgMailEntity.setCreator(1L);
        sysMsgMailEntity.setContent("您的登录验证码为：" + code);
        sysMsgMailEntity.setSubject("登录验证码");
        sysMsgMailEntity.setTempId(1L);
        // 发送邮件/短线验证码
        try {
            Objects.requireNonNull(MailMsgFactory.build()).send(login.getAccount(), code, 1L);
            sysMsgMailEntity.setStatus(SendTypeEnum.SUCCESS.value());
        } catch (ExecutionException | InterruptedException e) {
            sysMsgMailEntity.setStatus(SendTypeEnum.FAIL.value());
            throw new RuntimeException(e);
        }

        String uuidKey = login.getAccount() + ":" + login.getCaptcha() + ":" + login.getUuid();
        captchaService.setCache(uuidKey, code);
        sysMsgMailService.save(sysMsgMailEntity);
        return new Result();
    }

    @PostMapping("logout")
    @ApiOperation(value = "退出")
    public Result logout(HttpServletRequest request) {
        UserDetail user = SecurityUser.getUser();

        Long userId = user.getId();
        //退出
        sysUserTokenService.logout(userId);

        //用户信息
        SysLogLoginEntity log = new SysLogLoginEntity();
        log.setOperation(LoginOperateEnum.LOGOUT.value());
        log.setIp(IpUtils.getIpAddr(request));
        log.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
        log.setIp(IpUtils.getIpAddr(request));
        log.setStatus(LoginStatusEnum.SUCCESS.value());
        log.setCreator(userId);
        log.setCreatorName(user.getUsername());
        log.setCreateDate(new Date());
        sysLogLoginService.save(log);

        SysOnlineService sysOnlineService = SpringContextUtils.getBean(SysOnlineService.class);
        sysOnlineService.removeUserCache(userId);

        return new Result();
    }

    @PostMapping("checkToken")
    @ApiOperation(value = "检验Token")
    public Result checkToken(HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader(Constant.TOKEN_HEADER);
        //如果header中不存在token，则从参数中获取token
        if (StringUtils.isBlank(token)) {
            token = httpRequest.getParameter(Constant.TOKEN_HEADER);
        }
        if (StringUtils.isBlank(token)) {
            return new Result().error(ErrorCode.TOKEN_NOT_EMPTY);
        }
        SysUserTokenEntity sysUserTokenEntity = shiroService.getByToken(token);
        if (sysUserTokenEntity == null) {
            return new Result().error(ErrorCode.TOKEN_INVALID);
        }
        if (sysUserTokenEntity.getExpireDate().getTime() < System.currentTimeMillis()) {
            return new Result().error(ErrorCode.TOKEN_INVALID);
        }
        return new Result();
    }
}
