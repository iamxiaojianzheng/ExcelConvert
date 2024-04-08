package dongfeng.handler;

import cn.hutool.core.util.StrUtil;
import cn.xiaojianzheng.framework.handler.RemoveRowHandler;
import cn.xiaojianzheng.framework.util.CellParseUtil;
import cn.xiaojianzheng.framework.xml.Column;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.List;
import java.util.Optional;

/**
 * 删除行处理器
 */
public class ExcelRemoveRowHandler implements RemoveRowHandler {
    public boolean needRemove(Row row, List<Column> columns) {
        // 删除【被告名称】列为空的行
        Optional<Column> optional = columns.stream().filter((column) -> column.getName().equals("被告名称")).findFirst();
        if (optional.isPresent()) {
            Column column = optional.get();
            Integer index = column.getIndex();
            Cell cell = row.getCell(index);
            String value = CellParseUtil.doParse(cell, column);
            return cell == null || StrUtil.isBlank(value);
        }
        return false;
    }
}
