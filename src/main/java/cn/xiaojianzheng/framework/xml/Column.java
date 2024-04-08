package cn.xiaojianzheng.framework.xml;

import cn.xiaojianzheng.framework.convert.CellValueConvert;
import cn.xiaojianzheng.framework.enums.ExcelCellType;
import cn.xiaojianzheng.framework.xml.adapter.attribute.CellTypeAdapter;
import cn.xiaojianzheng.framework.xml.adapter.attribute.CellValueConvertAdapter;
import cn.xiaojianzheng.framework.xml.adapter.lang.BooleanAdapter;
import cn.xiaojianzheng.framework.xml.adapter.lang.IntegerAdapter;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * 列配置
 */
@Data
@ToString(exclude = {"excelXml"})
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

    /**
     * 日期格式，如果单元格类型为{@link cn.xiaojianzheng.framework.enums.ExcelCellType#DATE}，则根据该属性格式化日期
     */
    @XmlAttribute
    private String dateFormat;

    /**
     * 浮点数，如果单元格类型为{@link ExcelCellType#NUMERIC}，则根据该属性计算最终数值
     */
    @XmlAttribute
    @XmlJavaTypeAdapter(IntegerAdapter.class)
    private Integer scale;

    /**
     * 固定值，如果不为空则该列都填写该值
     */
    @XmlAttribute
    private String fixedValue;

    /**
     * 是否允许为空
     */
    @Getter(AccessLevel.NONE)
    @XmlAttribute
    @XmlJavaTypeAdapter(BooleanAdapter.class)
    private Boolean allowEmpty;

    /**
     * 单元格内容转换器
     * <p>
     * 用于处理复杂转换逻辑，可以根据整行单元格信息决定该列单元格最终填写的内容
     */
    @XmlAttribute
    @XmlJavaTypeAdapter(CellValueConvertAdapter.class)
    private CellValueConvert cellValueConvertClass;

    /**
     * 单元格内容转换规则，用于将原固定内容转为指定内容。
     * <p>
     * 例如：原内容为 “旧”，新内容指定为 “新”。
     * <p>
     * 配置方式为：{@code <convertRule oldValue="旧" newValue="新"/> }
     */
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
