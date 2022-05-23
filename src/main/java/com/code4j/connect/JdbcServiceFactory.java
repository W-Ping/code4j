package com.code4j.connect;

import com.code4j.connect.mysql.MysqlJDBCServiceImpl;
import com.code4j.connect.postgresql.PostgresqlJDBCServiceImpl;
import com.code4j.connect.sqllite.SQLiteJDBCServiceImpl;
import com.code4j.pojo.JdbcSourceInfo;

/**
 * @author liu_wp
 * @date 2020/11/18
 * @see
 */
public class JdbcServiceFactory {
    /**
     * @param jdbcSourceInfo
     * @return
     */
    public static JDBCService getJdbcService(JdbcSourceInfo jdbcSourceInfo) {
        switch (jdbcSourceInfo.getDataSourceTypeEnum()) {
            case MYSQL:
                return new MysqlJDBCServiceImpl(jdbcSourceInfo);
            case POSTGRESQL:
                return new PostgresqlJDBCServiceImpl(jdbcSourceInfo);
            case SQLITE:
                return new SQLiteJDBCServiceImpl(jdbcSourceInfo);
            default:
                return null;
        }
    }


}
