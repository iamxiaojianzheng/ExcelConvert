# ExcelConvert

基于XML配置的Excel表格转换器（学习使用）

SpringBoot + Maven + JDK8/JDK11 + JXAB

分支
- main(jdk8)
- jdk11

# Usage

通过XML配置两个Excel表格之间的转换逻辑
```xml
<?xml version="1.0" encoding="UTF-8"?>

<!-- removeBlankRowAfterConvert: 是否在转换后删除空行 -->
<!-- excelConvertHandlerClass: excel转换处理类，用于编写生成新表标题行、获取原表标题行的逻辑代码 -->
<excel removeBlankRowAfterConvert="true"
       excelConvertHandlerClass="cn.xiaojianzheng.convert.CustomExcelConvert">
    
    <!-- 全局配置 -->
    <properties>
        <!-- 布尔值单元格 转换 -->
        <booleanProperty yes="是" no="否"/>
        <!-- 小数保留几位，默认四舍五入 -->
        <globalScale>2</globalScale>
        <!-- 是否需要四舍五入 -->
        <needScale>false</needScale>
        <!-- 百分比格式化 -->
        <globalPercentageFormat>0.##</globalPercentageFormat>
        <!-- 日期格式化 -->
        <globalDatetimeFormat>yyyy年MM月dd日</globalDatetimeFormat>
        <!-- 解析日期格式，可以添加多个 -->
        <parseDateFormats>
            <parseDateFormat>yyyy-MM</parseDateFormat>
            <parseDateFormat>HH:mm</parseDateFormat>
        </parseDateFormats>
    </properties>

    <columns>
        <!-- 假设要跟原表的商品名称、商品价格列处理生成新表的序号列 -->
        <!-- cellType: NUMERIC、STRING、FORMULA、BLANK、BOOLEAN、DATE。该列以什么格式处理 -->
        <column name="新表序号列" from="原表序号列" cellType="STRING" allowEmpty="false">
            <!-- 该处理类需要实现AssociateOriginColumnsHandler接口 -->
            <associate handler="cn.xiaojianzheng.handler.IndexNumberHandler">
                <!-- 支持正则匹配，可以写多个 -->
                <columnName>.*商品名称.*</columnName>
                <columnName>商品价格</columnName>
            </associate>
        </column>
        <column name="商品名称"/>
        <column name="商品价格" cellType="NUMERIC"/>
        <!-- 其他剩余列 -->
    </columns>
</excel>
```

调用convert接口
```java

import cn.xiaojianzheng.framework.util.ExcelConvertUtil;

@Autowaired
ExcelConvertUtil excelConvertUtil;

excelConvertUtil.convert(sourceExcelFile, targetExcelFile, convertXmlFile);
```

