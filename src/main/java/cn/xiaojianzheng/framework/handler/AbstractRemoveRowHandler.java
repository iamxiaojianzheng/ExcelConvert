package cn.xiaojianzheng.framework.handler;

import cn.xiaojianzheng.framework.util.CellParseUtil;
import cn.xiaojianzheng.framework.xml.Column;
import org.apache.poi.ss.usermodel.Row;

import java.util.List;

public class AbstractRemoveRowHandler implements RemoveRowHandler {

    protected CellParseUtil cellParseUtil;

    protected AbstractRemoveRowHandler(CellParseUtil cellParseUtil) {
        this.cellParseUtil = cellParseUtil;
    }

    @Override
    public boolean needRemove(Row row, List<Column> columns) {
        return false;
    }

}
