package com.code4j.pojo;

import com.code4j.annotation.PropertyKeyIndexId;
import com.code4j.config.Code4jConstants;

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
    ;
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
     * 索引位置
     */
    private Integer index;



    public ProjectCodeConfigInfo() {
        this(null, null);
    }

    public ProjectCodeConfigInfo(String projectName, Integer index) {
        this.voPackageName = Code4jConstants.DEFAULT_VO_PACKAGE;
        this.voPath = Code4jConstants.DEFAULT_PATH;
        this.doPackageName = Code4jConstants.DEFAULT_DO_PACKAGE;
        this.doPath = Code4jConstants.DEFAULT_PATH;
        this.mapperPackageName = Code4jConstants.DEFAULT_MAPPER_PACKAGE;
        this.mapperPath = Code4jConstants.DEFAULT_PATH;
        this.xmlPackageName = Code4jConstants.DEFAULT_SQL_XML_PACKAGE;
        this.xmlPath = Code4jConstants.DEFAULT_XML_PATH;
        this.serviceApiPackageName = Code4jConstants.DEFAULT_SERVICE_PACKAGE;
        this.serviceApiPath = Code4jConstants.DEFAULT_PATH;
        this.projectName = projectName;
        this.index = index;
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
}
