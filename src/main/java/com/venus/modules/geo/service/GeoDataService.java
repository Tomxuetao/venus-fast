package com.venus.modules.geo.service;

import com.venus.common.base.service.BaseService;
import com.venus.modules.geo.dto.GeoDataDTO;
import com.venus.modules.geo.entity.GeoDataEntity;

import java.util.List;

public interface GeoDataService extends BaseService<GeoDataEntity> {
    // 删除
    void deleteBySourceIds(Long[] id);

    // 创建
    void create(List<GeoDataEntity> dataList);

    // 获取指定数据源的所有点
    List<GeoDataDTO> getListBySourceId(Long source);

    // 获取指定数据源的所有wkb
    List<GeoDataDTO> getWkbBySourceId(Long sourceId, Double dilution);

    // 获取指定数据源的指定精度的点(Java实现抽稀)
    List<GeoDataDTO> getListBySourceId(Long source, Double dilution);

    // 获取指定数据源的指定精度的点(SQL实现抽稀)
    List<GeoDataDTO> dilutionBySourceId(Long sourceId, Double dilution);
}
