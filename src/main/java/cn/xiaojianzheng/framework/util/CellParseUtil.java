package cn.xiaojianzheng.framework.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.xiaojianzheng.framework.enums.ExcelCellType;
import cn.xiaojianzheng.framework.exception.CellParseException;
import cn.xiaojianzheng.framework.xml.Column;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 单元格解析工具类
 */
@Slf4j
public class CellParseUtil {

    private static final BigDecimal percentageConstant = new BigDecimal(100);

    private static List<String> parseDateFormatList = new ArrayList<>(Arrays.asList("yyyy-MM-dd", "yyyy/MM/dd", "yyyy年MM月dd日", "yyyyMMdd"));

    @Setter
    private static String globalDateFormat = "yyyy年MM月dd日";

    @Setter
    private static DecimalFormat decimalFormat;

    @Setter
    private static Boolean needScale;

    @Setter
    private static Integer globalScale;

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
                cellValue = String.valueOf(cell.getNumberValue());
                break;
            case FORMULA:
                break;
            default:
                break;
        }
        return cellValue;
    }

    private static boolean containsAny(CharSequence str, CharSequence... testStrs) {
        if (StrUtil.isNotBlank(str) && ObjectUtil.isNotEmpty(Arrays.asList(testStrs))) {
            for (CharSequence checkStr : testStrs) {
                if (str.toString().contains(checkStr)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static Date parseDate(String dateStr, CharSequence... testStrs) {
        if (dateStr == null) {
            return null;
        }

        for (CharSequence testStr : testStrs) {
            SimpleDateFormat sdf = new SimpleDateFormat(testStr.toString());
            try {
                return sdf.parse(dateStr);
            } catch (ParseException ignored) {
            }
        }

        return null;
    }

    public static String doParse(Cell cell) {
        return doParse(cell, null);
    }

    public static String doParse(Cell cell, Column column) {
        if (cell == null) {
            return null;
        }

        String value = null;

        // column指定类型优先，其次为单元格类型
        ExcelCellType cellType;
        if (column != null && column.getCellType() != null) {
            cellType = column.getCellType();
        } else {
            cellType = ExcelCellType.valueOf(cell.getCellType().name());
        }

        try {
            switch (cellType) {
                case STRING:
                    value = doStringCell(cell);
                    break;
                case NUMERIC:
                    BigDecimal number = doNumericCell(cell);
                    if (number != null) {
                        if (decimalFormat != null) {
                            number = new BigDecimal(decimalFormat.format(number.doubleValue()));
                        }
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
                        value = new SimpleDateFormat(globalDateFormat).format(date);
                    } else if (column != null && column.isAllowEmpty()) {
                        value = "";
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
                    throw new CellParseException("[{}行，{}列]单元格类型异常: {}",
                            cell.getRow().getRowNum(), cell.getColumnIndex(), cell.getErrorCellValue());
            }
        } catch (Exception ignored) {
        }

        return value;
    }

    /**
     * 处理文本单元格
     *
     * @param cell 单元格
     * @return 单元格中的文本内容
     */
    protected static String doStringCell(Cell cell) {
        return cell.getStringCellValue();
    }

    /**
     * 处理数字类单元格
     * <P>Numeric cell type (whole numbers, fractional numbers, dates)</P>
     *
     * @param cell 单元格
     * @return 单元格最终计算出来的数值
     */
    protected static BigDecimal doNumericCell(Cell cell) {
        BigDecimal value = null;

        try {
            value = BigDecimal.valueOf(cell.getNumericCellValue());
        } catch (Exception e) {
            switch (cell.getCellType()) {
                case STRING:
                    String cellStr = cell.getStringCellValue().trim();
                    // 0 - 100 范围保留两位小数的百分数
                    if (cellStr.matches("^(100|[1-9]?\\d(\\.\\d\\d?\\d?)?)%$")) {
                        String var1 = cellStr.replace("%", "");
                        Matcher matcher = Pattern.compile("\\.(.*?)$", Pattern.DOTALL).matcher(var1);
                        String ext = null;
                        if (matcher.find()) {
                            ext = matcher.group(1);
                        }
                        value = new BigDecimal(var1).divide(percentageConstant, ext == null ? 2 : ext.length() + 2, RoundingMode.HALF_UP);
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
                    String s = doFormulaCell(cell);
                    if (StrUtil.isNotBlank(s)) {
                        value = new BigDecimal(s);
                    } else {
                        try {
                            double numericCellValue = cell.getNumericCellValue();
                            value = new BigDecimal(numericCellValue);
                        } catch (Exception ignored) {
                        }
                    }
                    break;
                case BLANK:
                    break;
                default:
                    log.error("Unexpected value: {}", cell.getCellType());
            }
        }

        return value;
    }

    /**
     * 处理日期单元格
     *
     * @param cell 单元格
     * @return format之后的日期字符串
     */
    protected static Date doDateCell(Cell cell) {
        Date date = null;

        try {
            date = cell.getDateCellValue();
        } catch (Exception e) {
            switch (cell.getCellType()) {
                case STRING:
                    if (StrUtil.isNotBlank(cell.getStringCellValue())) {
                        date = parseDate(cell.getStringCellValue(), "yyyy-MM-dd", "yyyy/MM/dd", "yyyy年MM月dd日", "yyyyMMdd");
                    }
                    break;
                case NUMERIC:
                    // 如果是数字类型的话,判断是不是日期类型
                    if (DateUtil.isCellInternalDateFormatted(cell)) {
                        // 获取日期类型的单元格的值
                        date = cell.getDateCellValue();
                    } else {
                        CellStyle cellStyle = cell.getCellStyle();
                        // excel 自定义日期
                        if (cellStyle != null && containsAny(String.valueOf(cellStyle.getDataFormat()), "49", "14", "31", "57", "58", "20", "32", "179")) {
                            date = cell.getDateCellValue();
                        }
                    }
                    break;
            }
        }

        return date;
    }

    /**
     * 处理公式单元格
     *
     * @param cell 单元格
     * @return 计算公式之后的结果字符串
     */
    protected static String doFormulaCell(Cell cell) {
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
                CellValue cellValue = evaluator.evaluate(cell);
                result = getCellValue(cellValue);
                break;
        }

        return result;
    }

    /**
     * 读取带有逗号的金额数字
     */
    private static BigDecimal readStrAsBigDecimalAndCheckFormat(String str) {
        DecimalFormat format = new DecimalFormat();
        format.setParseBigDecimal(true);
        ParsePosition position = new ParsePosition(0);
        BigDecimal parse = (BigDecimal) format.parse(str, position);
        if (str.length() == position.getIndex()) {
            return parse;
        }
        return null;
    }

    public static void setParseDateFormatList(List<String> parseDateFormatList) {
        CellParseUtil.parseDateFormatList.addAll(parseDateFormatList);
        CellParseUtil.parseDateFormatList = parseDateFormatList.stream().distinct().collect(Collectors.toList());
    }

}
