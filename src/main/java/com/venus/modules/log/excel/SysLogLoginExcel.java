package com.venus.modules.log.excel;

import cn.idev.excel.annotation.ExcelProperty;
import cn.idev.excel.annotation.format.DateTimeFormat;
import cn.idev.excel.annotation.write.style.ColumnWidth;
import cn.idev.excel.annotation.write.style.ContentRowHeight;
import cn.idev.excel.annotation.write.style.HeadRowHeight;
import com.venus.modules.log.excel.converter.SysLogOperationConverter;
import com.venus.modules.log.excel.converter.SysLogStatusConverter;
import lombok.Data;

import java.util.Date;

@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
@ColumnWidth(25)
public class SysLogLoginExcel {
    @ExcelProperty(value = "操作类型", converter = SysLogOperationConverter.class)
    private Integer operation;

    @ExcelProperty(value = "状态", converter = SysLogStatusConverter.class)
    private Integer status;

    @ExcelProperty("User-Agent")
    private String userAgent;

    @ExcelProperty("操作IP")
    private String ip;

    @ExcelProperty("用户名")
    private String creatorName;

    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty("创建时间")
    private Date createDate;

}