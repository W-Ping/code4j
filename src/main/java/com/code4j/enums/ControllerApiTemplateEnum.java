package com.code4j.enums;

import java.util.Arrays;

/**
 * @author lwp
 * @date 2022-06-21
 */
public enum ControllerApiTemplateEnum {
    /**
     *
     */
    SAVE("save", "保存", "save", "Boolean","/save", RequestMethod.POST),
    /**
     *
     */
    UPDATE("update", "更新", "update", "Boolean","/update/{id}", RequestMethod.PUT),
    /**
     *
     */
    DELETE("delete", "删除", "delete", "Boolean","/delete/{id}", RequestMethod.DELETE),
    /**
     *
     */
    DETAIL("detail", "详情", "getDetail", "object","get/{id}", RequestMethod.GET),
    /**
     *
     */
    SEARCH("search", "查询", "search", "objectList","/search", RequestMethod.POST),
    /**
     *
     */
    PAGE_SEARCH("page search", "分页查询", "page", "pageObject","/page", RequestMethod.POST),
    ;
    private String templateId;
    private String templateDesc;
    private String api;
    private String requestMapping;
    private RequestMethod requestMethod;
    private String resultType;

    ControllerApiTemplateEnum(String templateId, String templateDesc, String api,String resultType, String requestMapping, RequestMethod requestMethod) {
        this.templateId = templateId;
        this.templateDesc = templateDesc;
        this.api = api;
        this.requestMapping = requestMapping;
        this.resultType = resultType;
        this.requestMethod = requestMethod;
    }

    public static boolean isIdPathVariable(ControllerApiTemplateEnum enu) {
        return enu == UPDATE || enu == DELETE || enu == DETAIL;
    }
    public static boolean isRequestBody(ControllerApiTemplateEnum enu) {
        return enu == UPDATE || enu == SAVE || enu == SEARCH;
    }
    public static boolean isObjectListResultType(String templateId) {
        return Arrays.stream(values()).anyMatch(v -> v.templateId.equals(templateId) && "objectList".equals(v.resultType));
    }
    public static boolean isObjectResultType(String templateId) {
        return Arrays.stream(values()).anyMatch(v -> v.templateId.equals(templateId) && "object".equals(v.resultType));
    }
    public static boolean isBooleanResultType(String templateId) {
        return Arrays.stream(values()).anyMatch(v -> v.templateId.equals(templateId) && "Boolean".equals(v.resultType));
    }
    public static boolean isPageResultType(String templateId) {
        return Arrays.stream(values()).anyMatch(v -> v.templateId.equals(templateId) && "pageObject".equals(v.resultType));
    }
    public static ControllerApiTemplateEnum getControllerApiTemplateEnumById(String templateId) {
        for (final ControllerApiTemplateEnum value : values()) {
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

    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    public String getApi() {
        return api;
    }

    public String getRequestMapping() {
        return requestMapping;
    }
}
