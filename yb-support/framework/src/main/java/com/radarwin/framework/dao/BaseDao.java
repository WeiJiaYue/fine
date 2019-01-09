package com.radarwin.framework.dao;

import com.radarwin.framework.dao.impl.RowLock;
import com.radarwin.framework.page.Page;
import com.radarwin.framework.page.PageList;

import java.sql.Connection;
import java.util.List;

/**
 * Created by radarwin on 15/6/17.
 */
public interface BaseDao<T> {

    void save(T t);

    void save(T t, Connection con);

    void update(T t);

    void physicalDelete(Object key);

    void delete(Object key);

    void update(T t, Connection con);

    void execUpdate(String sql, Object... params);

    @Deprecated
    Connection lock(Object key);

    RowLock<T> lockRow(Object key);

    RowLock<T> lockRow(Object key, Connection connection);

    void unLock(Connection connection);

    int[] batchSave(final List<T> list);

    int[] batchUpdate(final List<T> list);

    int[] batchPhysicalDelete(final List<?> keyList);

    int[] batchDelete(final List<?> keyList);

    T get(Object key);

    T get(Object key, String... propertyNames);

    boolean exists(Object key);

    List<T> getAll();

    PageList<T> getPageAll(Page page);

    PageList<T> getPageAllWithNext(Page page);

    List<T> getListBySql(String sql);

    List<T> getListBySql(String sql, Object... parameters);

    PageList<T> getPageListBySql(String sql, Page page);

    PageList<T> getPageListBySqlWithNext(String sql, Page page);

    PageList<T> getPageListBySql(String sql, Page page, Object... parameters);

    PageList<T> getPageListBySqlWithNext(String sql, Page page, Object... parameters);
}
