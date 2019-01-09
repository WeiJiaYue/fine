package com.radarwin.framework.orm;

import java.lang.reflect.Method;

/**
 * Created by josh on 15/7/22.
 */
public class FieldMetaData {
    private String fieldName;
    private Class fieldType;
    private Method fieldSetMethod;
    private Method fieldGetMethod;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Class getFieldType() {
        return fieldType;
    }

    public void setFieldType(Class fieldType) {
        this.fieldType = fieldType;
    }

    public Method getFieldSetMethod() {
        return fieldSetMethod;
    }

    public void setFieldSetMethod(Method fieldSetMethod) {
        this.fieldSetMethod = fieldSetMethod;
    }

    public Method getFieldGetMethod() {
        return fieldGetMethod;
    }

    public void setFieldGetMethod(Method fieldGetMethod) {
        this.fieldGetMethod = fieldGetMethod;
    }
}
