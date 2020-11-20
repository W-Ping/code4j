package com.code4j.pojo;

import java.util.List;

/**
 * @author liu_wp
 * @date 2020/11/20
 * @see
 */
public class XMLParamsInfo extends BaseTemplateInfo {
    /**
     * mapper 包路径
     */
    private String namespace;
    /**
     * DO 包路径
     */
    private String resultMapType;
    /**
     *
     */
    private List<JdbcMapJavaInfo> tableColumnInfos;

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(final String namespace) {
        this.namespace = namespace;
    }

    public String getResultMapType() {
        return resultMapType;
    }

    public void setResultMapType(final String resultMapType) {
        this.resultMapType = resultMapType;
    }

    public List<JdbcMapJavaInfo> getTableColumnInfos() {
        return tableColumnInfos;
    }

    public void setTableColumnInfos(final List<JdbcMapJavaInfo> tableColumnInfos) {
        this.tableColumnInfos = tableColumnInfos;
    }

}
