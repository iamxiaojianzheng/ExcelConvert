package cn.xiaojianzheng.framework.xml.adapter;

import cn.xiaojianzheng.framework.xml.adapter.attribute.AbstractExcelConvertClassAdapter;
import cn.xiaojianzheng.framework.xml.adapter.attribute.AssociateOriginColumnsHandlerAdapter;
import cn.xiaojianzheng.framework.xml.adapter.attribute.CellTypeAdapter;
import cn.xiaojianzheng.framework.xml.adapter.attribute.CellValueConvertAdapter;
import cn.xiaojianzheng.framework.xml.adapter.lang.BooleanAdapter;
import cn.xiaojianzheng.framework.xml.adapter.lang.IntegerAdapter;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import java.util.ArrayList;
import java.util.List;

public class AdapterUtil {

    public static List<XmlAdapter<?, ?>> getDefaultAdapters() {
        List<XmlAdapter<?, ?>> adapters = new ArrayList<XmlAdapter<?, ?>>();

        // attribute
        adapters.add(new AbstractExcelConvertClassAdapter());
        adapters.add(new AssociateOriginColumnsHandlerAdapter());
        adapters.add(new CellTypeAdapter());
        adapters.add(new CellValueConvertAdapter());

        // lang
        adapters.add(new BooleanAdapter());
        adapters.add(new IntegerAdapter());

        return adapters;
    }

}
