package cn.xiaojianzheng.framework.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
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
