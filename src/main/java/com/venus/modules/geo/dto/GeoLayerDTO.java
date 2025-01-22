package com.venus.modules.geo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.venus.common.validator.group.AddGroup;
import com.venus.common.validator.group.DefaultGroup;
import com.venus.common.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;

@Data
@ApiModel(value = "GeoServer 图层")
public class GeoLayerDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @Null(message = "{id.null}", groups = AddGroup.class)
    @NotNull(message = "{id.require}", groups = UpdateGroup.class)
    private Long id;

    @ApiModelProperty(value = "图层名称", required = true)
    @NotBlank(message = "图层名称不能为空", groups = DefaultGroup.class)
    private String name;

    @ApiModelProperty(value = "图层标题")
    @NotBlank(message = "图层标题不能为空", groups = DefaultGroup.class)
    private String title;

    @ApiModelProperty(value = "数据地址", required = true)
    @NotBlank(message = "数据地址不能为空", groups = DefaultGroup.class)
    private String ossUrl;

    @ApiModelProperty(value = "工作空间", required = true)
    @NotBlank(message = "工作空间不能为空", groups = DefaultGroup.class)
    private String workspace;

    @ApiModelProperty(value = "存储仓库", required = true)
    @NotBlank(message = "存储仓库不能为空", groups = DefaultGroup.class)
    private String datastore;
}
