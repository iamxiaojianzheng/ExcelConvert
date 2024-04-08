package dongfeng.handler;

import cn.hutool.core.util.StrUtil;
import cn.xiaojianzheng.framework.handler.AssociateOriginColumnsHandler;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 0转空处理器
 */
public class ZeroSetEmptyHandler implements AssociateOriginColumnsHandler {

    public Map<String, Cell> handleBeforeParseCell(Map<String, Cell> titleToCellMap, Row rowReader) {
        return null;
    }

    public List<String> handleAfterParseCell(Map<String, String> titleToCellValueMap, Row rowReader) {
        return titleToCellValueMap.values().stream()
                .map((v) -> StrUtil.isNotBlank(v) && Double.parseDouble(v) <= 0.0 ? "" : v)
                .collect(Collectors.toList());
    }
}
