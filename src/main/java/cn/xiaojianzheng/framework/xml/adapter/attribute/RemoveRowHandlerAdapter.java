package cn.xiaojianzheng.framework.xml.adapter.attribute;

import cn.xiaojianzheng.framework.handler.RemoveRowHandler;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;


@Component
public class RemoveRowHandlerAdapter extends XmlAdapter<String, RemoveRowHandler> {

    private final ApplicationContext applicationContext;

    public RemoveRowHandlerAdapter(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public RemoveRowHandler unmarshal(String v) throws Exception {
        if (v == null) {
            return null;
        }
        return (RemoveRowHandler) applicationContext.getAutowireCapableBeanFactory()
                .createBean(Class.forName(v), AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, false);
    }

    @Override
    public String marshal(RemoveRowHandler v) throws Exception {
        return v.getClass().getName();
    }
}
