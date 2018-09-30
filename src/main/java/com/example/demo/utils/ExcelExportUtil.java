package com.example.demo.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;
import org.jeecgframework.poi.excel.export.ExcelExportServer;

import java.util.Collection;

/**
 * excel 导出工具类
 *
 * @author zhou.xy
 * @since 1.0.0
 */
public class ExcelExportUtil {
    /**
     * 导出数据到Excel
     *
     * @param exportParams 表格标题属性
     * @param pojoClass    Excel对象Class
     * @param dataSet      Excel对象数据List
     * @return Workbook
     */
    public static Workbook exportExcel(ExportParams exportParams, Class<?> pojoClass, Collection<?> dataSet) {
        Workbook workbook;
        if (ExcelType.HSSF.equals(exportParams.getType())) {
            workbook = new HSSFWorkbook();
        } else if (dataSet.size() < 1000) {
            workbook = new XSSFWorkbook();
        } else {
            workbook = new SXSSFWorkbook();
        }
        new ExcelExportServer().createSheet(workbook, exportParams, pojoClass, dataSet);
        return workbook;
    }
}
