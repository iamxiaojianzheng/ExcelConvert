package dongfeng.handler;

import cn.hutool.core.util.StrUtil;
import cn.xiaojianzheng.framework.handler.AssociateOriginColumnsHandler;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 利率处理器
 */
public class InterestRateHandler implements AssociateOriginColumnsHandler {
    private final BigDecimal ONE_HUNDRED = new BigDecimal(100);

    public Map<String, Cell> handleBeforeParseCell(Map<String, Cell> titleToCellMap, Row rowReader) {
        return null;
    }

    public List<String> handleAfterParseCell(Map<String, String> titleToCellValueMap, Row rowReader) {
        return titleToCellValueMap.values().stream()
                .map((rate) -> StrUtil.isNotBlank(rate) ? (new BigDecimal(rate)).multiply(this.ONE_HUNDRED).stripTrailingZeros().toPlainString() : rate)
                .collect(Collectors.toList());
    }
}
