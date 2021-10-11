package cn.xiaojianzheng.framework.xml.adapter.lang;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import org.springframework.stereotype.Component;

@Component
public class BooleanAdapter extends XmlAdapter<String, Boolean> {
    @Override
    public Boolean unmarshal(String v) throws Exception {
        return "true".equals(v);
    }

    @Override
    public String marshal(Boolean v) throws Exception {
        return v ? "true" : "false";
    }
}
