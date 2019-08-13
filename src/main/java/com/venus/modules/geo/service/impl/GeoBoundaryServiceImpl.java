package com.venus.modules.geo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.venus.common.utils.PageUtils;
import com.venus.common.utils.Query;
import com.venus.common.utils.RedisKeys;
import com.venus.common.utils.RedisUtils;
import com.venus.modules.geo.dao.GeoBoundaryDao;
import com.venus.modules.geo.entity.GeoBoundaryEntity;
import com.venus.modules.geo.service.GeoBoundaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

@Service("geoJsonService")
public class GeoBoundaryServiceImpl extends ServiceImpl<GeoBoundaryDao, GeoBoundaryEntity> implements GeoBoundaryService {
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private GeoBoundaryDao geoBoundaryDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String getGeoJsonById(Integer id) {
        String redisKey = RedisKeys.getGeoBoundaryKey(String.valueOf(id));
        if (redisUtils.get(redisKey) != null) {
            return redisUtils.get(redisKey);
        }
        String geoJson = geoBoundaryDao.getGeoJsonById(id);
        JSONObject geoJsonObject = JSONObject.parseObject(geoJson);
        if (geoJsonObject.get("features") != null) {
            redisUtils.set(redisKey, geoJson);
        } else {
            geoJson = null;
        }
        return geoJson;
    }

    @Override
    public void saveGeomText(GeoBoundaryEntity geoBoundaryEntity) {
        geoBoundaryEntity.setCreateTime(new Date());
        geoBoundaryDao.saveGeomText(geoBoundaryEntity);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Long createUserId = (Long) params.get("createUserId");
        String areaCode = (String) params.get("areaCode");
        IPage<GeoBoundaryEntity> page = this.page(
                new Query<GeoBoundaryEntity>().getPage(params),
                new QueryWrapper<GeoBoundaryEntity>()
                        .eq(createUserId != null, "create_user_id", createUserId)
                        .like(areaCode != null, "area_code", areaCode)
        );
        return new PageUtils(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(Long[] boundaryIds) {
        for (Long id : boundaryIds) {
            String redisKey = RedisKeys.getGeoBoundaryKey(String.valueOf(id));
            redisUtils.delete(redisKey);
        }
        this.removeByIds(Arrays.asList(boundaryIds));
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(GeoBoundaryEntity geoBoundaryEntity) {
        this.updateById(geoBoundaryEntity);
        String redisKey = RedisKeys.getGeoBoundaryKey(String.valueOf(geoBoundaryEntity.getGid()));
        redisUtils.delete(redisKey);
    }

}
