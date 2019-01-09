package com.radarwin.framework.orm;

import com.radarwin.framework.dao.impl.SqlHelper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by josh on 15/6/17.
 */
public class TableMetaData {

    // 表名称
    private String tableName;

    // pojo 类 class
    private Class entityClass;

    // 是否是自增长
    private boolean hasIdentity = false;

    // 是否有主键
    private boolean hasPrimaryKey = false;

    // 主键列名
    private String primaryKeyColumn;

    // 自增列的get方法
    private Method identityKeyGetMethod;

    // 自增列的set方法
    private Method identityKeySetMethod;

    // 主键的get方法
    private Method primaryKeyGetMethod;

    // 主键的set方法
    private Method primaryKeySetMethod;

    // 默认的insert SQl
    private SqlHelper defaultInsertSql;

    // 默认的update SQLl
    private SqlHelper defaultUpdateSql;

    // 默认删除语句
    private SqlHelper defaultDeleteSql;

    // 默认逻辑删除语句
    private SqlHelper defaultLogicDeleteSql;

    // 是否是BaseModel的子类
    private boolean parentBaseModel;

    // 列的相关信息 key：属性名称
    private Map<String, ColumnMetaData> columnMetaDataMap = new HashMap<>();

    // 列的相关信息 key：列名称
    private Map<String, ColumnMetaData> columnMetaDataMap2 = new HashMap<>();

    // 列的相关信息
    private List<ColumnMetaData> columnMetaDataList = new ArrayList<ColumnMetaData>();

    // 不带column标签的属性
    private Map<String, FieldMetaData> fieldMetaDataMapWithNoColumnAnno = new HashMap<>();

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Class getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(Class entityClass) {
        this.entityClass = entityClass;
    }

    public boolean getHasIdentity() {
        return hasIdentity;
    }

    public void setHasIdentity(boolean hasIdentity) {
        this.hasIdentity = hasIdentity;
    }

    public boolean getHasPrimaryKey() {
        return hasPrimaryKey;
    }

    public void setHasPrimaryKey(boolean hasPrimaryKey) {
        this.hasPrimaryKey = hasPrimaryKey;
    }

    public String getPrimaryKeyColumn() {
        return primaryKeyColumn;
    }

    public void setPrimaryKeyColumn(String primaryKeyColumn) {
        this.primaryKeyColumn = primaryKeyColumn;
    }

    public Map<String, ColumnMetaData> getColumnMetaDataMap() {
        return columnMetaDataMap;
    }

    public void setColumnMetaDataMap(Map<String, ColumnMetaData> columnMetaDataMap) {
        this.columnMetaDataMap = columnMetaDataMap;
    }

    public Map<String, ColumnMetaData> getColumnMetaDataMap2() {
        return columnMetaDataMap2;
    }

    public void setColumnMetaDataMap2(Map<String, ColumnMetaData> columnMetaDataMap2) {
        this.columnMetaDataMap2 = columnMetaDataMap2;
    }

    public List<ColumnMetaData> getColumnMetaDataList() {
        return columnMetaDataList;
    }

    public void setColumnMetaDataList(List<ColumnMetaData> columnMetaDataList) {
        this.columnMetaDataList = columnMetaDataList;
    }

    public Map<String, FieldMetaData> getFieldMetaDataMapWithNoColumnAnno() {
        return fieldMetaDataMapWithNoColumnAnno;
    }

    public void setFieldMetaDataMapWithNoColumnAnno(Map<String, FieldMetaData> fieldMetaDataMapWithNoColumnAnno) {
        this.fieldMetaDataMapWithNoColumnAnno = fieldMetaDataMapWithNoColumnAnno;
    }

    public Method getIdentityKeyGetMethod() {
        return identityKeyGetMethod;
    }

    public void setIdentityKeyGetMethod(Method identityKeyGetMethod) {
        this.identityKeyGetMethod = identityKeyGetMethod;
    }

    public Method getIdentityKeySetMethod() {
        return identityKeySetMethod;
    }

    public void setIdentityKeySetMethod(Method identityKeySetMethod) {
        this.identityKeySetMethod = identityKeySetMethod;
    }

    public Method getPrimaryKeyGetMethod() {
        return primaryKeyGetMethod;
    }

    public void setPrimaryKeyGetMethod(Method primaryKeyGetMethod) {
        this.primaryKeyGetMethod = primaryKeyGetMethod;
    }

    public Method getPrimaryKeySetMethod() {
        return primaryKeySetMethod;
    }

    public void setPrimaryKeySetMethod(Method primaryKeySetMethod) {
        this.primaryKeySetMethod = primaryKeySetMethod;
    }

    public SqlHelper getDefaultInsertSql() {
        return defaultInsertSql;
    }

    public void setDefaultInsertSql(SqlHelper defaultInsertSql) {
        this.defaultInsertSql = defaultInsertSql;
    }

    public SqlHelper getDefaultUpdateSql() {
        return defaultUpdateSql;
    }

    public void setDefaultUpdateSql(SqlHelper defaultUpdateSql) {
        this.defaultUpdateSql = defaultUpdateSql;
    }

    public SqlHelper getDefaultDeleteSql() {
        return defaultDeleteSql;
    }

    public void setDefaultDeleteSql(SqlHelper defaultDeleteSql) {
        this.defaultDeleteSql = defaultDeleteSql;
    }

    public SqlHelper getDefaultLogicDeleteSql() {
        return defaultLogicDeleteSql;
    }

    public void setDefaultLogicDeleteSql(SqlHelper defaultLogicDeleteSql) {
        this.defaultLogicDeleteSql = defaultLogicDeleteSql;
    }

    public boolean isParentBaseModel() {
        return parentBaseModel;
    }

    public void setParentBaseModel(boolean parentBaseModel) {
        this.parentBaseModel = parentBaseModel;
    }
}
