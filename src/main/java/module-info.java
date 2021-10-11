module excel.convert {

    requires static transitive java.naming;
    requires transitive com.sun.xml.bind;
    requires java.annotation;

    requires poi;
    requires poi.ooxml;
    requires org.slf4j;
    requires static lombok;

    requires spring.beans;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.core;

    exports cn.xiaojianzheng.framework.convert;
    exports cn.xiaojianzheng.framework.enums;
    exports cn.xiaojianzheng.framework.exception;
    exports cn.xiaojianzheng.framework.handler;
    exports cn.xiaojianzheng.framework.util;
    exports cn.xiaojianzheng.framework.xml;
    exports cn.xiaojianzheng.framework.xml.property;
    exports cn.xiaojianzheng.framework.xml.adapter.lang;
    exports cn.xiaojianzheng.framework.xml.adapter.attribute;

    opens cn.xiaojianzheng to spring.core, spring.beans, spring.context;
    opens cn.xiaojianzheng.dongfeng.convert to spring.core, spring.beans;
    opens cn.xiaojianzheng.dongfeng.handler to spring.core, spring.beans;
    opens cn.xiaojianzheng.framework.handler to spring.core, spring.beans;
    opens cn.xiaojianzheng.framework.handler.impl to spring.core, spring.beans;
    opens cn.xiaojianzheng.framework.util to spring.core, spring.beans;
    opens cn.xiaojianzheng.framework.xml to com.sun.xml.bind, com.sun.xml.bind.core, jakarta.xml.bind;
    opens cn.xiaojianzheng.framework.xml.property to com.sun.xml.bind, com.sun.xml.bind.core, jakarta.xml.bind;
    opens cn.xiaojianzheng.framework.xml.adapter.lang to spring.beans, spring.core;
    opens cn.xiaojianzheng.framework.xml.adapter.attribute to spring.beans, spring.core;

}