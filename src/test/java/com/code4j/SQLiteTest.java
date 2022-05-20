package com.code4j;

import com.code4j.connect.JDBCService;
import com.code4j.connect.JdbcServiceFactory;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author lwp
 * @date 2022-05-20
 */
public class SQLiteTest {
    @Test
    public void test(){
        final JDBCService sqLiteJDBCService = JdbcServiceFactory.getSQLiteJDBCService();
        final boolean test = sqLiteJDBCService.test();
        Assert.assertTrue(test);
    }
    @Test
    public void createTable(){
        final JDBCService sqLiteJDBCService = JdbcServiceFactory.getSQLiteJDBCService();
        final boolean test = sqLiteJDBCService.createTableIfAbsent("jdbc_info","CREATE TABLE jdbc_info(id VARCHAR(50) PRIMARY KEY, name VARCHAR(50) NOT NULL, sex VARCHAR(50) NOT NULL)");
        Assert.assertTrue(test);
    }
}
