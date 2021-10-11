package cn.xiaojianzheng.framework.xml;

import cn.xiaojianzheng.framework.handler.AssociateOriginColumnsHandler;
import cn.xiaojianzheng.framework.xml.adapter.attribute.AssociateOriginColumnsHandlerAdapter;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Data
@ToString
@XmlAccessorType(XmlAccessType.FIELD)
public class AssociateOriginColumns {

    @Getter(AccessLevel.NONE)
    @XmlElement(name = "columnName")
    private List<String> columnNames;

    @XmlAttribute
    @XmlJavaTypeAdapter(AssociateOriginColumnsHandlerAdapter.class)
    private AssociateOriginColumnsHandler handler;

    public List<String> getColumnNames() {
        return Optional.ofNullable(columnNames).orElse(Collections.emptyList());
    }
}
