package com.code4j.config;

import java.awt.*;
import java.io.File;

/**
 * @author liu_wp
 * @date 2020/11/17
 * @see
 */
public class Code4jConstants {
    public static String APP_VERSION = "V1.0.0";
    public static String SUCCESS = "success";
    public static String FAIL = "fail";
    public static String APPLICATION_JAR="code4j-v1.jar";
    public static Dimension FROM_MIN_DEFAULT_SIZE = new Dimension(1250, 650);
    public static Dimension TOP_MIN_DEFAULT_SIZE = new Dimension(1250, 25);
    public static Dimension LEFT_MIN_DEFAULT_SIZE = new Dimension(200, 530);
    public static Dimension RIGHT_MIN_DEFAULT_SIZE = new Dimension(1030, 530);
    public static Dimension BOTTOM_MIN_DEFAULT_SIZE = new Dimension(1250, 25);

    /**
     * 数据连接配置文件
     */
    public final static String DEFAULT_DB_CONFIG_FILE_NAME = "code4j_db.properties";
    public final static String DEFAULT_DB_CONFIG_KEY_PREFIX = "dataSource";
    /**
     * 项目配置文件
     */
    public final static String DEFAULT_PROJECT_CONFIG_FILE_NAME = "code4j_config.properties";
    public final static String DEFAULT_DB_PROJECT_CONFIG_KEY_PREFIX = "PRConfig";

    public final static String DEFAULT_ROOT_PACKAGE = "com";
    public final static String PROJECT_ROOT_PATH = System.getProperty("user.dir");
    public final static String SYS_TEMP_PATH = System.getProperty("java.io.tmpdir");

    public final static String DO_SUFFIX = "Entity";
    public final static String VO_SUFFIX = "Vo";
    public final static String MAPPER_SUFFIX = "Dao";
    public final static String XML_SUFFIX = "Mapper";
    public final static String SERVICE_SUFFIX = "Service";


    public final static String DEFAULT_PATH = "src/main/java";
    public final static String DEFAULT_XML_PATH = "src/main/resources";
    public final static String DEFAULT_MAPPER_PACKAGE = "com.ebx.queen.dao";
    public final static String DEFAULT_SQL_XML_PACKAGE = "mapping.mysql";
    public final static String DEFAULT_VO_PACKAGE = "com.ebx.skeleton.dto.response";
    public final static String DEFAULT_DO_PACKAGE = "com.ebx.skeleton.entity";
    public final static String DEFAULT_SERVICE_PACKAGE = "com.ebx.skeleton.interfaces";

    public static final String TEMPLATE_PATH = "src/main/java/com/code4j/templates";

    public static final String MAPPER_SUPER_CLASS="com.ks.skeleton.dao.itf.IBaseDao";
    public static final String DO_SUPER_CLASS="com.ks.skeleton.entity.base.BaseEntity";
    public static final String VO_SUPER_CLASS="com.ebx.skeleton.dto.response.BaseVo";
    public static final String SERVICE_SUPER_CLASS="com.ks.framework.service.BaseService";

    public static final  String CONFIG_NAME="新建项目配置";

    /**
     * 选择的项目地址
     */
    public static File CACHE_PROJECT_SELECTED_FILE = new File("");
}
