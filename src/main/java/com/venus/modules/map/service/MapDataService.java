package com.venus.modules.map.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.venus.common.utils.PageUtils;
import com.venus.modules.map.entity.MapDataEntity;

import java.util.Map;

public interface MapDataService extends IService<MapDataEntity> {


    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存数据
     */

    void saveMapData(MapDataEntity mapDataEntity);


    void deleteBatch(Long[] ids);
}
