package cn.xiaojianzheng.framework.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import lombok.Data;
import lombok.ToString;

/**
 * 转换标签，用于单元格内容的准换
 */
@Data
@ToString
@XmlAccessorType(XmlAccessType.FIELD)
public class Convert {

    /**
     * 原表单元格的内容
     */
    @XmlAttribute
    private String oldValue;

    /**
     * 新表对应单元格的内容
     */
    @XmlAttribute
    private String newValue;

}
