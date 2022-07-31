package com.code4j.pojo;

import org.apache.commons.lang3.StringUtils;

/**
 * @author liu_wp
 * @date 2020/11/20
 * @see
 */
public class SuperPojoInfo extends BaseTemplateInfo {
    /**
     * 是否为泛型对象
     */
    public boolean genericFlag;
    /**
     * entity 名称
     */
    private String genericPojoName;
    /**
     * entity 包路径
     */
    private String genericPackageName;

    /**
     * Mapper 名称
     */
    private String genericMapperName;
    /**
     * Mapper 包路径
     */
    private String genericMapperPackageName;

    public String getGenericPagePath() {
        if (StringUtils.isNotBlank(this.genericPackageName) && StringUtils.isNotBlank(this.genericPojoName)) {
            StringBuilder sb = new StringBuilder();
            sb.append(this.genericPackageName);
            sb.append(".");
            sb.append(this.genericPojoName);
            return sb.toString();
        }
        return "";
    }

    public String getGenericMapperPagePath() {
        if (StringUtils.isNotBlank(this.genericMapperPackageName) && StringUtils.isNotBlank(this.genericMapperName)) {
            StringBuilder sb = new StringBuilder();
            sb.append(this.genericMapperPackageName);
            sb.append(".");
            sb.append(this.genericMapperName);
            return sb.toString();
        }
        return "";
    }

    public SuperPojoInfo() {
    }

    public SuperPojoInfo(String pojoName, String genericPojoName, String genericPackageName) {
        this(pojoName, genericPojoName, genericPackageName, null, null);
    }

    public SuperPojoInfo(String pojoName, String genericPojoName, String genericPackageName, String genericMapperName, String genericMapperPackageName) {
        super(pojoName);
        this.genericPojoName = genericPojoName;
        this.genericPackageName = genericPackageName;
        this.genericMapperName = genericMapperName;
        this.genericMapperPackageName = genericMapperPackageName;
    }

    public boolean isGenericFlag() {
        return genericFlag;
    }

    public void setGenericFlag(boolean genericFlag) {
        this.genericFlag = genericFlag;
    }

    public String getGenericPojoName() {
        return genericPojoName;
    }

    public void setGenericPojoName(String genericPojoName) {
        this.genericPojoName = genericPojoName;
    }

    public String getGenericPackageName() {
        return genericPackageName;
    }

    public void setGenericPackageName(String genericPackageName) {
        this.genericPackageName = genericPackageName;
    }

    public String getGenericMapperName() {
        return genericMapperName;
    }

    public void setGenericMapperName(String genericMapperName) {
        this.genericMapperName = genericMapperName;
    }

    public String getGenericMapperPackageName() {
        return genericMapperPackageName;
    }

    public void setGenericMapperPackageName(String genericMapperPackageName) {
        this.genericMapperPackageName = genericMapperPackageName;
    }
}
