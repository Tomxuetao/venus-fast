package com.venus.modules.geo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.venus.common.base.service.impl.BaseServiceImpl;
import com.venus.common.constant.Constant;
import com.venus.common.page.PageData;
import com.venus.common.utils.GeoLayerUtils;
import com.venus.modules.geo.config.GeoServerConfig;
import com.venus.modules.geo.dao.GeoLayerDao;
import com.venus.modules.geo.dto.GeoLayerDTO;
import com.venus.modules.geo.entity.GeoLayerEntity;
import com.venus.modules.geo.service.GeoLayerService;
import com.venus.modules.oss.service.SysOssService;
import com.venus.modules.sys.service.SysParamsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class GeoLayerServiceImpl extends BaseServiceImpl<GeoLayerDao, GeoLayerEntity> implements GeoLayerService {
    @Autowired
    private SysOssService sysOssService;

    @Autowired
    private SysParamsService sysParamsService;

    @Override
    public PageData<GeoLayerEntity> page(Map<String, Object> params) {
        IPage<GeoLayerEntity> page = baseDao.selectPage(getPage(params, Constant.CREATE_DATE, false), new QueryWrapper<>());
        return getPageData(page, GeoLayerEntity.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(GeoLayerDTO dto) {
        String ossUrl = dto.getOssUrl();
        // 读取文件流
        InputStream inputStream = sysOssService.readStream(ossUrl);
        if(inputStream != null) {
            GeoServerConfig config = sysParamsService.getValueObject(Constant.GEOSERVER_CONFIG_KEY, GeoServerConfig.class);
            config.setLayerName(dto.getName());
            config.setDatastore(dto.getDatastore());
            config.setWorkspace(dto.getWorkspace());
            GeoLayerUtils.publishLayer(config, inputStream);

            GeoLayerEntity entity = new GeoLayerEntity();
            entity.setStatus(1);
            entity.setType("shp");
            entity.setName(dto.getName());
            entity.setNativeSrs("EPSG:4326");
            entity.setTitle(dto.getTitle());
            entity.setOssUrl(dto.getOssUrl());
            entity.setWorkspace(dto.getWorkspace());
            entity.setDatastore(dto.getDatastore());

            baseDao.insert(entity);
        }
    }

    @Override
    public void update(GeoLayerDTO dto) {

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(Long[] ids) {
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    @Override
    public List<GeoLayerEntity> list(Map<String, Object> params) {
        return baseDao.selectList(new QueryWrapper<>());
    }
}
