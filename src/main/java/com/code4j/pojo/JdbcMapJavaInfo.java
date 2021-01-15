package com.code4j.pojo;

import com.code4j.util.StrUtil;
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

    private boolean ignore;

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

    public JdbcMapJavaInfo(String column, String jdbcType, String comment) {
        this.column = column;
        this.jdbcType = jdbcType.equalsIgnoreCase("INT") ? "INTEGER" : jdbcType;
        this.comment = comment;
        this.javaProperty = toJavaProperty(column);
        this.javaType = toJavaType(jdbcType);
    }

    private String toJavaType(String jdbcType) {
        if (StringUtils.isNotBlank(jdbcType)) {
            return formatDataType(jdbcType);
        }
        return null;
    }

    private String toJavaProperty(String column) {
        if (StringUtils.isNotBlank(column)) {
            return StrUtil.underlineToCamel(column);
        }
        return null;
    }

    private String formatDataType(String dataType) {
        dataType = dataType.toUpperCase();
        if (dataType.equals("CHAR")
                || dataType.equals("TEXT")
                || dataType.equals("VARCHAR")
                || dataType.equals("TINYTEXT")
                || dataType.equals("LONGTEXT")) {
            dataType = "java.lang.String";
        } else if (dataType.equals("BIGINT")) {
            dataType = "java.lang.Long";
        } else if (dataType.equals("INT")
                || dataType.equals("INTEGER")
                || dataType.equals("MEDIUMINT")
                || dataType.equals("TINYINT")) {
            dataType = "java.lang.Integer";
        } else if (dataType.equals("FLOAT")) {
            dataType = "java.lang.Float";
        } else if (dataType.equals("DOUBLE")) {
            dataType = "java.lang.Double";
        } else if (dataType.equals("NUMERIC")
                || dataType.equals("DECIMAL")) {
            dataType = "java.math.BigDecimal";
        } else if (dataType.equals("DATE")
                || dataType.equals("DATETIME")
                || dataType.equals("TIMESTAMP")
                || dataType.equals("YEAR")
                || dataType.equals("TIME")) {
            return "java.util.Date";
        } else if (dataType.equals("BIT")) {
            return "java.lang.Boolean";
        } else if (dataType.equals("BLOB")) {
            return "Byte[]";
        } else if (dataType.equals("CLOB")) {
            dataType = "java.sql.Clob";
        } else {
            dataType = "java.lang.Object";
        }
        return dataType;
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
}
