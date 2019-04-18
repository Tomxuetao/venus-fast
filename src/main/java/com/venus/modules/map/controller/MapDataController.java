package com.venus.modules.map.controller;

import com.venus.common.annotation.SysLog;
import com.venus.common.utils.Constant;
import com.venus.common.utils.PageUtils;
import com.venus.common.utils.R;
import com.venus.common.validator.ValidatorUtils;
import com.venus.common.validator.group.AddGroup;
import com.venus.modules.map.entity.MapDataEntity;
import com.venus.modules.map.service.MapDataService;
import com.venus.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 地理数据
 */
@RestController
@RequestMapping("/map/data")
public class MapDataController extends AbstractController {
    @Autowired
    private MapDataService mapDataService;

    /**
     * 所有用户列表
     */
    @GetMapping("/list")
    @RequiresPermissions("map:data:list")
    public R list(@RequestParam Map<String, Object> params) {
        //只有超级管理员，才能查看所有管理员列表
        if (getUserId() != Constant.SUPER_ADMIN) {
            params.put("createUserId", getUserId());
        }
        PageUtils page = mapDataService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 保存用户
     */
    @SysLog("保存范围数据")
    @PostMapping("/save")
    @RequiresPermissions("map:data:save")
    public R save(@RequestBody MapDataEntity mapDataEntity) {
        ValidatorUtils.validateEntity(mapDataEntity, AddGroup.class);

        mapDataEntity.setCreateUserId(getUserId());
        mapDataService.saveMapData(mapDataEntity);
        return R.ok();
    }

    @SysLog("删除边界数据")
    @PostMapping("/delete")
    @RequiresPermissions("map:data:delete")
    public R delete(@RequestBody Long[] ids) {
        mapDataService.deleteBatch(ids);
        return R.ok();
    }
}
