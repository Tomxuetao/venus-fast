package com.venus.modules.job.task;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.venus.common.constant.Constant;
import com.venus.common.utils.GeoLayerUtils;
import com.venus.modules.geo.config.GeoServerConfig;
import com.venus.modules.geo.dto.GeoFeatureDTO;
import com.venus.modules.geo.dto.GeoCommonDTO;
import com.venus.modules.geo.entity.GeoLayerEntity;
import com.venus.modules.geo.service.GeoLayerService;
import com.venus.modules.sys.redis.SysParamsRedis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component("updateGeoLayerTask")
public class UpdateGeoLayerTask implements ITask {
    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    SysParamsRedis sysParamsRedis;
    @Autowired
    GeoLayerService geoLayerService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void run(String params) {
        logger.info("UpdateGeoLayerTask定时任务，参数：{}", params);

        String geoServerConfig = sysParamsRedis.get(Constant.GEOSERVER_CONFIG_KEY);
        GeoServerConfig config = new Gson().fromJson(geoServerConfig, GeoServerConfig.class);

        String bodyString = GeoLayerUtils.getRestLayerList(config);
        logger.info("layerListStr: {}", bodyString);

        String tempStr = bodyString.replace("{\"layers\":{\"layer\":", "");
        String restLayerListStr = tempStr.substring(0, tempStr.length() - 2);

        Type mapType = new TypeToken<List<GeoCommonDTO>>() {
        }.getType();
        List<GeoCommonDTO> geoRestLayers = new Gson().fromJson(restLayerListStr, mapType);

        logger.info("geoLayerList: {}", geoRestLayers);
        List<GeoFeatureDTO> geoFeatureDTOList = this.getLayerDetailList(config, geoRestLayers);
        logger.info("geoFeatureDTOList: {}", geoFeatureDTOList);

        List<GeoLayerEntity> geoLayerList = geoLayerService.list(new HashMap<>());

        logger.info("geoLayerList: {}", geoLayerList);

        List<GeoLayerEntity> needAddList = new ArrayList<>();
        List<GeoLayerEntity> needUpdateList = new ArrayList<>();
        Date now = new Date();

        for (GeoFeatureDTO geoFeatureDTO : geoFeatureDTOList) {
            if(geoLayerList.isEmpty()) {
                GeoLayerEntity addLayerEntity = this.getGeoLayerEntity(geoFeatureDTO);
                needAddList.add(addLayerEntity);
            } else {
                List<GeoLayerEntity> list = geoLayerList
                        .stream()
                        .filter(item ->
                                item.getName().equals(geoFeatureDTO.getName())
                                        && item.getDatastore().equals(geoFeatureDTO.getDatastore())
                                        && item.getWorkspace().equals(geoFeatureDTO.getWorkspace()))
                        .collect(Collectors.toList());
                if(!list.isEmpty()) {
                    list.forEach(geoLayerEntity -> {
                        geoLayerEntity.setUpdateDate(now);
                        geoLayerEntity.setTitle(geoFeatureDTO.getTitle());
                        geoLayerEntity.setNativeSrs(geoFeatureDTO.getSrs());
                        geoLayerEntity.setDatastore(geoFeatureDTO.getDatastore());
                        geoLayerEntity.setStatus(geoFeatureDTO.isEnabled() ? 1 : 0);
                        needUpdateList.add(geoLayerEntity);
                    });
                } else {
                    GeoLayerEntity addLayerEntity = this.getGeoLayerEntity(geoFeatureDTO);
                    needAddList.add(addLayerEntity);
                }
            }
        }

        List<GeoLayerEntity> needRemoveList = geoLayerList.stream().filter(item -> !needUpdateList.stream().map(GeoLayerEntity::getId).collect(Collectors.toList()).contains(item.getId())).collect(Collectors.toList());

        if(!needAddList.isEmpty()) {
            geoLayerService.insertBatch(needAddList);
        }

        if(!needUpdateList.isEmpty()) {
            geoLayerService.updateBatchById(needUpdateList);
        }

        if(!needRemoveList.isEmpty()) {
            geoLayerService.deleteBatchIds(needRemoveList.stream().map(GeoLayerEntity::getId).collect(Collectors.toList()));
        }
    }

    /**
     * 获取图层实体
     * @param geoFeatureDTO 图层详情
     */
    private GeoLayerEntity getGeoLayerEntity(GeoFeatureDTO geoFeatureDTO) {
        Date now = new Date();
        GeoLayerEntity addLayerEntity = new GeoLayerEntity();
        addLayerEntity.setType("shp");
        addLayerEntity.setCreateDate(now);
        addLayerEntity.setUpdateDate(now);
        addLayerEntity.setName(geoFeatureDTO.getName());
        addLayerEntity.setTitle(geoFeatureDTO.getTitle());
        addLayerEntity.setNativeSrs(geoFeatureDTO.getSrs());
        addLayerEntity.setDatastore(geoFeatureDTO.getDatastore());
        addLayerEntity.setStatus(geoFeatureDTO.isEnabled() ? 1 : 0);
        addLayerEntity.setWorkspace(geoFeatureDTO.getWorkspace());
        return addLayerEntity;
    }

    /**
     * 获取图层详情列表
     *
     * @param geoRestLayers 图层列表
     */
    private List<GeoFeatureDTO> getLayerDetailList(GeoServerConfig config, List<GeoCommonDTO> geoRestLayers) {
        List<GeoFeatureDTO> geoFeatureDTOList = new ArrayList<>();
        for (GeoCommonDTO geoRestLayerDTO : geoRestLayers) {
            String[] layerNameArr = geoRestLayerDTO.getName().split(":");
            if(layerNameArr.length == 2) {
                String workspace = layerNameArr[0];
                String layerName = layerNameArr[1];
                config.setLayerName(layerName);
                config.setWorkspace(workspace);
                String bodyString = GeoLayerUtils.getLayerDetail(config);
                String tempStr = bodyString.replace("{\"featureType\":", "");
                String featureTypeStr = tempStr.substring(0, tempStr.length() - 1);
                GeoFeatureDTO geoFeatureDTO = new Gson().fromJson(featureTypeStr, GeoFeatureDTO.class);
                GeoCommonDTO datastore = geoFeatureDTO.getStore();
                geoFeatureDTO.setDatastore(datastore.getName().split(":")[1]);
                geoFeatureDTO.setWorkspace(workspace);
                geoFeatureDTOList.add(geoFeatureDTO);
            }
        }
        return geoFeatureDTOList;
    }
}
