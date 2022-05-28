package com.code4j.pojo;

/**
 * @author liu_wp
 * @date 2020/12/15
 * @see
 */
public class JdbcDbInfo {
    private JdbcSourceInfo jdbcSourceInfo;
    private String dbName;

    public JdbcDbInfo() {
    }


    public JdbcDbInfo(String dbName, JdbcSourceInfo jdbcSourceInfo) {
        this.dbName = dbName;
        this.jdbcSourceInfo = jdbcSourceInfo;
    }

    @Override
    public String toString() {
        return this.dbName;
    }

    public JdbcSourceInfo getJdbcSourceInfo() {
        return jdbcSourceInfo;
    }

    public void setJdbcSourceInfo(final JdbcSourceInfo jdbcSourceInfo) {
        this.jdbcSourceInfo = jdbcSourceInfo;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(final String dbName) {
        this.dbName = dbName;
    }
}
