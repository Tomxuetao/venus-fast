package com.venus.modules.geo.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@ApiModel(value = "Geoserver Rest Layer")
@EqualsAndHashCode(callSuper = false)
public class GeoRestLayerDTO {
    private String name;
    private String href;
}
