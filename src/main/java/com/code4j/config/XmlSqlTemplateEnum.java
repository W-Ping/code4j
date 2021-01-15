package com.code4j.config;

import java.util.Arrays;

/**
 * @author liu_wp
 * @date Created in 2020/12/22 15:25
 * @see
 */
public enum XmlSqlTemplateEnum {
    INSERT("1", "insert ", "insertOne", "int","object"),
    INSERT_BATCH("2", "insert batch", "insertList", "int","objectList"),
    DELETE("3", "delete", "deleteByObject", "int","object"),
    UPDATE("4", "update", "updateByObject", "int","object"),
    SELECT("5", "select", "selectByObject", "objectList","object"),
    SELECT_ONE("6", "select one", "selectOne", "object","object"),
    SELECT_PAGE("7", "page select", "selectPageByObject", "objectList","object"),
    INSERT_DUPLICATEKEY("8", "insert duplicateKey", "insertDuplicateKey", "int","object"),
    INSERT_DUPLICATEKEY_BATCH("9", "insert duplicateKey", "insertListDuplicateKey", "int","objectList");
    private String templateId;
    private String apiId;
    private String templateDesc;
    private String resultType;
    private String parameterType;

    XmlSqlTemplateEnum(String templateId, String templateDesc, String apiId, String resultType,String parameterType) {
        this.templateId = templateId;
        this.templateDesc = templateDesc;
        this.apiId = apiId;
        this.resultType = resultType;
        this.parameterType = parameterType;
    }

    public static boolean isIntResultType(String templateId) {
        return Arrays.stream(values()).anyMatch(v -> v.templateId.equals(templateId) && v.resultType.equals("int"));
    }

    public static boolean isObjectResultType(String templateId) {
        return Arrays.stream(values()).anyMatch(v -> v.templateId.equals(templateId) && v.resultType.equals("object"));
    }

    public static boolean isObjectListResultType(String templateId) {
        return Arrays.stream(values()).anyMatch(v -> v.templateId.equals(templateId) && v.resultType.equals("objectList"));
    }
    public static boolean isObjectParameterType(String templateId) {
        return Arrays.stream(values()).anyMatch(v -> v.templateId.equals(templateId) && v.parameterType.equals("object"));
    }

    public static boolean isObjectListParameterType(String templateId) {
        return Arrays.stream(values()).anyMatch(v -> v.templateId.equals(templateId) && v.parameterType.equals("objectList"));
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
