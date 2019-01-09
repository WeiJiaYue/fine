package com.radarwin.generate;

/**
 * Created by josh on 15/7/8.
 */
public class FieldInfo {
    private String fieldType;
    private String fieldName;
    private String columnName;
    private String methodGetName;
    private String methodSetName;
    private boolean primaryKey;
    private boolean identityKey;

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getMethodGetName() {
        return methodGetName;
    }

    public void setMethodGetName(String methodGetName) {
        this.methodGetName = methodGetName;
    }

    public String getMethodSetName() {
        return methodSetName;
    }

    public void setMethodSetName(String methodSetName) {
        this.methodSetName = methodSetName;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public boolean isIdentityKey() {
        return identityKey;
    }

    public void setIdentityKey(boolean identityKey) {
        this.identityKey = identityKey;
    }
}
