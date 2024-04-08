package dongfeng.handler.jiekuan;

import cn.hutool.core.util.StrUtil;
import cn.xiaojianzheng.framework.util.CellParseUtil;
import dongfeng.handler.CompanyAddressHandler;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 公司注册地址处理
 */
public class CompanyRegistryAddressHandler extends CompanyAddressHandler {

    public List<String> handleAfterParseCell(Map<String, String> titleToCellValueMap, Row rowReader) {
        List<String> list = super.handleAfterParseCell(titleToCellValueMap, rowReader);
        String name = titleToCellValueMap.get("一被姓名");
        if (StrUtil.isNotBlank(name) && name.contains("公司")) {
            Optional<Map.Entry<String, String>> optionalEntry = titleToCellValueMap.entrySet().stream().filter((entry) -> entry.getKey().contains("一被住址")).findFirst();
            if (optionalEntry.isPresent()) {
                String address = optionalEntry.get().getValue();
                if (StrUtil.isNotBlank(address)) {
                    list.set(0, address);
                }
            }
        }

        return list;
    }
}
