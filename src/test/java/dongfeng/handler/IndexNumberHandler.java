package dongfeng.handler;

import cn.xiaojianzheng.framework.handler.AssociateOriginColumnsHandler;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 序号列处理器
 */
public class IndexNumberHandler implements AssociateOriginColumnsHandler {
    private final ThreadLocal<List<Row>> threadLocal = new ThreadLocal<>();

    public IndexNumberHandler() {
        this.threadLocal.set(new ArrayList<>());
        Runtime.getRuntime().addShutdownHook(new Thread(this.threadLocal::remove));
    }

    public Map<String, Cell> handleBeforeParseCell(Map<String, Cell> titleToCellMap, Row rowReader) {
        return null;
    }

    public List<String> handleAfterParseCell(Map<String, String> titleToCellValueMap, Row rowReader) {
        List<Row> rows = this.threadLocal.get();
        if (!rows.contains(rowReader)) {
            rows.add(rowReader);
        }

        String fillValue = String.valueOf(rows.size());
        String[] strings = new String[titleToCellValueMap.size()];
        Arrays.fill(strings, fillValue);
        return Arrays.asList(strings);
    }
}
