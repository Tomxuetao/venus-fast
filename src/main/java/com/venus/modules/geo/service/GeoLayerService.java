package com.venus.modules.geo.service;

import com.venus.common.base.service.BaseService;
import com.venus.common.page.PageData;
import com.venus.modules.geo.dto.GeoLayerDTO;
import com.venus.modules.geo.entity.GeoLayerEntity;

import java.util.List;
import java.util.Map;

public interface GeoLayerService extends BaseService<GeoLayerEntity> {
    PageData<GeoLayerEntity> page(Map<String, Object> params);

    void save(GeoLayerDTO dto);

    void update(GeoLayerDTO dto);

    List<GeoLayerEntity> list(Map<String, Object> params);
}
