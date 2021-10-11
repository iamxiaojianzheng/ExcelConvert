package cn.xiaojianzheng.framework.xml;

import cn.xiaojianzheng.framework.xml.adapter.lang.BooleanAdapter;
import cn.xiaojianzheng.framework.xml.property.BooleanProperty;
import cn.xiaojianzheng.framework.xml.adapter.lang.IntegerAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@ToString
@XmlAccessorType(XmlAccessType.FIELD)
public class Properties {

    @XmlElement
    private BooleanProperty booleanProperty;

    /**
     * 小数保留几位
     */
    @XmlElement
    @Getter(AccessLevel.NONE)
    @XmlJavaTypeAdapter(IntegerAdapter.class)
    private Integer globalScale;

    @XmlElement
    @Getter(AccessLevel.NONE)
    @XmlJavaTypeAdapter(BooleanAdapter.class)
    private Boolean needScale;

    /**
     * 小数格式化（默认保留两位小数）
     */
    @Getter(AccessLevel.NONE)
    @XmlElement
    private String globalBigDecimalFormat;

    /**
     * 日期格式
     */
    @XmlElement
    @Getter(AccessLevel.NONE)
    private String globalDatetimeFormat;

    @Getter(AccessLevel.NONE)
    @XmlElement(name = "parseDateFormat")
    @XmlElementWrapper(name = "parseDateFormats")
    private List<String> parseDateFormats;

    public Integer getGlobalScale() {
        if (Optional.ofNullable(globalScale).isPresent() && globalScale > 0) {
            return globalScale;
        }
        return 2;
    }

    public Boolean getNeedScale() {
        return Optional.ofNullable(needScale).orElse(false);
    }

    public List<String> getParseDateFormats() {
        return Optional.ofNullable(parseDateFormats).orElse(new ArrayList<>());
    }

    public String getGlobalBigDecimalFormat() {
        return Optional.ofNullable(globalBigDecimalFormat).orElse("0.#####");
    }

    public String getGlobalDatetimeFormat() {
        return Optional.ofNullable(globalDatetimeFormat).orElse("yyyy年MM月dd日");
    }
}
