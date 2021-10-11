package cn.xiaojianzheng.framework.xml.property;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@XmlAccessorType(XmlAccessType.FIELD)
public class BooleanProperty {

    @XmlAttribute
    private String yes;

    @XmlAttribute
    private String no;

}
