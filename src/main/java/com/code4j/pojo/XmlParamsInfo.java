package com.code4j.pojo;

import com.code4j.config.XmlSqlTemplateEnum;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author liu_wp
 * @date 2020/11/20
 * @see
 */
public class XmlParamsInfo extends BaseTemplateInfo {
    /**
     * mapper 包路径
     */
    private String namespace;
    /**
     * DO 包路径
     */
    private String resultMapType;
    /**
     * 接口参数
     */
    private List<XmlApiParamsInfo> xmlApiParamsInfos;

    public XmlParamsInfo(List<JdbcMapJavaInfo> tableColumnInfos) {
        super(tableColumnInfos);
    }

    /**
     *
     */
    public void defaultXmlApiParamsInfos() {
        if (CollectionUtils.isEmpty(xmlApiParamsInfos)) {
            XmlApiParamsInfo x1 = new XmlApiParamsInfo(XmlSqlTemplateEnum.INSERT.getTemplateId(), XmlSqlTemplateEnum.INSERT.getApiId());
            XmlApiParamsInfo x2 = new XmlApiParamsInfo(XmlSqlTemplateEnum.DELETE.getTemplateId(), XmlSqlTemplateEnum.DELETE.getApiId());
            XmlApiParamsInfo x3 = new XmlApiParamsInfo(XmlSqlTemplateEnum.UPDATE.getTemplateId(), XmlSqlTemplateEnum.UPDATE.getApiId());
            XmlApiParamsInfo x4 = new XmlApiParamsInfo(XmlSqlTemplateEnum.UPDATE_NOT_NULL.getTemplateId(), XmlSqlTemplateEnum.UPDATE_NOT_NULL.getApiId());
            XmlApiParamsInfo x5 = new XmlApiParamsInfo(XmlSqlTemplateEnum.SELECT.getTemplateId(), XmlSqlTemplateEnum.SELECT.getApiId());
            XmlApiParamsInfo x6 = new XmlApiParamsInfo(XmlSqlTemplateEnum.SELECT_ONE.getTemplateId(), XmlSqlTemplateEnum.SELECT_ONE.getApiId());
            XmlApiParamsInfo x7 = new XmlApiParamsInfo(XmlSqlTemplateEnum.INSERT_DUPLICATEKEY.getTemplateId(), XmlSqlTemplateEnum.INSERT_DUPLICATEKEY.getApiId());
            xmlApiParamsInfos = Stream.of(x1, x2, x3, x4, x5, x6, x7).collect(Collectors.toList());
        }
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(final String namespace) {
        this.namespace = namespace;
    }

    public String getResultMapType() {
        return resultMapType;
    }

    public void setResultMapType(final String resultMapType) {
        this.resultMapType = resultMapType;
    }


    public List<XmlApiParamsInfo> getXmlApiParamsInfos() {
        return xmlApiParamsInfos;
    }

    public void setXmlApiParamsInfos(final List<XmlApiParamsInfo> xmlApiParamsInfos) {
        this.xmlApiParamsInfos = xmlApiParamsInfos;
    }
}
