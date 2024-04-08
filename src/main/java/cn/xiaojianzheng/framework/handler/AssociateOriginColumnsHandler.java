package cn.xiaojianzheng.framework.handler;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.List;
import java.util.Map;

/**
 * 关联原始列处理器
 * <p>
 * 可以用于根据原始的A、B、C列生成新的D列
 */
public interface AssociateOriginColumnsHandler {

    /**
     * 处理解析之前的原始数据
     */
    default Map<String, Cell> handleBeforeParseCell(Map<String, Cell> titleToCellMap, Row rowReader) {
        return null;
    }

    /**
     * 处理解析单元格之后的数据
     *
     * @param titleToCellValueMap 标题与单元格值的映射
     * @param rowReader {@link org.apache.poi.ss.usermodel.Row }对象
     * @return 返回的{@code List<String>}用于填写单元格，填写规则是基于{@code rowReader}
     */
    default List<String> handleAfterParseCell(Map<String, String> titleToCellValueMap, Row rowReader) {
        return null;
    }

}
