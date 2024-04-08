package dongfeng.handler;

import cn.hutool.core.util.StrUtil;
import cn.xiaojianzheng.framework.handler.AssociateOriginColumnsHandler;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 保证人处理器
 */
public class GuarantorHandler implements AssociateOriginColumnsHandler {

    public List<String> handleAfterParseCell(Map<String, String> titleToCellValueMap, Row rowReader) {
        List<String> list = new ArrayList<>(titleToCellValueMap.values());
        List<String> notEmptyNameList = list.stream().filter(StrUtil::isNotBlank).collect(Collectors.toList());
        int size = notEmptyNameList.size();

        for (int i = 0; i < list.size(); ++i) {
            String name = list.get(i);
            if (StrUtil.isNotBlank(name)) {
                int index = notEmptyNameList.indexOf(name);
                if (index + 1 < size) {
                    list.set(i, notEmptyNameList.get(index + 1));
                } else {
                    list.set(i, "");
                }
            }
        }

        return list;
    }
}
