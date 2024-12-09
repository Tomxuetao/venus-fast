package com.venus.modules.gen.controller;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import cn.hutool.core.io.IoUtil;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.venus.common.page.PageData;
import com.venus.common.utils.Result;
import com.venus.modules.gen.dto.TableDTO;
import com.venus.modules.gen.service.SysGenService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sys/gen")
public class SysGenController {
    @Resource
    private SysGenService sysGenService;

    /**
     * 列表
     */
    @GetMapping("list")
    public Result list(@RequestParam Map<String, Object> params) {
        PageData<TableDTO> page = sysGenService.queryList(params);

        return new Result<>().ok(page);
    }

    @PostMapping("createTable")
    public Result createTable(@RequestBody Map<String, Object> body) {
        String sql = (String) body.get("sql");
        List<String> tableNames = new ArrayList<>();
        List<SQLStatement> sqlStatements = SQLUtils.parseStatements(sql, DbType.postgresql);
        for (SQLStatement sqlStatement : sqlStatements) {
            if (sqlStatement instanceof SQLCreateTableStatement) {
                SQLCreateTableStatement createTableStatement = (SQLCreateTableStatement) sqlStatement;
                if (sysGenService.createTable(createTableStatement.toString())) {
                    tableNames.add(createTableStatement.getTableName());
                }
            } else {
                sysGenService.createTable(sqlStatement.toString());
            }
        }
        System.out.println(tableNames);
        return new Result();
    }

    /**
     * 生成代码
     */
    @GetMapping("code")
    public void code(String tables, HttpServletResponse response) throws IOException {
        byte[] data = sysGenService.genCode(tables.split(","));

        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"venus.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");

        IoUtil.write(response.getOutputStream(), false, data);
    }
}
