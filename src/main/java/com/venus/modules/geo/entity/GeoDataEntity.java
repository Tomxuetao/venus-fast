package com.venus.modules.geo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.venus.common.base.entity.BaseEntity;
import com.venus.common.config.GeometryTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.locationtech.jts.geom.Geometry;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("geo_data")
public class GeoDataEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long sourceId;

    private String props;

    @TableField(typeHandler = GeometryTypeHandler.class)
    private Geometry geom;
}
