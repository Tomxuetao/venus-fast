package com.venus.modules.geo.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.venus.common.config.GeometryTypeHandler;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "地理数据")
public class GeoDataDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String props;

    @TableField(typeHandler = GeometryTypeHandler.class)
    private String geom;
}
