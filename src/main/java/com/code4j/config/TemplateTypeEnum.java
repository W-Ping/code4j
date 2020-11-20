package com.code4j.config;

import org.apache.commons.lang3.StringUtils;

/**
 * @author liu_wp
 * @date Created in 2020/11/19 18:24
 * @see
 */
public enum TemplateTypeEnum {
    DO("DO", "pojo_do.ftl", ".java", "DO 配置"),
    VO("VO", "pojo_vo.ftl", ".java", "VO 配置"),
    DIY("DIY", "pojo_diy.ftl", ".java", "自定义对象配置"),
    XML("XML", "pojo_xml.ftl", ".xml", "xml 配置"),
    MAPPER("MAPPER", "pojo_mapper.ftl", ".java", "mapper 配置"),
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
