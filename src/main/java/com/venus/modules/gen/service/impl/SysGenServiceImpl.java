package com.venus.modules.gen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.venus.common.base.service.impl.BaseServiceImpl;
import com.venus.common.page.PageData;
import com.venus.common.utils.GenUtils;
import com.venus.modules.gen.dao.SysGenDao;
import com.venus.modules.gen.dto.TableDTO;
import com.venus.modules.gen.service.SysGenService;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Service;

@Service
public class SysGenServiceImpl extends BaseServiceImpl<SysGenDao, TableDTO> implements SysGenService {
    @Resource
    private SysGenDao sysGenDao;

    @Override
    public PageData<TableDTO> queryList(Map<String, Object> params) {
        IPage<TableDTO> page = getPage(params, "", false);

        List<TableDTO> list = baseDao.queryList(params);
        return getPageData(list, page.getTotal(), TableDTO.class);
    }

    @Override
    public TableDTO queryTable(String tableName) {
        return sysGenDao.queryTable(tableName);
    }

    @Override
    public List<TableDTO> queryColumns(String tableName) {
        return sysGenDao.queryColumns(tableName);
    }

    @Override
    public byte[] genCode(String[] tableNames) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);

        for (String tableName : tableNames) {
            //查询表信息
            TableDTO table = queryTable(tableName);
            //查询列信息
            List<TableDTO> columns = queryColumns(tableName);
            //生成代码
            GenUtils.genCode(table, columns, zip);
        }
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    @Override
    public boolean createTable(String sql) {
        return sysGenDao.createTable(sql) == 0;
    }

    private QueryWrapper<TableDTO> getWrapper(Map<String, Object> params) {
        String tableName = (String) params.get("tableName");

        QueryWrapper<TableDTO> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(tableName), "table_name", tableName);

        return wrapper;
    }
}
