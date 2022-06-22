package com.code4j.pojo;

import com.code4j.enums.ControllerApiTemplateEnum;
import com.code4j.enums.RequestMethod;
import com.google.common.base.Objects;

import java.util.Set;

/**
 * @author lwp
 * @date 2022-06-21
 */
public class ControllerApiParamsInfo {
    private String apiId;
    private String apiDesc;
    private String resultType;
    private Set<ParameterInfo> parameterInfos;
    private boolean swagger;
    private RequestMethod requestMethod;
    /**
     *
     */
    private String method;

    private String templateId;
    private ControllerApiTemplateEnum controllerApiTemplateEnum;

    private String requestMapping;

    public ControllerApiParamsInfo(ControllerApiTemplateEnum controllerApiTemplateEnum) {
        this.controllerApiTemplateEnum = controllerApiTemplateEnum;
        if (controllerApiTemplateEnum != null) {
            this.requestMethod = controllerApiTemplateEnum.getRequestMethod();
            this.requestMapping = controllerApiTemplateEnum.getRequestMapping();
            this.templateId = controllerApiTemplateEnum.getTemplateId();
            //拼接成 RequestMethod.POST 格式
            this.method = "RequestMethod." + controllerApiTemplateEnum.getRequestMethod().name();
            this.apiId = controllerApiTemplateEnum.getApi();
            this.apiDesc = controllerApiTemplateEnum.getTemplateDesc();
            this.resultType = "Object";
        }
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getApiDesc() {
        return apiDesc;
    }

    public void setApiDesc(String apiDesc) {
        this.apiDesc = apiDesc;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public boolean isSwagger() {
        return swagger;
    }

    public void setSwagger(boolean swagger) {
        this.swagger = swagger;
    }

    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(RequestMethod requestMethod) {
        this.requestMethod = requestMethod;
    }

    public ControllerApiTemplateEnum getControllerApiTemplateEnum() {
        return controllerApiTemplateEnum;
    }

    public void setControllerApiTemplateEnum(ControllerApiTemplateEnum controllerApiTemplateEnum) {
        this.controllerApiTemplateEnum = controllerApiTemplateEnum;
    }

    public String getRequestMapping() {
        return requestMapping;
    }

    public Set<ParameterInfo> getParameterInfos() {
        return parameterInfos;
    }

    public void setParameterInfos(Set<ParameterInfo> parameterInfos) {
        this.parameterInfos = parameterInfos;
    }

    public void setRequestMapping(String requestMapping) {
        this.requestMapping = requestMapping;
    }

    public static class ParameterInfo {
        private String parameterType;
        private String parameterTypePath;

        private String parameterName;

        private String annotations;

        public String getParameterType() {
            return parameterType;
        }

        public void setParameterType(String parameterType) {
            this.parameterType = parameterType;
        }

        public String getParameterTypePath() {
            return parameterTypePath;
        }

        @Override
        public String toString() {
            return "ParameterInfo{" +
                    "parameterType='" + parameterType + '\'' +
                    ", parameterTypePath='" + parameterTypePath + '\'' +
                    ", parameterName='" + parameterName + '\'' +
                    ", annotations='" + annotations + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            ParameterInfo that = (ParameterInfo) o;
            return Objects.equal(parameterType, that.parameterType) && Objects.equal(parameterTypePath, that.parameterTypePath) && Objects.equal(parameterName, that.parameterName) && Objects.equal(annotations, that.annotations);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(parameterType, parameterTypePath, parameterName, annotations);
        }

        public void setParameterTypePath(String parameterTypePath) {
            this.parameterTypePath = parameterTypePath;
        }

        public String getAnnotations() {
            return annotations;
        }

        public void setAnnotations(String annotations) {
            this.annotations = annotations;
        }

        public String getParameterName() {
            return parameterName;
        }

        public void setParameterName(String parameterName) {
            this.parameterName = parameterName;
        }
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }
}
