package com.code4j.pojo;

import com.code4j.annotation.PropertyKeyIndexId;
import com.code4j.config.Code4jConstants;
import com.code4j.config.TemplateTypeEnum;
import com.code4j.util.StrUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * 项目配置信息
 *
 * @author lwp
 * @date 2022-03-12
 */
@PropertyKeyIndexId
public class ProjectCodeConfigInfo extends BaseInfo {
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 包名称
     */
    private String voPackageName;

    /**
     * vo 父类
     */
    private String voSuperClass;
    /**
     * 路径
     */
    private String voPath;
    /**
     * 包名称
     */
    private String doPackageName;

    /**
     * 路径
     */
    private String doPath;

    /**
     * do 父类
     */
    private String doSuperClass;
    /**
     * 包名称
     */
    private String xmlPackageName;

    /**
     * 路径
     */
    private String xmlPath;
    /**
     * 包名称
     */
    private String mapperPackageName;
    /**
     * mapper 父类
     */
    private String mapperSuperClass;
    /**
     * 路径
     */
    private String mapperPath;
    /**
     * 包名称
     */
    private String serviceApiPackageName;

    /**
     * 路径
     */
    private String serviceApiPath;
    /**
     * mapper 父类
     */
    private String serviceSuperClass;

    /**
     * 索引位置
     */
    private Integer index;


    public ProjectCodeConfigInfo() {
        this(null, -99, null, false);
    }

    public ProjectCodeConfigInfo(String tableName) {
        this("--默认配置--", -1, tableName, true);
    }

    public ProjectCodeConfigInfo(boolean isSetDefault) {
        this(null, null, null, isSetDefault);
    }

    public ProjectCodeConfigInfo(String projectName, Integer index, String tableName, boolean isSetDefault) {
        if (isSetDefault) {
            this.voPackageName = BaseTemplateInfo.getDefaultPackageName(TemplateTypeEnum.VO, null);
            this.voPath = Code4jConstants.DEFAULT_PATH;
            this.doPackageName = BaseTemplateInfo.getDefaultPackageName(TemplateTypeEnum.DO, null);
            this.doPath = Code4jConstants.DEFAULT_PATH;
            this.mapperPackageName = BaseTemplateInfo.getDefaultPackageName(TemplateTypeEnum.MAPPER, null);
            this.mapperPath = Code4jConstants.DEFAULT_PATH;
            this.xmlPackageName = BaseTemplateInfo.getDefaultPackageName(TemplateTypeEnum.XML, null);
            this.xmlPath = Code4jConstants.DEFAULT_XML_PATH;
            this.serviceApiPackageName = BaseTemplateInfo.getDefaultPackageName(TemplateTypeEnum.SERVICE_API, null);
            this.serviceApiPath = Code4jConstants.DEFAULT_PATH;
            this.projectName = projectName;
            this.doSuperClass = Code4jConstants.DO_SUPER_CLASS;
            this.voSuperClass = Code4jConstants.VO_SUPER_CLASS;
            this.serviceSuperClass = Code4jConstants.SERVICE_SUPER_CLASS;
            this.mapperSuperClass = Code4jConstants.MAPPER_SUPER_CLASS;
            this.index = index;
        }
    }

    private String getPackageName(String str) {
        if (StringUtils.isNotBlank(str)) {
            if (str.startsWith("t_")) {
                str = StrUtil.subFirstStr(str, "t_");
            }
            return StrUtil.underlineToCamelToLower(str);
        }
        return null;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }


    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getVoPackageName() {
        return voPackageName;
    }

    public void setVoPackageName(String voPackageName) {
        this.voPackageName = voPackageName;
    }

    public String getVoPath() {
        return voPath;
    }

    public void setVoPath(String voPath) {
        this.voPath = voPath;
    }

    public String getDoPackageName() {
        return doPackageName;
    }

    public void setDoPackageName(String doPackageName) {
        this.doPackageName = doPackageName;
    }

    public String getDoPath() {
        return doPath;
    }

    public void setDoPath(String doPath) {
        this.doPath = doPath;
    }

    public String getXmlPackageName() {
        return xmlPackageName;
    }

    public void setXmlPackageName(String xmlPackageName) {
        this.xmlPackageName = xmlPackageName;
    }

    public String getXmlPath() {
        return xmlPath;
    }

    public void setXmlPath(String xmlPath) {
        this.xmlPath = xmlPath;
    }

    public String getMapperPackageName() {
        return mapperPackageName;
    }

    public void setMapperPackageName(String mapperPackageName) {
        this.mapperPackageName = mapperPackageName;
    }

    public String getMapperPath() {
        return mapperPath;
    }

    public void setMapperPath(String mapperPath) {
        this.mapperPath = mapperPath;
    }

    public String getServiceApiPackageName() {
        return serviceApiPackageName;
    }

    public void setServiceApiPackageName(String serviceApiPackageName) {
        this.serviceApiPackageName = serviceApiPackageName;
    }

    public String getServiceApiPath() {
        return serviceApiPath;
    }

    public void setServiceApiPath(String serviceApiPath) {
        this.serviceApiPath = serviceApiPath;
    }

    @Override
    public String toString() {
        return this.getProjectName();
    }

    public String getDoSuperClass() {
        return doSuperClass;
    }

    public void setDoSuperClass(String doSuperClass) {
        this.doSuperClass = doSuperClass;
    }

    public String getMapperSuperClass() {
        return mapperSuperClass;
    }

    public void setMapperSuperClass(String mapperSuperClass) {
        this.mapperSuperClass = mapperSuperClass;
    }

    public String getServiceSuperClass() {
        return serviceSuperClass;
    }

    public void setServiceSuperClass(String serviceSuperClass) {
        this.serviceSuperClass = serviceSuperClass;
    }

    public String getVoSuperClass() {
        return voSuperClass;
    }

    public void setVoSuperClass(String voSuperClass) {
        this.voSuperClass = voSuperClass;
    }
}
