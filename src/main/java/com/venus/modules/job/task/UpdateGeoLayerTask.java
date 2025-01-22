package com.venus.modules.job.task;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.venus.common.constant.Constant;
import com.venus.common.utils.GeoLayerUtils;
import com.venus.modules.geo.config.GeoServerConfig;
import com.venus.modules.geo.entity.GeoLayerEntity;
import com.venus.modules.geo.service.GeoLayerService;
import com.venus.modules.sys.redis.SysParamsRedis;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component("updateGeoLayerTask")
public class UpdateGeoLayerTask implements ITask {
    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    SysParamsRedis sysParamsRedis;
    @Autowired
    GeoLayerService geoLayerService;

    @Override
    public void run(String params) {
        logger.info("UpdateGeoLayerTask定时任务，参数：{}", params);
        logger.info("UpdateGeoLayerTask定时任务执行完毕");

        String geoServerConfig = sysParamsRedis.get(Constant.GEOSERVER_CONFIG_KEY);
        GeoServerConfig config = new Gson().fromJson(geoServerConfig, GeoServerConfig.class);
        if (config != null) {
            logger.info("UpdateGeoLayerTask定时任务开始执行");
            List<GeoLayerEntity> geoLayerList =geoLayerService.list(new HashMap<>());

            for (GeoLayerEntity geoLayerEntity : geoLayerList) {
                config.setLayerName(geoLayerEntity.getName());
                config.setDatastore(geoLayerEntity.getDatastore());
                config.setWorkspace(geoLayerEntity.getWorkspace());
                String bodyString = GeoLayerUtils.getLayerDetail(config);
                logger.info("UpdateGeoLayerTask定时任务开始执行，bodyString：{}", bodyString);
            }
        }
    }
}
