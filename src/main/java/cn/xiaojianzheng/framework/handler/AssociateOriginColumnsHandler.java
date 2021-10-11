package cn.xiaojianzheng.framework.handler;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.List;
import java.util.Map;

public interface AssociateOriginColumnsHandler {
    /**
     * 处理解析之前的原始数据
     */
    Map<String, Cell> handleBeforeParseCell(Map<String, Cell> titleToCellMap, Row rowReader);

    /**
     * 处理解析单元格之后的数据
     */
    List<String> handleAfterParseCell(Map<String, String> titleToCellValueMap, Row rowReader);
}
