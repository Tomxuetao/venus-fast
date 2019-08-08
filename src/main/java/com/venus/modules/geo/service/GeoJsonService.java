package com.venus.modules.geo.service;


import com.venus.modules.geo.form.BuildingForm;

public interface GeoJsonService {
    String getGeoJsonById(Integer id);

    void saveGeomText(BuildingForm buildingForm);
}
