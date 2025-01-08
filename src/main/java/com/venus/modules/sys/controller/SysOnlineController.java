package com.venus.modules.sys.controller;

import com.venus.common.annotation.LogOperation;
import com.venus.common.constant.Constant;
import com.venus.common.page.PageData;
import com.venus.common.utils.Result;
import com.venus.common.validator.AssertUtils;
import com.venus.modules.sys.entity.SysOnlineEntity;
import com.venus.modules.sys.service.SysOnlineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sys/online")
@Api(tags = "在线用户")
public class SysOnlineController {

    @Autowired
    SysOnlineService sysOnlineService;

    @GetMapping("list")
    @ApiOperation("在线用户")
    @ApiImplicitParams({@ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int", dataTypeClass = Integer.class), @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int", dataTypeClass = Integer.class), @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String", dataTypeClass = String.class), @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String", dataTypeClass = String.class), @ApiImplicitParam(name = "username", value = "用户名", paramType = "query", dataType = "String", dataTypeClass = String.class), @ApiImplicitParam(name = "gender", value = "性别", paramType = "query", dataType = "String", dataTypeClass = String.class), @ApiImplicitParam(name = "deptId", value = "部门ID", paramType = "query", dataType = "String", dataTypeClass = String.class)})
    @RequiresPermissions("sys:online:page")
    public Result<PageData<SysOnlineEntity>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<SysOnlineEntity> page = sysOnlineService.page(params);

        return new Result<PageData<SysOnlineEntity>>().ok(page);
    }

    @DeleteMapping("logout")
    @ApiOperation(value = "强制退出")
    @LogOperation("强制退出")
    @RequiresPermissions("sys:online:logout")
    public Result delete(@RequestBody Map<String, Long[]> dataForm) {
        Long[] ids = dataForm.get("ids");

        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        return sysOnlineService.batchForceLogout(Arrays.asList(ids));
    }
}
