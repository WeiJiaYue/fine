package com.radarwin.framework.dao.impl;

import com.radarwin.framework.dao.BaseDao;
import com.radarwin.framework.model.BaseModel;
import com.radarwin.framework.orm.TableMetaData;
import com.radarwin.framework.page.Page;
import com.radarwin.framework.page.PageList;
import com.radarwin.framework.util.StringUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.KeyHolder;

import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by josh on 15/6/18.
 */
public class BaseDaoImpl<T> implements BaseDao<T> {

    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    private Class<T> persistentClass;

    private DbHelper dbHelper;

    private static final Logger logger = LogManager.getLogger(BaseDaoImpl.class);

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public BaseDaoImpl() {
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];

        switch (DbHelper.DATA_BASE_DIALECT) {
            case DbHelper.DATA_BASE_DIALECT_MYSQL:
                dbHelper = new MysqlHelper();
                break;
            case DbHelper.DATA_BASE_DIALECT_ORACLE:
                dbHelper = new OracleHelper();
                break;
            default:
                dbHelper = new MysqlHelper();

        }
    }

    protected Class<T> getPersistentClass() {
        return persistentClass;
    }

    /**
     * 只新增
     *
     * @param t
     */
    @Override
    public void save(T t) {

        TableMetaData tableMetaData = dbHelper.resolveTable(t);
        final String sql = tableMetaData.getDefaultInsertSql().getSql();
        final Object[] values = dbHelper.generateInsertValue(t);

        if (tableMetaData.getHasIdentity()) {

            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(
                    new PreparedStatementCreator() {
                        public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                            DbHelper.resolvePreparedStatement(ps, values);
                            return ps;
                        }
                    }, keyHolder);

            dbHelper.resolveIdentityKey(tableMetaData, t, keyHolder);
        } else {
            jdbcTemplate.update(
                    new PreparedStatementCreator() {
                        public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                            PreparedStatement ps = con.prepareStatement(sql);
                            DbHelper.resolvePreparedStatement(ps, values);
                            return ps;
                        }
                    });
        }
    }

    /**
     * 新增 需要配合
     *
     * @param t
     * @param con
     */
    public void save(T t, Connection con) {
        TableMetaData tableMetaData = dbHelper.resolveTable(t);
        final String sql = tableMetaData.getDefaultInsertSql().getSql();
        final Object[] values = dbHelper.generateInsertValue(t);

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            if (tableMetaData.getHasIdentity()) {
                ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            } else {
                ps = con.prepareStatement(sql);
            }

            DbHelper.resolvePreparedStatement(ps, values);
            ps.executeUpdate();
            if (tableMetaData.getHasIdentity()) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    dbHelper.resolveIdentityKey(tableMetaData, t, rs.getObject(1));
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException("insert error with entity" + t.getClass().getName(), ex);
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(ps);
        }
    }

    /**
     * 只更新
     *
     * @param t
     */
    @Override
    public void update(T t) {
        TableMetaData tableMetaData = dbHelper.resolveTable(t);
        SqlHelper sqlHelper = tableMetaData.getDefaultUpdateSql();
        if (sqlHelper == null) {
            throw new RuntimeException("default update sql not generated probably primary key not set in " + t.getClass().getName());
        }
        final String sql = sqlHelper.getSql();
        final Object[] values = dbHelper.generateUpdateValue(t);
        jdbcTemplate.update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                        PreparedStatement ps = con.prepareStatement(sql);
                        DbHelper.resolvePreparedStatement(ps, values);
                        return ps;
                    }
                });
    }

    /**
     * 物理删除一条数据
     *
     * @param key
     */
    @Override
    public void physicalDelete(Object key) {
        TableMetaData tableMetaData = dbHelper.resolveTable(getPersistentClass());
        if (StringUtil.isBlank(tableMetaData.getTableName())) {
            throw new RuntimeException(tableMetaData.getEntityClass().getName() + " not config with @Table");
        }

        SqlHelper sqlHelper = tableMetaData.getDefaultDeleteSql();

        if (StringUtil.isBlank(tableMetaData.getTableName())) {
            throw new RuntimeException(tableMetaData.getEntityClass().getName() + " not config with @Table");
        }

        if (!tableMetaData.getHasPrimaryKey()) {
            throw new RuntimeException("default update sql not generated probably primary key not set in " + getPersistentClass());
        }
        if (key == null) {
            throw new RuntimeException("primary key is null when physicalDelete " + getPersistentClass());
        }

        final String sql = sqlHelper.getSql();
        final Object[] values = new Object[]{key};
        jdbcTemplate.update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                        PreparedStatement ps = con.prepareStatement(sql);
                        DbHelper.resolvePreparedStatement(ps, values);
                        return ps;
                    }
                });
    }

    /**
     * 逻辑删除一条数据，必须继承BaseModel,否则会报错
     * <p/>
     * 逻辑删除会把BaseModel的deleted字段设为1
     *
     * @param key
     */
    @Override
    public void delete(Object key) {
        TableMetaData tableMetaData = dbHelper.resolveTable(getPersistentClass());
        if (StringUtil.isBlank(tableMetaData.getTableName())) {
            throw new RuntimeException(tableMetaData.getEntityClass().getName() + " not config with @Table");
        }
        SqlHelper sqlHelper = tableMetaData.getDefaultLogicDeleteSql();
        if (!tableMetaData.isParentBaseModel()) {
            throw new RuntimeException("entity class " + getPersistentClass() + " not inherited from " + BaseModel.class);
        }

        if (key == null) {
            throw new RuntimeException("primary key is null when logic delete " + getPersistentClass());
        }

        final String sql = sqlHelper.getSql();
        final Object[] values = new Object[]{key};
        jdbcTemplate.update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                        PreparedStatement ps = con.prepareStatement(sql);
                        DbHelper.resolvePreparedStatement(ps, values);
                        return ps;
                    }
                });
    }

    /**
     * 暴露 connection的更新(不建议常用操作，通常和lock方法配合使用)
     *
     * @param t
     * @param con
     */
    @Override
    public void update(T t, Connection con) {
        TableMetaData tableMetaData = dbHelper.resolveTable(t);
        SqlHelper sqlHelper = tableMetaData.getDefaultUpdateSql();
        if (sqlHelper == null) {
            throw new RuntimeException("default update sql not generated probably primary key not set in " + t.getClass().getName());
        }

        String sql = sqlHelper.getSql();
        Object[] values = dbHelper.generateUpdateValue(t);
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            DbHelper.resolvePreparedStatement(ps, values);
            ps.executeUpdate();
        } catch (Exception ex) {
            throw new RuntimeException("update error with entity" + t.getClass().getName(), ex);
        } finally {
            JdbcUtils.closeStatement(ps);
        }
    }

    /**
     * 执行新增或更新sql语句
     *
     * @param sql
     */
    @Override
    public void execUpdate(String sql, Object... params) {
        jdbcTemplate.update(sql, params);
    }

    /**
     * 根据主键行锁数据
     *
     * @param key
     */
    @Deprecated
    @Override
    public Connection lock(Object key) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        if (key == null) {
            throw new RuntimeException("Lock" + getPersistentClass().getName() + " error cause primary key is null");
        }
        try {
            connection = getJdbcTemplate().getDataSource().getConnection();
            if (connection.getAutoCommit()) {
                connection.setAutoCommit(false);
            }
            String sql = dbHelper.generateLockSql(getPersistentClass());
            ps = connection.prepareStatement(sql);
            DbHelper.resolvePreparedStatement(ps, new Object[]{key});
            resultSet = ps.executeQuery();
            ColumnMapRowMapper columnMapRowMapper = new ColumnMapRowMapper();
            RowMapperResultSetExtractor<Map<String, Object>> rowMapperResultSetExtractor = new RowMapperResultSetExtractor<>(columnMapRowMapper);
            List<Map<String, Object>> list = rowMapperResultSetExtractor.extractData(resultSet);
            if (list.size() > 1) {
                throw new RuntimeException("Lock" + getPersistentClass().getName() + " error cause lock rows > 1, lock lows:" + list.size());
            }
            list.clear();
        } catch (Exception ex) {
            logger.error("Lock" + getPersistentClass().getName() + " error");
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    logger.error(ExceptionUtils.getStackTrace(e));
                }
                DataSourceUtils.releaseConnection(connection, jdbcTemplate.getDataSource());
            }
            throw new RuntimeException(ex);
        } finally {
            JdbcUtils.closeResultSet(resultSet);
            JdbcUtils.closeStatement(ps);
        }
        return connection;
    }


    public RowLock<T> lockRow(Object key) {
        RowLock rowLock = new RowLock();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        if (key == null) {
            throw new RuntimeException("Lock" + getPersistentClass().getName() + " error cause primary key is null");
        }
        try {
            connection = getJdbcTemplate().getDataSource().getConnection();
            if (connection.getAutoCommit()) {
                connection.setAutoCommit(false);
            }
            String sql = dbHelper.generateLockSql(getPersistentClass());
            ps = connection.prepareStatement(sql);
            DbHelper.resolvePreparedStatement(ps, new Object[]{key});
            resultSet = ps.executeQuery();
            ColumnMapRowMapper columnMapRowMapper = new ColumnMapRowMapper();
            RowMapperResultSetExtractor<Map<String, Object>> rowMapperResultSetExtractor = new RowMapperResultSetExtractor<>(columnMapRowMapper);
            List<Map<String, Object>> list = rowMapperResultSetExtractor.extractData(resultSet);
            if (list.size() > 1) {
                throw new RuntimeException("Lock" + getPersistentClass().getName() + " error cause lock rows > 1, lock lows:" + list.size());
            }
            rowLock.setConnection(connection);
            if (list.size() == 1) {
                T t = dbHelper.resolveMapToBean(getPersistentClass(), list.get(0));
                rowLock.setObject(t);
                list.clear();
            }
        } catch (Exception ex) {
            logger.error("Lock" + getPersistentClass().getName() + " error", ex);
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (Exception e) {
                    logger.error(ExceptionUtils.getStackTrace(e));
                }
            }
            DataSourceUtils.releaseConnection(connection, jdbcTemplate.getDataSource());
            throw new RuntimeException(ex);
        } finally {
            JdbcUtils.closeResultSet(resultSet);
            JdbcUtils.closeStatement(ps);
        }
        return rowLock;
    }

    public RowLock<T> lockRow(Object key, Connection connection) {
        RowLock rowLock = new RowLock();
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        if (key == null) {
            throw new RuntimeException("Lock" + getPersistentClass().getName() + " error cause primary key is null");
        }
        try {
            if (connection.getAutoCommit()) {
                connection.setAutoCommit(false);
            }
            String sql = dbHelper.generateLockSql(getPersistentClass());
            ps = connection.prepareStatement(sql);
            DbHelper.resolvePreparedStatement(ps, new Object[]{key});
            resultSet = ps.executeQuery();
            ColumnMapRowMapper columnMapRowMapper = new ColumnMapRowMapper();
            RowMapperResultSetExtractor<Map<String, Object>> rowMapperResultSetExtractor = new RowMapperResultSetExtractor<>(columnMapRowMapper);
            List<Map<String, Object>> list = rowMapperResultSetExtractor.extractData(resultSet);
            if (list.size() > 1) {
                throw new RuntimeException("Lock" + getPersistentClass().getName() + " error cause lock rows > 1, lock lows:" + list.size());
            }
            rowLock.setConnection(connection);
            if (list.size() == 1) {
                rowLock.setObject(dbHelper.resolveMapToBean(getPersistentClass(), list.get(0)));
                list.clear();
            }
        } catch (Exception ex) {
            logger.error("Lock" + getPersistentClass().getName() + " error", ex);

            if (connection != null) {
                try {
                    connection.rollback();
                } catch (Exception e) {
                    logger.error(ExceptionUtils.getStackTrace(e));
                }
            }
            DataSourceUtils.releaseConnection(connection, jdbcTemplate.getDataSource());
            throw new RuntimeException(ex);
        } finally {
            JdbcUtils.closeResultSet(resultSet);
            JdbcUtils.closeStatement(ps);
        }
        return rowLock;
    }

    /**
     * 解锁行锁
     *
     * @param connection
     */
    @Override
    public void unLock(Connection connection) {
        try {
            connection.commit();
        } catch (Exception ex) {
            logger.error(ExceptionUtils.getStackTrace(ex));
        } finally {
            DataSourceUtils.releaseConnection(connection, jdbcTemplate.getDataSource());
        }
    }

    /**
     * 批量新增
     *
     * @param list
     * @return
     */
    @Override
    public int[] batchSave(final List<T> list) {
        if (list != null && list.size() > 0) {
            TableMetaData tableMetaData = dbHelper.resolveTable(getPersistentClass());
            if (StringUtil.isBlank(tableMetaData.getTableName())) {
                throw new RuntimeException(tableMetaData.getEntityClass().getName() + " not config with @Table");
            }
            String insertSql = tableMetaData.getDefaultInsertSql().getSql();
            int[] result = jdbcTemplate.batchUpdate(insertSql, new BatchPreparedStatementSetter() {
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    T t = list.get(i);
                    if (t != null) {
                        DbHelper.resolvePreparedStatement(ps, dbHelper.generateInsertValue(t));
                    }
                }

                public int getBatchSize() {
                    return list.size();
                }
            });
            return result;
        }
        return null;
    }


    /**
     * 批量更新
     *
     * @param list
     * @return
     */
    @Override
    public int[] batchUpdate(final List<T> list) {
        if (list != null && list.size() > 0) {
            TableMetaData tableMetaData = dbHelper.resolveTable(getPersistentClass());
            if (StringUtil.isBlank(tableMetaData.getTableName())) {
                throw new RuntimeException(tableMetaData.getEntityClass().getName() + " not config with @Table");
            }
            if (!tableMetaData.getHasPrimaryKey()) {
                throw new RuntimeException("batch update error due to primary key not set in" + tableMetaData.getEntityClass().getName());
            }
            String updateSql = tableMetaData.getDefaultUpdateSql().getSql();
            int[] result = jdbcTemplate.batchUpdate(updateSql, new BatchPreparedStatementSetter() {
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    T t = list.get(i);
                    if (t != null) {
                        DbHelper.resolvePreparedStatement(ps, dbHelper.generateUpdateValue(t));
                    }
                }

                public int getBatchSize() {
                    return list.size();
                }
            });
            return result;
        }
        return null;
    }

    /**
     * 批量根据主键删除（物理删除）
     *
     * @param keyList
     * @return
     */
    @Override
    public int[] batchPhysicalDelete(final List<?> keyList) {
        if (keyList != null && keyList.size() > 0) {
            TableMetaData tableMetaData = dbHelper.resolveTable(getPersistentClass());
            if (StringUtil.isBlank(tableMetaData.getTableName())) {
                throw new RuntimeException(tableMetaData.getEntityClass().getName() + " not config with @Table");
            }
            if (!tableMetaData.getHasPrimaryKey()) {
                throw new RuntimeException("batch physical delete error due to primary key not set in" + tableMetaData.getEntityClass().getName());
            }
            String deleteSql = tableMetaData.getDefaultDeleteSql().getSql();
            int[] result = jdbcTemplate.batchUpdate(deleteSql, new BatchPreparedStatementSetter() {
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    Object key = keyList.get(i);
                    if (key != null) {
                        DbHelper.resolvePreparedStatement(ps, new Object[]{key});
                    }
                }

                public int getBatchSize() {
                    return keyList.size();
                }
            });
            return result;
        }
        return null;
    }

    /**
     * 批量逻辑删除,model必须继承BaseModel否则会报错
     * <p/>
     * 逻辑删除会把BaseModel的deleted字段设为1
     *
     * @param keyList
     * @return
     */
    @Override
    public int[] batchDelete(final List<?> keyList) {
        if (keyList != null && keyList.size() > 0) {
            TableMetaData tableMetaData = dbHelper.resolveTable(getPersistentClass());
            if (StringUtil.isBlank(tableMetaData.getTableName())) {
                throw new RuntimeException(tableMetaData.getEntityClass().getName() + " not config with @Table");
            }
            if (!tableMetaData.getHasPrimaryKey()) {
                throw new RuntimeException("batch logic delete error due to primary key not set in" + tableMetaData.getEntityClass().getName());
            }
            if (!tableMetaData.isParentBaseModel()) {
                throw new RuntimeException("batch logic delete error due to entity not inherited from " + BaseModel.class);
            }

            String logicDeleteSql = tableMetaData.getDefaultLogicDeleteSql().getSql();

            int[] result = jdbcTemplate.batchUpdate(logicDeleteSql, new BatchPreparedStatementSetter() {
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    Object key = keyList.get(i);
                    if (key != null) {
                        DbHelper.resolvePreparedStatement(ps, new Object[]{key});
                    }
                }

                public int getBatchSize() {
                    return keyList.size();
                }
            });
            return result;
        }
        return null;
    }

    /**
     * 根据主键获取(所有列)
     *
     * @param key
     */
    @Override
    public T get(Object key) {
        String sql = dbHelper.generateSingleSelectSql(getPersistentClass());
        Map<String, Object> map = null;
        try {
            map = jdbcTemplate.queryForMap(sql, key);
            T t = dbHelper.resolveMapToBean(getPersistentClass(), map);
            if (map != null) {
                map.clear();
            }
            return t;
        } catch (EmptyResultDataAccessException ex) {
            return null;
        } catch (IncorrectResultSizeDataAccessException ex) {
            return null;
        }
    }

    /**
     * 根据主键获取部分列
     *
     * @param key
     * @param propertyNames 需要查询的属性名
     * @return
     */
    @Override
    public T get(Object key, String... propertyNames) {
        String sql = dbHelper.generateSingleSelectSql(getPersistentClass(), propertyNames);
        Map<String, Object> map = jdbcTemplate.queryForMap(sql, key);
        T t = dbHelper.resolveMapToBean(getPersistentClass(), map);
        if (map != null) {
            map.clear();
        }
        return t;
    }

    /**
     * 验证是否存在（根据主键验证）
     *
     * @param key
     * @return
     */
    @Override
    public boolean exists(Object key) {
        T t = this.get(key);
        return t != null;
    }

    /**
     * 获取所有数据
     *
     * @return
     */
    @Override
    public List<T> getAll() {
        String sql = dbHelper.generateAllSelectSql(getPersistentClass());
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        List<T> resultList = new ArrayList<>();
        for (Map<String, Object> map : list) {
            T t = dbHelper.resolveMapToBean(getPersistentClass(), map);
            if (t != null) {
                resultList.add(t);
            }
            map.clear();
        }
        list.clear();
        return resultList;
    }

    /**
     * 分页获取所有数据
     *
     * @param page
     * @return
     */
    @Override
    public PageList<T> getPageAll(Page page) {
        String sql = dbHelper.generateAllSelectSql(getPersistentClass());
        return this.getPageListBySql(sql, page);
    }

    /**
     * 分页获取所有数据
     *
     * @param page
     * @return
     */
    @Override
    public PageList<T> getPageAllWithNext(Page page) {
        String sql = dbHelper.generateAllSelectSql(getPersistentClass());
        return this.getPageListBySqlWithNext(sql, page);
    }

    /**
     * 查询SQl 没有查询参数
     *
     * @param sql
     * @return
     */
    @Override
    public List<T> getListBySql(String sql) {
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        List<T> resultList = new ArrayList<>();
        for (Map<String, Object> map : list) {
            T t = dbHelper.resolveMapToBean(persistentClass, map);
            if (t != null) {
                resultList.add(t);
            }
            map.clear();
        }
        list.clear();
        return resultList;
    }


    /**
     * 查询SQL 带分页 没有参数
     * 数据量多的时候建议不要使用此方式查询
     *
     * @param sql
     * @param page
     * @return
     */
    @Override
    public PageList<T> getPageListBySql(String sql, Page page) {
        String pageSql = dbHelper.generatePageSql(sql,
                page.getStartIndex(), page.getPageSize(), page.getSortName(), page.getSortDirect());
        String countSql = dbHelper.generateCountSql(sql);
        PageList pageList = new PageList();

        int totalCount = jdbcTemplate.queryForObject(countSql, Integer.class);
        pageList.setTotalCount(totalCount);

        List<T> list = this.getListBySql(pageSql);
        pageList.setPage(page);
        pageList.setResultList(list);
        return pageList;
    }

    /**
     * 查询SQL 带分页 没有参数
     * 这种查询方式不会查询总条数，而是查询给定的条目数+1
     * 如果返回的结果=给定的条目数+1，说明有下一页
     *
     * @param sql
     * @param page
     * @return
     */
    @Override
    public PageList<T> getPageListBySqlWithNext(String sql, Page page) {
        String pageSql = dbHelper.generatePageSql(sql,
                page.getStartIndex(), page.getPageSize() + 1, page.getSortName(), page.getSortDirect());
        PageList pageList = new PageList();
        List<T> list = this.getListBySql(pageSql);

        if (list != null && list.size() == (page.getPageSize() + 1)) {
            pageList.setHasNext(true);

            // 移除最后一个
            list.remove(list.size() - 1);
        }

        pageList.setPage(page);
        pageList.setResultList(list);
        return pageList;
    }

    /**
     * 查询SQl 带有查询参数
     *
     * @param sql
     * @param parameters
     * @return
     */
    @Override
    public List<T> getListBySql(String sql, Object... parameters) {
        if (parameters == null || parameters.length == 0) {
            return getListBySql(sql);
        }
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, parameters, dbHelper.resolveQueryParameter(parameters));
        List<T> resultList = new ArrayList<>();
        for (Map<String, Object> map : list) {
            T t = dbHelper.resolveMapToBean(getPersistentClass(), map);
            if (t != null) {
                resultList.add(t);
            }
            map.clear();
        }
        list.clear();
        return resultList;
    }

    /**
     * 查询SQl 分页 带有查询参数
     * 数据量多的时候建议不要使用此方式查询
     *
     * @param sql
     * @param page
     * @param parameters
     * @return
     */
    @Override
    public PageList<T> getPageListBySql(String sql, Page page, Object... parameters) {
        if (parameters == null || parameters.length == 0) {
            return this.getPageListBySql(sql, page);
        }
        String pageSql = dbHelper.generatePageSql(sql,
                page.getStartIndex(), page.getPageSize(), page.getSortName(), page.getSortDirect());
        String countSql = dbHelper.generateCountSql(sql);
        PageList pageList = new PageList();

        int totalCount = jdbcTemplate.queryForObject(countSql, parameters, dbHelper.resolveQueryParameter(parameters), Integer.class);
        pageList.setTotalCount(totalCount);

        List<T> list = this.getListBySql(pageSql, parameters);
        pageList.setPage(page);
        pageList.setResultList(list);
        return pageList;
    }

    /**
     * 查询SQL 带分页 带参数
     * 这种查询方式不会查询总条数，而是查询给定的条目数+1
     * 如果返回的结果=给定的条目数+1，说明有下一页
     *
     * @param sql
     * @param page
     * @param parameters
     * @return
     */
    @Override
    public PageList<T> getPageListBySqlWithNext(String sql, Page page, Object... parameters) {
        if (parameters == null || parameters.length == 0) {
            return this.getPageListBySqlWithNext(sql, page);
        }
        String pageSql = dbHelper.generatePageSql(sql,
                page.getStartIndex(), page.getPageSize() + 1, page.getSortName(), page.getSortDirect());
        PageList pageList = new PageList();

        List<T> list = this.getListBySql(pageSql, parameters);

        if (list != null && list.size() == (page.getPageSize() + 1)) {
            pageList.setHasNext(true);

            // 移除最后一个
            list.remove(list.size() - 1);
        }

        pageList.setPage(page);
        pageList.setResultList(list);
        return pageList;
    }

    public DbHelper getDbHelper() {
        return dbHelper;
    }
}
