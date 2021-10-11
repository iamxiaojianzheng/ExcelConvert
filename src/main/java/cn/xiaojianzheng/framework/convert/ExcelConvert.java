package cn.xiaojianzheng.framework.convert;

import cn.xiaojianzheng.framework.xml.ExcelXml;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

public interface ExcelConvert {
    /**
     * 目标表写处理器
     *
     * @param sheetReader 源表
     * @param sheetWriter 目标表
     * @param excelXml    excel配置对象
     * @return 写了多少行
     */
    int doWriterTitleRowHandler(Sheet sheetReader, Sheet sheetWriter, ExcelXml excelXml);

    /**
     * 获取源表标题
     *
     * @param sheetReader 源表
     * @return 标题集合
     */
    List<String> getSourceExcelTitles(Sheet sheetReader);

    /**
     * 单行处理器
     *
     * @param sourceExcelTitles 源表标题集
     * @param sheetReader       源表
     * @param sheetWriter       目标表
     * @return 目标表新增了多少行
     */
    int doRowHandler(List<String> sourceExcelTitles, Sheet sheetReader, Sheet sheetWriter);

}