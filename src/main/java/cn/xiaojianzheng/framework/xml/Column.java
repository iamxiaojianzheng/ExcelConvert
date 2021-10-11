package cn.xiaojianzheng.framework.xml;

import cn.xiaojianzheng.framework.convert.CellValueConvert;
import cn.xiaojianzheng.framework.enums.ExcelCellType;
import cn.xiaojianzheng.framework.xml.adapter.lang.BooleanAdapter;
import cn.xiaojianzheng.framework.xml.adapter.attribute.CellTypeAdapter;
import cn.xiaojianzheng.framework.xml.adapter.attribute.CellValueConvertAdapter;
import cn.xiaojianzheng.framework.xml.adapter.lang.IntegerAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Data
@ToString(exclude = { "excelXml" })
@XmlAccessorType(XmlAccessType.FIELD)
public class Column {

    /**
     * 每列在XML中配置的顺序索引
     */
    private Integer index;

    /**
     * 所属Excel
     */
    private ExcelXml excelXml;

    /**
     * 列名
     */
    @XmlAttribute
    private String name;

    /**
     * 转换后的列名
     */
    @XmlAttribute
    private String from;

    /**
     * 整列的单元格类型
     */
    @XmlAttribute
    @XmlJavaTypeAdapter(CellTypeAdapter.class)
    private ExcelCellType cellType;

    @XmlAttribute
    private String dateFormat;

    @XmlAttribute
    @XmlJavaTypeAdapter(IntegerAdapter.class)
    private Integer scale;

    @XmlAttribute
    private String fixedValue;

    @Getter(AccessLevel.NONE)
    @XmlAttribute
    @XmlJavaTypeAdapter(BooleanAdapter.class)
    private Boolean allowEmpty;

    @XmlAttribute
    @XmlJavaTypeAdapter(CellValueConvertAdapter.class)
    private CellValueConvert cellValueConvertClass;

    @XmlElement(name = "convertRule")
    private List<Convert> convertRules;

    @XmlElement(name = "associate")
    private AssociateOriginColumns associateOriginColumns;

    public boolean isNeedConvert() {
        return convertRules != null;
    }

    public String getFrom() {
        if (from == null) {
            return name;
        }
        return from;
    }

    public boolean isAllowEmpty() {
        if (allowEmpty == null) {
            return true;
        }
        return allowEmpty;
    }

}
