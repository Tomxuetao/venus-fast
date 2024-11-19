package com.venus.modules.sys.dao;

import com.venus.common.base.dao.BaseDao;
import com.venus.modules.sys.entity.DictData;
import com.venus.modules.sys.entity.SysDictDataEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysDictDataDao extends BaseDao<SysDictDataEntity> {
    /**
     * 字典数据列表
     */
    List<DictData> getDictDataList();
}
