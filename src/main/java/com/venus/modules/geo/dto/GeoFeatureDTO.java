package com.venus.modules.geo.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@ApiModel(value = "地理要素")
@EqualsAndHashCode(callSuper = false)
public class FeatureType implements Serializable {
    private static final long serialVersionUID = 1L;

    private String srs;
    private String name;
    private String title;
    private String enabled;
    private String nativeName;
}
