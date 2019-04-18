package com.venus.modules.map.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.venus.common.validator.group.AddGroup;
import com.venus.common.validator.group.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@Data
@TableName("map_data")
public class MapDataEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableId
    private Long id;

    /**
     * 范围
     */
    @NotBlank(message="范围不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String bounds;
    /**
     * 中心坐标
     */
    @NotBlank(message="中心点不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String center;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建者ID
     */
    private Long createUserId;

    /**
     * 名称
     */
    private String name;
    /**
     * 状态  0：禁用   1：正常
     */
    private Integer status;
}
