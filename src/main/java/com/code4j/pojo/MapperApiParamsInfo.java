package com.code4j.pojo;

import com.code4j.enums.XmlSqlTemplateEnum;

/**
 * @author liu_wp
 * @date 2021/1/14
 * @see
 */
public class MapperApiParamsInfo {
    private String apiId;
    private String templateId;
    private String resultType;
    private String resultTypePath;
    private String parameterType;
    private String parameterTypePath;
    private boolean parameterTypeIsList;
    private boolean pageSelect;
    private boolean parameterTypeIsPk;
    private XmlSqlTemplateEnum xmlSqlTemplateEnum;

    public String getApiId() {
        return apiId;
    }


    public MapperApiParamsInfo(XmlSqlTemplateEnum xmlSqlTemplateEnum) {
        this.xmlSqlTemplateEnum = xmlSqlTemplateEnum;
        this.autoSetTemplate(xmlSqlTemplateEnum);
    }

    public void autoSetTemplate(XmlSqlTemplateEnum xmlSqlTemplateEnum) {
        if (xmlSqlTemplateEnum != null) {
            this.setTemplateId(xmlSqlTemplateEnum.getTemplateId());
            this.setApiId(xmlSqlTemplateEnum.getApiId());
            this.setParameterTypeIsList(XmlSqlTemplateEnum.isObjectListParameterType(xmlSqlTemplateEnum.getTemplateId()));
            this.setParameterTypeIsPk(XmlSqlTemplateEnum.isParameterPk(xmlSqlTemplateEnum.getTemplateId()));
            this.setPageSelect(XmlSqlTemplateEnum.isPageSelect(xmlSqlTemplateEnum.getTemplateId()));
        }
    }

    public void setApiId(final String apiId) {
        this.apiId = apiId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(final String templateId) {
        this.templateId = templateId;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(final String resultType) {
        this.resultType = resultType;
    }

    public String getResultTypePath() {
        return resultTypePath;
    }

    public void setResultTypePath(final String resultTypePath) {
        this.resultTypePath = resultTypePath;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(final String parameterType) {
        this.parameterType = parameterType;
    }

    public String getParameterTypePath() {
        return parameterTypePath;
    }

    public void setParameterTypePath(final String parameterTypePath) {
        this.parameterTypePath = parameterTypePath;
    }

    public boolean isParameterTypeIsList() {
        return parameterTypeIsList;
    }

    public void setParameterTypeIsList(final boolean parameterTypeIsList) {
        this.parameterTypeIsList = parameterTypeIsList;
    }

    public boolean isPageSelect() {
        return pageSelect;
    }

    public void setPageSelect(boolean pageSelect) {
        this.pageSelect = pageSelect;
    }

    public boolean isParameterTypeIsPk() {
        return parameterTypeIsPk;
    }

    public void setParameterTypeIsPk(boolean parameterTypeIsPk) {
        this.parameterTypeIsPk = parameterTypeIsPk;
    }

    public XmlSqlTemplateEnum getXmlSqlTemplateEnum() {
        return xmlSqlTemplateEnum;
    }

    public void setXmlSqlTemplateEnum(XmlSqlTemplateEnum xmlSqlTemplateEnum) {
        this.xmlSqlTemplateEnum = xmlSqlTemplateEnum;
    }
}
