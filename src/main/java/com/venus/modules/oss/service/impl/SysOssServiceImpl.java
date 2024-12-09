package com.venus.modules.oss.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.venus.common.base.service.impl.BaseServiceImpl;
import com.venus.common.constant.Constant;
import com.venus.common.page.PageData;
import com.venus.modules.oss.cloud.OSSFactory;
import com.venus.modules.oss.dao.SysOssDao;
import com.venus.modules.oss.entity.SysOssEntity;
import com.venus.modules.oss.service.SysOssService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class SysOssServiceImpl extends BaseServiceImpl<SysOssDao, SysOssEntity> implements SysOssService {

    @Override
    public PageData<SysOssEntity> page(Map<String, Object> params) {
        IPage<SysOssEntity> page = baseDao.selectPage(
                getPage(params, Constant.CREATE_DATE, false),
                new QueryWrapper<>()
        );
        return getPageData(page, SysOssEntity.class);
    }

    @Override
    public Map<String, Object> upload(MultipartFile file) throws IOException {
        //上传文件
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String name = FilenameUtils.getName(file.getOriginalFilename());
        String url = Objects.requireNonNull(OSSFactory.build()).uploadSuffix(file.getBytes(), extension);

        //保存文件信息
        SysOssEntity ossEntity = new SysOssEntity();
        ossEntity.setUrl(url);
        ossEntity.setName(name);
        ossEntity.setCreateDate(new Date());
        this.insert(ossEntity);
        Map<String, Object> data = new HashMap<>(1);
        data.put("src", url);
        data.put("name", name);
        return data;
    }
}
