package cn.xiaojianzheng.framework.xml;

import cn.xiaojianzheng.framework.handler.AssociateOriginColumnsHandler;
import cn.xiaojianzheng.framework.xml.adapter.attribute.AssociateOriginColumnsHandlerAdapter;
import cn.xiaojianzheng.framework.xml.adapter.lang.BooleanAdapter;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 行转列
 * <p>
 * 可用于将一行中多个单元格的值，向指定列的单元格，垂直向下填写。
 */
@Data
@ToString
@XmlAccessorType(XmlAccessType.FIELD)
public class AssociateOriginColumns {

    /**
     * 是否允许符合匹配的所有列的单元格内容为空
     * <p>
     * 如果不允许为空，并且单元格内容也不为空，则会使用{@code AssociateOriginColumnsHandler}处理器完成内容填写
     */
    @Getter(AccessLevel.NONE)
    @XmlAttribute
    @XmlJavaTypeAdapter(BooleanAdapter.class)
    private Boolean allowEmpty;

    /**
     * 匹配列名的正则表达式集合
     * <p>
     * 填写内容为正则表达式，用于匹配符合条件的列名。
     */
    @Getter(AccessLevel.NONE)
    @XmlElement(name = "columnNameRegex")
    private List<String> columnNameRegex;

    @XmlAttribute
    @XmlJavaTypeAdapter(AssociateOriginColumnsHandlerAdapter.class)
    private AssociateOriginColumnsHandler handler;

    public boolean isAllowEmpty() {
        if (allowEmpty == null) {
            return true;
        }
        return allowEmpty;
    }

    public List<String> getColumnNameRegex() {
        return Optional.ofNullable(columnNameRegex).orElse(Collections.emptyList());
    }

}
