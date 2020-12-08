package com.code4j.config;

import org.apache.commons.lang3.StringUtils;

/**
 * @author liu_wp
 * @date Created in 2020/11/19 18:24
 * @see
 */
public enum TemplateTypeEnum {
    DO("DO", "template_do.ftl", ".java", "DO 配置"),
    VO("VO", "template_vo.ftl", ".java", "VO 配置"),
    XML("XML", "template_xml.ftl", ".xml", "xml 配置"),
    MAPPER("MAPPER", "template_mapper.ftl", ".java", "mapper 配置"),
    SERVICE_API("SERVICE_API", "template_service_api.ftl", ".java", "service API 配置"),
    SERVICE("SERVICE", "template_service.ftl", ".java", "service API 配置"),
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
