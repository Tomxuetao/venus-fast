package com.venus.modules.geo.dao;

import com.venus.modules.geo.form.BuildingForm;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GeoJsonDao {
    String getGeoJsonById(Integer id);

    void saveGeomText(BuildingForm buildingForm);
}
