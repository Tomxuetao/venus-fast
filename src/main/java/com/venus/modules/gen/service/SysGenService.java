package com.venus.modules.gen.service;

import com.venus.common.base.service.BaseService;
import com.venus.common.page.PageData;
import com.venus.modules.gen.dto.TableDTO;

import java.util.List;
import java.util.Map;

public interface SysGenService extends BaseService<TableDTO> {
    PageData<TableDTO> queryList(Map<String, Object> params);

    boolean createTable(String sql);

    byte[] genCode(String[] tableNames);

    TableDTO queryTable(String tableName);

    List<TableDTO> queryColumns(String tableName);
}
