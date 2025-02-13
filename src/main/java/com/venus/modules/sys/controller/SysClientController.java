package com.venus.modules.sys.controller;

import com.venus.common.annotation.LogOperation;
import com.venus.common.constant.Constant;
import com.venus.common.page.PageData;
import com.venus.common.utils.Result;
import com.venus.common.validator.AssertUtils;
import com.venus.common.validator.ValidatorUtils;
import com.venus.common.validator.group.AddGroup;
import com.venus.common.validator.group.DefaultGroup;
import com.venus.common.validator.group.UpdateGroup;
import com.venus.modules.sys.dto.SysClientDTO;
import com.venus.modules.sys.entity.SysClientEntity;
import com.venus.modules.sys.service.SysClientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Arrays;
import java.util.Map;

@RestController
@RequestMapping("/sys/client")
@Api(tags = "客户端管理")
public class SysClientController {

    @Autowired
    private SysClientService sysClientService;

    @GetMapping("list")
    @ApiOperation("客户端列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "appName", value = "应用名称", paramType = "query", dataType = "String", dataTypeClass = String.class)
    })
    @RequiresPermissions("sys:client:page")
    public Result<PageData<SysClientDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<SysClientDTO> page = sysClientService.page(params);

        return new Result<PageData<SysClientDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    @RequiresPermissions("sys:client:info")
    public Result<SysClientEntity> get(@PathVariable("id") Long id) {
        SysClientEntity data = sysClientService.get(id);

        return new Result<SysClientEntity>().ok(data);
    }

    @PostMapping("save")
    @ApiOperation("保存")
    @LogOperation("保存")
    @RequiresPermissions("sys:client:save")
    public Result save(@RequestBody SysClientDTO dto) {
        // 效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        sysClientService.save(dto);

        return new Result();
    }

    @PutMapping("update")
    @ApiOperation("修改")
    @LogOperation("修改")
    @RequiresPermissions("sys:client:update")
    public Result update(@RequestBody SysClientDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        sysClientService.update(dto);

        return new Result();
    }

    @DeleteMapping("delete")
    @ApiOperation("删除")
    @LogOperation("删除")
    @RequiresPermissions("sys:client:delete")
    public Result delete(@RequestBody Map<String, Long[]> dataForm) {
        Long[] ids = dataForm.get("ids");
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        sysClientService.deleteBatchIds(Arrays.asList(ids));

        return new Result();
    }
}
