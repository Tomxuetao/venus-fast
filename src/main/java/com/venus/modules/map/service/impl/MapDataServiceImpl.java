package com.venus.modules.map.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.venus.common.utils.PageUtils;
import com.venus.common.utils.Query;
import com.venus.modules.map.dao.MapDataDao;
import com.venus.modules.map.entity.MapDataEntity;
import com.venus.modules.map.service.MapDataService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

@Service("mapDataService")
public class MapDataServiceImpl extends ServiceImpl<MapDataDao, MapDataEntity> implements MapDataService {
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String username = (String) params.get("name");
        Long createUserId = (Long) params.get("createUserId");
        IPage<MapDataEntity> page = this.page(
                new Query<MapDataEntity>().getPage(params),
                new QueryWrapper<MapDataEntity>()
                        .like(StringUtils.isNotBlank(username), "name", username)
                        .eq(createUserId != null, "create_user_id", createUserId)
        );

        return new PageUtils(page);
    }

    @Override
    public void saveMapData(MapDataEntity mapDataEntity) {
        mapDataEntity.setCreateTime(new Date());
    }

    @Override
    public void deleteBatch(Long[] ids) {
        this.removeByIds(Arrays.asList(ids));
    }
}
