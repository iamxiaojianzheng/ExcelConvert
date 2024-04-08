package cn.xiaojianzheng.framework.handler;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.xiaojianzheng.framework.convert.CellValueConvert;
import cn.xiaojianzheng.framework.exception.ExcelConvertExceptionUtil;
import cn.xiaojianzheng.framework.util.CellParseUtil;
import cn.xiaojianzheng.framework.xml.AssociateOriginColumns;
import cn.xiaojianzheng.framework.xml.Column;
import cn.xiaojianzheng.framework.xml.Convert;
import cn.xiaojianzheng.framework.xml.ExcelXml;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 行转换规则处理器
 */
@Slf4j
public class RowConvertRuleHandler {

    /**
     * {@code <convertRules> } convertRules标签处理器
     */
    public boolean convertRulesTag(Column column, Cell cellReader, Cell cellWriter) {
        String oldCellValue = null;
        if (cellReader != null) {
            oldCellValue = CellParseUtil.doParse(cellReader, column);
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
     * {@code <column cellValueConvertClass=""> } column标签cellValueConvertClass属性处理器
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
     * {@code <associate>} 标签处理器
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
            List<String> columnNameRegexList = associateOriginColumns.getColumnNameRegex();
            boolean allowEmpty = associateOriginColumns.isAllowEmpty();

            // 过滤出<columnName>
            Map<String, Cell> filterColumnNameToCellMap = new LinkedHashMap<>();
            sourceTitleToCellMap.entrySet().stream()
                    .filter(item -> {
                        // 过滤出符合条件的columnName
                        String name = item.getKey();
                        for (String columnNameRegex : columnNameRegexList) {
                            if (name.equals(columnNameRegex) || name.matches(columnNameRegex)) {
                                Cell cell = item.getValue();
                                String value = CellParseUtil.doParse(cell);
                                // 不允许为空 且 value为空，除了这种情况下，其余为true
                                if (!(!allowEmpty && StrUtil.isBlank(value))) {
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
                    (ObjectUtil.isNotEmpty(var1) ? var1 : filterColumnNameToCellMap).entrySet().stream()
                            .collect(Collectors.toMap(Map.Entry::getKey,
                                    item -> {
                                        String s = CellParseUtil.doParse(item.getValue(), column);
                                        return item.getValue() == null ? "" : s == null ? "" : s;
                                    },
                                    (a, b) -> b, LinkedHashMap::new));

            // 优先使用handler生成的值
            final AtomicInteger rowWriterIndex = new AtomicInteger(rowWriter.getRowNum());
            final int columnWriterIndex = column.getIndex();
            Sheet sheetWriter = rowWriter.getSheet();
            if (!associateHandlerAttribute(filterColumnNameToCellValueMap, column, handler, rowReader, rowWriter)) {
                // 垂直填写，列不变，行向下填写。
                filterColumnNameToCellValueMap.forEach((key, cellValue) ->
                        createRowCell(sheetWriter, rowWriterIndex.getAndIncrement(), columnWriterIndex).setCellValue(cellValue));
            }
            return true;
        }

        return false;
    }

    /**
     * {@code <associate handler=""> } associate标签handler属性处理器
     */
    public boolean associateHandlerAttribute(Map<String, String> filterColumnNameToCellValueMap, Column column,
                                             AssociateOriginColumnsHandler handler, Row rowReader, Row rowWriter) {
        // 优先使用handler生成的值
        if (handler != null) {
            AtomicInteger rowWriterIndex = new AtomicInteger(rowWriter.getRowNum());
            final int columnWriterIndex = column.getIndex();
            Sheet sheetWriter = rowWriter.getSheet();
            List<String> cellValueList = new ArrayList<>();
            try {
                cellValueList.addAll(handler.handleAfterParseCell(filterColumnNameToCellValueMap, rowReader));
            } catch (Exception e) {
                e.printStackTrace();
                ExcelConvertExceptionUtil.of("处理解析单元格之后的数据异常：接口 => {} 列 => {}", handler.getClass().getCanonicalName(), column.getFrom());
            }
            // 垂直填写，列不变，行向下填写。
            cellValueList.forEach(cellValue -> createRowCell(sheetWriter, rowWriterIndex.getAndIncrement(), columnWriterIndex).setCellValue(cellValue));
            return true;
        }
        return false;
    }

    private Cell createRowCell(Sheet sheet, int rowIndex, int columnIndex) {
        Row r = Optional.ofNullable(sheet.getRow(rowIndex)).orElseGet(() -> sheet.createRow(rowIndex));
        return Optional.ofNullable(r.getCell(columnIndex)).orElseGet(() -> r.createCell(columnIndex));
    }

}
