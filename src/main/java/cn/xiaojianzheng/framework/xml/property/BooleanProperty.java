package cn.xiaojianzheng.framework.xml.property;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
