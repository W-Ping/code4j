package com.code4j.connect;

import com.code4j.connect.mysql.MysqlJDBCServiceImpl;
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
        if (jdbcSourceInfo.getDataSourceTypeEnum() == DataSourceTypeEnum.MYSQL) {
            return new MysqlJDBCServiceImpl(jdbcSourceInfo);
        }
        return null;
    }

}
