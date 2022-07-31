package com.code4j.connect;

import com.code4j.annotation.Column;
import com.code4j.annotation.Table;
import com.code4j.enums.DataSourceTypeEnum;
import com.code4j.exception.Code4jException;
import com.code4j.pojo.*;
import com.code4j.util.StrUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author liu_wp
 * @date 2020/11/18
 * @see
 */
public abstract class AbstractJDBCService<T extends BaseInfo> implements JDBCService<T> {
    protected static final Logger log = LoggerFactory.getLogger(AbstractJDBCService.class);
    protected JdbcSourceInfo jdbcSourceInfo;

    public AbstractJDBCService(JdbcSourceInfo jdbcSourceInfo) {
        checkJdbcDataSource(jdbcSourceInfo);
        this.jdbcSourceInfo = jdbcSourceInfo;
    }

    /**
     * @param cls
     * @param dbTableInfos
     * @param forceCreate
     * @return
     */
    @Override
    public boolean createTableIfAbsent(Class<T> cls, List<DbTableInfo> dbTableInfos, boolean forceCreate) {
        final Table annotation = cls.getAnnotation(Table.class);
        final String tableName = annotation.value();
        if (!forceCreate && checkTableIsExist(tableName)) {
            return true;
        }
        Connection connection = null;
        Statement statement = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            log.info(">>>>>> drop table if exists {}", tableName);
            statement.execute("DROP TABLE IF EXISTS " + tableName);
            statement.execute(this.createTableSql(tableName, dbTableInfos));
            log.info(">>>>>> create table {}", tableName);
            final String[] uniqueKey = annotation.uniqueKey();
            if (uniqueKey != null && uniqueKey.length > 0) {
                StringBuilder uk = new StringBuilder();
                String ukName = tableName + "_uk";
                uk.append("CREATE UNIQUE INDEX ");
                uk.append(ukName);
                uk.append(" ON ");
                uk.append(tableName);
                uk.append("(");
                uk.append(Arrays.stream(uniqueKey).collect(Collectors.joining(",")));
                uk.append(")");
                statement.execute(uk.toString());
            }
            return true;
        } catch (Exception e) {
            log.error("错误！创建表失败【{}】", e.getMessage());
        } finally {
            close(connection, statement, null);
        }
        return false;
    }

    protected String createTableSql(String tableName, List<DbTableInfo> dbTableInfos) {
        StringBuilder sb = new StringBuilder("CREATE TABLE ");
        sb.append(tableName);
        sb.append("(");
        List<String> primaryKeys = new ArrayList<>();
        for (int i = 0; i < dbTableInfos.size(); i++) {
            DbTableInfo dbTableInfo = dbTableInfos.get(i);
            sb.append(dbTableInfo.getColumn());
            sb.append(" ");
            sb.append(dbTableInfo.getJdbcType());
            if (dbTableInfo.getCanNull() != null && !dbTableInfo.getCanNull()) {
                sb.append(" NOT NULL ");
            }
            if (dbTableInfo.getPrimaryKey() != null && dbTableInfo.getPrimaryKey()) {
                primaryKeys.add(dbTableInfo.getColumn());
            }
            if (dbTableInfo.getAutoIncrement() != null && dbTableInfo.getAutoIncrement()) {
                sb.append(" AUTO_INCREMENT ");
            }
            if (StringUtils.isNotBlank(dbTableInfo.getDefaultValue())) {
                sb.append(" DEFAULT ");
                sb.append(dbTableInfo.getDefaultValue());
            }
            if (StringUtils.isNotBlank(dbTableInfo.getComment())) {
                sb.append(" COMMENT ");
                sb.append("'");
                sb.append(dbTableInfo.getComment());
                sb.append("'");
            }
            if (i != dbTableInfos.size() - 1) {
                sb.append(",");
            }
        }
        if (!CollectionUtils.isEmpty(primaryKeys)) {
            final String pkStr = primaryKeys.stream().collect(Collectors.joining(","));
            sb.append(",PRIMARY KEY ");
            sb.append("(");
            sb.append(pkStr);
            sb.append(")");
        }
        sb.append(")");
        log.debug("创建表SQL：{}", sb);
        return sb.toString();
    }

    /**
     * @param obj
     * @return
     */
    @Override
    public boolean checkUniqueKey(T obj, boolean exclude) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            Class<? extends BaseInfo> aClass = obj.getClass();
            Table tbAnnotation = aClass.getAnnotation(Table.class);
            final String tableName = tbAnnotation.value();
            final String[] uniqueKeys = tbAnnotation.uniqueKey();
            if (uniqueKeys == null && uniqueKeys.length <= 0) {
                return true;
            }
            connection = getConnection();
            StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM ");
            sql.append(tableName);
            sql.append(" WHERE ");
            List<Object> objects = new ArrayList<>();
            for (int i = 0; i < uniqueKeys.length; i++) {
                String uniqueKey = uniqueKeys[i];
                sql.append(uniqueKey);
                final String filedName = StrUtil.underlineToCamel(uniqueKey);
                final Field field = aClass.getDeclaredField(filedName);
                field.setAccessible(true);
                if (field == null) {
                    continue;
                }
                final Object value = field.get(obj);
                if (value == null) {
                    continue;
                }
                objects.add(value);
                sql.append(" = ?");
                if (i != uniqueKeys.length - 1) {
                    sql.append(" AND ");
                }
            }
            if (exclude) {
                final Field primaryKeyField = getPrimaryKeyField(FieldUtils.getAllFields(aClass));
                if (primaryKeyField != null) {
                    final Object val = primaryKeyField.get(obj);
                    if (val != null) {
                        sql.append(" AND ");
                        sql.append(primaryKeyField.getName());
                        sql.append(" <> ?");
                        objects.add(val);
                    }
                }
            }
            log.debug("校验数据唯一性SQL:{}", sql);
            statement = connection.prepareStatement(sql.toString());
            for (int i = 0; i < objects.size(); i++) {
                Object val = objects.get(i);
                if (val instanceof String) {
                    statement.setString(i + 1, val.toString());
                } else if (val instanceof Integer) {
                    statement.setInt(i + 1, Integer.valueOf(val.toString()));
                } else if (val instanceof Long) {
                    statement.setLong(i + 1, Long.valueOf(val.toString()));
                } else if (val instanceof BigDecimal) {
                    statement.setBigDecimal(i + 1, new BigDecimal(val.toString()));
                } else {
                    statement.setObject(i + 1, val);
                }
            }
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                final int count = resultSet.getInt(1);
                return count <= 0;
            }
        } catch (Exception e) {
            log.error("错误！校验数据唯一性失败【{}】", e.getMessage());
        } finally {
            close(connection, statement, resultSet);
        }
        return false;
    }

    @Override
    public boolean insert(T obj) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            Class<? extends BaseInfo> aClass = obj.getClass();
            Field[] declaredFields = FieldUtils.getAllFields(aClass);
            Table tbAnnotation = aClass.getAnnotation(Table.class);
            StringBuilder columns = new StringBuilder();
            StringBuilder columnValues = new StringBuilder();
            columns.append(" (");
            columnValues.append(" (");
            final List<Field> fields = Arrays.stream(declaredFields).filter(f -> f.getAnnotation(Column.class) != null).collect(Collectors.toList());
            boolean valueAllIsNull = true;
            for (int i = 0; i < fields.size(); i++) {
                Field field = fields.get(i);
                field.setAccessible(true);
                Object objValue = field.get(obj);
                final Column annotation = field.getAnnotation(Column.class);
                String value = annotation.value();
                columns.append(value);
                if (field.getType().equals(String.class) && objValue != null) {
                    columnValues.append("'");
                    columnValues.append(objValue);
                    columnValues.append("'");
                    valueAllIsNull = false;
                } else {
                    if (objValue != null) {
                        valueAllIsNull = false;
                    }
                    columnValues.append(objValue);
                }
                if (i != fields.size() - 1) {
                    columns.append(",");
                    columnValues.append(",");
                }
            }
            columns.append(") values");
            columnValues.append(") ");
            StringBuilder sql = new StringBuilder("INSERT INTO ");
            sql.append(tbAnnotation.value());
            sql.append(" ");
            sql.append(columns);
            sql.append(columnValues);
            log.debug("数据新增SQL:{}", sql);
            if (valueAllIsNull) {
                log.error("新增数据失败！{} 字段值都是null", tbAnnotation.value());
                return false;
            }
            statement = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            final Field field = getPrimaryKeyField(declaredFields);
            while (resultSet != null && resultSet.next()) {
                final long pk = resultSet.getLong(1);
                log.debug("数据新增返回主键：{}", pk);
                field.set(obj, pk);
            }
            return true;
        } catch (Exception e) {
            log.error("错误！数据新增失败【{}】", e.getMessage());
        } finally {
            close(connection, statement, resultSet);
        }
        return false;
    }

    @Override
    public boolean updateByPk(T obj) {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = getConnection();
            final Class<? extends BaseInfo> cls = obj.getClass();
            final Table annotation = cls.getAnnotation(Table.class);
            final String tableName = annotation.value();
            final Field[] declaredFields = FieldUtils.getAllFields(cls);
            final Field field = getPrimaryKeyField(declaredFields);
            if (field == null && field.get(obj) == null) {
                return false;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("UPDATE ");
            sb.append(tableName);
            sb.append(" ");
            sb.append("SET ");
            final List<Field> fieldList = Arrays.stream(declaredFields).filter(v -> v.getAnnotation(Column.class) != null).collect(Collectors.toList());
            for (int i = 0; i < fieldList.size(); i++) {
                final Field f = fieldList.get(i);
                f.setAccessible(true);
                final Object value = f.get(obj);
                if (value != null) {
                    sb.append(f.getAnnotation(Column.class).value());
                    sb.append("=");
                    sb.append("'");
                    sb.append(value);
                    sb.append("'");
                    if (i != fieldList.size() - 1) {
                        sb.append(",");
                    }
                }
            }
            sb.append(" WHERE ");
            sb.append(field.getName());
            sb.append("=");
            sb.append(field.get(obj));
            statement = connection.createStatement();
            statement.executeUpdate(sb.toString());
            return true;
        } catch (Exception e) {
            log.error("数据库连接失败！{}", e.getMessage());
        } finally {
            close(connection, statement, null);
        }
        return false;
    }

    /**
     * @param primaryKey
     * @param cls
     * @return
     */
    @Override
    public boolean deleteByPk(Long primaryKey, Class<T> cls) {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            StringBuilder sb = new StringBuilder();
            Table tbAnnotation = cls.getAnnotation(Table.class);
            final Field field = getPrimaryKeyField(FieldUtils.getAllFields(cls));
            if (field == null || primaryKey == null) {
                return false;
            }
            sb.append("DELETE FROM ");
            sb.append(tbAnnotation.value());
            sb.append(" WHERE ");
            sb.append(field.getName());
            sb.append("=");
            sb.append(primaryKey);
            statement.executeUpdate(sb.toString());
            return true;
        } catch (Exception e) {
            log.error("错误！更新数据失败【{}】】【{}】", primaryKey, e.getMessage());
        } finally {
            close(connection, statement, null);
        }
        return false;
    }

    @Override
    public List<T> select(T obj) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            final Class<? extends BaseInfo> cls = obj.getClass();
            Field[] declaredFields = FieldUtils.getAllFields(cls);
            List<Field> fields = new ArrayList<>();
            final Table tableAnnotation = cls.getAnnotation(Table.class);
            final String table = tableAnnotation.value();
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT * FROM ");
            sql.append(table);
            StringBuilder where = new StringBuilder(" where 1=1");
            for (Field declaredField : declaredFields) {
                final Column annotation = declaredField.getAnnotation(Column.class);
                if (annotation == null) {
                    continue;
                }
                declaredField.setAccessible(true);
                final String value = annotation.value();
                final Object val = declaredField.get(obj);
                final Class<?> type = declaredField.getType();
                if (val != null) {
                    where.append(" AND ");
                    where.append(value);
                    where.append("=");
                    if (type.equals(String.class) || type.equals(Integer.class) || type.equals(Long.class)) {
                        where.append("'");
                        where.append(val);
                        where.append("'");
                    } else {
                        where.append(val);
                    }
                }
                fields.add(declaredField);
            }
            if (StringUtils.isNotBlank(where)) {
                sql.append(" ");
                sql.append(where);
            }
            log.debug("数据查询SQL:{}", sql);
            resultSet = statement.executeQuery(sql.toString());
            List<T> list = new ArrayList<>();
            while (resultSet.next()) {
                T t = (T) cls.newInstance();
                for (Field field : fields) {
                    Column annotation = field.getAnnotation(Column.class);
                    field.setAccessible(true);
                    Class<?> type = field.getType();
                    if (Integer.class.equals(type)) {
                        int anInt = resultSet.getInt(annotation.value());
                        field.set(t, anInt);
                    } else if (Long.class.equals(type)) {
                        long aLong = resultSet.getLong(annotation.value());
                        field.set(t, aLong);
                    } else if (String.class.equals(type)) {
                        String value = resultSet.getString(annotation.value());
                        if (StringUtils.isNotBlank(value)) {
                            field.set(t, value);
                        }
                    } else if (Double.class.equals(type)) {
                        double aDouble = resultSet.getDouble(annotation.value());
                        field.setDouble(t, aDouble);
                    } else if (BigDecimal.class.equals(annotation.value())) {
                        BigDecimal bigDecimal = resultSet.getBigDecimal(annotation.value());
                        if (bigDecimal != null) {
                            field.setDouble(t, bigDecimal.doubleValue());
                        }
                    }
                }
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            log.error("错误！查询数据失败【{}】", e.getMessage());
        } finally {
            close(connection, statement, resultSet);
        }
        return null;
    }

    @Override
    public boolean test() {
        Connection connection = null;
        try {
            return (connection = getConnection()) != null;
        } catch (Exception e) {
            log.error("错误！数据库连接失败【{}】", e.getMessage());
        } finally {
            close(connection, null, null);
        }
        return false;
    }

    @Override
    public boolean checkTableIsExist(String tableName) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            statement = connection.prepareStatement("SELECT count(*) from sqlite_master where type='table' and name=?");
            statement.setString(1, tableName);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (Exception e) {
            log.error("错误！检查表是否存在失败【{}】", e.getMessage());
        } finally {
            close(connection, statement, resultSet);
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
            log.error("错误！获取数据库信息失败！【{}】", e.getMessage());
        } finally {
            close(connection, null, null);
        }
        return null;
    }

    @Override
    public List<JdbcSchemaInfo> getJdbcSchemaInfo(String dbName) {
        Connection connection = null;
        try {
            jdbcSourceInfo.setInitDb(dbName);
            connection = getConnection();
            if (connection == null) {
                return null;
            }
            DatabaseMetaData metaData = connection.getMetaData();
            List<JdbcSchemaInfo> schemaInfoList = new ArrayList<>();
            final ResultSet schemas = metaData.getSchemas();
            while (schemas.next()) {
                final String tableSchema = schemas.getString("TABLE_SCHEM");
                JdbcSchemaInfo jdbcSchemaInfo = new JdbcSchemaInfo();
                jdbcSchemaInfo.setSchemaName(tableSchema);
                schemaInfoList.add(jdbcSchemaInfo);
            }
            return schemaInfoList;
        } catch (Exception e) {
            log.error("错误！获取数据库信息失败！【{}】", e.getMessage());
        } finally {
            close(connection, null, null);
        }
        return null;
    }

    @Override
    public List<JdbcTableInfo> getJdbcTableInfo(final JdbcDbInfo jdbcDbInfo, final String schema) {
        Connection connection = null;
        ResultSet rs = null;
        try {
            jdbcSourceInfo.setInitDb(jdbcDbInfo.getDbName());
            connection = getConnection();
            if (connection == null) {
                return null;
            }
            String dbName = jdbcDbInfo.getDbName();
            List<JdbcTableInfo> tableInfos = new ArrayList<>();
            connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            DatabaseMetaData metaData = connection.getMetaData();
            //目录名称; 数据库名; 表名称; 表类型;
            rs = metaData.getTables(this.catalog(connection.getCatalog(), dbName), Optional.ofNullable(schema).orElse(schemaPattern(dbName)), tableNamePattern(), types());
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
            log.error("错误！获取数据库表信息失败【{}】", e.getMessage());
        } finally {
            close(connection, null, rs);
        }
        return null;
    }

    protected String catalog(String catalog, String dbName) {
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
    }


    /**
     * @param schema
     * @param dbName
     * @param tableName
     * @return
     */
    @Override
    public List<JdbcMapJavaInfo> getTableColumnInfo(final String schema, final String dbName, final String tableName) {
        Connection connection = null;
        ResultSet rs = null;
        try {
            connection = getConnection();
            if (connection == null) {
                return null;
            }
            List<JdbcMapJavaInfo> result = new ArrayList<>();
            DatabaseMetaData meta = connection.getMetaData();
            rs = meta.getColumns(this.catalog(connection.getCatalog(), dbName), Optional.ofNullable(schema).orElse(schemaPattern(dbName)), tableName.trim(), null);
            Set<String> columnCache = new HashSet<>();
            String pk = getTablePrimaryKeys(schema, dbName, tableName, connection);
            log.debug("获取表信息参数：数据库{}主键是{}", dbName, pk);
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
            log.error("错误！获取数据库表信息失败【{}】", e.getMessage());
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
    private String getTablePrimaryKeys(String schema, String dbName, String tableName, Connection connection) {
        try (ResultSet rs = connection.getMetaData().getPrimaryKeys(catalog(connection.getCatalog(), dbName), Optional.ofNullable(schema).orElse(schemaPattern(dbName)), tableName.trim())) {
            if (rs != null) {
                while (rs.next()) {
                    return rs.getString("COLUMN_NAME");
                }
            }
        } catch (Exception e) {
            log.error("错误！获取数据库表主键信息失败【{}】", e.getMessage());
        }
        return null;
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

    private Field getPrimaryKeyField(Field[] fields) {
        final List<Field> pkList = Arrays.asList(fields).stream().filter(v -> {
            final Column annotation = v.getAnnotation(Column.class);
            if (annotation != null && annotation.pk()) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(pkList)) {
            return null;
        }
        final Field field = pkList.get(0);
        field.setAccessible(true);
        return field;
    }
}
