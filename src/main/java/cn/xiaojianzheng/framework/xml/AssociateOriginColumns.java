package cn.xiaojianzheng.framework.xml;

import cn.xiaojianzheng.framework.handler.AssociateOriginColumnsHandler;
import cn.xiaojianzheng.framework.xml.adapter.lang.BooleanAdapter;
import cn.xiaojianzheng.framework.xml.adapter.attribute.AssociateOriginColumnsHandlerAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Data
@ToString
@XmlAccessorType(XmlAccessType.FIELD)
public class AssociateOriginColumns {

    @Getter(AccessLevel.NONE)
    @XmlAttribute
    @XmlJavaTypeAdapter(BooleanAdapter.class)
    private Boolean allowEmpty;

    @Getter(AccessLevel.NONE)
    @XmlElement(name = "columnName")
    private List<String> columnNames;

    @XmlAttribute
    @XmlJavaTypeAdapter(AssociateOriginColumnsHandlerAdapter.class)
    private AssociateOriginColumnsHandler handler;

    public boolean isAllowEmpty() {
        if (allowEmpty == null) {
            return true;
        }
        return allowEmpty;
    }

    public List<String> getColumnNames() {
        return Optional.ofNullable(columnNames).orElse(Collections.emptyList());
    }
}
