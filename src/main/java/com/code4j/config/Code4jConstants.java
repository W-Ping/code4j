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

    public static Dimension FROM_MIN_DEFAULT_SIZE = new Dimension(1050, 620);
    public static Dimension TOP_MIN_DEFAULT_SIZE = new Dimension(900, 25);
    public static Dimension LEFT_MIN_DEFAULT_SIZE = new Dimension(200, 530);
    public static Dimension RIGHT_MIN_DEFAULT_SIZE = new Dimension(830, 530);
    public static Dimension BOTTOM_MIN_DEFAULT_SIZE = new Dimension(900, 15);
    public final static String DEFAULT_DB_CONFIG_FILE_NAME = "code4j_db.properties";
    public final static String DEFAULT_DB_CONFIG_KEY_PREFIX = "dataSource";
    public final static String DEFAULT_ROOT_PACKAGE = "com";
    public final static String PROJECT_ROOT_PATH = System.getProperty("user.dir");
    public final static String SYS_TEMP_PATH = System.getProperty("java.io.tmpdir");

    public final static String DO_SUFFIX = "DO";
    public final static String VO_SUFFIX = "VO";
    public final static String MAPPER_SUFFIX = "Mapper";
    public final static String SERVICE_SUFFIX = "Service";


    public final static String DEFAULT_PATH = "src/main/java";
    public final static String DEFAULT_XML_PATH = "src/main/resources";
    public final static String DEFAULT_MAPPER_PACKAGE = "mapper";
    public final static String DEFAULT_SQL_XML_PACKAGE = "sqlmap";
    public final static String DEFAULT_VO_PACKAGE = "pojo.vo";
    public final static String DEFAULT_DO_PACKAGE = "pojo.dao";

    public static final String TEMPLATE_PATH = "src/main/java/com/code4j/templates";

    /**
     * 选择的项目地址
     */
    public static File CACHE_PROJECT_SELECTED_FILE = new File("");
}
