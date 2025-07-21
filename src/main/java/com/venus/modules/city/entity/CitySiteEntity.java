package com.venus.modules.city.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("city_site")

public class CitySiteEntity {

    @TableId
    private Long id;

    private String lng;

    private String lat;

    private String x;

    private String y;

    private String name;

    private String area;

    private String num;

    private String type;

    private String dist;

    private String files;

    private String intro;

    /**
     * 是否展示在手绘地图  0：不展示   1：展示
     */
    private Integer isShow;

    /**
     * 状态  0：停用   1：正常
     */
    private Integer status;

    private String street;

    private String address;

    private String busHours;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createDate;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateDate;
}
