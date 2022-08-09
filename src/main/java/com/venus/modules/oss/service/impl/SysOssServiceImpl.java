package com.venus.modules.oss.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.venus.common.base.service.impl.BaseServiceImpl;
import com.venus.common.constant.Constant;
import com.venus.common.page.PageData;
import com.venus.modules.oss.dao.SysOssDao;
import com.venus.modules.oss.entity.SysOssEntity;
import com.venus.modules.oss.service.SysOssService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SysOssServiceImpl extends BaseServiceImpl<SysOssDao, SysOssEntity> implements SysOssService {

	@Override
	public PageData<SysOssEntity> page(Map<String, Object> params) {
		IPage<SysOssEntity> page = baseDao.selectPage(
				getPage(params, Constant.CREATE_DATE, false),
				new QueryWrapper<>()
		);
		return getPageData(page, SysOssEntity.class);
	}
}
