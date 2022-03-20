package com.code4j.connect;

import com.code4j.pojo.JdbcSourceInfo;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @author liu_wp
 * @date Created in 2020/11/18 17:00
 * @see
 */
public enum DataSourceTypeEnum {
    MYSQL {
        @Override
        public String getDriver() {
            return "com.mysql.cj.jdbc.Driver";
        }

        @Override
        public String typeName() {
            return "MYSQL";
        }

        @Override
        public String getUrl(final JdbcSourceInfo JDBCSourceInfo) {
            String url = "jdbc:mysql://" + JDBCSourceInfo.getConnectHost() + ":" + JDBCSourceInfo.getConnectPort()+"?useOldAliasMetadataBehavior=true&serverTimezone=Asia/Shanghai";
            return url;
        }

        @Override
        public Connection getConnection(final JdbcSourceInfo jdbcSourceInfo) {
            Connection conn = null;
            try {
                Class.forName(this.getDriver());
                conn = DriverManager.getConnection(this.getUrl(jdbcSourceInfo), jdbcSourceInfo.getUserName(), jdbcSourceInfo.getPassword());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return conn;
        }
    },
    ;

    /**
     * @return
     */
    public abstract String getDriver();

    /**
     * @return
     */
    public abstract String typeName();

    public abstract String getUrl(JdbcSourceInfo JDBCSourceInfo);

    public abstract Connection getConnection(JdbcSourceInfo jdbcSourceInfo);

    public static DataSourceTypeEnum getDataSourceTypeEnum(String typeName) {
        for (final DataSourceTypeEnum value : values()) {
            if (value.typeName().equals(typeName)) {
                return value;
            }
        }
        return null;
    }
}
