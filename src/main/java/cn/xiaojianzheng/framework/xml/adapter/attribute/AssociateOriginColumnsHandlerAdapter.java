package cn.xiaojianzheng.framework.xml.adapter.attribute;

import cn.xiaojianzheng.framework.handler.AssociateOriginColumnsHandler;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class AssociateOriginColumnsHandlerAdapter extends XmlAdapter<String, AssociateOriginColumnsHandler> {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public AssociateOriginColumnsHandler unmarshal(String v) throws Exception {
        try {
            return (AssociateOriginColumnsHandler) applicationContext.getAutowireCapableBeanFactory().createBean(Class.forName(v));
        } catch (ClassNotFoundException ignored) {
        }
        return null;
    }

    @Override
    public String marshal(AssociateOriginColumnsHandler v) throws Exception {
        return v.getClass().getName();
    }

}

