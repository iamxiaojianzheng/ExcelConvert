package cn.xiaojianzheng.framework.handler;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ReUtil;
import cn.xiaojianzheng.framework.enums.ExcelCellType;
import cn.xiaojianzheng.framework.exception.CellParseException;
import cn.xiaojianzheng.framework.exception.ExcelConvertExceptionUtil;
import cn.xiaojianzheng.framework.xml.Column;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class CellParseHandler {

    private final Logger logger = LoggerFactory.getLogger(CellParseHandler.class);

    public final BigDecimal percentageConstant =  new BigDecimal(100);

    @Getter
    private List<String> parseDateFormatList = new ArrayList<>(Arrays.asList("yyyy-MM-dd", "yyyy/MM/dd", "yyyy年MM月dd日", "yyyyMMdd"));

    @Setter
    private String globalDateFormat;

    @Setter
    private String globalPercentageFormat;

    @Setter
    private Boolean needScale;

    @Setter
    private Integer globalScale;

    public String doParse(Cell cell) {
        return doParse(cell, null);
    }

    public String doParse(Cell cell, Column column) {
        if (cell == null) {
            return null;
        }

        String value = null;

        if (column == null) {
            column = new Column();
        }

        // column指定类型优先，其次为单元格类型
        ExcelCellType cellType = Optional.ofNullable(column.getCellType())
                .orElse(ExcelCellType.valueOf(cell.getCellType().name()));

        switch (cellType) {
            case STRING:
                value = doStringCell(cell);
                break;
            case NUMERIC:
                BigDecimal number = doNumericCell(cell);
                if (number != null) {
                    if (needScale) {
                        value = number.setScale(globalScale, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString();
                    } else {
                        value = number.stripTrailingZeros().toPlainString();
                    }
                }
                break;
            case DATE:
                Date date = doDateCell(cell);
                if (date != null) {
                    String dateFormat = Optional.ofNullable(column.getDateFormat()).orElse(globalDateFormat);
                    value = DateUtil.format(date, dateFormat);
                }
                break;
            case BOOLEAN:
                value = cell.getBooleanCellValue() ? "true" : "false";
                break;
            case FORMULA:
                value = doFormulaCell(cell);
                break;
            case BLANK:
                value = "";
                break;
            default:
                throw new CellParseException("[{}行，{}列，{}]单元格类型异常: {}",
                        cell.getRow().getRowNum(), cell.getColumnIndex(), column.getName(), cell.getErrorCellValue());
        }

        ExcelConvertExceptionUtil.booleanOf(value == null, "解析日期单元格失败，[{}行，{}列，{}]",
                cell.getRow().getRowNum(), cell.getColumnIndex(), column.getName());

        return value;
    }

    /**
     * 处理文本单元格
     *
     * @param cell 单元格
     * @return 单元格中的文本内容
     */
    protected String doStringCell(Cell cell) {
        return cell.getStringCellValue();
    }

    /**
     * 处理数字类单元格
     * <P>Numeric cell type (whole numbers, fractional numbers, dates)</P>
     *
     * @param cell 单元格
     * @return 单元格最终计算出来的数值
     */
    protected BigDecimal doNumericCell(Cell cell) {
        BigDecimal value = null;

        switch (cell.getCellType()) {
            case STRING:
                String cellStr = cell.getStringCellValue();
                // 0 - 100 范围保留两位小数的百分数
                if (cellStr.matches("^(100|[1-9]?\\d(\\.\\d\\d?\\d?)?)%$")) {
                    String var1 = cellStr.replace("%", "");
                    String ext = ReUtil.get("\\.(.*?)$", var1, 1);
                    value = new BigDecimal(var1).divide(percentageConstant, ext == null ? 2 : ext.length() + 2 ,RoundingMode.HALF_UP);
                }
                // 待逗号的正负小数数值
                else if (cellStr.matches("^-?(\\d+|\\d{1,3}(,\\d{3})*)(.\\d+)?$")) {
                    value = readStrAsBigDecimalAndCheckFormat(cellStr);
                } else {
                    value = new BigDecimal(cellStr);
                }
                break;
            case NUMERIC:
                value = BigDecimal.valueOf(cell.getNumericCellValue());
                break;
            case BOOLEAN:
                value = cell.getBooleanCellValue() ? BigDecimal.ONE : BigDecimal.ZERO;
                break;
            case FORMULA:
                value = new BigDecimal(doFormulaCell(cell));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + cell.getCellType());
        }

        return value;
    }

    /**
     * 处理日期单元格
     *
     * @param cell 单元格
     * @return format之后的日期字符串
     */
    protected Date doDateCell(Cell cell) {
        Date date = null;

        switch (cell.getCellType()) {
            case STRING:
                if (StringUtils.hasText(cell.getStringCellValue())) {
                    date = DateUtil.parse(cell.getStringCellValue(),
                            "yyyy-MM-dd", "yyyy/MM/dd", "yyyy年MM月dd日", "yyyyMMdd");
                }
                break;
            case NUMERIC:
                // 如果是数字类型的话,判断是不是日期类型
                if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                    // 获取日期类型的单元格的值
                    date = cell.getDateCellValue();
                } else {
                    CellStyle cellStyle = cell.getCellStyle();
                    // excel 自定义日期
                    if (cellStyle != null && CharSequenceUtil.containsAny(String.valueOf(cellStyle.getDataFormat()), "49", "14", "31", "57", "58", "20", "32")) {
                        date = cell.getDateCellValue();
                    }
                }
                break;
        }

        return date;
    }

    /**
     * 处理公式单元格
     *
     * @param cell 单元格
     * @return 计算公式之后的结果字符串
     */
    protected String doFormulaCell(Cell cell) {
        FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
        String result = null;

        switch (cell.getCellType()) {
            case NUMERIC:
                // 如果是数字类型的话,判断是不是日期类型
                if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                    // 获取日期类型的单元格的值
                    Date d = cell.getDateCellValue();
                } else {
                    double number = cell.getNumericCellValue();
                    DecimalFormat df = new DecimalFormat("#.##");
                    result = df.format(number);
                }
                break;
            case FORMULA:
                result = getCellValue(evaluator.evaluate(cell));
                break;
        }

        return result;
    }

    /**
     * 格式化为文书用金额格式0.########
     */
    private String formatAmount4Doc(BigDecimal amount) {
        if (amount == null) {
            return null;
        }
        try {
            DecimalFormat format = new DecimalFormat("0.########");
            return format.format(amount);
        } catch (Exception e) {
            logger.error("转换数字" + amount + "异常", e);
        }
        return null;
    }

    /**
     * 读取带有逗号的金额数字
     */
    private BigDecimal readStrAsBigDecimalAndCheckFormat(String str) {
        DecimalFormat format = new DecimalFormat();
        format.setParseBigDecimal(true);
        ParsePosition position = new ParsePosition(0);
        BigDecimal parse = (BigDecimal) format.parse(str, position);
        if (str.length() == position.getIndex()) {
            return parse;
        }
        return null;
    }

    /**
     * 含公式处理val
     */
    private static String getCellValue(CellValue cell) {
        String cellValue = null;
        switch (cell.getCellType()) {
            case STRING:
                cellValue = cell.getStringValue().trim();
                break;
            case NUMERIC:
                double number = cell.getNumberValue();
                DecimalFormat df = new DecimalFormat("#.##");
                cellValue = df.format(number);
                break;
            case FORMULA:
                break;
            default:
                break;
        }
        return cellValue;
    }

    public void appendParseDateFormatList(List<String> list) {
        parseDateFormatList.addAll(list);
        parseDateFormatList = parseDateFormatList.stream().distinct().collect(Collectors.toList());
    }

    public static void main(String[] args) {
//        System.out.println(new BigDecimal(6).divide(new BigDecimal(100), 4, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString());
        System.out.println(ReUtil.get("\\.(.*?)$", "7", 0));
    }

}
