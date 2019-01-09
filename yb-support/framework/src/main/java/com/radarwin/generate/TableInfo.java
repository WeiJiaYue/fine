package com.radarwin.generate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by josh on 15/7/8.
 */
public class TableInfo {
    private String tableName;
    private String entityName;
    private String className;
    private List<FieldInfo> fieldInfoList = new ArrayList<>();
    private boolean hasDate = false;
    private boolean hasDecimal = false;
    private String primaryKeyField;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<FieldInfo> getFieldInfoList() {
        return fieldInfoList;
    }

    public void setFieldInfoList(List<FieldInfo> fieldInfoList) {
        this.fieldInfoList = fieldInfoList;
    }

    public boolean getHasDate() {
        return hasDate;
    }

    public void setHasDate(boolean hasDate) {
        this.hasDate = hasDate;
    }

    public boolean getHasDecimal() {
        return hasDecimal;
    }

    public void setHasDecimal(boolean hasDecimal) {
        this.hasDecimal = hasDecimal;
    }

    public String getPrimaryKeyField() {
        return primaryKeyField;
    }

    public void setPrimaryKeyField(String primaryKeyField) {
        this.primaryKeyField = primaryKeyField;
    }
}
