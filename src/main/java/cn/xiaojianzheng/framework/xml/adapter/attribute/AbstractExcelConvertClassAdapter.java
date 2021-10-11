package cn.xiaojianzheng.framework.xml.adapter.attribute;


import cn.xiaojianzheng.framework.convert.AbstractExcelConvertHandler;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;


@Component
public class AbstractExcelConvertClassAdapter extends XmlAdapter<String, AbstractExcelConvertHandler> {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public AbstractExcelConvertHandler unmarshal(String v) throws Exception {
        if (v == null) {
            return null;
        }
        return (AbstractExcelConvertHandler) applicationContext.getAutowireCapableBeanFactory()
                .createBean(Class.forName(v), AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, false);
    }

    @Override
    public String marshal(AbstractExcelConvertHandler v) throws Exception {
        return v.getClass().getName();
    }
}
