package cn.xiaojianzheng.framework.xml.adapter.attribute;

import cn.xiaojianzheng.framework.handler.RemoveRowHandler;
import cn.xiaojianzheng.framework.util.CellParseUtil;
import javax.xml.bind.annotation.adapters.XmlAdapter;


public class RemoveRowHandlerAdapter extends XmlAdapter<String, RemoveRowHandler> {
    @Override
    public RemoveRowHandler unmarshal(String v) throws Exception {
        if (v == null) {
            return null;
        }
        return (RemoveRowHandler) Class.forName(v).getDeclaredConstructor().newInstance();
    }

    @Override
    public String marshal(RemoveRowHandler v) throws Exception {
        return v.getClass().getName();
    }
}
