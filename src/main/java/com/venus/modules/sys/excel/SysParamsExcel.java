package com.venus.modules.sys.excel;

import cn.idev.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class SysParamsExcel {
    @ExcelProperty("参数编码")
    private String paramCode;
    @ExcelProperty("参数值")
    private String paramValue;
    @ExcelProperty("备注")
    private String remark;
}
