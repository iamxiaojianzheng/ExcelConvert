package cn.xiaojianzheng.framework.xml;

import cn.xiaojianzheng.framework.convert.CellValueConvert;
import cn.xiaojianzheng.framework.enums.ExcelCellType;
import cn.xiaojianzheng.framework.xml.adapter.lang.BooleanAdapter;
import cn.xiaojianzheng.framework.xml.adapter.attribute.CellTypeAdapter;
import cn.xiaojianzheng.framework.xml.adapter.attribute.CellValueConvertAdapter;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;

@Data
@ToString
@XmlAccessorType(XmlAccessType.FIELD)
public class Column {

    /**
     * 每列在XML中配置的顺序索引
     */
    private Integer index;

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
    @XmlJavaTypeAdapter(CellTypeAdapter.class)
    @XmlAttribute
    private ExcelCellType cellType;

    @XmlAttribute
    private String dateFormat;

    @XmlAttribute
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
