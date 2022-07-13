package com.code4j.connect.postgresql;

import com.code4j.connect.AbstractJDBCService;
import com.code4j.enums.DataSourceTypeEnum;
import com.code4j.pojo.JdbcDbInfo;
import com.code4j.pojo.JdbcSourceInfo;
import com.code4j.pojo.JdbcTableInfo;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lwp
 * @date 2022-05-20
 */
public class PostgresqlJDBCServiceImpl extends AbstractJDBCService {

    public PostgresqlJDBCServiceImpl(final JdbcSourceInfo jdbcSourceInfo) {
        super(jdbcSourceInfo);
    }

    @Override
    protected DataSourceTypeEnum dataSourceTypeEnum() {
        return DataSourceTypeEnum.POSTGRESQL;
    }

    @Override
    protected String schemaPattern(String schemaPattern) {
        return "public";
    }


    @Override
    public List<JdbcDbInfo> getAllJdbcDbInfo() {
        Connection connection = null;
        ResultSet resultSet = null;
        Statement statement = null;
        try {
            connection = getConnection();
            if (connection == null) {
                return null;
            }
            String sql = "select datname from pg_database";
            List<JdbcDbInfo> dbNames = new ArrayList<>();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                final String dbName = resultSet.getString(1);
                if (StringUtils.isNotBlank(dbName) && dbName.startsWith("template")) {
                    continue;
                }
                JdbcDbInfo jdbcDbInfo = new JdbcDbInfo(dbName, jdbcSourceInfo);
                dbNames.add(jdbcDbInfo);
            }
            return dbNames;
        } catch (Exception e) {
            log.error("错误！postgresql获取数据库信息失败【{}】", e.getMessage());
        } finally {
            close(connection, statement, resultSet);
        }
        return null;
    }

    public List<JdbcTableInfo> getJdbcTableInfo1(JdbcDbInfo jdbcDbInfo) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            jdbcSourceInfo.setInitDb(jdbcDbInfo.getDbName());
            connection = getConnection();
            if (connection == null) {
                return null;
            }
            String sql = "SELECT relname, obj_description(relname::REGCLASS::OID) AS comments FROM pg_class C LEFT JOIN pg_catalog.pg_namespace n ON n.oid = C.relnamespace LEFT JOIN pg_catalog.pg_database d ON d.datname = ? WHERE relkind IN( 'r') AND n.nspname NOT IN ( 'pg_catalog', 'information_schema' ) AND n.nspname !~ '^pg_toast' ORDER BY relname";
            statement = connection.prepareStatement(sql);
            statement.setString(1, jdbcDbInfo.getDbName());
            resultSet = statement.executeQuery();
            List<JdbcTableInfo> list = new ArrayList<>();
            while (resultSet != null && resultSet.next()) {
                final String tableName = resultSet.getString(1);
                final String remarks = resultSet.getString(2);
                JdbcTableInfo jdbcTableInfo = new JdbcTableInfo();
                jdbcTableInfo.setDbName(jdbcDbInfo.getDbName());
                jdbcTableInfo.setTableName(tableName);
                jdbcTableInfo.setRemark(remarks);
                jdbcTableInfo.setJdbcSourceInfo(jdbcDbInfo.getJdbcSourceInfo());
                list.add(jdbcTableInfo);
            }
            return list;
        } catch (Exception e) {
            log.error("获取数据库表信息失败！{}", e.getMessage());
        } finally {
            close(connection, statement, resultSet);
        }
        return null;
    }
}
