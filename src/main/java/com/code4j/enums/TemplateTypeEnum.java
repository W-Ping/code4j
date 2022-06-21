package com.code4j.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 模板枚举
 *
 * @author liu_wp
 * @date Created in 2020/11/19 18:24
 * @see
 */
public enum TemplateTypeEnum {
    /**
     *
     */
    DO("DO", "tp_do.ftlx", ".java", "Entity 配置"),
    /**
     *
     */
    VO("VO", "tp_vo.ftlx", ".java", "VO 配置"),
    /**
     *
     */
    XML("XML", "tp_xml.ftlx", ".xml", "Xml 配置"),
    /**
     *
     */
    MAPPER("MAPPER", "tp_mapper.ftlx", ".java", "Mapper 配置"),
    /**
     *
     */
    SERVICE_API("SERVICE_API", "tp_service_api.ftlx", ".java", "Service Api 配置"),
    /**
     *
     */
    SERVICE("SERVICE", "tp_service.ftlx", ".java", "Service Impl 配置"),

    /**
     *
     */
    CONTROLLER("CONTROLLER", "tp_controller.ftlx", ".java", "Controller 配置"),
    ;
    private String templateId;
    private String templateName;
    private String fileType;
    private String templateDesc;

    TemplateTypeEnum(String templateId, String templateName, String fileType, String templateDesc) {
        this.templateId = templateId;
        this.templateName = templateName;
        this.fileType = fileType;
        this.templateDesc = templateDesc;
    }

    /**
     * @param templateId
     * @return
     */
    public static TemplateTypeEnum getTemplateTypeEnum(String templateId) {
        if (StringUtils.isBlank(templateId)) {
            return null;
        }
        for (final TemplateTypeEnum value : values()) {
            if (value.getTemplateId().equals(templateId)) {
                return value;
            }
        }
        return null;
    }

    public String getTemplateId() {
        return templateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public String getTemplateDesc() {
        return templateDesc;
    }

    public String getFileType() {
        return fileType;
    }
}
