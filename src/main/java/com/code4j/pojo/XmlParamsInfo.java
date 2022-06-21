package com.code4j.pojo;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

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
    public void defaultXmlApiParamsInfos(List<MapperApiParamsInfo> mapperApiParamsInfos) {
        if (CollectionUtils.isEmpty(xmlApiParamsInfos) && CollectionUtils.isNotEmpty(mapperApiParamsInfos)) {
            xmlApiParamsInfos = new ArrayList<>();
            for (MapperApiParamsInfo mapperApiParamsInfo : mapperApiParamsInfos) {
                xmlApiParamsInfos.add(new XmlApiParamsInfo(mapperApiParamsInfo.getXmlSqlTemplateEnum()));
            }
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
