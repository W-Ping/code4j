package com.code4j;

import com.code4j.enums.DataSourceTypeEnum;
import com.code4j.connect.JDBCService;
import com.code4j.pojo.JdbcSourceInfo;
import com.code4j.pojo.ProjectCodeConfigInfo;
import com.code4j.util.JSONUtil;
import com.code4j.util.SQLiteUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author lwp
 * @date 2022-05-20
 */
public class SQLiteTest {

    private static String TABLE_NAME = "jdbc_source_info";
    private static String TABLE_NAME_2 = "project_code_cfg_info";

    @Test
    public void test() {
        final JDBCService sqLiteJDBCService = SQLiteUtil.getSingleton();
        final boolean test = sqLiteJDBCService.test();
        Assert.assertTrue(test);
    }

    @Test
    public void init() {
        final boolean isSuccess1 = SQLiteUtil.createJDBCSourceTable(true);
        final boolean isSuccess2 = SQLiteUtil.createProjectCodeConfigTable(true);
        Assert.assertTrue(isSuccess1 && isSuccess2);
    }

    @Test
    public void createTableIfAbsent() {
        final boolean isSuccess = SQLiteUtil.createJDBCSourceTable(true);
        Assert.assertTrue(isSuccess);
    }

    @Test
    public void checkTableIsExist() {
        final JDBCService sqLiteJDBCService = SQLiteUtil.getSingleton();
        final boolean isSuccess = sqLiteJDBCService.checkTableIsExist(TABLE_NAME);
        Assert.assertTrue(isSuccess);
    }

    @Test
    public void insert() {
        JdbcSourceInfo jdbcSourceInfo = new JdbcSourceInfo();
        jdbcSourceInfo.setUserName("root");
        jdbcSourceInfo.setPassword("root");
        jdbcSourceInfo.setConnectName("测试PG123");
        jdbcSourceInfo.setConnectHost("localhost");
        jdbcSourceInfo.setConnectPort(5432);
        jdbcSourceInfo.setSourceType(DataSourceTypeEnum.POSTGRESQL.typeName());
        final boolean isSuccess = SQLiteUtil.insertOrUpdate(jdbcSourceInfo);
        Assert.assertTrue(isSuccess);
    }

    @Test
    public void select() {
        final List<JdbcSourceInfo> list = SQLiteUtil.select(new JdbcSourceInfo());
        Assert.assertTrue(CollectionUtils.isNotEmpty(list));
        for (JdbcSourceInfo jdbcSourceInfo : list) {
            System.out.println(JSONUtil.Object2JSON(jdbcSourceInfo));
        }
    }

    @Test
    public void deleteByPk() {
        final boolean isSuccess = SQLiteUtil.deleteByPk(3L, JdbcSourceInfo.class);
        Assert.assertTrue(isSuccess);
    }


    @Test
    public void createTableIfAbsent2() {
        final boolean isSuccess = SQLiteUtil.createProjectCodeConfigTable(true);
        Assert.assertTrue(isSuccess);
    }

    @Test
    public void checkTableIsExist2() {
        final JDBCService sqLiteJDBCService = SQLiteUtil.getSingleton();
        final boolean isSuccess = sqLiteJDBCService.checkTableIsExist(TABLE_NAME_2);
        Assert.assertTrue(isSuccess);
    }

    @Test
    public void insert2() {
        ProjectCodeConfigInfo pi = new ProjectCodeConfigInfo();
        pi.setProjectName("测试配置");
        final boolean isSuccess = SQLiteUtil.insertOrUpdate(pi);
        Assert.assertTrue(isSuccess);
    }

    @Test
    public void select2() {
        final List<ProjectCodeConfigInfo> list = SQLiteUtil.select(new ProjectCodeConfigInfo());
        Assert.assertTrue(CollectionUtils.isNotEmpty(list));
        for (ProjectCodeConfigInfo projectCodeConfigInfo : list) {
            System.out.println(JSONUtil.Object2JSON(projectCodeConfigInfo));
        }
    }

    @Test
    public void deleteByPk2() {
        final boolean isSuccess = SQLiteUtil.deleteByPk(1L, ProjectCodeConfigInfo.class);
        Assert.assertTrue(isSuccess);
    }
}
