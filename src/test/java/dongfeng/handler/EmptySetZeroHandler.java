package dongfeng.handler;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.xiaojianzheng.framework.handler.AssociateOriginColumnsHandler;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 空数值处理器
 */
public class EmptySetZeroHandler implements AssociateOriginColumnsHandler {

    public Map<String, Cell> handleBeforeParseCell(Map<String, Cell> titleToCellMap, Row rowReader) {
        return null;
    }

    public List<String> handleAfterParseCell(Map<String, String> titleToCellValueMap, Row rowReader) {
        return titleToCellValueMap.values().stream()
                .map((v) -> NumberUtil.isNumber(v) && Double.parseDouble(v) > 0.0 ? v : "0")
                .collect(Collectors.toList());
    }
}
