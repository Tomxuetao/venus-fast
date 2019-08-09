package com.venus.modules.geo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.venus.modules.geo.entity.GeoBoundaryEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GeoBoundaryDao extends BaseMapper<GeoBoundaryEntity> {
    String getGeoJsonById(Integer id);

    void saveGeomText(GeoBoundaryEntity geoBoundaryEntity);
}
