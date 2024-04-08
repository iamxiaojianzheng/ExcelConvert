package dongfeng.handler.rongzi;

import cn.xiaojianzheng.framework.exception.ExcelConvertExceptionUtil;
import cn.xiaojianzheng.framework.handler.AssociateOriginColumnsHandler;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 承租人处理器
 */
public class LesseeHandler implements AssociateOriginColumnsHandler {

    public Map<String, Cell> handleBeforeParseCell(Map<String, Cell> titleToCellMap, Row rowReader) {
        return null;
    }

    public List<String> handleAfterParseCell(Map<String, String> titleToCellValueMap, Row rowReader) {
        if (titleToCellValueMap.size() != 1) {
            ExcelConvertExceptionUtil.of("当前获取获取到的承租人数量异常：{}", String.join(", ", titleToCellValueMap.values()));
        }

        return new ArrayList<>(titleToCellValueMap.values());
    }
}
