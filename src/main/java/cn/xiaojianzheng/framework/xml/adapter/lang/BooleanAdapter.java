package cn.xiaojianzheng.framework.xml.adapter.lang;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import java.util.Objects;

public class BooleanAdapter extends XmlAdapter<String, Boolean> {
    @Override
    public Boolean unmarshal(String v) throws Exception {
        return Objects.equals(v, "true");
    }

    @Override
    public String marshal(Boolean v) throws Exception {
        return v ? "true" : "false";
    }
}
