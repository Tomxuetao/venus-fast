package com.venus.modules.city.controller;

import com.venus.common.annotation.LogOperation;
import com.venus.common.constant.Constant;
import com.venus.common.exception.ErrorCode;
import com.venus.common.page.PageData;
import com.venus.common.utils.ExcelUtils;
import com.venus.common.utils.Result;
import com.venus.common.validator.AssertUtils;
import com.venus.common.validator.ValidatorUtils;
import com.venus.common.validator.group.AddGroup;
import com.venus.common.validator.group.DefaultGroup;
import com.venus.modules.city.entity.CitySiteEntity;
import com.venus.modules.city.service.CitySiteService;
import com.venus.modules.oss.service.SysOssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/city/site")
@Api(tags = "城市烟火")
public class CitySiteController {
    @Autowired
    SysOssService sysOssService;
    @Autowired
    private CitySiteService citySiteService;

    @GetMapping("list")
    @ApiOperation("点位列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "name", value = "点位名称", paramType = "query", dataType = "String", dataTypeClass = String.class), @ApiImplicitParam(name = "area", value = "城区", paramType = "query", dataType = "String", dataTypeClass = String.class), @ApiImplicitParam(name = "status", value = "状态", paramType = "query", dataType = "String", dataTypeClass = String.class), @ApiImplicitParam(name = "type", value = "业态分布", paramType = "query", dataType = "String", dataTypeClass = String.class)})
    public Result<List<CitySiteEntity>> list(@ApiIgnore @RequestParam Map<String, Object> params) {
        List<CitySiteEntity> list = citySiteService.list(params);

        return new Result<List<CitySiteEntity>>().ok(list);
    }

    @GetMapping("page")
    @ApiOperation("点位列表")
    @ApiImplicitParams({@ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int", dataTypeClass = Integer.class), @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int", dataTypeClass = Integer.class), @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String", dataTypeClass = String.class), @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String", dataTypeClass = String.class), @ApiImplicitParam(name = "name", value = "点位名称", paramType = "query", dataType = "String", dataTypeClass = String.class), @ApiImplicitParam(name = "area", value = "城区", paramType = "query", dataType = "String", dataTypeClass = String.class), @ApiImplicitParam(name = "status", value = "状态", paramType = "query", dataType = "String", dataTypeClass = String.class), @ApiImplicitParam(name = "type", value = "业态分布", paramType = "query", dataType = "String", dataTypeClass = String.class)})
    public Result<PageData<CitySiteEntity>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<CitySiteEntity> page = citySiteService.page(params);

        return new Result<PageData<CitySiteEntity>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public Result<CitySiteEntity> getById(@PathVariable("id") Long id) {
        CitySiteEntity citySiteEntity = citySiteService.selectById(id);
        return new Result<CitySiteEntity>().ok(citySiteEntity);
    }

    @PostMapping("save")
    @ApiOperation("保存")
    @LogOperation("保存")
    public Result save(@RequestBody CitySiteEntity dto) {
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        citySiteService.insert(dto);
        return new Result();
    }

    @PutMapping("update")
    @ApiOperation("修改")
    @LogOperation("修改")
    public Result update(@RequestBody CitySiteEntity dto) {
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        citySiteService.updateById(dto);
        return new Result();
    }

    @DeleteMapping("delete")
    @ApiOperation("删除")
    @LogOperation("删除")
    public Result delete(@RequestBody Map<String, Long[]> dataForm) {
        Long[] ids = dataForm.get("ids");
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        citySiteService.deleteBatchIds(Arrays.asList(ids));
        return new Result();
    }

    @PostMapping("updateStyle")
    @ApiOperation("修改样式")
    @LogOperation("修改样式")
    public Result updateStyle(@RequestBody List<CitySiteEntity> list) {
        citySiteService.updateBatchById(list);
        return new Result();
    }

    @PostMapping("upload")
    @ApiOperation(value = "上传文件")
    public Result<Map<String, Object>> upload(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return new Result<Map<String, Object>>().error(ErrorCode.UPLOAD_FILE_EMPTY);
        }
        return new Result<Map<String, Object>>().ok(sysOssService.upload(file));
    }

    @PostMapping("import")
    @ApiOperation("导入")
    @LogOperation("导入")
    public Result importExcel(@RequestParam("file") MultipartFile file) throws Exception {
        if(file.isEmpty()) {
            return new Result().error(ErrorCode.UPLOAD_FILE_EMPTY);
        }
        if(ExcelUtils.isExcel(file)) {
            return new Result().error("请上传Excel文件");
        }

        List<CitySiteEntity> importedList = citySiteService.importExcel(file);

        // 获取所有现有数据，使用name和type组合作为键
        Map<String, CitySiteEntity> existingMap = new HashMap<>();
        List<CitySiteEntity> existingSites = citySiteService.list(new HashMap<>());

        for (CitySiteEntity entity : existingSites) {
            String key = entity.getName() + "_" + entity.getType();
            existingMap.put(key, entity);
        }

        // 分类：需要更新的和需要新插入的
        List<CitySiteEntity> toUpdate = new ArrayList<>();
        List<CitySiteEntity> toInsert = new ArrayList<>();

        for (CitySiteEntity importedEntity : importedList) {
            String key = importedEntity.getName() + "_" + importedEntity.getType();
            if(existingMap.containsKey(key)) {
                CitySiteEntity existingEntity = existingMap.get(key);
                importedEntity.setId(existingEntity.getId());
                toUpdate.add(importedEntity);
            } else {
                toInsert.add(importedEntity);
            }
        }

        // 批量更新
        if(!toUpdate.isEmpty()) {
            citySiteService.updateBatchById(toUpdate);
        }

        // 批量插入
        if(!toInsert.isEmpty()) {
            citySiteService.insertBatch(toInsert);
        }
        return new Result();
    }
}
