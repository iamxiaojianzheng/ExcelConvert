package cn.xiaojianzheng.framework.handler.impl;

import cn.xiaojianzheng.framework.handler.AbstractRemoveRowHandler;
import cn.xiaojianzheng.framework.util.CellParseUtil;
import cn.xiaojianzheng.framework.xml.Column;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

@Component
public class CustomRemoveRowHandler extends AbstractRemoveRowHandler {

    public CustomRemoveRowHandler(CellParseUtil cellParseUtil) {
        super(cellParseUtil);
    }

    @Override
    public boolean needRemove(Row row, List<Column> columns) {
        if (row == null) {
            return true;
        }

        for (int j = 0; j < row.getLastCellNum(); j++) {
            Cell cell = row.getCell(j);
            if (cell != null && StringUtils.hasText(cellParseUtil.doParse(cell))) {
                return false;
            }
        }
        return true;
    }

}
