package com.venus.modules.oss.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.venus.common.utils.PageUtils;
import com.venus.modules.oss.entity.SysOssEntity;

import java.util.Map;

/**
 * 文件上传
 *
 * @author Tomxuetao
 */
public interface SysOssService extends IService<SysOssEntity> {

	PageUtils queryPage(Map<String, Object> params);
}
