package com.venus.modules.city.service;

import com.venus.common.base.service.BaseService;
import com.venus.common.page.PageData;
import com.venus.modules.city.entity.CitySiteEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface CitySiteService extends BaseService<CitySiteEntity> {
    PageData<CitySiteEntity> page(Map<String, Object> params);

    List<CitySiteEntity> list(Map<String, Object> params);

    List<CitySiteEntity> importExcel(MultipartFile file) throws Exception;
}
