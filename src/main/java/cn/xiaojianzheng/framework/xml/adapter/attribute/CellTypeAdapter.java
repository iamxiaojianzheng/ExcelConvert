package cn.xiaojianzheng.framework.xml.adapter.attribute;

import cn.xiaojianzheng.framework.enums.ExcelCellType;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class CellTypeAdapter extends XmlAdapter<String, ExcelCellType> {
    @Override
    public ExcelCellType unmarshal(String v) throws Exception {
        try {
            return ExcelCellType.valueOf(v.toUpperCase());
        } catch (Exception e) {
            return ExcelCellType.STRING;
        }
    }

    @Override
    public String marshal(ExcelCellType v) throws Exception {
        return v.name();
    }
}
