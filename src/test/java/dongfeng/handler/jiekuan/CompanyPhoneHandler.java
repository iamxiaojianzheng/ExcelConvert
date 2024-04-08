package dongfeng.handler.jiekuan;

import cn.hutool.core.util.StrUtil;
import dongfeng.handler.CompanyHandler;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 公司联系方式处理
 */
public class CompanyPhoneHandler extends CompanyHandler {

    public List<String> handleAfterParseCell(Map<String, String> titleToCellValueMap, Row rowReader) {
        List<String> list = super.handleAfterParseCell(titleToCellValueMap, rowReader);
        String name = titleToCellValueMap.get("一被姓名");
        if (StrUtil.isNotBlank(name) && name.contains("公司")) {
            Optional<Map.Entry<String, String>> optional = titleToCellValueMap.entrySet().stream().filter((entry) -> entry.getKey().contains("一被电话")).findFirst();
            if (optional.isPresent()) {
                String phone = optional.get().getValue();
                if (StrUtil.isNotBlank(phone)) {
                    list.set(0, phone);
                }
            }
        }

        return list;
    }
}
