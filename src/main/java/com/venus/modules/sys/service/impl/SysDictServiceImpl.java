package com.venus.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.venus.common.base.service.impl.BaseServiceImpl;
import com.venus.common.page.PageData;
import com.venus.common.utils.ConvertUtils;
import com.venus.modules.sys.dao.SysDictDao;
import com.venus.modules.sys.dto.SysDictDTO;
import com.venus.modules.sys.entity.SysDictEntity;
import com.venus.modules.sys.service.SysDictService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class SysDictServiceImpl extends BaseServiceImpl<SysDictDao, SysDictEntity> implements SysDictService {

    @Override
    public PageData<SysDictDTO> page(Map<String, Object> params) {
        IPage<SysDictEntity> page = baseDao.selectPage(getPage(params, "sort", true), getWrapper(params));

        return getPageData(page, SysDictDTO.class);
    }

    @Override
    public SysDictDTO get(Long id) {
        SysDictEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, SysDictDTO.class);
    }

    @Override
    public List<SysDictEntity> list(Map<String, Object> params) {
        return baseDao.selectList(getWrapper(params));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(SysDictDTO dto) {
        SysDictEntity entity = ConvertUtils.sourceToTarget(dto, SysDictEntity.class);

        insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysDictDTO dto) {
        SysDictEntity entity = ConvertUtils.sourceToTarget(dto, SysDictEntity.class);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long[] ids) {
        //删除
        deleteBatchIds(Arrays.asList(ids));
    }

    private QueryWrapper<SysDictEntity> getWrapper(Map<String, Object> params) {;
        String label = (String) params.get("label");
        String value = (String) params.get("value");

        QueryWrapper<SysDictEntity> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(label), "label", label);
        wrapper.like(StringUtils.isNotBlank(value), "value", value);

        return wrapper;
    }
}
