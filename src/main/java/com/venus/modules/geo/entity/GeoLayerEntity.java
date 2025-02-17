package com.venus.modules.geo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("geo_layer")
@EqualsAndHashCode(callSuper = false)
public class GeoLayerEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private Long id;

    private String name;

    private String type;

    private String title;

    private String ossUrl;

    private Integer status;

    private String nativeSrs;

    private String workspace;

    private String datastore;

    private Long creator;

    private Date createDate;

    private Date updateDate;
}
