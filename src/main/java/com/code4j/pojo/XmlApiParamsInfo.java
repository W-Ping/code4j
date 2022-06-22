package com.code4j.pojo;

import com.code4j.enums.XmlSqlTemplateEnum;

import java.util.List;

/**
 * @author liu_wp
 * @date 2021/1/14
 * @see
 */
public class XmlApiParamsInfo {
    private String apiId;
    private String templateId;
    private String resultMap = "BaseResultMap";
    private String parameterType;
    private List<JdbcMapJavaInfo> xmlApiWhereParamsInfos;
    private boolean isPageSelect;
    private XmlSqlTemplateEnum xmlSqlTemplateEnum;

    public String getApiId() {
        return apiId;
    }


    public XmlApiParamsInfo() {

    }

    public XmlApiParamsInfo(XmlSqlTemplateEnum xmlSqlTemplateEnum) {
        this.xmlSqlTemplateEnum = xmlSqlTemplateEnum;
        if (xmlSqlTemplateEnum != null) {
            this.templateId = xmlSqlTemplateEnum.getTemplateId();
            this.apiId = xmlSqlTemplateEnum.getApiId();
        }
    }
    
    public void setApiId(final String apiId) {
        this.apiId = apiId;
    }


    public String getResultMap() {
        return resultMap;
    }

    public void setResultMap(final String resultMap) {
        this.resultMap = resultMap;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(final String parameterType) {
        this.parameterType = parameterType;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(final String templateId) {
        this.templateId = templateId;
    }

    public List<JdbcMapJavaInfo> getXmlApiWhereParamsInfos() {
        return xmlApiWhereParamsInfos;
    }

    public void setXmlApiWhereParamsInfos(final List<JdbcMapJavaInfo> xmlApiWhereParamsInfos) {
        this.xmlApiWhereParamsInfos = xmlApiWhereParamsInfos;
    }

    public boolean isPageSelect() {
        return isPageSelect;
    }

    public void setPageSelect(final boolean pageSelect) {
        isPageSelect = pageSelect;
    }

    public XmlSqlTemplateEnum getXmlSqlTemplateEnum() {
        return xmlSqlTemplateEnum;
    }

    public void setXmlSqlTemplateEnum(XmlSqlTemplateEnum xmlSqlTemplateEnum) {
        this.xmlSqlTemplateEnum = xmlSqlTemplateEnum;
    }
}
