package cn.xiaojianzheng.framework.enums;

import org.apache.poi.ss.formula.FormulaType;
import org.apache.poi.ss.usermodel.FormulaError;

public enum ExcelCellType {

    /**
     * Numeric cell type (whole numbers, fractional numbers, dates)
     */
    NUMERIC(0),

    /**
     * String (text) cell type
     */
    STRING(1),

    /**
     * Formula cell type
     *
     * @see FormulaType
     */
    FORMULA(2),

    /**
     * Blank cell type
     */
    BLANK(3),

    /**
     * Boolean cell type
     */
    BOOLEAN(4),

    /**
     * Error cell type
     *
     * @see FormulaError
     */
    ERROR(5),

    /**
     * DATE cell type
     */
    DATE(6),

    ;

    private final int code;

    ExcelCellType(int code) {
        this.code = code;
    }

    public static ExcelCellType forInt(int code) {
        for (ExcelCellType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid CellType code: " + code);
    }

    public int getCode() {
        return code;
    }

}
