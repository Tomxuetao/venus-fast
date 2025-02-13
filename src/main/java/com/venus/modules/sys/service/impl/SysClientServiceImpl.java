package com.venus.modules.oauth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.venus.common.base.service.impl.BaseServiceImpl;
import com.venus.common.constant.Constant;
import com.venus.common.page.PageData;
import com.venus.common.utils.ConvertUtils;
import com.venus.modules.oauth.dao.SysClientDao;
import com.venus.modules.oauth.dto.SysClientDTO;
import com.venus.modules.oauth.entity.SysClientEntity;
import com.venus.modules.oauth.service.SysClientService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SysClientServiceImpl extends BaseServiceImpl<SysClientDao, SysClientEntity> implements SysClientService {
    @Override
    public PageData<SysClientDTO> page(Map<String, Object> params) {
        //分页
        IPage<SysClientEntity> page = getPage(params, Constant.CREATE_DATE, true);

        //查询
        List<SysClientEntity> list = baseDao.selectList(getWrapper(params));

        return getPageData(list, page.getTotal(), SysClientDTO.class);
    }

    @Override
    public SysClientDTO get(Long id) {
        SysClientEntity entity = baseDao.selectById(id);
        return ConvertUtils.sourceToTarget(entity, SysClientDTO.class);
    }

    @Override
    public void save(SysClientDTO dto) {
       SysClientEntity entity = ConvertUtils.sourceToTarget(dto, SysClientEntity.class);

       insert(entity);
    }

    private QueryWrapper<SysClientEntity> getWrapper(Map<String, Object> params) {
        String appName = (String) params.get("appName");

        QueryWrapper<SysClientEntity> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(appName), "app_name", appName);
        wrapper.orderByAsc("sort");
        return wrapper;
    }
}
