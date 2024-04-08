package dongfeng.handler.jiekuan;

import cn.hutool.core.util.StrUtil;
import cn.xiaojianzheng.framework.handler.AssociateOriginColumnsHandler;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 标的金额处理器
 */
public class AmountHandler implements AssociateOriginColumnsHandler {
    @Override
    public Map<String, Cell> handleBeforeParseCell(Map<String, Cell> titleToCellMap, Row rowReader) {
        return Collections.emptyMap();
    }

    public List<String> handleAfterParseCell(Map<String, String> titleToCellValueMap, Row rowReader) {
        String num1 = StrUtil.blankToDefault(titleToCellValueMap.getOrDefault("剩余本金", "0"), "0");
        String num2 = StrUtil.blankToDefault(titleToCellValueMap.getOrDefault("违约金", "0"), "0");
        return Collections.singletonList((new BigDecimal(num1)).plus().add(new BigDecimal(num2)).toPlainString());
    }
}
