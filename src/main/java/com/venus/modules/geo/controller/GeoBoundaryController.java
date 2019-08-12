package com.venus.modules.geo.controller;

import com.alibaba.fastjson.JSON;
import com.venus.common.annotation.SysLog;
import com.venus.common.utils.Constant;
import com.venus.common.utils.PageUtils;
import com.venus.common.utils.R;
import com.venus.modules.geo.entity.GeoBoundaryEntity;
import com.venus.modules.geo.service.GeoBoundaryService;
import com.venus.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/geo/boundary")
public class GeoBoundaryController extends AbstractController {
    @Autowired
    private GeoBoundaryService geoBoundaryService;

    @PostMapping("/json")
    public R getGeoJsonById(@RequestBody Map<String, String> param) {
        Integer id = Integer.valueOf(param.get("id"));
        JSON geoJson = JSON.parseObject(geoBoundaryService.getGeoJsonById(id));
        if (geoJson != null) {
            return R.ok().put("geoJson", geoJson);
        }
        return R.error("未查询到相关数据");
    }

    @PostMapping("/save")
    @RequiresPermissions("geo:boundary:save")
    public R saveGeomText(@RequestBody GeoBoundaryEntity geoBoundaryEntity) {
        geoBoundaryEntity.setCreateUserId(getUserId());
        geoBoundaryService.saveGeomText(geoBoundaryEntity);
        return R.ok();
    }

    @GetMapping("/list")
    @RequiresPermissions("geo:boundary:list")
    public R list(@RequestParam Map<String, Object> params) {
        if (getUserId() != Constant.SUPER_ADMIN) {
            params.put("createUserId", getUserId());
        }
        PageUtils page = geoBoundaryService.queryPage(params);
        return R.ok().put("page", page);
    }

    @GetMapping("/info/{gid}")
    @RequiresPermissions("geo:boundary:info")
    public R info(@PathVariable("gid") Integer gid){
        return R.ok().put("boundary", geoBoundaryService.getById(gid));
    }

    @SysLog("删除周界")
    @PostMapping("/delete")
    @RequiresPermissions("geo:boundary:delete")
    public R delete (@RequestBody Long[] boundaryIds) {
        geoBoundaryService.deleteBatch(boundaryIds);
        return R.ok();
    }

    @SysLog("修改周界")
    @PostMapping("/update")
    @RequiresPermissions("geo:boundary:update")
    public R update(@RequestBody GeoBoundaryEntity geoBoundaryEntity){
        geoBoundaryService.update(geoBoundaryEntity);
        return R.ok();
    }
}
