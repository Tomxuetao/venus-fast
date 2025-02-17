package com.venus.modules.sys.controller;

import com.venus.common.annotation.LogOperation;
import com.venus.common.constant.Constant;
import com.venus.common.exception.ErrorCode;
import com.venus.common.page.PageData;
import com.venus.common.utils.ConvertUtils;
import com.venus.common.utils.ExcelUtils;
import com.venus.common.utils.Result;
import com.venus.common.validator.AssertUtils;
import com.venus.common.validator.ValidatorUtils;
import com.venus.common.validator.group.AddGroup;
import com.venus.common.validator.group.DefaultGroup;
import com.venus.common.validator.group.UpdateGroup;
import com.venus.modules.login.password.PasswordUtils;
import com.venus.modules.login.user.SecurityUser;
import com.venus.modules.login.user.UserDetail;
import com.venus.modules.oss.service.SysOssService;
import com.venus.modules.sys.dto.*;
import com.venus.modules.sys.entity.SysUserEntity;
import com.venus.modules.sys.enums.SuperAdminEnum;
import com.venus.modules.sys.excel.SysUserExcel;
import com.venus.modules.sys.service.SysMenuService;
import com.venus.modules.sys.service.SysRoleService;
import com.venus.modules.sys.service.SysRoleUserService;
import com.venus.modules.sys.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequestMapping("/sys/user")
@Api(tags = "用户管理")
public class SysUserController {
    @Value("${spring.profiles.active}")
    private String activeEnv;

    @Autowired
    private SysOssService sysOssService;
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysRoleUserService sysRoleUserService;

    @GetMapping("list")
    @ApiOperation("用户列表")
    @ApiImplicitParams({@ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int", dataTypeClass = Integer.class), @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int", dataTypeClass = Integer.class), @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String", dataTypeClass = String.class), @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String", dataTypeClass = String.class), @ApiImplicitParam(name = "username", value = "用户名", paramType = "query", dataType = "String", dataTypeClass = String.class), @ApiImplicitParam(name = "gender", value = "性别", paramType = "query", dataType = "String", dataTypeClass = String.class), @ApiImplicitParam(name = "deptId", value = "部门ID", paramType = "query", dataType = "String", dataTypeClass = String.class)})
    @RequiresPermissions("sys:user:page")
    public Result<PageData<SysUserDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<SysUserDTO> page = sysUserService.page(params);

        return new Result<PageData<SysUserDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    @RequiresPermissions("sys:user:info")
    public Result<SysUserDTO> get(@PathVariable("id") Long id) {
        SysUserDTO data = sysUserService.get(id);

        //用户角色列表
        List<Long> roleIdList = sysRoleUserService.getRoleIdList(id);
        data.setRoleIdList(roleIdList);

        return new Result<SysUserDTO>().ok(data);
    }

    @GetMapping("info")
    @ApiOperation("登录用户信息")
    public Result<SysUserDTO> info() {
        UserDetail user = SecurityUser.getUser();

        SysUserDTO data = ConvertUtils.sourceToTarget(user, SysUserDTO.class);

        List<SysMenuDTO> menuList = sysMenuService.getUserMenuList(user, null);

        data.setMenuList(menuList);

        return new Result<SysUserDTO>().ok(data);
    }

    @PutMapping("password")
    @ApiOperation("修改密码")
    @LogOperation("修改密码")
    public Result password(@RequestBody PasswordDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto);

        UserDetail user = SecurityUser.getUser();

        //原密码不正确
        if(!PasswordUtils.matches(dto.getPassword(), user.getPassword())) {
            return new Result().error(ErrorCode.PASSWORD_ERROR);
        }

        sysUserService.updatePassword(user.getId(), dto.getNewPassword());

        return new Result();
    }

    @PostMapping("save")
    @ApiOperation("保存")
    @LogOperation("保存")
    @RequiresPermissions("sys:user:save")
    public Result save(@RequestBody SysUserDTO dto) {
        // 效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        // 校验手机号
        if(sysUserService.getByMobile(dto.getMobile()) > 0) {
            return new Result().error("手机号已存在");
        }
        // 校验邮箱
        if(sysUserService.getByEmail(dto.getEmail()) > 0) {
            return new Result().error("邮箱已存在");
        }
        // 校验用户名
        if(sysUserService.getByUsername(dto.getUsername()) != null) {
            return new Result().error("用户名已存在");
        }

        sysUserService.save(dto);

        return new Result();
    }

    @PutMapping("update")
    @ApiOperation("修改")
    @LogOperation("修改")
    @RequiresPermissions("sys:user:update")
    public Result update(@RequestBody SysUserDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        sysUserService.update(dto);

        return new Result();
    }

    @PutMapping("resetPwd")
    @ApiOperation("重置密码")
    @LogOperation("重置密码")
    @RequiresPermissions("sys:user:reset")
    public Result reset(@RequestBody Map<String, Long> dataForm) {
        Long id = dataForm.get("id");
        AssertUtils.isNull(id, "id");

        sysUserService.updatePassword(id, "888888");

        return new Result();
    }

    @DeleteMapping("delete")
    @ApiOperation("删除")
    @LogOperation("删除")
    @RequiresPermissions("sys:user:delete")
    public Result delete(@RequestBody Map<String, Long[]> dataForm) {
        Long[] ids = dataForm.get("ids");
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        sysUserService.deleteBatchIds(ids);
        sysRoleUserService.deleteByUserIds(ids);

        return new Result();
    }

    @GetMapping("roles")
    @ApiOperation("用户角色")
    public Result<PageData<SysRoleDTO>> roles(@ApiIgnore @RequestParam Map<String, Object> params) {
        String userIdStr = (String) params.get("userId");

        AssertUtils.isNull(userIdStr, "userId");
        params.put("userId", Long.valueOf(userIdStr));
        PageData<SysRoleDTO> page = sysRoleService.getListByUserId(params);

        return new Result<PageData<SysRoleDTO>>().ok(page);
    }

    @PutMapping("saveRoles")
    @ApiOperation("新增角色")
    @LogOperation("新增角色")
    @RequiresPermissions("sys:user:update")
    public Result savRoles(@RequestBody SysUserRolesDTO dto) {
        // 效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        sysRoleUserService.saveUserRoles(dto.getUserId(), dto.getRoleIds());

        return new Result();
    }

    @DeleteMapping("deleteRoles")
    @ApiOperation("删除角色")
    @LogOperation("删除角色")
    @RequiresPermissions("sys:user:update")
    public Result deleteRoles(@RequestBody SysUserRolesDTO dto) {
        // 效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        sysRoleUserService.deleteUserRoles(dto.getUserId(), dto.getRoleIds());

        return new Result();
    }

    @PostMapping("import")
    @ApiOperation("导入")
    @LogOperation("导入")
    @RequiresPermissions("sys:user:import")
    public Result importExcel(@RequestParam("file") MultipartFile file) throws Exception {
        if(file.isEmpty()) {
            return new Result().error(ErrorCode.UPLOAD_FILE_EMPTY);
        }
        if(ExcelUtils.isExcel(file)) {
            return new Result().error("请上传Excel文件");
        }

        //上传文件
        if(Objects.equals(activeEnv, "prod")) {
            try {
                sysOssService.upload(file);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        List<SysUserEntity> list = sysUserService.importExcel(file);
        if(list.isEmpty()) {
            return new Result().error("导入数据为空");
        }
        list.forEach(sysUser -> {
            // 设置密码 默认 888888
            sysUser.setPassword("$2a$10$QvGRkLAw11cvUMG6G2yqDuVYbQaVVp07ap5IbCT1lleLTyTrQz3w2");
            sysUser.setSuperAdmin(SuperAdminEnum.NO.value());
        });
        sysUserService.insertBatch(list, 1000);
        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @LogOperation("导出")
    @RequiresPermissions("sys:user:export")
    @ApiImplicitParam(name = "username", value = "用户名", paramType = "query", dataType = "String", dataTypeClass = String.class)
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<SysUserDTO> list = sysUserService.list(params);

        ExcelUtils.exportExcelToTarget(response, "导出用户", "用户管理", list, SysUserExcel.class);
    }
}
