package com.code4j.pojo;

import com.code4j.util.StrUtil;
import org.apache.commons.lang3.StringUtils;

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


    public JdbcMapJavaInfo() {

    }

    public JdbcMapJavaInfo(String column, String jdbcType, String comment) {
        this.column = column;
        this.jdbcType = jdbcType;
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
        if (dataType.contains("CHAR")
                || dataType.contains("TEXT")
                || dataType.contains("VARCHAR")
                || dataType.contains("TINYTEXT")
                || dataType.contains("LONGTEXT")) {
            dataType = "java.lang.String";
        } else if (dataType.contains("INT")
                || dataType.contains("INTEGER")
                || dataType.contains("MEDIUMINT")
                || dataType.contains("TINYINT")) {
            dataType = "java.lang.Integer";
        } else if (dataType.contains("TINYINT")) {
        } else if (dataType.contains("BIGINT")) {
            dataType = "java.lang.Long";
        } else if (dataType.contains("FLOAT")) {
            dataType = "java.lang.Float";
        } else if (dataType.contains("DOUBLE")) {
            dataType = "java.lang.Double";
        } else if (dataType.contains("NUMERIC")
                || dataType.contains("DECIMAL")) {
            dataType = "java.math.BigDecimal";
        } else if (dataType.contains("DATE")
                || dataType.contains("DATETIME")
                || dataType.contains("TIMESTAMP")
                || dataType.contains("YEAR")
                || dataType.contains("TIME")) {
            return "java.util.Date";
        } else if (dataType.contains("BIT")) {
            return "java.lang.Boolean";
        } else if (dataType.contains("BLOB")) {
            return "Byte[]";
        } else if (dataType.contains("CLOB")) {
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
        return jdbcType;
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
}
