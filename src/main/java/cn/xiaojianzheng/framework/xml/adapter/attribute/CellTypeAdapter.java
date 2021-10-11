package cn.xiaojianzheng.framework.xml.adapter.attribute;

import cn.xiaojianzheng.framework.enums.ExcelCellType;
import org.springframework.stereotype.Component;

import javax.xml.bind.annotation.adapters.XmlAdapter;

@Component
public class CellTypeAdapter extends XmlAdapter<String, ExcelCellType> {
    @Override
    public ExcelCellType unmarshal(String v) throws Exception {
        return ExcelCellType.valueOf(v.toUpperCase());
    }

    @Override
    public String marshal(ExcelCellType v) throws Exception {
        return v.name();
    }
}
