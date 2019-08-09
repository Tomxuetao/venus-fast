package com.venus.modules.geo.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.venus.common.utils.PageUtils;
import com.venus.modules.geo.entity.GeoBoundaryEntity;

import java.util.Map;

public interface GeoBoundaryService extends IService<GeoBoundaryEntity> {
    String getGeoJsonById(Integer id);

    void saveGeomText(GeoBoundaryEntity geoBoundaryEntity);

    PageUtils queryPage(Map<String, Object> params);
}
