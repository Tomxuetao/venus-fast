package com.venus.modules.geo.service.impl;

import com.venus.common.base.service.impl.BaseServiceImpl;
import com.venus.common.utils.GeoDataUtils;
import com.venus.modules.geo.dao.GeoDataDao;
import com.venus.modules.geo.dto.GeoDataDTO;
import com.venus.modules.geo.entity.GeoDataEntity;
import com.venus.modules.geo.service.GeoDataService;
import com.venus.modules.oss.service.SysOssService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class GeoDataServiceImpl extends BaseServiceImpl<GeoDataDao, GeoDataEntity> implements GeoDataService {
    private static final Logger logger = LoggerFactory.getLogger(GeoDataServiceImpl.class);

    @Autowired
    private SysOssService sysOssService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBySourceIds(Long[] ids) {
        sysOssService.deleteBatchIds(Arrays.asList(ids));
        baseDao.deleteBySourceIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(List<GeoDataEntity> dataList){
        this.insertBatch(dataList, 200);
    }

    @Override
    public List<GeoDataDTO> getWkbBySourceId(Long sourceId, Double dilution) {
        return baseDao.getWkbBySourceId(sourceId, dilution);
    }

    @Override
    public List<GeoDataDTO> getListBySourceId(Long sourceId) {
        return baseDao.getListBySourceId(sourceId);
    }

    @Override
    public List<GeoDataDTO> getListBySourceId(Long sourceId, Double dilution) {
        return GeoDataUtils.dilutionGeoJson(baseDao.getListBySourceId(sourceId), dilution);
    }

    @Override
    public List<GeoDataDTO> dilutionBySourceId(Long sourceId, Double dilution) {
        return baseDao.dilutionBySourceId(sourceId, dilution);
    }
}
