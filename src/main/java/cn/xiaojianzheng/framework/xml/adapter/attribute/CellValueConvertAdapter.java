package cn.xiaojianzheng.framework.xml.adapter.attribute;

import cn.xiaojianzheng.framework.convert.CellValueConvert;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import org.springframework.stereotype.Component;


@Component
public class CellValueConvertAdapter extends XmlAdapter<String, CellValueConvert> {
    @Override
    public CellValueConvert unmarshal(String v) throws Exception {
        if (v == null) {
            return null;
        }
        return (CellValueConvert) Class.forName(v).getDeclaredConstructor().newInstance();
    }

    @Override
    public String marshal(CellValueConvert v) throws Exception {
        return v.getClass().getName();
    }
}
