package com.code4j.enums;

import java.util.Arrays;

/**
 * @author liu_wp
 * @date Created in 2020/12/22 15:25
 * @see
 */
public enum XmlSqlTemplateEnum {
    /**
     *
     */
    INSERT("1", "insert", "insertSelective", "int", "object"),
    INSERT_BATCH("2", "insert batch", "insertBatch", "int", "objectList"),
    DELETE("3", "delete", "deleteByPrimaryKey", "int", "pk"),
    UPDATE("4", "update", "updateByPrimaryKey", "int", "object"),
    UPDATE_NOT_NULL("41", "update not null", "updateByPrimaryKeySelective", "int", "object"),
    SELECT("5", "select", "selectByEntity", "objectList", "object"),
    SELECT_ONE("6", "select one", "selectByPrimaryKey", "object", "pk"),
    SELECT_PAGE("7", "page select", "selectPageByObject", "objectList", "object"),
    INSERT_DUPLICATEKEY("8", "insertOrUpdate", "insertOrUpdateSelective", "int", "object"),
    INSERT_DUPLICATEKEY_BATCH("9", "insert duplicateKey", "insertListDuplicateKey", "int", "objectList");

    private String templateId;
    private String templateDesc;
    private String apiId;
    private String resultType;
    private String parameterType;

    XmlSqlTemplateEnum(String templateId, String templateDesc, String apiId, String resultType, String parameterType) {
        this.templateId = templateId;
        this.templateDesc = templateDesc;
        this.apiId = apiId;
        this.resultType = resultType;
        this.parameterType = parameterType;
    }

    public static boolean isIntResultType(String templateId) {
        return Arrays.stream(values()).anyMatch(v -> v.templateId.equals(templateId) && "int".equals(v.resultType));
    }

    public static boolean isObjectResultType(String templateId) {
        return Arrays.stream(values()).anyMatch(v -> v.templateId.equals(templateId) && "object".equals(v.resultType));
    }

    public static boolean isObjectListResultType(String templateId) {
        return Arrays.stream(values()).anyMatch(v -> v.templateId.equals(templateId) && "objectList".equals(v.resultType));
    }

    public static boolean isObjectParameterType(String templateId) {
        return Arrays.stream(values()).anyMatch(v -> v.templateId.equals(templateId) && "object".equals(v.parameterType));
    }

    /**
     * @param templateId
     * @return
     */
    public static boolean isPageSelect(String templateId) {
        return templateId != null && templateId != "" && templateId.equals(SELECT_PAGE.getTemplateId());
    }

    public static boolean isParameterPk(String templateId) {
        return templateId != null && Arrays.stream(values()).anyMatch(v -> v.templateId.equals(templateId) && "pk".equals(v.parameterType));
    }

    public static boolean isObjectListParameterType(String templateId) {
        return Arrays.stream(values()).anyMatch(v -> v.templateId.equals(templateId) && "objectList".equals(v.parameterType));
    }

    public static XmlSqlTemplateEnum getXmlSqlTemplateEnumById(String templateId) {
        for (final XmlSqlTemplateEnum value : values()) {
            if (value.templateId.equals(templateId)) {
                return value;
            }
        }
        return null;
    }

    public String getTemplateId() {
        return templateId;
    }

    public String getTemplateDesc() {
        return templateDesc;
    }

    public String getApiId() {
        return apiId;
    }
}
