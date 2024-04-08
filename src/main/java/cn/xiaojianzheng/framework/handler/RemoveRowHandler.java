package cn.xiaojianzheng.framework.handler;

import cn.xiaojianzheng.framework.xml.Column;
import org.apache.poi.ss.usermodel.Row;

import java.util.List;

/**
 * 删除行处理器
 */
public interface RemoveRowHandler {
    boolean needRemove(Row row, List<Column> columns);
}
