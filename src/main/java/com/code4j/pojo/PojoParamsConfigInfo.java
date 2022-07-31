package com.code4j.pojo;

import java.util.List;

/**
 * @author lwp
 * @date 2022-07-29
 */
public class PojoParamsConfigInfo {
    private List<JdbcMapJavaInfo> tableColumnInfos;
    private ProjectCodeConfigInfo projectCodeConfigInfo;

    public PojoParamsConfigInfo(List<JdbcMapJavaInfo> tableColumnInfos, ProjectCodeConfigInfo projectCodeConfigInfo) {
        this.tableColumnInfos = tableColumnInfos;
        this.projectCodeConfigInfo = projectCodeConfigInfo;
    }

    public List<JdbcMapJavaInfo> getTableColumnInfos() {
        return tableColumnInfos;
    }

    public void setTableColumnInfos(List<JdbcMapJavaInfo> tableColumnInfos) {
        this.tableColumnInfos = tableColumnInfos;
    }

    public ProjectCodeConfigInfo getProjectCodeConfigInfo() {
        return projectCodeConfigInfo;
    }

    public void setProjectCodeConfigInfo(ProjectCodeConfigInfo projectCodeConfigInfo) {
        this.projectCodeConfigInfo = projectCodeConfigInfo;
    }
}
