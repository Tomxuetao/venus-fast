package com.venus.modules.log.controller;

import com.venus.common.annotation.LogOperation;
import com.venus.common.constant.Constant;
import com.venus.common.page.PageData;
import com.venus.common.utils.ExcelUtils;
import com.venus.common.utils.Result;
import com.venus.modules.log.dto.SysLogOperateDTO;
import com.venus.modules.log.excel.SysLogOperateExcel;
import com.venus.modules.log.service.SysLogOperateService;
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

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("sys/log/operation")
@Api(tags="操作日志")
public class SysLogOperateController {
    @Autowired
    private SysLogOperateService sysLogOperateService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int", dataTypeClass=Integer.class) ,
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int", dataTypeClass=Integer.class) ,
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String", dataTypeClass=String.class) ,
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String", dataTypeClass=String.class) ,
            @ApiImplicitParam(name = "status", value = "状态  0：失败    1：成功", paramType = "query", dataType="int", dataTypeClass=Integer.class)
    })
    @RequiresPermissions("sys:log:operation")
    public Result<PageData<SysLogOperateDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<SysLogOperateDTO> page = sysLogOperateService.page(params);

        return new Result<PageData<SysLogOperateDTO>>().ok(page);
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @LogOperation("导出")
    @RequiresPermissions("sys:log:operation")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<SysLogOperateDTO> list = sysLogOperateService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, list, SysLogOperateExcel.class);
    }

}
