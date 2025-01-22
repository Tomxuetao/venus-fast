package com.venus.modules.oss.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.venus.common.base.service.impl.BaseServiceImpl;
import com.venus.common.constant.Constant;
import com.venus.common.exception.ErrorCode;
import com.venus.common.exception.VenusException;
import com.venus.common.page.PageData;
import com.venus.modules.oss.cloud.OSSFactory;
import com.venus.modules.oss.dao.SysOssDao;
import com.venus.modules.oss.entity.SysOssEntity;
import com.venus.modules.oss.service.SysOssService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class SysOssServiceImpl extends BaseServiceImpl<SysOssDao, SysOssEntity> implements SysOssService {

    @Override
    public PageData<SysOssEntity> page(Map<String, Object> params) {
        QueryWrapper<SysOssEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("source", params.get("source"));
        IPage<SysOssEntity> page = baseDao.selectPage(
                getPage(params, Constant.CREATE_DATE, false),
                queryWrapper
        );
        return getPageData(page, SysOssEntity.class);
    }

    @Override
    public boolean exists(String url) {
        return Objects.requireNonNull(OSSFactory.build()).exists(url);
    }

    @Override
    public Map<String, Object> upload(MultipartFile file) {
        return this.upload(file, Constant.OssSource.DEFAULT.getValue());
    }

    @Override
    public Map<String, Object> upload(MultipartFile file, Integer source) {
        //上传文件
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String name = FilenameUtils.getName(file.getOriginalFilename());
        String url;
        try {
            url = Objects.requireNonNull(OSSFactory.build()).uploadSuffix(file.getBytes(), extension);
        } catch (IOException e) {
            throw new VenusException(ErrorCode.OSS_UPLOAD_FILE_ERROR, e);
        }

        //保存文件信息
        SysOssEntity ossEntity = new SysOssEntity();
        ossEntity.setUrl(url);
        ossEntity.setName(name);
        ossEntity.setSource(source);
        ossEntity.setCreateDate(new Date());
        this.insert(ossEntity);
        Map<String, Object> data = new HashMap<>(1);
        data.put("src", url);
        data.put("name", name);
        data.put("id", ossEntity.getId());
        return data;
    }

    @Override
    public InputStream readStream(String url) {
        return Objects.requireNonNull(OSSFactory.build()).readStream(url);
    }
}
