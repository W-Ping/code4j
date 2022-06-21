package com.code4j.pojo;

import java.util.List;

/**
 * @author lwp
 * @date 2022-06-21
 */
public class ControllerParamsInfo extends BaseTemplateInfo {
    private SuperPojoInfo superPojoInfo;
    private List<ControllerApiParamsInfo> controllerApiParamsInfos;
    private String rootRequestMapping;

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
}
