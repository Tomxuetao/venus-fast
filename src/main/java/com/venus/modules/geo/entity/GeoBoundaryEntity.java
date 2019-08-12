package com.venus.modules.geo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("geo_boundary")
public class GeoBoundaryEntity {
    @TableId
    private Integer gid;
    private String name;
    private Integer layer;
    private Integer height;
    private String color;
    private String selectedColor;
    private String areaCode;
    private Long createUserId;
    private Date createTime;
    @TableField(exist = false)
    private String geomText;
}
