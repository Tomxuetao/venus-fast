package com.venus.modules.geo.controller;

import com.alibaba.fastjson.JSON;
import com.venus.common.utils.R;
import com.venus.modules.geo.service.GeoJsonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/geo/data")
public class GeoJsonController {
    @Autowired
    private GeoJsonService geoJsonService;

    @PostMapping("/json")
    public R getGeoJsonById(@RequestBody Map<String, String> param) {
        System.out.println(param);
        Integer id = Integer.valueOf(param.get("id"));
        JSON geoJson = JSON.parseObject(geoJsonService.getGeoJsonById(id));
        if (geoJson != null) {
            System.out.println(geoJson);
            return R.ok().put("geoJson", geoJson);
        }
        return R.error("未查询到相关数据");
    }
}
