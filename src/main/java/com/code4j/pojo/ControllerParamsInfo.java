package com.code4j.pojo;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author lwp
 * @date 2022-06-21
 */
public class ControllerParamsInfo extends BaseTemplateInfo {
    private SuperPojoInfo superPojoInfo;
    /**
     * 引用接口
     */
    private InterfaceParamsInfo interfaceParamsInfo;
    private List<ControllerApiParamsInfo> controllerApiParamsInfos;
    private String rootRequestMapping;
    /**
     *
     */
    private String resultClass;

    public String getResultName() {
        if (StringUtils.isNotBlank(this.resultClass)) {
            return this.resultClass.substring(this.resultClass.lastIndexOf(".") + 1);
        }
        return null;
    }

    public ControllerParamsInfo(List<JdbcMapJavaInfo> tableColumnInfos) {
        super(tableColumnInfos);
    }

    public SuperPojoInfo getSuperPojoInfo() {
        return superPojoInfo;
    }

    public void setSuperPojoInfo(SuperPojoInfo superPojoInfo) {
        this.superPojoInfo = superPojoInfo;
    }

    public List<ControllerApiParamsInfo> getControllerApiParamsInfos() {
        return controllerApiParamsInfos;
    }

    public void setControllerApiParamsInfos(List<ControllerApiParamsInfo> controllerApiParamsInfos) {
        this.controllerApiParamsInfos = controllerApiParamsInfos;
    }

    public String getRootRequestMapping() {
        return rootRequestMapping;
    }

    public void setRootRequestMapping(String rootRequestMapping) {
        this.rootRequestMapping = rootRequestMapping;
    }

    public String getResultClass() {
        return resultClass;
    }

    public void setResultClass(String resultClass) {
        this.resultClass = resultClass;
    }
    public InterfaceParamsInfo getInterfaceParamsInfo() {
        return interfaceParamsInfo;
    }

    public void setInterfaceParamsInfo(InterfaceParamsInfo interfaceParamsInfo) {
        this.interfaceParamsInfo = interfaceParamsInfo;
    }
}
