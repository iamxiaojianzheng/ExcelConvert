package dongfeng.handler;

import cn.hutool.core.util.StrUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 公司地址处理器
 */
public class CompanyAddressHandler extends CompanyHandler {

    public Map<String, Cell> handleBeforeParseCell(Map<String, Cell> titleToCellMap, Row rowReader) {
        return super.handleBeforeParseCell(titleToCellMap, rowReader);
    }

    public List<String> handleAfterParseCell(Map<String, String> titleToCellValueMap, Row rowReader) {
        return super.handleAfterParseCell(titleToCellValueMap, rowReader).stream()
                .map((address) -> StrUtil.isNotBlank(address) && address.length() > 5 ? address : "")
                .collect(Collectors.toList());
    }
}
