package com.code4j.pojo;

import com.code4j.config.Code4jConstants;
import org.apache.commons.lang3.StringUtils;

/**
 * @author liu_wp
 * @date 2020/11/20
 * @see
 */
public class TemplateInfo {
    private String templatePath;
    private String templateId;

    public String getTemplatePath() {
        if (StringUtils.isBlank(templatePath)) {
            return templatePath = Code4jConstants.TEMPLATE_PATH;
        }
        return templatePath;
    }

    public void setTemplatePath(final String templatePath) {
        this.templatePath = templatePath;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(final String templateId) {
        this.templateId = templateId;
    }
}
