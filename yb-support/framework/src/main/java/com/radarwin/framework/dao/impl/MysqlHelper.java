package com.radarwin.framework.dao.impl;

import com.radarwin.framework.orm.ColumnMetaData;
import com.radarwin.framework.orm.TableMetaData;
import com.radarwin.framework.util.StringUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by josh on 15/6/20.
 */

/**
 * 解析Mysql语句的帮助类
 */
public class MysqlHelper extends DbHelper {
    private static Logger logger = LogManager.getLogger(MysqlHelper.class);

    /**
     * 解析新增数据的insertSql
     *
     * @param tableMetaData
     */
    protected void resolveInsertSql(TableMetaData tableMetaData) {
        // 获取 pojo 中解析出的列
        List<ColumnMetaData> list = tableMetaData.getColumnMetaDataList();
        SqlHelper sqlHelper = new SqlHelper();
        StringBuilder sb = new StringBuilder();

        sb.append(INSERT_INTO).append(tableMetaData.getTableName()).append(LEFT_BRACKETS);
        for (ColumnMetaData columnMetaData : list) {
            if (!columnMetaData.isIdentity()) {
                sb.append(columnMetaData.getColumnName());
                sb.append(COMMA);
            }
        }
        sb.delete(sb.lastIndexOf(COMMA), sb.length()).append(RIGHT_BRACKETS).append(VALUES);

        for (ColumnMetaData columnMetaData : list) {
            if (!columnMetaData.isIdentity()) {
                sb.append(QUESTION);
                sb.append(COMMA);
            }
        }
        sb.delete(sb.lastIndexOf(COMMA), sb.length()).append(RIGHT_BRACKETS);

        sqlHelper.setSql(sb.toString());
        sqlHelper.setIdentity(tableMetaData.getHasIdentity());
        tableMetaData.setDefaultInsertSql(sqlHelper);
    }

    /**
     * 生成insert sql
     *
     * @param object
     * @return
     */
    protected Object[] generateInsertValue(Object object) {
        TableMetaData tableMetaData = resolveTable(object);

        if (StringUtil.isBlank(tableMetaData.getTableName())) {
            throw new RuntimeException(tableMetaData.getEntityClass().getName() + " not config with @Table");
        }

        List<ColumnMetaData> list = tableMetaData.getColumnMetaDataList();

        Date now = new Date();

        List<Object> values = new ArrayList<Object>();
        for (ColumnMetaData columnMetaData : list) {
            if (!columnMetaData.isIdentity()) {
                Object columnValue = null;
                try {
                    switch (columnMetaData.getPropertyName()) {
                        case DELETED:
                            columnValue = 0;
                            break;
                        case CREATE_TIME:
                            columnValue = now;
                            columnMetaData.getColumnSetMethod().invoke(object, now);
                            break;
                        case CREATE_USER:
                            if (currentUser != null) {
                                columnValue = currentUser.getCurrentUserName();
                                columnMetaData.getColumnSetMethod().invoke(object, currentUser.getCurrentUserName());
                            }
                            break;
                        case UPDATE_TIME:
                            columnValue = now;
                            columnMetaData.getColumnSetMethod().invoke(object, now);
                            break;
                        case UPDATE_USER:
                            if (currentUser != null) {
                                columnValue = currentUser.getCurrentUserName();
                                columnMetaData.getColumnSetMethod().invoke(object, currentUser.getCurrentUserName());
                            }
                            break;
                        default:
                            columnValue = columnMetaData.getColumnGetMethod().invoke(object);
                    }
                } catch (Exception ex) {
                    logger.error(ExceptionUtils.getStackTrace(ex));
                }
                values.add(columnValue);
            }
        }
        return values.toArray();
    }

    /**
     * 生成带分页的sql
     *
     * @param sql
     * @param startIndex
     * @param pageSize
     * @return
     */
    @Override
    protected String generatePageSql(String sql, int startIndex, int pageSize, String sortName, String sortDirect) {
        StringBuilder sb = new StringBuilder(sql);
        if (!sql.toLowerCase().contains("order by") && StringUtil.isNotBlank(sortName)) {
            sb.append(" order by ").append(sortName).append(" ").append(sortDirect);
        }
        sb.append(" limit ").append(startIndex).append(COMMA).append(pageSize);
        return sb.toString();
    }

    /**
     * 生成行锁的sql
     *
     * @param clss
     * @return
     */
    @Override
    protected String generateLockSql(Class clss) {
        TableMetaData tableMetaData = resolveTable(clss);
        if (StringUtil.isBlank(tableMetaData.getTableName())) {
            throw new RuntimeException(tableMetaData.getEntityClass().getName() + " not config with @Table");
        }
        if (!tableMetaData.getHasPrimaryKey()) {
            throw new RuntimeException("generate lock sql error due to not primary key in " + clss.getName());
        }
        StringBuilder sb = new StringBuilder();
        sb.append("select ")
                .append(" * ")
                .append(" from ")
                .append(tableMetaData.getTableName()).append(" where ")
                .append(tableMetaData.getPrimaryKeyColumn())
                .append("= ? for update");
        return sb.toString();
    }


    public static void main(String[] args) {

    }
}
