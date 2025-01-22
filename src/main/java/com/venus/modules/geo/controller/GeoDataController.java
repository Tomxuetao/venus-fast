package com.venus.modules.geo.controller;

import com.venus.common.annotation.LogOperation;
import com.venus.common.constant.Constant;
import com.venus.common.exception.ErrorCode;
import com.venus.common.exception.VenusException;
import com.venus.common.page.PageData;
import com.venus.common.utils.GeoDataUtils;
import com.venus.common.utils.Result;
import com.venus.common.utils.ZipFileUtils;
import com.venus.common.validator.AssertUtils;
import com.venus.modules.geo.dto.GeoDataDTO;
import com.venus.modules.geo.entity.GeoDataEntity;
import com.venus.modules.geo.service.GeoDataService;

import com.venus.modules.oss.entity.SysOssEntity;
import com.venus.modules.oss.service.SysOssService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/geo/data")
public class GeoDataController {
    @Autowired
    private SysOssService sysOssService;

    @Autowired
    private GeoDataService geoDataService;

    @GetMapping("page")
    @ApiOperation(value = "分页")
    @RequiresPermissions("geo:data:page")
    public Result<PageData<SysOssEntity>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        params.put("source", Constant.OssSource.GEODATA.getValue());
        PageData<SysOssEntity> page = sysOssService.page(params);

        return new Result<PageData<SysOssEntity>>().ok(page);
    }

    @PostMapping("create")
    @ApiOperation(value = "创建地理空间数据")
    @RequiresPermissions("geo:shp:create")
    public Result<Map<String, Object>> create(@RequestParam("file") MultipartFile zipFile) {
        if(zipFile.isEmpty()) {
            return new Result<Map<String, Object>>().error(ErrorCode.UPLOAD_FILE_EMPTY);
        }
        File tempDir = null;
        try {
            try {
                tempDir = Files.createTempDirectory("shp-zip-data").toFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                ZipFileUtils.extractZipToFolder(zipFile, tempDir);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Map<String, Object> ossResMap = sysOssService.upload(zipFile, Constant.OssSource.GEODATA.getValue());
            List<GeoDataEntity> list = GeoDataUtils.createFromShpDir(tempDir);
            list.forEach(item -> {
                item.setSourceId(Long.parseLong(ossResMap.get("id").toString()));
            });
            geoDataService.create(list);
            return new Result<Map<String, Object>>().ok(ossResMap);
        } finally {
            if(tempDir != null && tempDir.exists()) {
                try {
                    FileUtils.deleteDirectory(tempDir);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @GetMapping("toShp")
    @ApiOperation("导出")
    @LogOperation("导出")
    @RequiresPermissions("geo:export:shp")
    public void toShp(@RequestParam("id") Long id, HttpServletResponse response) {
        List<GeoDataDTO> list = geoDataService.getListBySourceId(id);
        GeoDataUtils.toShp(list, response);
    }

    @GetMapping("toGeoJson")
    @ApiOperation("导出")
    @LogOperation("导出")
    @RequiresPermissions("geo:export:json")
    public void toGeoJson(@RequestParam("id") Long id, HttpServletResponse response) {
        List<GeoDataDTO> list = geoDataService.getListBySourceId(id);
        GeoDataUtils.toGeoJson(list, response);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    @LogOperation("预览")
    @RequiresPermissions("geo:data:view")
    public Result<List<GeoDataDTO>> getListBySourceId(@PathVariable("id") Long id, @RequestParam Map<String, String> params) {
        double dilution = gainAccuracy(params);
        List<GeoDataDTO> list = geoDataService.dilutionBySourceId(id, dilution);
        return new Result<List<GeoDataDTO>>().ok(list);
    }

    @GetMapping("/dilution/{id}")
    @ApiOperation("信息")
    @LogOperation("预览")
    @RequiresPermissions("geo:data:view")
    public Result<List<GeoDataDTO>> dilutionBySourceId(@PathVariable("id") Long id, @RequestParam Map<String, String> params) {
        double dilution = gainAccuracy(params);
        List<GeoDataDTO> list = geoDataService.dilutionBySourceId(id, dilution);
        return new Result<List<GeoDataDTO>>().ok(list);
    }

    @GetMapping("/wkb/{id}")
    @ApiOperation("信息")
    @LogOperation("预览wkb")
    @RequiresPermissions("geo:data:view")
    public Result<List<GeoDataDTO>> getWkbBySourceId(@PathVariable("id") Long id, @RequestParam Map<String, String> params) {
        double dilution = gainAccuracy(params);
        List<GeoDataDTO> list = geoDataService.getWkbBySourceId(id, dilution);
        return new Result<List<GeoDataDTO>>().ok(list);
    }

    @GetMapping("/dilution2/{id}")
    @ApiOperation("信息")
    @LogOperation("GeoTools 抽稀")
    @RequiresPermissions("geo:data:view")
    public Result<List<GeoDataDTO>> dilution2BySourceId(@PathVariable("id") Long id, @RequestParam Map<String, String> params) {
        double dilution = gainAccuracy(params);
        List<GeoDataDTO> list = geoDataService.getListBySourceId(id, dilution);
        return new Result<List<GeoDataDTO>>().ok(list);
    }

    @DeleteMapping("delete")
    @ApiOperation("删除")
    @LogOperation("删除")
    @RequiresPermissions("sys:user:delete")
    public Result delete(@RequestBody Map<String, Long[]> dataForm) {
        Long[] ids = dataForm.get("ids");
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        geoDataService.deleteBySourceIds(ids);
        return new Result();
    }

    public double gainAccuracy(Map<String, String> params) {
        String dilutionStr = params.get("accuracy");
        double dilution;
        if(StringUtils.isNotBlank(dilutionStr)) {
            dilution = Double.parseDouble(StringUtils.trim(dilutionStr));
        } else {
            dilution = 0.0001;
        }
        return dilution;
    }
}
