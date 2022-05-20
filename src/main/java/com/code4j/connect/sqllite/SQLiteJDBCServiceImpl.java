package com.code4j.connect.sqllite;

import com.code4j.connect.AbstractJDBCService;
import com.code4j.connect.DataSourceTypeEnum;
import com.code4j.pojo.JdbcSourceInfo;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

/**
 * @author lwp
 * @date 2022-05-20
 */
public class SQLiteJDBCServiceImpl extends AbstractJDBCService<JdbcSourceInfo> {
    public SQLiteJDBCServiceImpl(final JdbcSourceInfo jdbcSourceInfo) {
        super(jdbcSourceInfo);
    }

    @Override
    protected DataSourceTypeEnum dataSourceTypeEnum() {
        return DataSourceTypeEnum.SQLITE;
    }

    @Override
    public boolean createTableIfAbsent(String tableName, String createSql) {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            statement.execute("DROP TABLE IF EXISTS" + tableName);
            statement.execute("CREATE TABLE " + tableName + createSql);
            return true;
        } catch (Exception e) {
            log.error("数据库连接失败！{}", e);
        } finally {
            close(connection, statement, null);
        }
        return false;
    }

    @Override
    public List<JdbcSourceInfo> select() {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO USER_INF VALUES('1', '程咬金', '男') ");
        } catch (Exception e) {
            log.error("数据库连接失败！{}", e);
        } finally {
            close(connection, statement, null);
        }
        return null;
    }

    public boolean insert(JdbcSourceInfo jdbcSourceInfo) {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO USER_INF VALUES('1', '程咬金', '男') ");
            return true;
        } catch (Exception e) {
            log.error("数据库连接失败！{}", e);
        } finally {
            close(connection, statement, null);
        }
        return false;
    }

}
