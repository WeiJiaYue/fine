package com.radarwin.framework.orm;

import java.lang.reflect.Method;

/**
 * Created by josh on 15/6/17.
 */
public class ColumnMetaData {

    // column annotation的name 也就是列名
    private String columnName;

    // 属性名
    private String propertyName;

    // 属性类型
    private String propertyType;

    // 是否自增长
    private boolean isIdentity = false;

    // 是否主键
    private boolean isPrimaryKey = false;

    // get 方法名
    private String gmthod;

    // set 方法名
    private String smthod;

    // 列的get方法
    private Method columnGetMethod;

    // 列的set方法
    private Method columnSetMethod;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public boolean isIdentity() {
        return isIdentity;
    }

    public void setIsIdentity(boolean isIdentity) {
        this.isIdentity = isIdentity;
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public void setIsPrimaryKey(boolean isPrimaryKey) {
        this.isPrimaryKey = isPrimaryKey;
    }

    public String getGmthod() {
        return gmthod;
    }

    public void setGmthod(String gmthod) {
        this.gmthod = gmthod;
    }

    public String getSmthod() {
        return smthod;
    }

    public void setSmthod(String smthod) {
        this.smthod = smthod;
    }

    public Method getColumnSetMethod() {
        return columnSetMethod;
    }

    public void setColumnSetMethod(Method columnSetMethod) {
        this.columnSetMethod = columnSetMethod;
    }

    public Method getColumnGetMethod() {
        return columnGetMethod;
    }

    public void setColumnGetMethod(Method columnGetMethod) {
        this.columnGetMethod = columnGetMethod;
    }
}
