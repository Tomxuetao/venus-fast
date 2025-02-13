package com.venus.modules.sys.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.venus.common.validator.group.AddGroup;
import com.venus.common.validator.group.DefaultGroup;
import com.venus.common.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(value = "客户端管理")
public class SysClientDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @Null(message = "{id.null}", groups = AddGroup.class)
    @NotNull(message = "{id.require}", groups = UpdateGroup.class)
    private Long id;

    @ApiModelProperty(value = "应用图标")
    private String appIcon;

    @ApiModelProperty(value = "应用类型", required = true)
    @Range(min = 0, max = 1, message = "类型值范围0~1", groups = DefaultGroup.class)
    private Integer appType;

    @ApiModelProperty(value = "应用名称", required = true)
    @NotBlank(message = "应用名称不能为空", groups = DefaultGroup.class)
    private String appName;

    @ApiModelProperty(value = "应用地址", required = true)
    @NotBlank(message = "应用地址不能为空", groups = DefaultGroup.class)
    private String redirectUri;

    @ApiModelProperty(value = "应用分类", required = true)
    @Min(value = 0, message = "{sort.number}", groups = DefaultGroup.class)
    private Integer classify;

    @ApiModelProperty(value = "排序")
    @Min(value = 0, message = "{sort.number}", groups = DefaultGroup.class)
    private Integer sort;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "状态  0：停用    1：正常", required = true)
    @Range(min = 0, max = 1, message = "状态取值范围0~1", groups = DefaultGroup.class)
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date createDate;

}
