package com.code4j.util;

import com.code4j.config.Code4jConstants;
import com.code4j.enums.DataSourceTypeEnum;
import com.code4j.connect.JDBCService;
import com.code4j.connect.JdbcServiceFactory;
import com.code4j.connect.sqllite.SQLiteJDBCServiceImpl;
import com.code4j.pojo.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static boolean init(boolean forceCreate) {
        return createJDBCSourceTable(forceCreate) && createProjectCodeConfigTable(forceCreate) && createGeneralConfigTable(forceCreate) && checkUpgradeAppVersion();
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
        DbTableInfo c7 = new DbTableInfo("init_db", "VARCHAR(50)", "初始数据库", false, false);
        List<DbTableInfo> tableInfoList = Arrays.asList(c0, c1, c2, c3, c4, c5, c6, c7);
        return sqLiteJDBCService.createTableIfAbsent(JdbcSourceInfo.class, tableInfoList, forceCreate);
    }

    /**
     * @param forceCreate
     * @return
     */
    public static boolean createGeneralConfigTable(boolean forceCreate) {
        DbTableInfo c0 = new DbTableInfo("id", "INTEGER", "索引", true, true);
        DbTableInfo c1 = new DbTableInfo("version", "VARCHAR(20)", "版本信息", false, false);
        DbTableInfo c2 = new DbTableInfo("config_data", "VARCHAR(500)", "配置信息JSON", false, false);
        List<DbTableInfo> tableInfoList = Arrays.asList(c0, c1, c2);
        return SQLiteUtil.getSingleton().createTableIfAbsent(GeneralConfigInfo.class, tableInfoList, forceCreate);
    }

    /**
     * @param forceCreate
     * @return
     */
    public static boolean createProjectCodeConfigTable(boolean forceCreate) {
        final JDBCService sqLiteJDBCService = SQLiteUtil.getSingleton();
        DbTableInfo c0 = new DbTableInfo("id", "INTEGER", "自增ID", true, true);
        DbTableInfo c1 = new DbTableInfo("project_name", "VARCHAR(200)", "项目名称", false, false);
        DbTableInfo c2 = new DbTableInfo("vo_project_name", "VARCHAR(200)", "vo包名称", false, false);
        DbTableInfo c3 = new DbTableInfo("vo_super_class", "VARCHAR(200)", "vo 父类", false, false);
        DbTableInfo c4 = new DbTableInfo("vo_path", "VARCHAR(200)", "vo路径", false, false);
        DbTableInfo c5 = new DbTableInfo("do_path", "VARCHAR(200)", "do路径", false, false);
        DbTableInfo c6 = new DbTableInfo("do_package_name", "VARCHAR(200)", "do包名称", false, false);
        DbTableInfo c7 = new DbTableInfo("do_super_class", "VARCHAR(200)", "do 父类", false, false);
        DbTableInfo c8 = new DbTableInfo("xml_package_name", "VARCHAR(200)", "xml包名称", false, false);
        DbTableInfo c9 = new DbTableInfo("xml_path", "VARCHAR(200)", "xml路径", false, false);
        DbTableInfo c10 = new DbTableInfo("mapper_package_name", "VARCHAR(200)", "mapper包名称", false, false);
        DbTableInfo c11 = new DbTableInfo("mapper_super_class", "VARCHAR(200)", "mapper父类", false, false);
        DbTableInfo c12 = new DbTableInfo("mapper_path", "VARCHAR(200)", "mapper路径", false, false);
        DbTableInfo c13 = new DbTableInfo("service_api_package_name", "VARCHAR(200)", "serviceApi包名称", false, false);
        DbTableInfo c14 = new DbTableInfo("service_api_path", "VARCHAR(200)", "serviceApi路径", false, false);
        DbTableInfo c15 = new DbTableInfo("service_super_class", "VARCHAR(200)", "serviceApi父类", false, false);
        DbTableInfo c16 = new DbTableInfo("service_impl_package_name", "VARCHAR(200)", "serviceImpl包名称", false, false);
        DbTableInfo c17 = new DbTableInfo("service_impl_path", "VARCHAR(200)", "serviceImpl路径", false, false);
        DbTableInfo c18 = new DbTableInfo("service_impl_super_class", "VARCHAR(200)", "serviceImpl父类", false, false);
        DbTableInfo c19 = new DbTableInfo("contr_package_name", "VARCHAR(200)", "controller包名称", false, false);
        DbTableInfo c20 = new DbTableInfo("contr_path", "VARCHAR(200)", "controller路径", false, false);
        DbTableInfo c21 = new DbTableInfo("contr_super_class", "VARCHAR(200)", "controller父类", false, false);
        DbTableInfo c22 = new DbTableInfo("contr_result_class", "VARCHAR(200)", "controller响应", false, false);
        DbTableInfo c23 = new DbTableInfo("do_ig_fields", "VARCHAR(200)", "do过滤字段", false, false);
        DbTableInfo c24 = new DbTableInfo("vo_ig_fields", "VARCHAR(200)", "vo过滤字段", false, false);
        List<DbTableInfo> tableInfoList = Arrays.asList(c0, c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15, c16, c17, c18, c19, c20, c21, c22, c23, c24);
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

    /**
     * 检查升级版本
     */
    public static boolean checkUpgradeAppVersion() {
        GeneralConfigInfo configInfo = new GeneralConfigInfo();
        final List<GeneralConfigInfo> configInfos = SQLiteUtil.select(configInfo);
        if (CollectionUtils.isEmpty(configInfos)) {
            configInfo.setVersion(Code4jConstants.APP_VERSION);
            //初始化通用配置信息
            SQLiteUtil.insertOrUpdate(configInfo);
        } else {
            final GeneralConfigInfo dbInfo = configInfos.get(0);
            if (!Code4jConstants.APP_VERSION.equals(dbInfo.getVersion())) {
                //版本升级需要重新设置
                dbInfo.setVersion(Code4jConstants.APP_VERSION);
                SQLiteUtil.insertOrUpdate(dbInfo);
            }
        }
        return true;
    }

    /**
     * @return
     */
    public static Map<String,String> getCurrGeneralConfigInfo() {
        GeneralConfigInfo configInfo = new GeneralConfigInfo();
        configInfo.setVersion(Code4jConstants.APP_VERSION);
        final List<GeneralConfigInfo> configInfos = SQLiteUtil.select(configInfo);
        if (!CollectionUtils.isEmpty(configInfos)) {
            configInfo = configInfos.get(0);
            final String configData = configInfo.getConfigData();
            if (StringUtils.isNotBlank(configData)) {
                return JSONUtil.jsonToMap(configData);
            }
        }
        return new HashMap(1);
    }
}
