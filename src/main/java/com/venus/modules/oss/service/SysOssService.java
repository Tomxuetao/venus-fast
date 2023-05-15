package com.venus.modules.oss.service;

import com.venus.common.base.service.BaseService;
import com.venus.common.page.PageData;
import com.venus.modules.oss.entity.SysOssEntity;

import java.util.Map;

/**
 * 文件上传
 *
 * @author Tomxuetao
 */
public interface SysOssService extends BaseService<SysOssEntity> {

    PageData<SysOssEntity> page(Map<String, Object> params);
}
