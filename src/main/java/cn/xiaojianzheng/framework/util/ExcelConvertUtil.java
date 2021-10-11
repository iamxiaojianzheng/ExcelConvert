package cn.xiaojianzheng.framework.util;

import cn.hutool.core.io.FileUtil;
import cn.xiaojianzheng.framework.convert.AbstractExcelConvertHandler;
import cn.xiaojianzheng.framework.handler.CellParseHandler;
import cn.xiaojianzheng.framework.xml.Column;
import cn.xiaojianzheng.framework.xml.ExcelXml;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.io.*;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Component
public class ExcelConvertUtil {

    @Autowired
    private List<XmlAdapter<?, ?>> adapters;

    @Autowired
    private CellParseHandler cellParseHandler;

    private final Unmarshaller unmarshaller;

    public ExcelConvertUtil() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(ExcelXml.class);
        unmarshaller = jaxbContext.createUnmarshaller();
    }

    public void convert(File source, File target, File xmlFile) {
        try (final BufferedInputStream stream = FileUtil.getInputStream(xmlFile)) {
            convert(source, target, stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void convert(File source, File target, InputStream xmlFile) {

        try (BufferedInputStream inputStream = FileUtil.getInputStream(source);
             Workbook excelReader = WorkbookFactory.create(inputStream);
             Workbook excelWriter = new XSSFWorkbook()) {

            Optional.ofNullable(adapters)
                    .orElse(Collections.emptyList())
                    .forEach(unmarshaller::setAdapter);

            ExcelXml excelXml = (ExcelXml) unmarshaller.unmarshal(xmlFile);
            cellParseHandler.appendParseDateFormatList(excelXml.getProperties().getParseDateFormats());
            cellParseHandler.setGlobalDateFormat(excelXml.getProperties().getGlobalDatetimeFormat());
            cellParseHandler.setNeedScale(excelXml.getProperties().getNeedScale());
            cellParseHandler.setGlobalScale(excelXml.getProperties().getGlobalScale());

            // 追加每列的索引
            List<Column> columns = excelXml.getColumns();
            for (int i = 0; i < columns.size(); i++) {
                columns.get(i).setIndex(i);
            }

            final Sheet sheetReader = excelReader.getSheetAt(excelReader.getActiveSheetIndex());
            Sheet sheet = excelWriter.createSheet();
            while (excelXml.getToSheetIndex() != excelWriter.getSheetIndex(sheet)) {
                sheet = excelWriter.createSheet();
            }
            final Sheet sheetWriter = sheet;

            AbstractExcelConvertHandler excelConvert = excelXml.getExcelConvertHandlerClass();
            List<String> sourceExcelTitles = excelConvert.getSourceExcelTitles(sheetReader);

            // 将源数据的前几行处理成目标数据的title
            int titleRows = excelConvert.doWriterTitleRowHandler(sheetReader, sheetWriter, excelXml);
            final Iterator<Row> rows = sheetReader.rowIterator();
            for (int i = 0; i < titleRows; i++) {
                rows.next();
            }

            rows.forEachRemaining(rowReader -> {
                if (rowReader.getPhysicalNumberOfCells() > 0 && !isBlankRow(rowReader)) {
                    int i = excelConvert.doRowHandler(sourceExcelTitles, sheetReader, sheetWriter);
                    if (i == 0) {
                        excelConvert.rowConvert(sourceExcelTitles, rowReader, sheetWriter, excelXml);
                    }
                }
            });

            // 删除空白行
            if (excelXml.getRemoveBlankRowAfterConvert()) {
                removeAllBlankRow(sheetWriter);
            }

            try (FileOutputStream outStream = new FileOutputStream(target)) {
                excelWriter.write(outStream);
            }
        } catch (IOException | JAXBException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除所有空白行
     */
    private void removeAllBlankRow(Sheet sheetReader) {
        int lastRowNum = sheetReader.getLastRowNum();
        for (int i = 0; i <= lastRowNum; i++) {
            Row row = sheetReader.getRow(i);
            if (row != null && isBlankRow(row)) {
                // 如果为空行则用下面的指定范围的行向上覆盖一行
                sheetReader.shiftRows(i + 1, lastRowNum + 1, -1);
                i--;
                lastRowNum--;
            }
        }
    }

    /**
     * 检测是否为空行
     */
    private boolean isBlankRow(Row row) {
        for (int j = 0; j < row.getLastCellNum(); j++) {
            Cell cell = row.getCell(j);
            if (cell != null && StringUtils.hasText(cellParseHandler.doParse(cell))) {
                return false;
            }
        }
        return true;
    }

}
