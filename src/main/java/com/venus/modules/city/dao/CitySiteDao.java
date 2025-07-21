package com.venus.modules.city.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.venus.modules.city.entity.CitySiteEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CitySiteDao extends BaseMapper<CitySiteEntity> {
}
