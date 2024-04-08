package cn.xiaojianzheng.framework.xml.adapter.attribute;

import cn.xiaojianzheng.framework.handler.AssociateOriginColumnsHandler;
import cn.xiaojianzheng.framework.util.CellParseUtil;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class AssociateOriginColumnsHandlerAdapter extends XmlAdapter<String, AssociateOriginColumnsHandler> {
    @Override
    public AssociateOriginColumnsHandler unmarshal(String v) throws Exception {
        try {
            return (AssociateOriginColumnsHandler) Class.forName(v).getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException ignored) {
        }
        return null;
    }

    @Override
    public String marshal(AssociateOriginColumnsHandler v) throws Exception {
        return v.getClass().getName();
    }

}

