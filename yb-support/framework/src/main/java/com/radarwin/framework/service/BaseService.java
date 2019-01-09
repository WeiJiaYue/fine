package com.radarwin.framework.service;

import com.radarwin.framework.page.Page;
import com.radarwin.framework.page.PageList;

import java.util.List;

/**
 * Created by josh on 15/7/5.
 */
public interface BaseService<T> {

    void save(T t);

    void update(T t);

    void physicalDelete(Object key);

    void delete(Object key);

    void batchPhysicalDelete(List<?> keys);

    void batchDelete(List<?> keys);

    T get(Object key);

    T get(Object key, String... propertyNames);

    boolean exists(Object key);

    List<T> getAll();

    PageList<T> getPageAll(Page page);
}
