package com.code4j.connect;

import com.code4j.pojo.DbTableInfo;
import com.code4j.pojo.JdbcDbInfo;
import com.code4j.pojo.JdbcMapJavaInfo;
import com.code4j.pojo.JdbcTableInfo;

import java.util.List;

/**
 * @author liu_wp
 * @date Created in 2020/11/18 18:16
 * @see
 */
public interface JDBCService<T> {
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


    /**
     * @param cls
     * @param dbTableInfos
     * @param forceCreate
     * @return
     */
    boolean createTableIfAbsent(Class<T> cls, List<DbTableInfo> dbTableInfos,boolean forceCreate);


    /**
     * @param obj
     * @return
     */
    boolean insert(T obj);

    /**
     * @param obj
     * @return
     */
    boolean updateByPk(T obj);
    /**
     * @param primaryKey
     * @return
     */
    boolean deleteByPk(Long primaryKey,Class<T> cls);

    /**
     * @return
     */
    List<T> select(T obj);

    /**
     * @param tableName
     * @return
     */
    boolean checkTableIsExist(String tableName);

}
