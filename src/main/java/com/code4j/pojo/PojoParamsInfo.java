package com.code4j.pojo;

/**
 * @author liu_wp
 * @date 2020/11/19
 * @see
 */
public class PojoParamsInfo extends BaseTemplateInfo {

    /**
     * 父类
     */
    private SuperPojoInfo superPojoInfo;

    public SuperPojoInfo getSuperPojoInfo() {
        return superPojoInfo;
    }

    public void setSuperPojoInfo(final SuperPojoInfo superPojoInfo) {
        this.superPojoInfo = superPojoInfo;
    }
}
