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
    private InterfaceParamsInfo interfaceParamsInfo;

    public SuperPojoInfo getSuperPojoInfo() {
        return superPojoInfo;
    }

    public void setSuperPojoInfo(final SuperPojoInfo superPojoInfo) {
        this.superPojoInfo = superPojoInfo;
    }

    public InterfaceParamsInfo getInterfaceParamsInfo() {
        return interfaceParamsInfo;
    }

    public void setInterfaceParamsInfo(final InterfaceParamsInfo interfaceParamsInfo) {
        this.interfaceParamsInfo = interfaceParamsInfo;
    }
}
