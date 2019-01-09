package com.radarwin.framework.util;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by josh on 15/8/4.
 */
public class JavaType {
    private static final Map<String, Object> javaType = new HashMap<>();

    static {
        javaType.put("byte", "byte");
        javaType.put("short", "short");
        javaType.put("int", "int");
        javaType.put("long", "long");
        javaType.put("float", "float");
        javaType.put("double", "double");
        javaType.put("boolean", "boolean");
        javaType.put(String.class.getName(), String.class.getName());
        javaType.put(Byte.class.getName(), Byte.class.getName());
        javaType.put(Short.class.getName(), Short.class.getName());
        javaType.put(Integer.class.getName(), Integer.class.getName());
        javaType.put(Long.class.getName(), Long.class.getName());
        javaType.put(Float.class.getName(), Float.class.getName());
        javaType.put(Double.class.getName(), Double.class.getName());
        javaType.put(Boolean.class.getName(), Boolean.class.getName());
        javaType.put(Date.class.getName(), Date.class.getName());
        javaType.put(BigDecimal.class.getName(), BigDecimal.class.getName());
    }

    public static boolean containsType(String type) {
        return (StringUtil.isNotBlank(type) && javaType.containsKey(type));
    }
}
