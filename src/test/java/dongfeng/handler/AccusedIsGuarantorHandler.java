package dongfeng.handler;

import cn.hutool.core.util.StrUtil;
import cn.xiaojianzheng.framework.handler.AssociateOriginColumnsHandler;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 被告是否担保人处理器
 */
public class AccusedIsGuarantorHandler implements AssociateOriginColumnsHandler {
    public Map<String, Cell> handleBeforeParseCell(Map<String, Cell> titleToCellMap, Row rowReader) {
        return null;
    }

    public List<String> handleAfterParseCell(Map<String, String> titleToCellValueMap, Row rowReader) {
        List<String> list = titleToCellValueMap.values().stream().map((name) -> StrUtil.isNotBlank(name) ? "是" : "").collect(Collectors.toList());
        if (list.isEmpty()) {
            list.add("否");
        } else {
            list.set(0, "否");
        }

        return list;
    }
}