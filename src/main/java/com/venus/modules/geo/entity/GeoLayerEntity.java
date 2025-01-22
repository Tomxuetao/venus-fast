package com.venus.modules.geo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.venus.common.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("geo_layer")
public class GeoLayerEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private String name;

    private String type;

    private String title;

    private String ossUrl;

    private Integer status;

    private String nativeSrs;

    private String workspace;

    private String datastore;
}
