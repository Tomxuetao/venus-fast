package com.venus.modules.sys.controller;

import com.venus.common.annotation.LogOperation;
import com.venus.common.constant.Constant;
import com.venus.common.exception.ErrorCode;
import com.venus.common.page.PageData;
import com.venus.common.utils.Result;
import com.venus.common.validator.AssertUtils;
import com.venus.common.validator.ValidatorUtils;
import com.venus.common.validator.group.AddGroup;
import com.venus.common.validator.group.DefaultGroup;
import com.venus.common.validator.group.UpdateGroup;
import com.venus.modules.sys.dto.SysRoleDTO;
import com.venus.modules.sys.dto.SysRoleUsersDTO;
import com.venus.modules.sys.dto.SysUserDTO;
import com.venus.modules.sys.entity.SysRoleUserEntity;
import com.venus.modules.sys.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sys/role")
@Api(tags = "角色管理")
public class SysRoleController {
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysRoleMenuService sysRoleMenuService;
    @Autowired
    private SysRoleUserService sysRoleUserService;
    @Autowired
    private SysRoleDataScopeService sysRoleDataScopeService;
    @Autowired
    private SysUserService sysUserService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({@ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int", dataTypeClass = Integer.class), @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int", dataTypeClass = Integer.class), @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String", dataTypeClass = String.class), @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String", dataTypeClass = String.class), @ApiImplicitParam(name = "name", value = "角色名", paramType = "query", dataType = "String", dataTypeClass = String.class)})
    @RequiresPermissions("sys:role:page")
    public Result<PageData<SysRoleDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<SysRoleDTO> page = sysRoleService.page(params);

        return new Result<PageData<SysRoleDTO>>().ok(page);
    }

    @GetMapping("list")
    @ApiOperation("列表")
    @RequiresPermissions("sys:role:list")
    public Result<List<SysRoleDTO>> list() {
        List<SysRoleDTO> data = sysRoleService.list(new HashMap<>(1));

        return new Result<List<SysRoleDTO>>().ok(data);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    @RequiresPermissions("sys:role:info")
    public Result<SysRoleDTO> get(@PathVariable("id") Long id) {
        SysRoleDTO data = sysRoleService.get(id);

        //查询角色对应的菜单
        List<Long> menuIdList = sysRoleMenuService.getMenuIdList(id);
        data.setMenuIdList(menuIdList);

        //查询角色对应的数据权限
        List<Long> deptIdList = sysRoleDataScopeService.getDeptIdList(id);
        data.setDeptIdList(deptIdList);

        return new Result<SysRoleDTO>().ok(data);
    }

    @PostMapping("save")
    @ApiOperation("保存")
    @LogOperation("保存")
    @RequiresPermissions("sys:role:save")
    public Result save(@RequestBody SysRoleDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        sysRoleService.save(dto);

        return new Result();
    }

    @PutMapping("update")
    @ApiOperation("修改")
    @LogOperation("修改")
    @RequiresPermissions("sys:role:update")
    public Result update(@RequestBody SysRoleDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        sysRoleService.update(dto);

        return new Result();
    }

    @DeleteMapping("delete")
    @ApiOperation("删除")
    @LogOperation("删除")
    @RequiresPermissions("sys:role:delete")
    public Result delete(@RequestBody Map<String, Long[]> dataForm) {
        Long[] ids = dataForm.get("ids");
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        Long count = sysRoleUserService.countByRoleIds(ids);

        if(count > 0) {
            return new Result().error(ErrorCode.ROLE_USER_EXIST);
        }

        sysRoleService.delete(ids);

        return new Result();
    }

    @GetMapping("users")
    @ApiOperation("用户列表")
    @LogOperation("获取用户列表")
    public Result<PageData<SysUserDTO>> users(@ApiIgnore @RequestParam Map<String, Object> params) {
        String roleIdStr = (String) params.get("roleId");

        AssertUtils.isNull(roleIdStr, "roleId");
        params.put("roleId", Long.valueOf(roleIdStr));
        PageData<SysUserDTO> page = sysUserService.getListByRoleId(params);

        return new Result<PageData<SysUserDTO>>().ok(page);
    }

    @PostMapping("saveUsers")
    @ApiOperation("保存用户")
    @LogOperation("保存用户")
    @RequiresPermissions("sys:role:save")
    public Result saveUsers(@RequestBody SysRoleUsersDTO sysRoleUsersDTO) {
        // 效验数据
        ValidatorUtils.validateEntity(sysRoleUsersDTO, AddGroup.class, DefaultGroup.class);

        sysRoleUserService.saveByRoleId(sysRoleUsersDTO.getRoleId(), sysRoleUsersDTO.getUserIds());

        return new Result();
    }

    @DeleteMapping("deleteUsers")
    @ApiOperation("删除用户")
    @LogOperation("删除用户")
    @RequiresPermissions("sys:role:delete")
    public Result deleteUsers(@RequestBody SysRoleUsersDTO sysRoleUsersDTO) {
        // 效验数据
        ValidatorUtils.validateEntity(sysRoleUsersDTO, UpdateGroup.class, DefaultGroup.class);

        sysRoleUserService.deleteByRoleId(sysRoleUsersDTO.getRoleId(), sysRoleUsersDTO.getUserIds());

        return new Result();
    }
}
