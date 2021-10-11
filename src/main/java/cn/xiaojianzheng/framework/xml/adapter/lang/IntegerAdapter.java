package cn.xiaojianzheng.framework.xml.adapter.lang;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import org.springframework.stereotype.Component;

@Component
public class IntegerAdapter extends XmlAdapter<String, Integer> {
    @Override
    public Integer unmarshal(String v) throws Exception {
        return Integer.valueOf(v);
    }

    @Override
    public String marshal(Integer v) throws Exception {
        return String.valueOf(v);
    }
}
