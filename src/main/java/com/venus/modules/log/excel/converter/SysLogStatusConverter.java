package com.venus.modules.log.excel.converter;

import cn.idev.excel.converters.Converter;
import cn.idev.excel.enums.CellDataTypeEnum;
import cn.idev.excel.metadata.GlobalConfiguration;
import cn.idev.excel.metadata.data.ReadCellData;
import cn.idev.excel.metadata.data.WriteCellData;
import cn.idev.excel.metadata.property.ExcelContentProperty;

public class SysLogStatusConverter implements Converter<Integer> {

    @Override
    public Class<Integer> supportJavaTypeKey() {
        return Integer.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public Integer convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        if (cellData.getStringValue().equals("失败")) {
            return 0;
        } else if (cellData.getStringValue().equals("成功")) {
            return 1;
        } else if (cellData.getStringValue().equals("账号已锁定")) {
            return 2;
        } else {
            return -1;
        }
    }

    @Override
    public WriteCellData<?> convertToExcelData(Integer value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        if (value == 0) {
            return new WriteCellData<>("失败");
        } else if (value == 1) {
            return new WriteCellData<>("成功");
        } else if (value == 2) {
            return new WriteCellData<>("账号已锁定");
        } else {
            return new WriteCellData<>("未知");
        }
    }

}
