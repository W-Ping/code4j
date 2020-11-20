package com.code4j.connect;

import com.code4j.pojo.JdbcMapJavaInfo;
import com.code4j.pojo.JdbcTableInfo;

import java.util.List;

/**
 * @author liu_wp
 * @date Created in 2020/11/18 18:16
 * @see
 */
public interface JDBCService {
    /**
     * @return
     */
    boolean test();

    /**
     * @return
     */
    List<String> getAllDbName();

    /**
     * @param dbName
     * @return
     */
    List<JdbcTableInfo> getJdbcTableInfo(String dbName);

    /**
     * @param tableName
     * @return
     */
    List<JdbcMapJavaInfo> getTableColumnInfo(String dbName, String tableName);
}
