package com.venus.common.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.alibaba.excel.EasyExcel;

import java.util.Date;
import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.BeanUtils;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.excel.converters.longconverter.LongStringConverter;

public class ExcelUtils {
    /**
     * Excel导出
     *
     * @param response    response
     * @param fileName    文件名
     * @param sheetName   sheetName
     * @param list        数据List
     * @param targetClass 对象Class
     */
    public static void exportExcel(HttpServletResponse response, String fileName, String sheetName, List<?> list, Class<?> targetClass) throws IOException {
        if (StrUtil.isBlank(fileName)) {
            //当前日期
            fileName = DateUtils.format(new Date());
        }

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("UTF-8");
        fileName = URLUtil.encode(fileName, StandardCharsets.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), targetClass).registerConverter(new LongStringConverter()).sheet(sheetName).doWrite(list);
    }

    /**
     * Excel导出，先sourceList转换成List<targetClass>，再导出
     *
     * @param response    response
     * @param fileName    文件名
     * @param sheetName   sheetName
     * @param sourceList  原数据List
     * @param targetClass 目标对象Class
     */
    public static void exportExcelToTarget(HttpServletResponse response, String fileName, String sheetName, List<?> sourceList, Class<?> targetClass) throws Exception {
        List<Object> targetList = new ArrayList<>(sourceList.size());
        for (Object source : sourceList) {
            Object target = targetClass.newInstance();
            BeanUtils.copyProperties(source, target);
            targetList.add(target);
        }

        exportExcel(response, fileName, sheetName, targetList, targetClass);
    }
}
