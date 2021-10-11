package cn.xiaojianzheng.framework.handler;

import cn.xiaojianzheng.framework.xml.Column;
import org.apache.poi.ss.usermodel.Row;

import java.util.List;

public interface RemoveRowHandler {
    boolean needRemove(Row row, List<Column> columns);
}
