package com.venus.modules.sys.dto;

import com.venus.common.validator.group.AddGroup;
import com.venus.common.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "角色管理")
public class SysRoleUsersDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "roleId")
    @NotNull(message = "{id.require}", groups = AddGroup.class)
    @NotNull(message = "{id.require}", groups = UpdateGroup.class)
    private Long roleId;

    @ApiModelProperty(value = "用户ID列表")
    private List<Long> userIds;
}
