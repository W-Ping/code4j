package com.code4j.enums;

import com.code4j.config.Code4jConstants;
import com.code4j.pojo.JdbcSourceInfo;
import com.code4j.util.JSONUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Optional;

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
            return "MySQL";
        }

        @Override
        public String getUrl(final JdbcSourceInfo JDBCSourceInfo) {
            String url = "jdbc:mysql://" + JDBCSourceInfo.getConnectHost() + ":" + JDBCSourceInfo.getConnectPort() + "?&nullCatalogMeansCurrent=true&useOldAliasMetadataBehavior=true&serverTimezone=Asia/Shanghai";
            return url;
        }

        @Override
        public Connection getConnection(final JdbcSourceInfo jdbcSourceInfo) {
            try {
                Class.forName(this.getDriver());
                return DriverManager.getConnection(this.getUrl(jdbcSourceInfo), jdbcSourceInfo.getUserName(), jdbcSourceInfo.getPassword());
            } catch (Exception e) {
                LoggerFactory.getLogger(DataSourceTypeEnum.class).error("MySQL获取连接失败参数：【{}】错误：【{}】", JSONUtil.Object2JSON(jdbcSourceInfo), e.getMessage());
            }
            return null;
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
            return "PostgreSQL";
        }

        @Override
        public String getUrl(final JdbcSourceInfo JDBCSourceInfo) {
            String url = "jdbc:postgresql://" + JDBCSourceInfo.getConnectHost() + ":" + JDBCSourceInfo.getConnectPort() + "/" + Optional.ofNullable(JDBCSourceInfo.getDbName()).orElse("");
            return url;
        }

        @Override
        public Connection getConnection(final JdbcSourceInfo jdbcSourceInfo) {
            try {
                Class.forName(this.getDriver());
                return DriverManager.getConnection(this.getUrl(jdbcSourceInfo), jdbcSourceInfo.getUserName(), jdbcSourceInfo.getPassword());
            } catch (Exception e) {
                LoggerFactory.getLogger(DataSourceTypeEnum.class).error("PostgreSQL获取连接失败参数：【{}】错误：【{}】", JSONUtil.Object2JSON(jdbcSourceInfo), e.getMessage());
            }
            return null;
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
            return "SQLite";
        }

        @Override
        public String getUrl(final JdbcSourceInfo JDBCSourceInfo) {
            String dbName = StringUtils.isNotBlank(JDBCSourceInfo.getDbName()) ? JDBCSourceInfo.getDbName() : "code4j";
            String url = "jdbc:sqlite:" + dbName + ".db";
            return url;
        }

        @Override
        public Connection getConnection(final JdbcSourceInfo jdbcSourceInfo) {
            try {
                Class.forName(this.getDriver());
                String userName = Optional.ofNullable(jdbcSourceInfo.getUserName()).orElse(Code4jConstants.SQLITE_DEFAULT_NAME);
                String pwd = Optional.ofNullable(jdbcSourceInfo.getPassword()).orElse(Code4jConstants.SQLITE_DEFAULT_PWD);
                return DriverManager.getConnection(this.getUrl(jdbcSourceInfo), userName, pwd);
            } catch (Exception e) {
                LoggerFactory.getLogger(DataSourceTypeEnum.class).error("SQLite获取连接失败参数：【{}】错误：【{}】", JSONUtil.Object2JSON(jdbcSourceInfo), e.getMessage());
            }
            return null;
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
