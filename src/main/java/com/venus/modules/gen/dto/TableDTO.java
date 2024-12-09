package com.venus.modules.gen.dto;

import lombok.Data;

import java.util.Date;

@Data
public class TableDTO {
      //表的名称
    private String tableName;
     //表的备注
    private String tableComment;
     //列名
    private String columnName;
    //列名类型
    private String dataType;
    //列名备注
    private String columnComment;
    //auto_increment
    private String extra;
    // 主键类型
    private String columnKey;
    // 创建时间
    private Date createDate;

}
