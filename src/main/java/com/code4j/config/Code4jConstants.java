package com.code4j.config;

import java.awt.*;
import java.io.File;

/**
 * @author liu_wp
 * @date 2020/11/17
 * @see
 */
public class Code4jConstants {
    public final static String APP_VERSION = "V1.1.1";
    public final static String GITEE_URL = "https://gitee.com/LW_Ping/code4j";
    public final static String WEB_DOC_URL = "https://w-ping.github.io/code4j/";
    /**
     * 作者 key
     */
    public final static String AUTHOR_KEY = "author";
    /**
     * 项目配置 key
     */
    public final static String DEFAULT_SELECTED_CONFIG_KEY = "config";
    /**
     * 项目地址 key
     */
    public final static String DEFAULT_PATH_KEY = "path";
    /**
     * 默认作者
     */
    public final static String DEFAULT_AUTHOR = "code4j";
    public final static String SUCCESS = "success";
    public final static String FAIL = "fail";
    public final static String APPLICATION_JAR = "code4j-v1.jar";
    public final static String SYS_ICON = "images/sys_icon.png";
    public static Dimension FROM_MIN_DEFAULT_SIZE = new Dimension(1270, 770);
    public static Dimension TOP_MIN_DEFAULT_SIZE = new Dimension(1270, 25);
    public static Dimension LEFT_MIN_DEFAULT_SIZE = new Dimension(200, 720);
    public static Dimension RIGHT_MIN_DEFAULT_SIZE = new Dimension(1050, 720);
    public static Dimension BOTTOM_MIN_DEFAULT_SIZE = new Dimension(1270, 20);

    /**
     * 数据连接配置文件
     */
    @Deprecated
    public final static String DEFAULT_DB_CONFIG_FILE_NAME = "code4j_db.properties";
    @Deprecated
    public final static String DEFAULT_DB_CONFIG_KEY_PREFIX = "dataSource";
    /**
     * 项目配置文件
     */
    @Deprecated
    public final static String DEFAULT_PROJECT_CONFIG_FILE_NAME = "code4j_config.properties";
    @Deprecated
    public final static String DEFAULT_DB_PROJECT_CONFIG_KEY_PREFIX = "PRConfig";

    public final static String DEFAULT_ROOT_PACKAGE = "com";
    public final static String PROJECT_ROOT_PATH = System.getProperty("user.dir");
    public final static String SYS_TEMP_PATH = System.getProperty("java.io.tmpdir");

    public final static String DO_SUFFIX = "Entity";
    public final static String VO_SUFFIX = "VO";
    public final static String MAPPER_SUFFIX = "Mapper";
    public final static String XML_SUFFIX = "Mapper";
    public final static String SERVICE_SUFFIX = "Service";
    public final static String CONTROLLER_SUFFIX = "Controller";


    public final static String DEFAULT_PATH = "src/main/java";
    public final static String DEFAULT_XML_PATH = "src/main/resources";
    public final static String DEFAULT_MAPPER_PACKAGE = "com.code4j.mapper";
    public final static String DEFAULT_SQL_XML_PACKAGE = "mapping.mysql";
    public final static String DEFAULT_VO_PACKAGE = "com.code4j.pojo.vo";
    public final static String DEFAULT_DO_PACKAGE = "com.code4j.pojo.entity";
    public final static String DEFAULT_SERVICE_PACKAGE = "com.code4j.service";
    public final static String DEFAULT_SERVICE_IMPL_PACKAGE = "com.code4j.service.impl";

    public final static String DEFAULT_CONTROLLER_PACKAGE = "com.code4j.controller";
    public final static String DEFAULT_PAGEINFO_PACKAGE = "com.code4j.PageInfo";

    public static final String TEMPLATE_PATH = "src/main/java/com/code4j/templates";

    public static final String MAPPER_SUPER_CLASS = "com.code4j.base.IBaseMapper";
    public static final String DO_SUPER_CLASS = "com.code4j.pojo.base.BaseEntity";
    public static final String VO_SUPER_CLASS = "com.code4j.pojo.base.BaseVO";
    public static final String SERVICE_SUPER_CLASS = "com.code4j.service.IBaseService";

    public static final String CONTROLLER_SUPER_CLASS = "com.code4j.controller.BaseController";
    public static final String CONTROLLER_RESULT_CLASS = "com.code4j.vo.Result";

    public static final String SERVICE_SUPER_IMPL_CLASS = "com.code4j.service.impl.BaseService";

    public static final String CONFIG_NAME = "新建";
    public static Color selectionBackground = new Color(0x3992EA);
    public static Color selectionForeground = Color.WHITE;
    /**
     * 选择的项目地址
     */
    public static File CACHE_PROJECT_SELECTED_FILE = new File("");

    public static final String SQLITE_DEFAULT_NAME = "code4j_sb";
    public static final String SQLITE_DEFAULT_PWD = "code4j_sb@";
    public static final String SQLITE_DEFAULT_DB = "code4j";

}
