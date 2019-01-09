package com.radarwin.framework.service.impl;

import com.radarwin.framework.dao.BaseDao;
import com.radarwin.framework.page.Page;
import com.radarwin.framework.page.PageList;
import com.radarwin.framework.service.BaseService;

import java.util.List;

/**
 * Created by josh on 15/7/5.
 */
public abstract class BaseServiceImpl<T> implements BaseService<T> {

    protected BaseDao<T> dao;

    /**
     * 新增
     *
     * @param t
     */
    @Override
    public void save(T t) {
        dao.save(t);
    }

    /**
     * 更新
     *
     * @param t
     */
    @Override
    public void update(T t) {
        dao.update(t);
    }

    /**
     * 物理删除
     *
     * @param key
     */
    @Override
    public void physicalDelete(Object key) {
        dao.physicalDelete(key);
    }

    /**
     * 逻辑删除
     *
     * @param key
     */
    @Override
    public void delete(Object key) {
        if (key != null) {
            dao.delete(key);
        }
    }

    /**
     * 批量删除
     *
     * @param keys
     */
    @Override
    public void batchPhysicalDelete(List<?> keys) {
        dao.batchPhysicalDelete(keys);
    }

    /**
     * 批量逻辑删除
     *
     * @param keys
     */
    @Override
    public void batchDelete(List<?> keys) {
        dao.batchDelete(keys);
    }

    /**
     * 获所有列取单行数据
     *
     * @param key
     * @return
     */
    @Override
    public T get(Object key) {
        return dao.get(key);
    }

    /**
     * 按照属性名获取给定的列（单行）
     *
     * @param key
     * @param propertyNames
     * @return
     */
    @Override
    public T get(Object key, String... propertyNames) {
        return dao.get(key, propertyNames);
    }

    /**
     * 是否存在某行数据（按照主键）
     *
     * @param key
     * @return
     */
    @Override
    public boolean exists(Object key) {
        return dao.exists(key);
    }

    /**
     * 获取所有数据
     *
     * @return
     */
    @Override
    public List<T> getAll() {
        return dao.getAll();
    }

    /**
     * 分页获取所有数据
     *
     * @param page
     * @return
     */
    @Override
    public PageList<T> getPageAll(Page page) {
        return dao.getPageAll(page);
    }
}
