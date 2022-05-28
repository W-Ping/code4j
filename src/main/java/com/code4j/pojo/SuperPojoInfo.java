package com.code4j.pojo;

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
     *
     */
    private String genericPojoName;
    /**
     *
     */
    private String genericPackageName;
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
}
