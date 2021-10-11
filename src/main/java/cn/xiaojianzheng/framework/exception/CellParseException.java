package cn.xiaojianzheng.framework.exception;

import org.slf4j.helpers.MessageFormatter;

import java.io.Serializable;
import java.util.Map;

public class CellParseException extends RuntimeException implements Serializable {

    private Object errorData;

    public CellParseException() {
    }

    public CellParseException(String errorMsg) {
        super(errorMsg);
    }

    public CellParseException(String format, String... argArray) {
        super(MessageFormatter.arrayFormat(format, argArray).getMessage());
    }

    public CellParseException(String format, Object... argArray) {
        super(MessageFormatter.arrayFormat(format, argArray).getMessage());
    }

    public CellParseException(Object errorMsg) {
        super(errorMsg != null ? errorMsg.toString() : "");
    }

    public CellParseException(String errorMsg, Map<String, Object> errorMap) {
        super(errorMsg);
        this.errorData = errorMap;
    }

    public CellParseException(RuntimeException e) {
        super(e);
    }

    public CellParseException(Throwable e) {
        super(e);
    }

    public Object getErrorData() {
        return this.errorData;
    }

}
