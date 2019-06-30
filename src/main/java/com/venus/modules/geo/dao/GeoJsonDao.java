package com.venus.modules.geo.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GeoJsonDao {
    String getGeoJsonById(Integer id);
}
