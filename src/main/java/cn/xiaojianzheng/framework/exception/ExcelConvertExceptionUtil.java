package cn.xiaojianzheng.framework.exception;

public class ExcelConvertExceptionUtil {

    private ExcelConvertExceptionUtil() {
    }

    public static void of(String format, Object... params) {
        throw new CellParseException(format, params);
    }

    public static void of(String msg) {
        throw new CellParseException(msg);
    }

    public static void booleanOf(Boolean judgement, String msg, Object... params) {
        if (judgement) {
            throw new CellParseException(msg, params);
        }
    }

    public static void of(Exception e) {
        throw new CellParseException(e);
    }

}
