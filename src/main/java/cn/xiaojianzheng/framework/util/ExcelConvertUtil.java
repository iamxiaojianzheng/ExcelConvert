package cn.xiaojianzheng.framework.util;

import cn.xiaojianzheng.framework.convert.AbstractExcelConvertHandler;
import cn.xiaojianzheng.framework.exception.ExcelConvertExceptionUtil;
import cn.xiaojianzheng.framework.handler.RemoveRowHandler;
import cn.xiaojianzheng.framework.xml.AssociateOriginColumns;
import cn.xiaojianzheng.framework.xml.Column;
import cn.xiaojianzheng.framework.xml.ExcelXml;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Component
public class ExcelConvertUtil {

    private final Unmarshaller unmarshaller;

    private final List<XmlAdapter<?, ?>> adapters;

    private final CellParseUtil cellParseUtil;

    private RemoveRowHandler removeRowHandler;

    public ExcelConvertUtil(List<XmlAdapter<?, ?>> adapters, CellParseUtil cellParseUtil, RemoveRowHandler removeRowHandler) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(ExcelXml.class);
        this.unmarshaller = jaxbContext.createUnmarshaller();
        this.adapters = adapters;
        this.cellParseUtil = cellParseUtil;
        this.removeRowHandler = removeRowHandler;
    }

    public void convert(File source, File target, File xmlFile) {
        try (final BufferedInputStream stream = new BufferedInputStream(new FileInputStream(xmlFile))) {
            convert(source, target, stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void convert(File source, File target, InputStream xmlFile) {

        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(source));
             Workbook excelReader = WorkbookFactory.create(inputStream);
             Workbook excelWriter = new XSSFWorkbook()) {

            Optional.ofNullable(adapters)
                    .orElse(Collections.emptyList())
                    .forEach(unmarshaller::setAdapter);

            ExcelXml excelXml = (ExcelXml) unmarshaller.unmarshal(xmlFile);
            excelXml.setExcelName(source.getName());
            checkExcelXml(excelXml);
            if (excelXml.getRemoveRowHandler() != null) {
                removeRowHandler = excelXml.getRemoveRowHandler();
            }

            cellParseUtil.setGlobalDateFormat(excelXml.getProperties().getGlobalDatetimeFormat());
            cellParseUtil.setGlobalBigDecimalFormat(excelXml.getProperties().getGlobalBigDecimalFormat());
            cellParseUtil.setNeedScale(excelXml.getProperties().getNeedScale());
            cellParseUtil.setGlobalScale(excelXml.getProperties().getGlobalScale());
            cellParseUtil.setParseDateFormatList(excelXml.getProperties().getParseDateFormats());

            // 追加信息
            List<Column> columns = excelXml.getColumns();
            for (int i = 0; i < columns.size(); i++) {
                Column column = columns.get(i);
                column.setIndex(i); // 该列在XML中的索引顺序
                column.setExcelXml(excelXml); // 该列所属的Excel
            }

            final Sheet sheetReader = excelReader.getSheetAt(0);
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
                removeAllBlankRow(sheetWriter, columns);
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
    private void removeAllBlankRow(Sheet sheetReader, List<Column> columns) {
        int lastRowNum = sheetReader.getLastRowNum();
        for (int i = 0; i <= lastRowNum; i++) {
            Row row = sheetReader.getRow(i);
            if (removeRowHandler.needRemove(row, columns)) {
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
            if (cell != null && StringUtils.hasText(cellParseUtil.doParse(cell))) {
                return false;
            }
        }
        return true;
    }

    private void checkExcelXml(ExcelXml excelXml) {
        excelXml.getColumns().forEach(column -> {
            AssociateOriginColumns associateOriginColumns = column.getAssociateOriginColumns();
            if (associateOriginColumns != null
                    && associateOriginColumns.getHandler() == null
                    && CollectionUtils.isEmpty(associateOriginColumns.getColumnNames())) {
                ExcelConvertExceptionUtil.of("{}列下存在空的associate标签（既没有handler属性，也没有columnName子元素）", column.getName());
            }
        });
    }

}
