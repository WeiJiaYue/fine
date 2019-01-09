package com.radarwin.framework.dao.impl;

import com.radarwin.framework.orm.TableMetaData;

/**
 * Created by josh on 15/6/20.
 */

/**
 * 解析oracle语句的帮助类
 */
public class OracleHelper extends DbHelper {
    @Override
    protected void resolveInsertSql(TableMetaData tableMetaData) {
    }

    @Override
    protected Object[] generateInsertValue(Object object) {
        return null;
    }

    @Override
    protected String generatePageSql(String sql, int startIndex, int pageSize, String sortName, String sortDirect) {
        return null;
    }

    @Override
    protected String generateLockSql(Class clss) {
        return null;
    }


}
