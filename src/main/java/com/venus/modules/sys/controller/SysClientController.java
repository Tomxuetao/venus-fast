package com.venus.modules.oauth.controller;

import com.venus.common.annotation.LogOperation;
import com.venus.common.constant.Constant;
import com.venus.common.page.PageData;
import com.venus.common.utils.Result;
import com.venus.common.validator.ValidatorUtils;
import com.venus.common.validator.group.AddGroup;
import com.venus.common.validator.group.DefaultGroup;
import com.venus.modules.oauth.dto.SysClientDTO;
import com.venus.modules.oauth.service.SysClientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

@RestController
@RequestMapping("/oauth/client")
@Api(tags = "客户端管理")
public class SysClientController {

    @Autowired
    private SysClientService oauthClientService;

    @GetMapping("list")
    @ApiOperation("客户端列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "username", value = "用户名", paramType = "query", dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "gender", value = "性别", paramType = "query", dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "deptId", value = "部门ID", paramType = "query", dataType = "String", dataTypeClass = String.class)
    })
    @RequiresPermissions("oauth:client:page")
    public Result<PageData<SysClientDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<SysClientDTO> page = oauthClientService.page(params);

        return new Result<PageData<SysClientDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    @RequiresPermissions("oauth:client:info")
    public Result<SysClientDTO> get(@PathVariable("id") Long id) {
        SysClientDTO data = oauthClientService.get(id);

        return new Result<SysClientDTO>().ok(data);
    }

    @PostMapping("save")
    @ApiOperation("保存")
    @LogOperation("保存")
    @RequiresPermissions("oauth:client:save")
    public Result save(@RequestBody SysClientDTO dto) {
        // 效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        oauthClientService.save(dto);

        return new Result();
    }
}
