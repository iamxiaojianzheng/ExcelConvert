package cn.xiaojianzheng.framework.util;

public class ExcelUtil {

    public static boolean isExcel(String fileName) {
        return fileName.endsWith(".xlsx") || fileName.endsWith(".xls");
    }

}
