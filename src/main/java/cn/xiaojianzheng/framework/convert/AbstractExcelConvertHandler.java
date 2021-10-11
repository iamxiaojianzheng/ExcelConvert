package cn.xiaojianzheng.framework.convert;

import cn.xiaojianzheng.framework.exception.ExcelConvertExceptionUtil;
import cn.xiaojianzheng.framework.handler.RowConvertRuleHandler;
import cn.xiaojianzheng.framework.util.CellParseUtil;
import cn.xiaojianzheng.framework.xml.Column;
import cn.xiaojianzheng.framework.xml.ExcelXml;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class AbstractExcelConvertHandler implements ExcelConvert {

    protected CellParseUtil cellParseUtil;

    private final RowConvertRuleHandler rowConvertRuleHandler;

    protected AbstractExcelConvertHandler(CellParseUtil cellParseUtil, RowConvertRuleHandler rowConvertRuleHandler) {
        this.cellParseUtil = cellParseUtil;
        this.rowConvertRuleHandler = rowConvertRuleHandler;
    }

    @Override
    public int doWriterTitleRowHandler(Sheet sheetReader, Sheet sheetWriter, ExcelXml excelXml) {
        Row row = sheetWriter.createRow(0);
        List<Column> columns = excelXml.getColumns();
        for (int i = 0; i < columns.size(); i++) {
            row.getCell(i).setCellValue(columns.get(i).getName());
        }
        return row.getRowNum() + 1;
    }

    @Override
    public abstract List<String> getSourceExcelTitles(Sheet sheetReader);

    @Override
    public int doRowHandler(List<String> sourceExcelTitles, Sheet sheetReader, Sheet sheetWriter) {
        return 0;
    }

    public void rowConvert(List<String> sourceExcelTitles, Row rowReader, Sheet sheetWriter, ExcelXml excelXml) {
        Map<String, Column> nameToColumnMap = excelXml.getColumns().stream()
                .collect(Collectors.toMap(Column::getName, c -> c, (a, b) -> b, LinkedHashMap::new));

        // 源表中 列名与单元格对象的映射
        LinkedHashMap<String, Cell> sourceColumnNameToCellMap = getSourceTitleToCellMap(sourceExcelTitles, rowReader);

        // 当前待写入的行
        int lastRowNum = sheetWriter.getLastRowNum() + 1;
        Row rowWriter = Optional.ofNullable(sheetWriter.getRow(lastRowNum)).orElse(sheetWriter.createRow(lastRowNum));

        // 基于目标表列名开始写入
        for (Map.Entry<String, Column> entry : nameToColumnMap.entrySet()) {
            // 当前待写入的单元格
            int newCellIndex = rowWriter.getLastCellNum() == -1 ? 0 : rowWriter.getLastCellNum();
            Cell cellWriter = Optional.ofNullable(rowWriter.getCell(newCellIndex)).orElse(rowWriter.createCell(newCellIndex));

            // 当前列信息
            Column column = entry.getValue();

            // 最高优先级
            String fixedValue = column.getFixedValue();
            if (fixedValue != null) {
                cellWriter.setCellValue(fixedValue);
                continue;
            }

            // 获取对应源表单元格的值
            String from = column.getFrom();
            Cell cellReader = sourceColumnNameToCellMap.get(from);
            String oldCellValue = null;
            if (cellReader != null) {
                oldCellValue = cellParseUtil.doParse(cellReader, column);
            }

            // <convertRules>
            if (rowConvertRuleHandler.convertRulesTag(column, cellReader, cellWriter)
                    // <column cellValueConvertClass="">
                    || rowConvertRuleHandler.cellValueConvertClassAttribute(sourceColumnNameToCellMap, excelXml, column, cellReader, cellWriter)
                    // <associate>
                    || rowConvertRuleHandler.associateTag(sourceColumnNameToCellMap, column, rowReader, rowWriter)) {
                continue;
            }

            if (!StringUtils.hasText(oldCellValue) && !column.isAllowEmpty()) {
                ExcelConvertExceptionUtil.of("单元格[{}行, {}列, {}]填充值失败，请检查原表中【{}】相关列是否缺失数据",
                        rowWriter.getRowNum() + 1, indexToColumn(cellReader.getColumnIndex()), column.getName(), column.getFrom());
            }

            // 最低优先级，什么配置都没有的情况下，直接取源表中的值
            cellWriter.setCellValue(oldCellValue);
        }

        rowWriter.getRowNum();
    }

    /**
     * 将源表的行转为title -> cell的map格式
     *
     * @param titles    源表的标题
     * @param rowReader 源表中的一行
     * @return 标题 -> 单元格
     */
    protected LinkedHashMap<String, Cell> getSourceTitleToCellMap(List<String> titles, Row rowReader) {
        LinkedHashMap<String, Cell> map = new LinkedHashMap<>();
        for (int i = 0; i < titles.size(); i++) {
            map.put(titles.get(i), rowReader.getCell(i));
        }
        return map;
    }

    /**
     * 列索引转列名（字母）
     *
     * @param index 单元格对应列索引
     * @return 字母列名 eg. 29 -> AC
     */
    private String indexToColumn(int index) {
        index--;
        StringBuilder column = new StringBuilder();
        do {
            if (column.length() > 0) {
                index--;
            }
            column.insert(0, ((char) (index % 26 + (int) 'A')));
            index = (index - index % 26) / 26;
        } while (index > 0);
        return column.toString();
    }

}
