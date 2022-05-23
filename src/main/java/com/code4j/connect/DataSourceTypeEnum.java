package com.code4j.connect;

import com.code4j.pojo.JdbcSourceInfo;
import org.apache.commons.lang3.StringUtils;

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
        public int defaultPort() {
            return 3306;
        }

        @Override
        public String typeName() {
            return "MYSQL";
        }

        @Override
        public String getUrl(final JdbcSourceInfo JDBCSourceInfo) {
            String url = "jdbc:mysql://" + JDBCSourceInfo.getConnectHost() + ":" + JDBCSourceInfo.getConnectPort() + "?useOldAliasMetadataBehavior=true&serverTimezone=Asia/Shanghai";
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
    POSTGRESQL {
        @Override
        public String getDriver() {
            return "org.postgresql.Driver";
        }

        @Override
        public int defaultPort() {
            return 5432;
        }

        @Override
        public String typeName() {
            return "POSTGRESQL";
        }

        @Override
        public String getUrl(final JdbcSourceInfo JDBCSourceInfo) {
            String url = "jdbc:postgresql://" + JDBCSourceInfo.getConnectHost() + ":" + JDBCSourceInfo.getConnectPort() + "/";
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
    SQLITE {
        @Override
        public String getDriver() {
            return "org.sqlite.JDBC";
        }

        @Override
        public int defaultPort() {
            return -1;
        }

        @Override
        public String typeName() {
            return "SQLITE";
        }

        @Override
        public String getUrl(final JdbcSourceInfo JDBCSourceInfo) {
            String dbName = StringUtils.isNotBlank(JDBCSourceInfo.getDbName()) ? JDBCSourceInfo.getDbName() : "code4j";
            String url = "jdbc:sqlite:" + dbName + ".db";
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
    };

    /**
     * @return
     */
    public abstract String getDriver();

    /**
     * @return
     */
    public abstract int defaultPort();

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
