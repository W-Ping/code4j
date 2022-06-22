package com.code4j.util;

import com.code4j.enums.DataSourceTypeEnum;
import com.code4j.connect.JDBCService;
import com.code4j.connect.JdbcServiceFactory;
import com.code4j.connect.sqllite.SQLiteJDBCServiceImpl;
import com.code4j.pojo.BaseInfo;
import com.code4j.pojo.DbTableInfo;
import com.code4j.pojo.JdbcSourceInfo;
import com.code4j.pojo.ProjectCodeConfigInfo;

import java.util.Arrays;
import java.util.List;

/**
 * @author lwp
 * @date 2022-05-23
 */
public class SQLiteUtil {
    public static SQLiteJDBCServiceImpl SERVER;

    public static SQLiteJDBCServiceImpl getSingleton() {
        if (SERVER == null) {
            synchronized (SQLiteUtil.class) {
                if (SERVER == null) {
                    JdbcSourceInfo jdbcSourceInfo = new JdbcSourceInfo();
                    jdbcSourceInfo.setUserName("code4j");
                    jdbcSourceInfo.setPassword("code4jPing123");
                    jdbcSourceInfo.setDataSourceTypeEnum(DataSourceTypeEnum.SQLITE);
                    return (SQLiteJDBCServiceImpl) JdbcServiceFactory.getJdbcService(jdbcSourceInfo);
                }
            }
        }
        return null;
    }

    private SQLiteUtil() {
    }

    public static boolean init() {
        return createJDBCSourceTable(false) && createProjectCodeConfigTable(false);
    }

    /**
     * @return
     */
    public static boolean createJDBCSourceTable(boolean forceCreate) {
        final JDBCService sqLiteJDBCService = SQLiteUtil.getSingleton();
        DbTableInfo c0 = new DbTableInfo("id", "INTEGER", "索引", true, true);
        DbTableInfo c1 = new DbTableInfo("connect_name", "VARCHAR(100)", "连接名称", false, false);
        DbTableInfo c2 = new DbTableInfo("connect_host", "VARCHAR(100)", "连接地址", false, false);
        DbTableInfo c3 = new DbTableInfo("connect_port", "INT2", "端口", false, false);
        DbTableInfo c4 = new DbTableInfo("user_name", "VARCHAR(100)", "用户名称", false, false);
        DbTableInfo c5 = new DbTableInfo("password", "VARCHAR(100)", "密码", false, false);
        DbTableInfo c6 = new DbTableInfo("source_type", "VARCHAR(50)", "类型", false, false);
        List<DbTableInfo> tableInfoList = Arrays.asList(c0, c1, c2, c3, c4, c5, c6);
        return sqLiteJDBCService.createTableIfAbsent(JdbcSourceInfo.class, tableInfoList, forceCreate);
    }


    /**
     * @param forceCreate
     * @return
     */
    public static boolean createProjectCodeConfigTable(boolean forceCreate) {
        final JDBCService sqLiteJDBCService = SQLiteUtil.getSingleton();
        DbTableInfo c0 = new DbTableInfo("id", "INTEGER", "自增ID", true, true);
        DbTableInfo c1 = new DbTableInfo("project_name", "VARCHAR(100)", "项目名称", false, false);
        DbTableInfo c2 = new DbTableInfo("vo_project_name", "VARCHAR(100)", "vo包名称", false, false);
        DbTableInfo c3 = new DbTableInfo("vo_super_class", "VARCHAR(100)", "vo 父类", false, false);
        DbTableInfo c4 = new DbTableInfo("vo_path", "VARCHAR(100)", "vo路径", false, false);
        DbTableInfo c5 = new DbTableInfo("do_path", "VARCHAR(100)", "do路径", false, false);
        DbTableInfo c6 = new DbTableInfo("do_package_name", "VARCHAR(100)", "do包名称", false, false);
        DbTableInfo c7 = new DbTableInfo("do_super_class", "VARCHAR(100)", "do 父类", false, false);
        DbTableInfo c8 = new DbTableInfo("xml_package_name", "VARCHAR(100)", "xml包名称", false, false);
        DbTableInfo c9 = new DbTableInfo("xml_path", "VARCHAR(100)", "xml路径", false, false);
        DbTableInfo c10 = new DbTableInfo("mapper_package_name", "VARCHAR(100)", "mapper包名称", false, false);
        DbTableInfo c11 = new DbTableInfo("mapper_super_class", "VARCHAR(100)", "mapper父类", false, false);
        DbTableInfo c12 = new DbTableInfo("mapper_path", "VARCHAR(100)", "mapper路径", false, false);
        DbTableInfo c13 = new DbTableInfo("service_api_package_name", "VARCHAR(100)", "serviceApi包名称", false, false);
        DbTableInfo c14 = new DbTableInfo("service_api_path", "VARCHAR(100)", "serviceApi路径", false, false);
        DbTableInfo c15 = new DbTableInfo("service_super_class", "VARCHAR(100)", "serviceApi父类", false, false);
        DbTableInfo c16 = new DbTableInfo("service_impl_package_name", "VARCHAR(100)", "serviceImpl包名称", false, false);
        DbTableInfo c17 = new DbTableInfo("service_impl_path", "VARCHAR(100)", "serviceImpl路径", false, false);
        DbTableInfo c18 = new DbTableInfo("service_impl_super_class", "VARCHAR(100)", "serviceImpl父类", false, false);
        DbTableInfo c19 = new DbTableInfo("contr_package_name", "VARCHAR(100)", "controller包名称", false, false);
        DbTableInfo c20 = new DbTableInfo("contr_path", "VARCHAR(100)", "controller路径", false, false);
        DbTableInfo c21 = new DbTableInfo("contr_super_class", "VARCHAR(100)", "controller父类", false, false);
        DbTableInfo c22 = new DbTableInfo("contr_result_class", "VARCHAR(100)", "controller响应", false, false);
        List<DbTableInfo> tableInfoList = Arrays.asList(c0, c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15, c16, c17, c18, c19, c20, c21,c22);
        return sqLiteJDBCService.createTableIfAbsent(ProjectCodeConfigInfo.class, tableInfoList, forceCreate);
    }

    /**
     * @param object
     * @param <T>
     * @return
     */
    public static <T extends BaseInfo> List<T> select(T object) {
        return SQLiteUtil.getSingleton().select(object);
    }

    /**
     * @param obj
     * @param <T>
     * @return
     */
    public static <T extends BaseInfo> boolean insertOrUpdate(T obj) {
        if (obj.getId() != null) {
            return SQLiteUtil.getSingleton().updateByPk(obj);
        }
        return SQLiteUtil.getSingleton().insert(obj);
    }

    /**
     * @param obj
     * @param exclude
     * @param <T>
     * @return
     */
    public static <T extends BaseInfo> boolean checkUnique(T obj, boolean exclude) {
        return SQLiteUtil.getSingleton().checkUniqueKey(obj, exclude);
    }

    /**
     * @param pk
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T extends BaseInfo> boolean deleteByPk(Long pk, Class<T> tClass) {
        return SQLiteUtil.getSingleton().deleteByPk(pk, tClass);
    }

}
