package com.venus.modules.geo.dao;

import com.venus.common.base.dao.BaseDao;
import com.venus.modules.geo.dto.GeoDataDTO;
import org.apache.ibatis.annotations.Mapper;
import com.venus.modules.geo.entity.GeoDataEntity;

import java.util.List;

@Mapper
public interface GeoDataDao extends BaseDao<GeoDataEntity> {
    void deleteBySourceIds(Long[] id);

    List<GeoDataDTO> getListBySourceId(Long sourceId);

    List<GeoDataDTO> getWkbBySourceId(Long sourceId, Double dilution);

    List<GeoDataDTO> dilutionBySourceId(Long sourceId, Double dilution);
}
