package com.venus.modules.sys.dao;

import com.venus.common.base.dao.BaseDao;
import com.venus.modules.sys.entity.SysParamsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysParamsDao extends BaseDao<SysParamsEntity> {
    /**
     * 根据参数编码，查询value
     * @param paramCode 参数编码
     * @return          参数值
     */
    String getValueByCode(String paramCode);

    /**
     * 获取参数编码列表
     * @param ids  ids
     * @return     返回参数编码列表
     */
    List<String> getParamCodeList(Long[] ids);

    /**
     * 根据参数编码，更新value
     * @param paramCode  参数编码
     * @param paramValue  参数值
     */
    int updateValueByCode(@Param("paramCode") String paramCode, @Param("paramValue") String paramValue);
}
