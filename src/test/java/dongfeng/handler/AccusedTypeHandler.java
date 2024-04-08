package dongfeng.handler;

import cn.hutool.core.util.StrUtil;
import cn.xiaojianzheng.framework.handler.AssociateOriginColumnsHandler;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 被告类型处理器
 */
public class AccusedTypeHandler implements AssociateOriginColumnsHandler {
    public List<String> handleAfterParseCell(Map<String, String> titleToCellValueMap, Row rowReader) {
        return titleToCellValueMap.values().stream()
                .map((name) -> StrUtil.isNotBlank(name) ? (name.endsWith("公司") ? "法人" : "自然人") : "")
                .collect(Collectors.toList());
    }
}
