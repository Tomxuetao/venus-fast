package com.venus.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.venus.common.base.service.impl.BaseServiceImpl;
import com.venus.common.constant.Constant;
import com.venus.common.page.PageData;
import com.venus.common.utils.GeneratorClientCredentials;
import com.venus.common.utils.ConvertUtils;
import com.venus.modules.sys.dao.SysClientDao;
import com.venus.modules.sys.dto.SysClientDTO;
import com.venus.modules.sys.entity.SysClientEntity;
import com.venus.modules.sys.service.SysClientService;
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

        return getPageData(list, list.size(), SysClientDTO.class);
    }

    @Override
    public SysClientEntity get(Long id) {
        return baseDao.selectById(id);
    }

    @Override
    public void save(SysClientDTO dto) {
       SysClientEntity entity = ConvertUtils.sourceToTarget(dto, SysClientEntity.class);

       // 生成clientId
       entity.setClientId(GeneratorClientCredentials.generateClientId());
       // 生成clientSecret
       entity.setClientSecret(GeneratorClientCredentials.generateClientSecret());
       insert(entity);
    }

    @Override
    public void update(SysClientDTO dto) {
        SysClientEntity entity = ConvertUtils.sourceToTarget(dto, SysClientEntity.class);
        updateById(entity);
    }

    @Override
    public SysClientEntity validateClient(String clientId, String clientSecret) {
        QueryWrapper<SysClientEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("client_id", clientId);
        wrapper.eq("status", 1);
        wrapper.eq("client_secret", clientSecret);
        return baseDao.selectOne(wrapper);
    }

    private QueryWrapper<SysClientEntity> getWrapper(Map<String, Object> params) {
        String appName = (String) params.get("appName");

        QueryWrapper<SysClientEntity> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(appName), "app_name", appName);
        wrapper.orderByAsc("sort");
        return wrapper;
    }
}
