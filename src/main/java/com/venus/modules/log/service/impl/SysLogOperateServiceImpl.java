package com.venus.modules.log.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.venus.common.base.service.impl.BaseServiceImpl;
import com.venus.common.constant.Constant;
import com.venus.common.page.PageData;
import com.venus.common.utils.ConvertUtils;
import com.venus.modules.log.dao.SysLogOperateDao;
import com.venus.modules.log.dto.SysLogOperateDTO;
import com.venus.modules.log.entity.SysLogOperateEntity;
import com.venus.modules.log.service.SysLogOperateService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class SysLogOperateServiceImpl extends BaseServiceImpl<SysLogOperateDao, SysLogOperateEntity> implements SysLogOperateService {
    @Override
    public PageData<SysLogOperateDTO> page(Map<String, Object> params) {
        IPage<SysLogOperateEntity> page = baseDao.selectPage(
                getPage(params, Constant.CREATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, SysLogOperateDTO.class);
    }

    @Override
    public List<SysLogOperateDTO> list(Map<String, Object> params) {
        List<SysLogOperateEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, SysLogOperateDTO.class);
    }

    private QueryWrapper<SysLogOperateEntity> getWrapper(Map<String, Object> params) {
        String status = (String) params.get("status");

        QueryWrapper<SysLogOperateEntity> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(status)) {
            wrapper.eq("status", Integer.parseInt(status));
        }

        return wrapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(SysLogOperateEntity entity) {
        insert(entity);
    }

}
