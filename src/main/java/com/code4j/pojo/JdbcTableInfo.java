package com.code4j.pojo;

/**
 * @author liu_wp
 * @date 2020/11/19
 * @see
 */
public class JdbcTableInfo {
    private String dbName;
    private String tableName;
    private String tableType;
    private String remark;
    private JdbcSourceInfo jdbcSourceInfo;
    public String getDbName() {
        return dbName;
    }

    public void setDbName(final String dbName) {
        this.dbName = dbName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(final String tableName) {
        this.tableName = tableName;
    }

    public String getTableType() {
        return tableType;
    }

    public void setTableType(final String tableType) {
        this.tableType = tableType;
    }

    @Override
    public String toString() {
        return this.tableName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(final String remark) {
        this.remark = remark;
    }

    public JdbcSourceInfo getJdbcSourceInfo() {
        return jdbcSourceInfo;
    }

    public void setJdbcSourceInfo(final JdbcSourceInfo jdbcSourceInfo) {
        this.jdbcSourceInfo = jdbcSourceInfo;
    }
}
