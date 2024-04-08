package dongfeng.handler.jiekuan;

import cn.hutool.core.util.StrUtil;
import dongfeng.handler.CompanyAddressHandler;
import org.apache.poi.ss.usermodel.Row;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 公司邮寄地址处理器
 */
public class CompanyEmailAddressHandler extends CompanyAddressHandler {

    public List<String> handleAfterParseCell(Map<String, String> titleToCellValueMap, Row rowReader) {
        List<String> list = super.handleAfterParseCell(titleToCellValueMap, rowReader);
        String name = titleToCellValueMap.get("一被姓名");
        if (StrUtil.isNotBlank(name) && name.contains("公司")) {
            Optional<Map.Entry<String, String>> optional = titleToCellValueMap.entrySet().stream().filter((entry) -> entry.getKey().contains("一被合同地址")).findFirst();
            if (optional.isPresent()) {
                String address = optional.get().getValue();
                if (StrUtil.isNotBlank(address)) {
                    list.set(0, address.length() > 5 ? address : "");
                }
            }
        }

        return list;
    }
}