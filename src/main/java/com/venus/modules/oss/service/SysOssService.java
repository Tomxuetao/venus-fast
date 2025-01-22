package com.venus.modules.oss.service;

import com.venus.common.base.service.BaseService;
import com.venus.common.page.PageData;
import com.venus.modules.oss.entity.SysOssEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * 文件上传
 *
 * @author Tomxuetao
 */
public interface SysOssService extends BaseService<SysOssEntity> {
    boolean exists(String url);

    InputStream readStream(String url);

    Map<String, Object> upload(MultipartFile file);

    PageData<SysOssEntity> page(Map<String, Object> params);

    Map<String, Object> upload(MultipartFile file, Integer source);
}
