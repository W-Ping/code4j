package com.code4j.connect;

import com.code4j.exception.Code4jException;
import com.code4j.pojo.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author liu_wp
 * @date 2020/11/18
 * @see
 */
public abstract class AbstractJDBCService<T extends BaseInfo> implements JDBCService<T> {
    protected static final Logger log = LoggerFactory.getLogger(AbstractJDBCService.class);
    private final JdbcSourceInfo jdbcSourceInfo;

    public AbstractJDBCService(JdbcSourceInfo jdbcSourceInfo) {
        checkJdbcDataSource(jdbcSourceInfo);
        this.jdbcSourceInfo = jdbcSourceInfo;
    }

    @Override
    public boolean createTableIfAbsent(String tableName, String createSql) {
        return false;
    }

    @Override
    public boolean insert(T obj) {
        return false;
    }

    @Override
    public List<T> select() {
        return null;
    }

    @Override
    public boolean test() {
        Connection connection = null;
        try {
            return (connection = getConnection()) != null;
        } catch (Exception e) {
            log.error("数据库连接失败！{}", e);
        } finally {
            close(connection, null, null);
        }
        return false;
    }

    @Override
    public List<JdbcDbInfo> getAllJdbcDbInfo() {
        Connection connection = null;
        try {
            connection = getConnection();
            if (connection == null) {
                return null;
            }
            DatabaseMetaData metaData = connection.getMetaData();
            List<JdbcDbInfo> dbNames = new ArrayList<>();
            ResultSet catalogs = metaData.getCatalogs();
            while (catalogs.next()) {
                String dbName = catalogs.getString("TABLE_CAT");
                JdbcDbInfo jdbcDbInfo = new JdbcDbInfo(dbName, jdbcSourceInfo);
                dbNames.add(jdbcDbInfo);
            }
            return dbNames;
        } catch (Exception e) {
            log.error("获取数据库信息失败！{}", e);
        } finally {
            close(connection, null, null);
        }
        return null;
    }

    @Override
    public List<JdbcTableInfo> getJdbcTableInfo(final JdbcDbInfo jdbcDbInfo) {
        Connection connection = null;
        ResultSet rs = null;
        try {
            connection = getConnection();
            if (connection == null) {
                return null;
            }
            String dbName = jdbcDbInfo.getDbName();
            List<JdbcTableInfo> tableInfos = new ArrayList<>();
            connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            DatabaseMetaData metaData = connection.getMetaData();
            //目录名称; 数据库名; 表名称; 表类型;
            rs = metaData.getTables(connection.getCatalog(), schemaPattern(dbName), tableNamePattern(), types());
            while (rs.next()) {
                JdbcTableInfo jdbcTableInfo = new JdbcTableInfo();
                jdbcTableInfo.setDbName(dbName);
                jdbcTableInfo.setTableType(rs.getString("TABLE_TYPE"));
                jdbcTableInfo.setTableName(rs.getString("TABLE_NAME"));
                jdbcTableInfo.setRemark(rs.getString("REMARKS"));
                jdbcTableInfo.setJdbcSourceInfo(jdbcDbInfo.getJdbcSourceInfo());
                tableInfos.add(jdbcTableInfo);
            }
            return tableInfos;
        } catch (Exception e) {
            log.error("获取数据库表信息失败！{}", e);
        } finally {
            close(connection, null, rs);
        }
        return null;
    }

    protected String catalog(String catalog) {
        return catalog;
    }

    protected String schemaPattern(String schemaPattern) {
        return schemaPattern;
    }

    protected String tableNamePattern() {
        return "%";
    }

    protected String[] types() {
        return new String[]{"TABLE"};
//        return new String[]{"TABLE", "VIEW"};
    }

    /**
     * @param dbName
     * @param tableName
     * @return
     */
    @Override
    public List<JdbcMapJavaInfo> getTableColumnInfo(final String dbName, final String tableName) {
        Connection connection = null;
        ResultSet rs = null;
        try {
            connection = getConnection();
            if (connection == null) {
                return null;
            }
            List<JdbcMapJavaInfo> result = new ArrayList<>();
            DatabaseMetaData meta = connection.getMetaData();
            rs = meta.getColumns(catalog(connection.getCatalog()), schemaPattern(dbName), tableName.trim(), null);
            Set<String> columnCache = new HashSet<>();
            String pk = getTablePrimaryKeys(dbName, tableName, connection);
            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME");
                if (columnCache.contains(columnName)) {
                    continue;
                }
                columnCache.add(columnName);
                String dataType = rs.getString("TYPE_NAME");
                String columnComment = rs.getString("REMARKS");
                JdbcMapJavaInfo jdbcMapJavaInfo = new JdbcMapJavaInfo(columnName, dataType, columnComment, columnName.equalsIgnoreCase(pk));
                result.add(jdbcMapJavaInfo);
            }
            return result;
        } catch (Exception e) {
            log.error("获取数据库表信息失败！{}", e);
        } finally {
            close(connection, null, rs);
        }
        return null;
    }

    /**
     * @param dbName
     * @param tableName
     * @return
     */
    private String getTablePrimaryKeys(String dbName, String tableName, Connection connection) {
        try (ResultSet rs = connection.getMetaData().getPrimaryKeys(catalog(connection.getCatalog()), schemaPattern(dbName), tableName.trim());) {
            if (rs != null) {
                while (rs.next()) {
                    return rs.getString("COLUMN_NAME");
                }
            }
        } catch (Exception e) {
            log.error("获取数据库表主键信息失败！{}", e);
        }
        return null;
    }

    /**
     * @param dbName
     * @param tableName
     * @return
     */
    protected String getTableFieldsSql(final String dbName, final String tableName) {
        return "SELECT COLUMN_NAME, DATA_TYPE, COLUMN_COMMENT" +
                " FROM INFORMATION_SCHEMA.COLUMNS" +
                " WHERE table_name = '" + tableName.toUpperCase() + "'" +
                " AND table_schema = '" + dbName + "'";
    }

    /**
     * @return
     */
    protected Connection getConnection() {
        return dataSourceTypeEnum().getConnection(jdbcSourceInfo);
    }

    /**
     * @param conn
     * @param ps
     * @param rs
     */
    protected void close(Connection conn, Statement ps, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                rs = null;
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                ps = null;
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                conn = null;
            }
        }
    }

    protected abstract DataSourceTypeEnum dataSourceTypeEnum();

    /**
     * @param jdbcSourceInfo
     */
    private void checkJdbcDataSource(JdbcSourceInfo jdbcSourceInfo) {
        if (jdbcSourceInfo == null) {
            throw new Code4jException("dataSource is null. ");
        }
        if (StringUtils.isBlank(jdbcSourceInfo.getUserName())) {
            throw new Code4jException("jdbc url is null. ");
        }
        if (StringUtils.isAnyBlank(jdbcSourceInfo.getUserName(), jdbcSourceInfo.getPassword())) {
            throw new Code4jException("jdbc user or password is null. ");
        }
    }

}
