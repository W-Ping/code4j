package com.code4j;

import com.code4j.connect.DataSourceTypeEnum;
import com.code4j.connect.JDBCService;
import com.code4j.connect.JdbcServiceFactory;
import com.code4j.pojo.DbTableInfo;
import com.code4j.pojo.JdbcSourceInfo;
import com.code4j.util.JSONUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author lwp
 * @date 2022-05-20
 */
public class SQLiteTest {

    private static String TABLE_NAME = "jdbc_info";

    @Test
    public void test() {
        final JDBCService sqLiteJDBCService = JdbcServiceFactory.getSQLiteJDBCService();
        final boolean test = sqLiteJDBCService.test();
        Assert.assertTrue(test);
    }

    @Test
    public void createTableIfAbsent() {
        final JDBCService sqLiteJDBCService = JdbcServiceFactory.getSQLiteJDBCService();
        DbTableInfo c0 = new DbTableInfo("id", "INTEGER", "索引", true, true);
        DbTableInfo c1 = new DbTableInfo("connect_name", "VARCHAR(100)", "连接名称", false, false);
        DbTableInfo c2 = new DbTableInfo("connect_host", "VARCHAR(100)", "连接地址", false, false);
        DbTableInfo c3 = new DbTableInfo("connect_port", "INT2", "端口", false, false);
        DbTableInfo c4 = new DbTableInfo("user_name", "VARCHAR(100)", "用户名称", false, false);
        DbTableInfo c5 = new DbTableInfo("password", "VARCHAR(100)", "密码", false, false);
        DbTableInfo c6 = new DbTableInfo("source_type", "VARCHAR(50)", "类型", false, false);
        List<DbTableInfo> tableInfoList = Arrays.asList(c0, c1, c2, c3, c4, c5, c6);
        final boolean isSuccess = sqLiteJDBCService.createTableIfAbsent(TABLE_NAME, tableInfoList, true);
        Assert.assertTrue(isSuccess);
    }

    @Test
    public void checkTableIsExist() {
        final JDBCService sqLiteJDBCService = JdbcServiceFactory.getSQLiteJDBCService();
        final boolean isSuccess = sqLiteJDBCService.checkTableIsExist(TABLE_NAME);
        Assert.assertTrue(isSuccess);
    }

    @Test
    public void insert() {
        final JDBCService sqLiteJDBCService = JdbcServiceFactory.getSQLiteJDBCService();
        JdbcSourceInfo jdbcSourceInfo = new JdbcSourceInfo();
        jdbcSourceInfo.setUserName("root");
        jdbcSourceInfo.setPassword("root");
        jdbcSourceInfo.setConnectName("测试PG123");
        jdbcSourceInfo.setConnectHost("localhost");
        jdbcSourceInfo.setConnectPort(5432);
        jdbcSourceInfo.setSourceType(DataSourceTypeEnum.POSTGRESQL.typeName());
        final boolean isSuccess = sqLiteJDBCService.insert(jdbcSourceInfo);
        Assert.assertTrue(isSuccess);
    }

    @Test
    public void select() {
        final JDBCService sqLiteJDBCService = JdbcServiceFactory.getSQLiteJDBCService();
        JdbcSourceInfo condition = new JdbcSourceInfo();
        final List<JdbcSourceInfo> list = sqLiteJDBCService.select(condition);
        Assert.assertTrue(CollectionUtils.isNotEmpty(list));
        for (JdbcSourceInfo jdbcSourceInfo : list) {
            System.out.println(JSONUtil.Object2JSON(jdbcSourceInfo));
        }
    }

    @Test
    public void deleteByPk() {
        final JDBCService sqLiteJDBCService = JdbcServiceFactory.getSQLiteJDBCService();
        final boolean isSuccess = sqLiteJDBCService.deleteByPk(1L, JdbcSourceInfo.class);
        Assert.assertTrue(isSuccess);
    }

}
