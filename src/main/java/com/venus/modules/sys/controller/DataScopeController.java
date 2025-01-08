package com.venus.modules.sys.controller;

import com.venus.common.annotation.LogOperation;
import com.venus.common.utils.Result;
import com.venus.common.validator.ValidatorUtils;
import com.venus.common.validator.group.DefaultGroup;
import com.venus.common.validator.group.UpdateGroup;
import com.venus.modules.sys.dto.SysRoleDTO;
import com.venus.modules.sys.service.SysRoleDataScopeService;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/data/scope")
public class DataScopeController {

    @Autowired
    private SysRoleDataScopeService sysRoleDataScopeService;

    @PostMapping("save")
    @ApiOperation("设置角色数据权限")
    @LogOperation("设置角色数据权限")
    @RequiresPermissions("data:scope:save")
    public Result save(@RequestBody SysRoleDTO sysRoleDTO) {
        //效验数据
        ValidatorUtils.validateEntity(sysRoleDTO, UpdateGroup.class, DefaultGroup.class);

        //保存角色数据权限关系
        sysRoleDataScopeService.saveOrUpdate(sysRoleDTO.getId(), sysRoleDTO.getDeptIdList());
        return new Result();
    }
}
