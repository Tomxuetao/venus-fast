package com.venus.modules.gen.dao;

import com.venus.common.base.dao.BaseDao;
import com.venus.modules.gen.dto.TableDTO;

import java.util.List;
import java.util.Map;

public interface SysGenDao extends BaseDao<TableDTO> {
    List<TableDTO> queryList(Map<String, Object> query);

    TableDTO queryTable(String tableName);

    List<TableDTO> queryColumns(String tableName);

    int createTable(String sql);
}
