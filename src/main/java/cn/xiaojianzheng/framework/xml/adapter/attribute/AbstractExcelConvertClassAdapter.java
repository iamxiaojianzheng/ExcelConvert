package cn.xiaojianzheng.framework.xml.adapter.attribute;


import cn.xiaojianzheng.framework.convert.AbstractExcelConvertHandler;
import javax.xml.bind.annotation.adapters.XmlAdapter;


public class AbstractExcelConvertClassAdapter extends XmlAdapter<String, AbstractExcelConvertHandler> {
    @Override
    public AbstractExcelConvertHandler unmarshal(String v) throws Exception {
        if (v == null) {
            return null;
        }
        return (AbstractExcelConvertHandler) Class.forName(v).getDeclaredConstructor().newInstance();
    }

    @Override
    public String marshal(AbstractExcelConvertHandler v) throws Exception {
        return v.getClass().getName();
    }
}
