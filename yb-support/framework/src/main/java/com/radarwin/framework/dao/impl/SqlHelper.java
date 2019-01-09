package com.radarwin.framework.dao.impl;

/**
 * Created by josh on 15/6/18.
 */
public class SqlHelper {
    private String sql;

    private Object[] values;

    private boolean identity = false;

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Object[] getValues() {
        return values;
    }

    public void setValues(Object[] values) {
        this.values = values;
    }

    public boolean isIdentity() {
        return identity;
    }

    public void setIdentity(boolean identity) {
        this.identity = identity;
    }
}
