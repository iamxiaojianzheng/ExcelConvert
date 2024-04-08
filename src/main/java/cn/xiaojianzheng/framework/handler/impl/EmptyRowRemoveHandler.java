package cn.xiaojianzheng.framework.handler.impl;

import cn.hutool.core.util.StrUtil;
import cn.xiaojianzheng.framework.handler.RemoveRowHandler;
import cn.xiaojianzheng.framework.util.CellParseUtil;
import cn.xiaojianzheng.framework.xml.Column;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.List;

/**
 * 删除空白行
 */
public class EmptyRowRemoveHandler implements RemoveRowHandler {

    @Override
    public boolean needRemove(Row row, List<Column> columns) {
        if (row == null) {
            return true;
        }

        for (int j = 0; j < row.getLastCellNum(); j++) {
            Cell cell = row.getCell(j);
            if (cell != null && StrUtil.isNotBlank(CellParseUtil.doParse(cell))) {
                return false;
            }
        }
        return true;
    }

}
