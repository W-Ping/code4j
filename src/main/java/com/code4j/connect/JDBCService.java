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
     * 测试连接
     *
     * @return
     */
    boolean test();

    /**
     * 获取所有数据库
     *
     * @return
     */
    List<JdbcDbInfo> getAllJdbcDbInfo();

    /**
     * 获取数据库表列表
     *
     * @param jdbcDbInfo
     * @return
     */
    List<JdbcTableInfo> getJdbcTableInfo(JdbcDbInfo jdbcDbInfo);

    /**
     * 获取表元素
     *
     * @param tableName
     * @return
     */
    List<JdbcMapJavaInfo> getTableColumnInfo(String dbName, String tableName);

    /**
     * 检查表是否存在
     *
     * @param tableName
     * @return
     */
    boolean checkTableIsExist(String tableName);

    /**
     * 新建表
     *
     * @param cls          表对应的实体类
     * @param dbTableInfos 表字段信息
     * @param forceCreate  是否强制创建
     * @return
     */
    boolean createTableIfAbsent(Class<T> cls, List<DbTableInfo> dbTableInfos, boolean forceCreate);


    /**
     * 新增
     *
     * @param obj
     * @return
     */
    boolean insert(T obj);

    /**
     * 校验数据唯一性
     *
     * @param obj
     * @param exclude 排除自己
     * @return
     */
    boolean checkUniqueKey(T obj, boolean exclude);

    /**
     * 根据主键修改数据
     *
     * @param obj
     * @return
     */
    boolean updateByPk(T obj);

    /**
     * 根据主键删除数据
     *
     * @param primaryKey
     * @param cls
     * @return
     */
    boolean deleteByPk(Long primaryKey, Class<T> cls);

    /**
     * 查询所有数据
     *
     * @return
     */
    List<T> select(T obj);


}
