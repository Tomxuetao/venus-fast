package com.venus.modules.geo.controller;

import com.venus.common.annotation.LogOperation;
import com.venus.common.constant.Constant;
import com.venus.common.exception.ErrorCode;
import com.venus.common.exception.VenusException;
import com.venus.common.page.PageData;
import com.venus.common.utils.GeoDataUtils;
import com.venus.common.utils.Result;
import com.venus.common.utils.ZipFileUtils;
import com.venus.common.validator.ValidatorUtils;
import com.venus.common.validator.group.AddGroup;
import com.venus.common.validator.group.DefaultGroup;
import com.venus.modules.geo.dto.GeoLayerDTO;
import com.venus.modules.geo.entity.GeoLayerEntity;
import com.venus.modules.geo.service.GeoLayerService;
import com.venus.modules.oss.service.SysOssService;
import com.venus.modules.sys.dto.SysUserDTO;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/geo/layer")
public class GeoLayerController {
    private static final Logger logger = LoggerFactory.getLogger(GeoLayerController.class);
    @Autowired
    private SysOssService sysOssService;
    @Autowired
    private GeoLayerService geoLayerService;

    @GetMapping("page")
    @ApiOperation(value = "分页")
    @RequiresPermissions("geo:layer:page")
    public Result<PageData<GeoLayerEntity>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        params.put("source", Constant.OssSource.GEODATA.getValue());
        PageData<GeoLayerEntity> page = geoLayerService.page(params);

        return new Result<PageData<GeoLayerEntity>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    @RequiresPermissions("geo:layer:info")
    public Result<GeoLayerEntity> get(@PathVariable("id") Long id) {
        GeoLayerEntity data = geoLayerService.selectById(id);
        return new Result<GeoLayerEntity>().ok(data);
    }

    @PostMapping("save")
    @ApiOperation("保存")
    @LogOperation("保存")
    @RequiresPermissions("geo:layer:save")
    public Result save(@RequestBody GeoLayerDTO dto) {
        // 效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        geoLayerService.save(dto);

        return new Result();
    }

    @PostMapping("upload")
    @ApiOperation(value = "上传文件")
    @RequiresPermissions("sys:oss:upload")
    public Result<Map<String, Object>> upload(@RequestParam("file") MultipartFile zipFile) throws IOException {
        if(zipFile.isEmpty()) {
            return new Result<Map<String, Object>>().error(ErrorCode.UPLOAD_FILE_EMPTY);
        }
        File tempDir = Files.createTempDirectory("shp-zip-data").toFile();
        try {
            List<String> fileNames = ZipFileUtils.extractZipToFolder(zipFile, tempDir);
            if(!GeoDataUtils.containsRequiredFiles(fileNames)) {
                FileUtils.deleteDirectory(tempDir);
                return new Result<Map<String, Object>>().error("Shp数据不完整");
            }
            FileUtils.deleteDirectory(tempDir);
        } catch (IOException e) {
            logger.error("上传文件失败", e);
            throw new VenusException("上传文件失败", e);
        } finally {
            FileUtils.deleteDirectory(tempDir);
        }

        return new Result<Map<String, Object>>().ok(sysOssService.upload(zipFile, Constant.OssSource.GEOSERVER.getValue()));
    }
}
