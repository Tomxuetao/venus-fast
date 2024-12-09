package com.venus.modules.msg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.venus.common.base.service.impl.BaseServiceImpl;
import com.venus.common.constant.Constant;
import com.venus.common.page.PageData;
import com.venus.common.utils.ConvertUtils;
import com.venus.modules.msg.dao.SysMsgMailDao;
import com.venus.modules.msg.dto.SysMsgMailDTO;
import com.venus.modules.msg.entity.SysMsgMailEntity;
import com.venus.modules.msg.service.SysMsgMailService;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class SysSysMsgMailServiceImpl extends BaseServiceImpl<SysMsgMailDao, SysMsgMailEntity> implements SysMsgMailService {
    @Override
    public PageData<SysMsgMailDTO> page(Map<String, Object> params) {
        IPage<SysMsgMailEntity> page = baseDao.selectPage(getPage(params, Constant.CREATE_DATE, false), getWrapper(params));

        return getPageData(page, SysMsgMailDTO.class);
    }

    @Override
    public List<SysMsgMailDTO> list(Map<String, Object> params) {
      List<SysMsgMailEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, SysMsgMailDTO.class);
    }

    private QueryWrapper<SysMsgMailEntity> getWrapper(Map<String, Object> params) {
        String status = (String) params.get("status");

        QueryWrapper<SysMsgMailEntity> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(status)) {
            wrapper.eq("status", Integer.parseInt(status));
        }

        return wrapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(SysMsgMailEntity entity) {
        insert(entity);
    }
}
