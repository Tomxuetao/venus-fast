package com.venus.modules.log.excel;

import cn.idev.excel.annotation.ExcelProperty;
import cn.idev.excel.annotation.format.DateTimeFormat;
import cn.idev.excel.annotation.write.style.ColumnWidth;
import cn.idev.excel.annotation.write.style.ContentRowHeight;
import cn.idev.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.util.Date;

@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
@ColumnWidth(25)
public class SysLogErrorExcel {
    @ExcelProperty("请求URI")
    private String requestUri;

    @ExcelProperty("请求方式")
    private String requestMethod;

    @ExcelProperty("请求参数")
    private String requestParams;

    @ExcelProperty("User-Agent")
    private String userAgent;

    @ExcelProperty("异常详情")
    private String errorInfo;

    @ExcelProperty("操作IP")
    private String ip;

    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty("创建时间")
    private Date createDate;
}
