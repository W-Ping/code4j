package com.code4j.pojo;

/**
 * @author lwp
 * @date 2022-07-06
 */
public class JdbcSchemaInfo {
    private JdbcDbInfo jdbcDbInfo;
    private String  schemaName;

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public JdbcDbInfo getJdbcDbInfo() {
        return jdbcDbInfo;
    }

    @Override
    public String toString() {
        return schemaName;
    }

    public void setJdbcDbInfo(JdbcDbInfo jdbcDbInfo) {
        this.jdbcDbInfo = jdbcDbInfo;
    }
}
