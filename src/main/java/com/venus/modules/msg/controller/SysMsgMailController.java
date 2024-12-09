package com.venus.modules.msg.controller;

import com.venus.common.constant.Constant;
import com.venus.common.page.PageData;
import com.venus.common.utils.Result;
import com.venus.modules.msg.dto.SysMsgMailDTO;
import com.venus.modules.msg.service.SysMsgMailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

@RestController
@RequestMapping("msg/mail")
@Api(tags = "邮件发送日志")
public class SysMsgMailController {
    @Autowired
    private SysMsgMailService sysMsgMailService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String", dataTypeClass = String.class)
    })
    @RequiresPermissions("msg:mail:page")
    public Result<PageData<SysMsgMailDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<SysMsgMailDTO> page = sysMsgMailService.page(params);

        return new Result<PageData<SysMsgMailDTO>>().ok(page);
    }
}
