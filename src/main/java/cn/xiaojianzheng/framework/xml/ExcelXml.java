package cn.xiaojianzheng.framework.xml;

import cn.xiaojianzheng.framework.convert.AbstractExcelConvertHandler;
import cn.xiaojianzheng.framework.handler.RemoveRowHandler;
import cn.xiaojianzheng.framework.xml.adapter.attribute.AbstractExcelConvertClassAdapter;
import cn.xiaojianzheng.framework.xml.adapter.attribute.RemoveRowHandlerAdapter;
import cn.xiaojianzheng.framework.xml.adapter.lang.BooleanAdapter;
import cn.xiaojianzheng.framework.xml.adapter.lang.IntegerAdapter;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Optional;

@Data
@ToString
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "excel")
public class ExcelXml {

    private String excelName;

    @XmlElement
    @Getter(AccessLevel.NONE)
    private Properties properties;

    @XmlAttribute
    @Getter(AccessLevel.NONE)
    @XmlJavaTypeAdapter(IntegerAdapter.class)
    private Integer fromSheetIndex;

    @XmlAttribute
    @Getter(AccessLevel.NONE)
    @XmlJavaTypeAdapter(IntegerAdapter.class)
    private Integer toSheetIndex;

    @XmlAttribute
    @Getter(AccessLevel.NONE)
    @XmlJavaTypeAdapter(BooleanAdapter.class)
    private Boolean removeBlankRowAfterConvert;

    @XmlAttribute
    @XmlJavaTypeAdapter(RemoveRowHandlerAdapter.class)
    private RemoveRowHandler removeRowHandler;

    @XmlAttribute
    @XmlJavaTypeAdapter(AbstractExcelConvertClassAdapter.class)
    private AbstractExcelConvertHandler excelConvertHandlerClass;

    @XmlElement(name = "column")
    @XmlElementWrapper(name = "columns")
    private List<Column> columns;

    public Properties getProperties() {
        return Optional.ofNullable(properties).orElse(new Properties());
    }

    public int getFromSheetIndex() {
        return Optional.ofNullable(fromSheetIndex).orElse(0);
    }

    public Boolean getRemoveBlankRowAfterConvert() {
        return Optional.ofNullable(removeBlankRowAfterConvert).orElse(false);
    }

    public int getToSheetIndex() {
        return Optional.ofNullable(toSheetIndex).orElse(0);
    }
}
