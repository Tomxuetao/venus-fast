package com.venus.modules.job.controller;

import com.venus.common.annotation.LogOperation;
import com.venus.common.constant.Constant;
import com.venus.common.page.PageData;
import com.venus.common.utils.Result;
import com.venus.common.validator.AssertUtils;
import com.venus.common.validator.ValidatorUtils;
import com.venus.common.validator.group.AddGroup;
import com.venus.common.validator.group.DefaultGroup;
import com.venus.common.validator.group.UpdateGroup;
import com.venus.modules.job.dto.ScheduleJobDTO;
import com.venus.modules.job.service.ScheduleJobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

/**
 * 定时任务
 *
 * @author Tomxuetao
 */
@RestController
@RequestMapping("/sys/schedule")
@Api(tags = "定时任务")
public class ScheduleJobController {
    @Autowired
    private ScheduleJobService scheduleJobService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({@ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int", dataTypeClass = Integer.class), @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int", dataTypeClass = Integer.class), @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String", dataTypeClass = String.class), @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String", dataTypeClass = String.class), @ApiImplicitParam(name = "beanName", value = "beanName", paramType = "query", dataType = "String", dataTypeClass = String.class)})
    @RequiresPermissions("sys:schedule:page")
    public Result<PageData<ScheduleJobDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<ScheduleJobDTO> page = scheduleJobService.page(params);

        return new Result<PageData<ScheduleJobDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    @RequiresPermissions("sys:schedule:info")
    public Result<ScheduleJobDTO> info(@PathVariable("id") Long id) {
        ScheduleJobDTO schedule = scheduleJobService.get(id);

        return new Result<ScheduleJobDTO>().ok(schedule);
    }

    @PostMapping("save")
    @ApiOperation("保存")
    @LogOperation("保存")
    @RequiresPermissions("sys:schedule:save")
    public Result save(@RequestBody ScheduleJobDTO dto) {
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        scheduleJobService.save(dto);

        return new Result();
    }

    @PutMapping("update")
    @ApiOperation("修改")
    @LogOperation("修改")
    @RequiresPermissions("sys:schedule:update")
    public Result update(@RequestBody ScheduleJobDTO dto) {
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        scheduleJobService.update(dto);

        return new Result();
    }

    @DeleteMapping("delete")
    @ApiOperation("删除")
    @LogOperation("删除")
    @RequiresPermissions("sys:schedule:delete")
    public Result delete(@RequestBody Map<String, Long[]> dataForm) {
        Long[] ids = dataForm.get("ids");

        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");
        scheduleJobService.deleteBatch(ids);

        return new Result();
    }

    @PutMapping("run")
    @ApiOperation("立即执行")
    @LogOperation("立即执行")
    @RequiresPermissions("sys:schedule:run")
    public Result run(@RequestBody Long[] ids) {
        scheduleJobService.run(ids);

        return new Result();
    }

    @PutMapping("pause")
    @ApiOperation("暂停")
    @LogOperation("暂停")
    @RequiresPermissions("sys:schedule:pause")
    public Result pause(@RequestBody Long[] ids) {
        scheduleJobService.pause(ids);

        return new Result();
    }

    @PutMapping("resume")
    @ApiOperation("恢复")
    @LogOperation("恢复")
    @RequiresPermissions("sys:schedule:resume")
    public Result resume(@RequestBody Long[] ids) {
        scheduleJobService.resume(ids);

        return new Result();
    }
}
