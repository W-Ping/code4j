package com.code4j.config;

/**
 * @author liu_wp
 * @date Created in 2020/12/22 15:25
 * @see
 */
public enum ServiceApiTemplateEnum {
    INSERT("1", "insert"),
    INSERT_BATCH("2", "insert batch"),
    DELETE("3", "delete"),
    UPDATE("4", "update"),
    SELECT("5", "select"),
    SELECT_ONE("6", "select one"),
    SELECT_PAGE("7", "page select");
    private String templateId;
    private String templateDesc;

    ServiceApiTemplateEnum(String templateId, String templateDesc) {
        this.templateId = templateId;
        this.templateDesc = templateDesc;
    }

    public String getTemplateId() {
        return templateId;
    }

    public String getTemplateDesc() {
        return templateDesc;
    }
}
