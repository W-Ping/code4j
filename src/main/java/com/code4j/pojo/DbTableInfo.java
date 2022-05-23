package com.code4j.pojo;

/**
 * @author :lwp
 * @date :Created in 2022-05-21
 */
public class DbTableInfo {
    private String column;
    private String jdbcType;
    private String comment;
    private String defaultValue;
    private Boolean canNull;

    private Boolean autoIncrement;

    private Boolean primaryKey;

    public DbTableInfo() {
    }

    public DbTableInfo(String column, String jdbcType, String comment,Boolean primaryKey,Boolean autoIncrement) {
        this.column = column;
        this.jdbcType = jdbcType;
        this.comment = comment;
        this.primaryKey = primaryKey;
        this.autoIncrement = autoIncrement;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getJdbcType() {
        return jdbcType;
    }

    public void setJdbcType(String jdbcType) {
        this.jdbcType = jdbcType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Boolean getCanNull() {
        return canNull;
    }

    public void setCanNull(Boolean canNull) {
        this.canNull = canNull;
    }

    public Boolean getAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(Boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    public Boolean getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(Boolean primaryKey) {
        this.primaryKey = primaryKey;
    }
}
