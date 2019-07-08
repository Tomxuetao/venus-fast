package com.venus.modules.geo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.venus.common.utils.RedisKeys;
import com.venus.common.utils.RedisUtils;
import com.venus.modules.geo.dao.GeoJsonDao;
import com.venus.modules.geo.service.GeoJsonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("geoJsonService")
public class GeoJsonServiceImpl implements GeoJsonService {
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private GeoJsonDao geoJsonDao;

    @Override
    public String getGeoJsonById(Integer id) {
        String redisKey = RedisKeys.getGeoConfigKey(String.valueOf(id));
        if (redisUtils.get(redisKey) != null) {
            return redisUtils.get(redisKey);
        }
        String geoJson = geoJsonDao.getGeoJsonById(id);
        JSONObject geoJsonObject = JSONObject.parseObject(geoJson);
        if (geoJsonObject.get("features") != null) {
            redisUtils.set(redisKey, geoJson);
        } else {
            geoJson = null;
        }
        return geoJson;
    }
}
