package cn.xiaojianzheng.framework.handler;

import cn.xiaojianzheng.framework.convert.CellValueConvert;
import cn.xiaojianzheng.framework.exception.ExcelConvertExceptionUtil;
import cn.xiaojianzheng.framework.util.CellParseUtil;
import cn.xiaojianzheng.framework.xml.AssociateOriginColumns;
import cn.xiaojianzheng.framework.xml.Column;
import cn.xiaojianzheng.framework.xml.Convert;
import cn.xiaojianzheng.framework.xml.ExcelXml;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class RowConvertRuleHandler {

    private static final Logger logger = LoggerFactory.getLogger(RowConvertRuleHandler.class);

    private final CellParseUtil cellParseUtil;

    public RowConvertRuleHandler(CellParseUtil cellParseUtil) {
        this.cellParseUtil = cellParseUtil;
    }

    /**
     * <convertRules> 标签处理器
     */
    public boolean convertRulesTag(Column column, Cell cellReader, Cell cellWriter) {
        String oldCellValue = null;
        if (cellReader != null) {
            oldCellValue = cellParseUtil.doParse(cellReader, column);
        }

        List<Convert> convertRules = column.getConvertRules();
        if (column.isNeedConvert() && oldCellValue != null) {
            Map<String, String> oldToNewValueMap =
                    convertRules.stream().collect(Collectors.toMap(Convert::getOldValue, Convert::getNewValue));
            if (!oldToNewValueMap.isEmpty()) {
                cellWriter.setCellValue(oldToNewValueMap.getOrDefault(oldCellValue, oldCellValue));
                return true;
            }
        }

        return false;
    }

    /**
     * <column cellValueConvertClass=""> 属性处理器
     */
    public boolean cellValueConvertClassAttribute(LinkedHashMap<String, Cell> sourceTitleToCellMap, ExcelXml excelXml, Column column, Cell cellReader, Cell cellWriter) {
        CellValueConvert cellValueConvertClass = column.getCellValueConvertClass();
        if (cellValueConvertClass != null) {
            cellWriter.setCellValue(cellValueConvertClass.convert(sourceTitleToCellMap, cellReader, excelXml));
            return true;
        }
        return false;
    }

    /**
     * <associate> 标签处理器
     *
     * @param sourceTitleToCellMap 原标题与单元格映射
     * @param column               当前操作列
     * @param rowReader            原表读取行
     * @param rowWriter            目标写入行
     */
    public boolean associateTag(LinkedHashMap<String, Cell> sourceTitleToCellMap, Column column, Row rowReader, Row rowWriter) {
        AssociateOriginColumns associateOriginColumns = column.getAssociateOriginColumns();

        if (associateOriginColumns != null) {
            AssociateOriginColumnsHandler handler = associateOriginColumns.getHandler();
            List<String> columnNames = associateOriginColumns.getColumnNames();
            boolean allowEmpty = associateOriginColumns.isAllowEmpty();

            // 过滤出<columnName>
            Map<String, Cell> filterColumnNameToCellMap = new LinkedHashMap<>();
            sourceTitleToCellMap.entrySet().stream()
                    .filter(item -> {
                        // 过滤出符合条件的columnName
                        String name = item.getKey();
                        for (String columnName : columnNames) {
                            if (name.equals(columnName) || name.matches(columnName)) {
                                Cell cell = item.getValue();
                                String value = cellParseUtil.doParse(cell);
                                // 不允许为空 且 value为空，除了这种情况下，其余为true
                                if (!(!allowEmpty && !StringUtils.hasText(value))) {
                                    return true;
                                }
                                break;
                            }
                        }
                        return false;
                    })
                    .forEach(entry -> filterColumnNameToCellMap.put(entry.getKey(), entry.getValue()));

            Map<String, Cell> var1 = null;
            if (handler != null) {
                // 前置处理cell
                var1 = handler.handleBeforeParseCell(filterColumnNameToCellMap, rowReader);
            }
            Map<String, String> filterColumnNameToCellValueMap =
                    (var1 != null ? var1 : filterColumnNameToCellMap).entrySet().stream()
                            .collect(Collectors.toMap(Map.Entry::getKey,
                                    item -> {
                                        String s = cellParseUtil.doParse(item.getValue(), column);
                                        return item.getValue() == null ? "" : s == null ? "" : s;
                                    },
                                    (a, b) -> b, LinkedHashMap::new));

            // 优先使用handler生成的值
            final AtomicInteger rowWriterIndex = new AtomicInteger(rowWriter.getRowNum());
            final int cellWriterIndex = column.getIndex();
            Sheet sheetWriter = rowWriter.getSheet();
            if (!associateHandlerAttribute(filterColumnNameToCellValueMap, column, handler, rowReader, rowWriter)) {
                // rowWriter index auto increment
                filterColumnNameToCellValueMap.forEach((key, cellValue) ->
                        createRowCell(sheetWriter, rowWriterIndex.getAndIncrement(), cellWriterIndex).setCellValue(cellValue));
            }
            return true;
        }

        return false;
    }

    /**
     * <associate handler=""> 属性处理器
     */
    public boolean associateHandlerAttribute(Map<String, String> filterColumnNameToCellValueMap, Column column,
                                             AssociateOriginColumnsHandler handler, Row rowReader, Row rowWriter) {
        // 优先使用handler生成的值
        if (handler != null) {
            AtomicInteger rowWriterIndex = new AtomicInteger(rowWriter.getRowNum());
            final int cellWriterIndex = column.getIndex();
            Sheet sheetWriter = rowWriter.getSheet();
            List<String> cellValueList = new ArrayList<>();
            try {
                cellValueList.addAll(handler.handleAfterParseCell(filterColumnNameToCellValueMap, rowReader));
            } catch (Exception e) {
                ExcelConvertExceptionUtil.of("处理解析单元格之后的数据异常：接口 => {} 列 => {}", handler.getClass().getCanonicalName(), column.getFrom());
            }
            // rowWriter index auto increment
            cellValueList.forEach(cellValue -> createRowCell(sheetWriter, rowWriterIndex.getAndIncrement(), cellWriterIndex).setCellValue(cellValue));
            return true;
        }
        return false;
    }

    private Cell createRowCell(Sheet sheet, int rowIndex, int cellIndex) {
        Row r = sheet.getRow(rowIndex);
        if (r == null) r = sheet.createRow(rowIndex);
        Cell c = r.getCell(cellIndex);
        if (c == null) c = r.createCell(cellIndex);
        return c;
    }

}
