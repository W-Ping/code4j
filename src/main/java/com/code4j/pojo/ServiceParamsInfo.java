package com.code4j.pojo;

/**
 * @author liu_wp
 * @date 2020/11/23
 * @see
 */
public class ServiceParamsInfo extends BaseTemplateInfo {
    /**
     * 父类
     */
    private SuperPojoInfo superPojoInfo;

    /**
     * 接口
     */
    private InterfaceInfo interfaceInfo;

    public SuperPojoInfo getSuperPojoInfo() {
        return superPojoInfo;
    }

    public void setSuperPojoInfo(final SuperPojoInfo superPojoInfo) {
        this.superPojoInfo = superPojoInfo;
    }

    public InterfaceInfo getInterfaceInfo() {
        return interfaceInfo;
    }

    public void setInterfaceInfo(final InterfaceInfo interfaceInfo) {
        this.interfaceInfo = interfaceInfo;
    }
}
