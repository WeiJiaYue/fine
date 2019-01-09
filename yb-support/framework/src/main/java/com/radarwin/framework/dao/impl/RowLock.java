package com.radarwin.framework.dao.impl;

import java.sql.Connection;

/**
 * Created by josh on 16/1/6.
 */
public class RowLock<T> {
    private Connection connection;
    private T object;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }
}
