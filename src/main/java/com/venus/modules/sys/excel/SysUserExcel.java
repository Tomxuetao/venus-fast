package com.venus.modules.sys.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;

import com.venus.modules.sys.excel.converter.GenderConverter;
import com.venus.modules.sys.excel.converter.StatusConverter;
import lombok.Data;

import java.util.Date;

@Data
public class SysUserExcel {
    @ExcelProperty("用户名")
    private String username;
    @ExcelProperty("姓名")
    private String realName;
    @ExcelProperty(value = "性别", converter = GenderConverter.class)
    private Integer gender;
    @ExcelProperty("邮箱")
    private String email;
    @ExcelProperty("手机号")
    private String mobile;
    @ExcelProperty("部门名称")
    private String deptName;
    @ExcelProperty(value = "状态", converter = StatusConverter.class)
    private Integer status;
    @ExcelProperty("备注")
    private String remark;
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty("创建时间")
    private Date createDate;
}
