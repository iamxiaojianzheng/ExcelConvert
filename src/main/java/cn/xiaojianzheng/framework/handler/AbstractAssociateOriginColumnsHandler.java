package cn.xiaojianzheng.framework.handler;

import cn.xiaojianzheng.framework.util.CellParseUtil;

public abstract class AbstractAssociateOriginColumnsHandler implements AssociateOriginColumnsHandler {

    protected CellParseUtil cellParseUtil;

    public AbstractAssociateOriginColumnsHandler(CellParseUtil cellParseUtil) {
        this.cellParseUtil = cellParseUtil;
    }

}
