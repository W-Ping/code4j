package com.code4j.pojo;

import com.code4j.util.StrUtil;
import com.code4j.util.SystemUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author liu_wp
 * @date 2020/11/19
 * @see
 */
public class JdbcMapJavaInfo {
    /**
     * 字段注释
     */
    private String comment;
    /**
     * 数据库字段
     */
    private String column;
    /**
     * 数据库类型
     */
    private String jdbcType;
    /**
     * java属性
     */
    private String javaProperty;

    /**
     * java类型
     */
    private String javaType;

    /**
     * 是否忽略此字段
     */
    private boolean ignore;

    /**
     * 是否为主键
     */
    private boolean primaryKey;

    public JdbcMapJavaInfo() {

    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        final JdbcMapJavaInfo that = (JdbcMapJavaInfo) o;
        return Objects.equals(column, that.column);
    }

    @Override
    public int hashCode() {
        return Objects.hash(comment, column, jdbcType, javaProperty, javaType, ignore);
    }

    public JdbcMapJavaInfo(String column, String jdbcType, String comment, boolean primaryKey) {
        this.column = column;
        this.primaryKey = primaryKey;
        this.jdbcType = toJdbcType(jdbcType);
        this.comment = comment;
        this.javaProperty = toJavaProperty(column);
        this.javaType = toJavaType(jdbcType);
    }

    private String toJdbcType(String jdbcType) {
        if (jdbcType == null) {
            return null;
        }
        if (jdbcType.equalsIgnoreCase("INT")) {
            return "INTEGER";
        } else if (jdbcType.equalsIgnoreCase("DATETIME")) {
            return "TIMESTAMP";
        }
        return jdbcType;
    }

    private String toJavaType(String jdbcType) {
        if (StringUtils.isNotBlank(jdbcType)) {
            return SystemUtil.formatDataType(jdbcType);
        }
        return null;
    }


    private String toJavaProperty(String column) {
        if (StringUtils.isNotBlank(column)) {
            return StrUtil.underlineToCamel(column);
        }
        return null;
    }


    public String getComment() {
        return comment;
    }

    public void setComment(final String comment) {
        this.comment = comment;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(final String column) {
        this.column = column;
    }

    public String getJdbcType() {
        return this.jdbcType = jdbcType.equalsIgnoreCase("INT") ? "INTEGER" : jdbcType;
    }

    public void setJdbcType(final String jdbcType) {
        this.jdbcType = jdbcType;
    }

    public String getJavaProperty() {
        return javaProperty;
    }

    public void setJavaProperty(final String javaProperty) {
        this.javaProperty = javaProperty;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(final String javaType) {
        this.javaType = javaType;
    }

    public boolean isIgnore() {
        return ignore;
    }

    public void setIgnore(final boolean ignore) {
        this.ignore = ignore;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }
}
