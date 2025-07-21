package com.venus.modules.city.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.venus.common.base.service.impl.BaseServiceImpl;
import com.venus.common.constant.Constant;
import com.venus.common.page.PageData;
import com.venus.common.utils.ConvertUtils;
import com.venus.common.utils.ExcelUtils;
import com.venus.modules.city.dao.CitySiteDao;
import com.venus.modules.city.entity.CitySiteEntity;
import com.venus.modules.city.excel.CitySiteExcel;
import com.venus.modules.city.service.CitySiteService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
public class CitySiteServiceImpl extends BaseServiceImpl<CitySiteDao, CitySiteEntity> implements CitySiteService {

    @Override
    public PageData<CitySiteEntity> page(Map<String, Object> params) {
        //转换成like
        paramsToLike(params, "name");
        IPage<CitySiteEntity> page = baseDao.selectPage(getPage(params, Constant.CREATE_DATE, false), getWrapper(params));

        return getPageData(page, CitySiteEntity.class);
    }

    @Override
    public List<CitySiteEntity> list(Map<String, Object> params) {
        return baseDao.selectList(getWrapper(params));
    }

    @Override
    public List<CitySiteEntity> importExcel(MultipartFile file) throws Exception {
        List<CitySiteExcel> list = ExcelUtils.importExcel(file, CitySiteExcel.class);

        return ConvertUtils.sourceToTarget(list, CitySiteEntity.class);
    }

    private QueryWrapper<CitySiteEntity> getWrapper(Map<String, Object> params) {
        String name = (String)params.get("name");
        String area = (String)params.get("area");
        String status = (String) params.get("status");

        QueryWrapper<CitySiteEntity> wrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(name)){
            wrapper.like("name", name);
        }
        if(StringUtils.isNotBlank(area)){
            wrapper.eq("area", area);
        }
        if (StringUtils.isNotBlank(status)) {
            wrapper.eq("status", Integer.parseInt(status));
        }

        return wrapper;
    }
}
