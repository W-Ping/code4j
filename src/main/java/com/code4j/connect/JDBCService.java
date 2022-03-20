package com.code4j.connect;

import com.code4j.pojo.JdbcDbInfo;
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
    List<JdbcDbInfo> getAllJdbcDbInfo();

    /**
     * @param jdbcDbInfo
     * @return
     */
    List<JdbcTableInfo> getJdbcTableInfo(JdbcDbInfo jdbcDbInfo);

    /**
     * @param tableName
     * @return
     */
    List<JdbcMapJavaInfo> getTableColumnInfo(String dbName, String tableName);

}
