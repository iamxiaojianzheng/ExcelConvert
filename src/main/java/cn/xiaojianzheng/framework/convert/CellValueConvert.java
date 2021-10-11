package cn.xiaojianzheng.framework.convert;

import cn.xiaojianzheng.framework.xml.ExcelXml;
import org.apache.poi.ss.usermodel.Cell;

import java.util.LinkedHashMap;

public interface CellValueConvert {
    /**
     * 单元格内容转换处理器
     *
     * @param titleToCellMap 源表标题与单元格映射
     * @param cell           源表某个单元格
     * @param excelXml       Excel解析XML对象
     * @return 转换后的单元格内容
     */
    String convert(LinkedHashMap<String, Cell> titleToCellMap, Cell cell, ExcelXml excelXml);
}
