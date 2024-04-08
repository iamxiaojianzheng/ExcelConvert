package dongfeng.handler;

import cn.xiaojianzheng.framework.handler.AssociateOriginColumnsHandler;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 公司处理器
 */
public class CompanyHandler implements AssociateOriginColumnsHandler {

    public Map<String, Cell> handleBeforeParseCell(Map<String, Cell> titleToCellMap, Row rowReader) {
        return null;
    }

    public List<String> handleAfterParseCell(Map<String, String> titleToCellValueMap, Row rowReader) {
        return titleToCellValueMap.entrySet().stream()
                .map((entry) -> entry.getKey().contains("法人") ? entry.getValue() : "")
                .collect(Collectors.toList());
    }
}
