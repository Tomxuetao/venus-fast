package com.venus.modules.city.excel;

import cn.idev.excel.annotation.ExcelProperty;
import com.venus.modules.city.excel.converter.ShowConverter;
import lombok.Data;

@Data
public class CitySiteExcel {
    @ExcelProperty("点位名称")
    private String name;

    @ExcelProperty("城区")
    private String area;

    @ExcelProperty("经度")
    private String lng;

    @ExcelProperty("纬度")
    private String lat;

    @ExcelProperty("摊位数量")
    private String num;

    @ExcelProperty("业态类型")
    private String type;

    @ExcelProperty("业态分布")
    private String dist;

    @ExcelProperty("点位介绍")
    private String intro;

    @ExcelProperty("街道")
    private String street;

    @ExcelProperty("详细地址")
    private String address;

    @ExcelProperty("营业时间")
    private String busHours;

    @ExcelProperty(value = "手绘地图", converter = ShowConverter.class)
    private Integer isShow;
}
