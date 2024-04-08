package dongfeng.convert;

import cn.hutool.core.util.StrUtil;
import cn.xiaojianzheng.framework.convert.AbstractExcelConvertHandler;
import cn.xiaojianzheng.framework.util.CellParseUtil;
import cn.xiaojianzheng.framework.xml.ExcelXml;
import org.apache.poi.ss.usermodel.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class DongfengExcelConvert extends AbstractExcelConvertHandler {

    public int doWriterTitleRowHandler(Sheet sheetReader, Sheet sheetWriter, ExcelXml excelXml) {
        Row row = sheetWriter.createRow(0);
        AtomicInteger i = new AtomicInteger();
        CellStyle cellStyle = sheetWriter.getWorkbook().createCellStyle();
        Font font = sheetWriter.getWorkbook().createFont();
        font.setBold(true);
        cellStyle.setFont(font);
        excelXml.getColumns().forEach((column) -> {
            Cell cell = Optional.ofNullable(row.getCell(i.get())).orElse(row.createCell(i.getAndIncrement()));
            cell.setCellStyle(cellStyle);
            cell.setCellValue(column.getName());
        });
        return 2;
    }

    public List<String> getSourceExcelTitles(Sheet sheetReader) {
        Row row1 = sheetReader.getRow(0);
        Row row2 = sheetReader.getRow(1);
        int columnNum = row1.getLastCellNum() > row2.getLastCellNum() ? row1.getLastCellNum() : row2.getLastCellNum();
        ArrayList<String> titles = new ArrayList<>(columnNum);
        String regex = "[a-zA-Z]+";

        for (int i = 0; i < columnNum; ++i) {
            String s1 = null;
            String s2 = null;
            Cell cell1 = row1.getCell(i);
            Cell cell2 = row2.getCell(i);
            if (cell1 != null || cell2 != null) {
                if (cell1 != null) {
                    s1 = CellParseUtil.doParse(cell1);
                }

                if (cell2 != null) {
                    s2 = CellParseUtil.doParse(cell2);
                }

                if (StrUtil.isNotBlank(s1) && !s1.matches(regex)) {
                    titles.add(s1);
                } else if (StrUtil.isNotBlank(s2) && !s2.matches(regex)) {
                    titles.add(s2);
                }
            }
        }

        return titles;
    }
}
