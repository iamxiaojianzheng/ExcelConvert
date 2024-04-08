package dongfeng.handler;

import cn.hutool.core.util.StrUtil;
import cn.xiaojianzheng.framework.handler.AssociateOriginColumnsHandler;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 被告送达地址类型处理器
 */
public class AccusedSendTypeHandler implements AssociateOriginColumnsHandler {
    public AccusedSendTypeHandler() {
    }

    public Map<String, Cell> handleBeforeParseCell(Map<String, Cell> titleToCellMap, Row rowReader) {
        return null;
    }

    public List<String> handleAfterParseCell(Map<String, String> titleToCellValueMap, Row rowReader) {
        return titleToCellValueMap.values().stream()
                .map((v) -> StrUtil.isNotBlank(v) && v.length() > 5 ? "约定EMS送达" : "无约定")
                .collect(Collectors.toList());
    }
}
