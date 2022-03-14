package com.code4j.action;

import com.code4j.component.CustomJCheckBox;
import com.code4j.component.panel.CommonPanel;
import com.code4j.component.panel.CustomJFileChooserPanel;
import com.code4j.config.Code4jConstants;
import com.code4j.config.TemplateTypeEnum;
import com.code4j.config.XmlSqlTemplateEnum;
import com.code4j.exception.Code4jException;
import com.code4j.pojo.*;
import com.code4j.util.CustomDialogUtil;
import com.code4j.util.FreemarkerUtil;
import com.code4j.util.StrUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liu_wp
 * @date 2020/11/19
 * @see
 */
public class GenerateCodeAction implements ActionListener {
    private CustomJFileChooserPanel customJFileChooserPanel;
    private Map<String, BaseTemplateInfo> templateParamsInfoMap = new ConcurrentHashMap<>();
    private JdbcTableInfo jdbcTableInfo;
    private JdbcSourceInfo jdbcSourceInfo;

    /**
     * 选中需要生成的代码的配置项
     */
    private List<CustomJCheckBox> customJCheckBoxes;

    public GenerateCodeAction(CustomJFileChooserPanel customJFileChooserPanel, JdbcTableInfo jdbcTableInfo, List<CustomJCheckBox> customJCheckBoxes, JdbcSourceInfo jdbcSourceInfo) {
        this.clearData();
        this.customJFileChooserPanel = customJFileChooserPanel;
        this.customJCheckBoxes = customJCheckBoxes;
        this.jdbcTableInfo = jdbcTableInfo;
        this.jdbcSourceInfo = jdbcSourceInfo;
    }

    public void clearData() {
        this.templateParamsInfoMap.clear();
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        this.setTemplateParams();
        String projectPath = customJFileChooserPanel.getSelectValue();
        if (StringUtils.isBlank(projectPath)) {
            CustomDialogUtil.showError("项目地址不能为空");
            return;
        }
        if (templateParamsInfoMap == null || templateParamsInfoMap.isEmpty()) {
            CustomDialogUtil.showError("生成代码配置不能为空");
            return;
        }
        List<GenerateResultInfo> result = new ArrayList<>();
        for (Map.Entry<String, ? extends BaseTemplateInfo> mp : templateParamsInfoMap.entrySet()) {
            String templateId = mp.getKey();
            BaseTemplateInfo baseTemplateInfo = mp.getValue();
            Map<String, Object> dataMap;
            if (TemplateTypeEnum.XML.getTemplateId().equals(templateId)) {
                dataMap = this.convertXmlTemplateData(baseTemplateInfo);
            } else {
                if (TemplateTypeEnum.SERVICE_API.getTemplateId().equals(templateId)) {
                    ServiceParamsInfo serviceParamsInfo = (ServiceParamsInfo) baseTemplateInfo;
                    InterfaceParamsInfo interfaceParamsInfo = serviceParamsInfo.getInterfaceParamsInfo();
                    Map<String, Object> interfaceDataMap = this.convertServiceApiPojoTemplateData(interfaceParamsInfo);
                    String interfacePath = FreemarkerUtil.generateCodeByTemplate(projectPath, interfaceParamsInfo, interfaceDataMap);
                    System.out.println("interface path:" + interfacePath);
                    dataMap = convertServicePojoTemplateData(baseTemplateInfo);
                } else if (TemplateTypeEnum.MAPPER.getTemplateId().equals(templateId)) {
                    dataMap = this.convertMapperTemplateData(baseTemplateInfo);
                } else {
                    dataMap = this.convertPojoTemplateData(baseTemplateInfo);
                }
            }
            String codePath = FreemarkerUtil.generateCodeByTemplate(projectPath, mp.getValue(), dataMap);
            GenerateResultInfo generateResultInfo = new GenerateResultInfo(templateId, StringUtils.isNotBlank(codePath) ? Code4jConstants.SUCCESS : Code4jConstants.FAIL, codePath);
            result.add(generateResultInfo);
            System.out.println("template path:" + codePath);
            //恢复
            baseTemplateInfo.setPackageRoot(null);
        }
        CustomDialogUtil.showGenerateResultDialog(customJFileChooserPanel, "代码生成结果", result);
    }

    /**
     * @param baseTemplateInfo
     * @return
     */
    private Map<String, Object> convertMapperTemplateData(BaseTemplateInfo baseTemplateInfo) {
        Map<String, Object> dataMap = new HashMap<>();
        MapperParamsInfo mapperParamsInfo = (MapperParamsInfo) baseTemplateInfo;
        dataMap.put("baseInfo", mapperParamsInfo);
        dataMap.put("apiInfos", mapperParamsInfo.getMapperApiParamsInfos());
        dataMap.put("packages", getMapperPackages(mapperParamsInfo));
        return dataMap;
    }

    private Set<String> getMapperPackages(MapperParamsInfo mapperParamsInfo) {
        List<MapperApiParamsInfo> mapperApiParamsInfos = mapperParamsInfo.getMapperApiParamsInfos();
        if (CollectionUtils.isNotEmpty(mapperApiParamsInfos)) {
            Set<String> packages = new HashSet<>();
            for (final MapperApiParamsInfo mapperApiParamsInfo : mapperApiParamsInfos) {
                if (XmlSqlTemplateEnum.isObjectListResultType(mapperApiParamsInfo.getTemplateId())) {
                    packages.add("java.util.List");
                    packages.add("org.apache.ibatis.annotations.Param");
                    packages.add(mapperApiParamsInfo.getResultTypePath());
                } else if (XmlSqlTemplateEnum.isObjectResultType(mapperApiParamsInfo.getTemplateId())) {
                    packages.add(mapperApiParamsInfo.getResultTypePath());
                }
                if (StringUtils.isNotBlank(mapperApiParamsInfo.getParameterTypePath()) && mapperApiParamsInfo.getParameterTypePath().indexOf(".") > 0) {
                    packages.add(mapperApiParamsInfo.getParameterTypePath());
                }
            }
            return packages;
        }
        return null;
    }

    private Map<String, Object> convertPojoTemplateData(BaseTemplateInfo baseTemplateInfo) {
        PojoParamsInfo pojoParamsInfo = (PojoParamsInfo) baseTemplateInfo;
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("packages", columnPackage(pojoParamsInfo.getTableColumnInfos()));
        dataMap.put("pojoInfo", pojoParamsInfo);
        return dataMap;
    }

    /**
     * @param baseTemplateInfo
     * @return
     */
    private Map<String, Object> convertServicePojoTemplateData(BaseTemplateInfo baseTemplateInfo) {
        ServiceParamsInfo serviceParamsInfo = (ServiceParamsInfo) baseTemplateInfo;
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("pojo", serviceParamsInfo);
        return dataMap;
    }

    /**
     * @param baseTemplateInfo
     * @return
     */
    private Map<String, Object> convertServiceApiPojoTemplateData(BaseTemplateInfo baseTemplateInfo) {
        InterfaceParamsInfo interfaceParamsInfo = (InterfaceParamsInfo) baseTemplateInfo;
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("pojo", interfaceParamsInfo);
        return dataMap;
    }

    /**
     * @param baseTemplateInfo
     * @return
     */
    private Map<String, Object> convertXmlTemplateData(BaseTemplateInfo baseTemplateInfo) {
        BaseTemplateInfo doPojo = templateParamsInfoMap.get(TemplateTypeEnum.DO.getTemplateId());
        BaseTemplateInfo mapperPojo = templateParamsInfoMap.get(TemplateTypeEnum.MAPPER.getTemplateId());
        XmlParamsInfo xmlParamsInfo = (XmlParamsInfo) baseTemplateInfo;

        if (mapperPojo != null) {
            xmlParamsInfo.setNamespace(StringUtils.isNotBlank(mapperPojo.getPackageRoot()) ? mapperPojo.getPackageRoot() + mapperPojo.getPackagePath() : mapperPojo.getPackagePath());
        }
        if (doPojo != null) {
            xmlParamsInfo.setTableColumnInfos(doPojo.getTableColumnInfos());
            xmlParamsInfo.setResultMapType(StringUtils.isNotBlank(doPojo.getPackageRoot()) ? doPojo.getPackageRoot() + doPojo.getPackagePath() : doPojo.getPackagePath());
        }
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("xmlMap", xmlParamsInfo);
        return dataMap;
    }

    private List<String> columnPackage(List<JdbcMapJavaInfo> tableColumnInfos) {
        List<String> packageList = new ArrayList<>();
        for (final JdbcMapJavaInfo tableColumnInfo : tableColumnInfos) {
            if (tableColumnInfo.isIgnore()) {
                continue;
            }
            String javaType = tableColumnInfo.getJavaType();
            if (javaType.contains(".")) {
                if (javaType.startsWith("java.lang")) {
                    continue;
                } else {
                    if (!packageList.contains(javaType)) {
                        packageList.add(javaType);
                    }
                }
            }
        }
        return packageList;
    }

    /**
     * @param baseTemplateInfo
     */
    private void checkTemplate(BaseTemplateInfo baseTemplateInfo) {
        String error = null;
        if (StringUtils.isBlank(baseTemplateInfo.getPojoName())) {
            CustomDialogUtil.showError(error = "【" + baseTemplateInfo.getPojoDesc() + "】名称不能为空");
        } else if (StringUtils.isBlank(baseTemplateInfo.getPackageName())) {
            CustomDialogUtil.showError(error = "【" + baseTemplateInfo.getPojoDesc() + "】包名不能为空");
        } else if (StringUtils.isBlank(baseTemplateInfo.getPojoPath())) {
            CustomDialogUtil.showError(error = "【" + baseTemplateInfo.getPojoDesc() + "】路径不能为空");
        }
        if (StringUtils.isNotBlank(error)) {
            CustomDialogUtil.showError(error);
            throw new Code4jException(error);
        }
    }

    /**
     * 设置模板参数
     */
    private void setTemplateParams() {
        this.clearData();
        for (final CustomJCheckBox customJCheckBox : customJCheckBoxes) {
            if (customJCheckBox.isSelected()) {
                TemplateTypeEnum templateTypeEnum = TemplateTypeEnum.getTemplateTypeEnum(customJCheckBox.getId());
                //绑定的配置容器daoP、voP、xmlP、mapperP、serviceP
                CommonPanel commonPanel = (CommonPanel) customJCheckBox.getBindObject();
                //获取名称
                JTextField packNameT = (JTextField) ((CommonPanel) commonPanel.getComponent(0)).getComponent(1);
                //获取包名
                JTextField packageT = (JTextField) ((CommonPanel) commonPanel.getComponent(1)).getComponent(1);
                //获取路径
                JTextField pojoPathT = (JTextField) ((CommonPanel) commonPanel.getComponent(2)).getComponent(1);
                if (TemplateTypeEnum.XML.getTemplateId().equals(customJCheckBox.getId())) {
                    XmlParamsInfo mapperParamsInfo = (XmlParamsInfo) commonPanel.getBindObject();
                    List<JdbcMapJavaInfo> jdbcMapJavaInfos = mapperParamsInfo.getTableColumnInfos();
                    //xml模板 参数
                    XmlParamsInfo xmlParamsInfo = (XmlParamsInfo) commonPanel.getBindObject();
                    xmlParamsInfo.setPojoType(customJCheckBox.getId());
                    xmlParamsInfo.setPojoName(packNameT.getText());
                    xmlParamsInfo.setPackageName(packageT.getText());
                    xmlParamsInfo.setPojoPath(pojoPathT.getText());
                    xmlParamsInfo.setAuthor(jdbcSourceInfo.getCreator());
                    xmlParamsInfo.setJdbcTableInfo(jdbcTableInfo);
                    xmlParamsInfo.setPojoDesc(TemplateTypeEnum.XML.getTemplateDesc());
                    TemplateInfo templateInfo = new TemplateInfo();
                    templateInfo.setTemplateId(customJCheckBox.getId());
                    xmlParamsInfo.setTemplateInfo(templateInfo);
                    xmlParamsInfo.setTableColumnInfos(jdbcMapJavaInfos);
                    xmlParamsInfo.setPackageRoot(xmlParamsInfo.getUseDefaultPackageRoot(customJFileChooserPanel.getFileName()));
                    xmlParamsInfo.setTemplateTypeEnum(templateTypeEnum);
                    this.checkTemplate(xmlParamsInfo);
                    List<XmlApiParamsInfo> xmlApiParamsInfos = xmlParamsInfo.getXmlApiParamsInfos();
                    if (CollectionUtils.isNotEmpty(xmlApiParamsInfos)) {
                        for (final XmlApiParamsInfo xmlApiParamsInfo : xmlApiParamsInfos) {
                            if (XmlSqlTemplateEnum.isObjectListResultType(xmlApiParamsInfo.getTemplateId()) ||
                                    XmlSqlTemplateEnum.isObjectResultType(xmlApiParamsInfo.getTemplateId())) {
                                xmlApiParamsInfo.setXmlApiWhereParamsInfos(jdbcMapJavaInfos);
                            }
                            //是否分页方法
                            xmlApiParamsInfo.setPageSelect(XmlSqlTemplateEnum.SELECT_PAGE.getTemplateId().equals(xmlApiParamsInfo.getTemplateId()));
                        }
                    }
                    templateParamsInfoMap.put(customJCheckBox.getId(), xmlParamsInfo);
                } else {
                    if (TemplateTypeEnum.SERVICE_API.getTemplateId().equals(customJCheckBox.getId())) {
                        //service api 模板 参数
                        InterfaceParamsInfo interfaceParamsInfo = new InterfaceParamsInfo();
                        interfaceParamsInfo.setPojoName(packNameT.getText());
                        interfaceParamsInfo.setPojoPath(pojoPathT.getText());
                        interfaceParamsInfo.setPackageName(packageT.getText());
                        interfaceParamsInfo.setDefaultPackageName(StrUtil.underlineToCamelToLower(jdbcTableInfo.getTableName()));
                        interfaceParamsInfo.setPackageRoot(interfaceParamsInfo.getUseDefaultPackageRoot(customJFileChooserPanel.getFileName()));
                        interfaceParamsInfo.setAuthor(jdbcSourceInfo.getCreator());
                        TemplateInfo interfaceTp = new TemplateInfo();
                        interfaceTp.setTemplateId(customJCheckBox.getId());
                        interfaceParamsInfo.setTemplateInfo(interfaceTp);
                        ServiceParamsInfo serviceParamsInfo = new ServiceParamsInfo();
                        serviceParamsInfo.setInterfaceParamsInfo(interfaceParamsInfo);
                        serviceParamsInfo.setPojoPath(interfaceParamsInfo.getPojoPath());
                        if (interfaceParamsInfo.getPojoName().startsWith("I")) {
                            serviceParamsInfo.setPojoName(interfaceParamsInfo.getPojoName().substring(1) + "Impl");
                        } else {
                            serviceParamsInfo.setPojoName(interfaceParamsInfo.getPojoName() + "Impl");
                        }
                        serviceParamsInfo.setPackageName(interfaceParamsInfo.getPackageName() + ".impl");
                        serviceParamsInfo.setPojoDesc(templateTypeEnum.getTemplateDesc());
                        serviceParamsInfo.setAuthor(interfaceParamsInfo.getAuthor());
                        serviceParamsInfo.setPackageRoot(interfaceParamsInfo.getPackageRoot());
                        TemplateInfo templateInfo = new TemplateInfo();
                        templateInfo.setTemplateId(TemplateTypeEnum.SERVICE.getTemplateId());
                        serviceParamsInfo.setTemplateInfo(templateInfo);
                        this.checkTemplate(serviceParamsInfo);
                        templateParamsInfoMap.put(customJCheckBox.getId(), serviceParamsInfo);
                    } else if (TemplateTypeEnum.MAPPER.getTemplateId().equals(customJCheckBox.getId())) {
                        //mapper模板 参数
                        MapperParamsInfo mapperParamsInfo = (MapperParamsInfo) commonPanel.getBindObject();
                        mapperParamsInfo.setPojoDesc(templateTypeEnum.getTemplateDesc());
                        mapperParamsInfo.setPojoType(customJCheckBox.getId());
                        mapperParamsInfo.setPackageName(packageT.getText());
                        mapperParamsInfo.setPojoName(packNameT.getText());
                        mapperParamsInfo.setPojoPath(pojoPathT.getText());
                        mapperParamsInfo.setAuthor(jdbcSourceInfo.getCreator());
                        mapperParamsInfo.setJdbcTableInfo(jdbcTableInfo);
                        mapperParamsInfo.setTemplateTypeEnum(templateTypeEnum);
                        TemplateInfo templateInfo = new TemplateInfo();
                        templateInfo.setTemplateId(customJCheckBox.getId());
                        mapperParamsInfo.setTemplateInfo(templateInfo);
                        mapperParamsInfo.setDefaultPackageName(PojoParamsInfo.getDefaultPackageName(templateTypeEnum, StrUtil.underlineToCamelToLower(jdbcTableInfo.getTableName())));
                        mapperParamsInfo.setPackageRoot(mapperParamsInfo.getUseDefaultPackageRoot(customJFileChooserPanel.getFileName()));
                        this.checkTemplate(mapperParamsInfo);
                        List<MapperApiParamsInfo> mapperApiParamsInfos = mapperParamsInfo.getMapperApiParamsInfos();
                        //设置接口参数信息
                        if (CollectionUtils.isNotEmpty(mapperApiParamsInfos)) {
                            //生成DO 则DO配置参数已经存在缓存
                            PojoParamsInfo pojoParamsInfo = (PojoParamsInfo) templateParamsInfoMap.get(TemplateTypeEnum.DO.getTemplateId());
                            //未生成DO 则设置默认对象参数信息
                            if (pojoParamsInfo == null) {
                                pojoParamsInfo = new PojoParamsInfo();
                                pojoParamsInfo.setPojoName(StrUtil.underlineToCamelFirstToUpper(jdbcTableInfo.getTableName()));
                            }
                            String pojoName = pojoParamsInfo.getPojoName().indexOf(".") >= 0 ? StrUtil.subStrLast(pojoParamsInfo.getPojoName(), ".") : pojoParamsInfo.getPojoName();
                            for (final MapperApiParamsInfo mapperApiParamsInfo : mapperApiParamsInfos) {
                                if (XmlSqlTemplateEnum.isObjectListResultType(mapperApiParamsInfo.getTemplateId()) ||
                                        XmlSqlTemplateEnum.isObjectResultType(mapperApiParamsInfo.getTemplateId())) {
                                    String resultTypePath = getTypePath(pojoParamsInfo.getPackageRoot(), pojoParamsInfo.getPackageName(), pojoParamsInfo.getPojoName());
                                    mapperApiParamsInfo.setResultTypePath(resultTypePath);
                                    if (XmlSqlTemplateEnum.isObjectListResultType(mapperApiParamsInfo.getTemplateId())) {
                                        mapperApiParamsInfo.setResultType("List<" + pojoName + ">");
                                    } else {
                                        mapperApiParamsInfo.setResultType(pojoName);
                                    }
                                } else {
                                    mapperApiParamsInfo.setResultType("int");
                                }
                                String parameterTypePath = getTypePath(pojoParamsInfo.getPackageRoot(), pojoParamsInfo.getPackageName(), pojoParamsInfo.getPojoName());
                                mapperApiParamsInfo.setParameterTypePath(parameterTypePath);
                                if (XmlSqlTemplateEnum.isObjectListParameterType(mapperApiParamsInfo.getTemplateId())) {
                                    mapperApiParamsInfo.setParameterType("List<" + pojoName + ">");
                                } else if (XmlSqlTemplateEnum.isObjectParameterType(mapperApiParamsInfo.getTemplateId())) {
                                    mapperApiParamsInfo.setParameterType(pojoName);
                                }
                            }
                        }
                        templateParamsInfoMap.put(customJCheckBox.getId(), mapperParamsInfo);
                    } else {
                        List<JdbcMapJavaInfo> jdbcMapJavaInfos = (List<JdbcMapJavaInfo>) commonPanel.getBindObject();
                        //do、vo模板参数
                        PojoParamsInfo pojoParamsInfo = new PojoParamsInfo();
                        pojoParamsInfo.setPojoDesc(templateTypeEnum.getTemplateDesc());
                        pojoParamsInfo.setPojoType(customJCheckBox.getId());
                        pojoParamsInfo.setPackageName(packageT.getText());
                        pojoParamsInfo.setPojoName(packNameT.getText());
                        pojoParamsInfo.setPojoPath(pojoPathT.getText());
                        pojoParamsInfo.setAuthor(jdbcSourceInfo.getCreator());
                        pojoParamsInfo.setJdbcTableInfo(jdbcTableInfo);
                        pojoParamsInfo.setTemplateTypeEnum(templateTypeEnum);
                        TemplateInfo templateInfo = new TemplateInfo();
                        templateInfo.setTemplateId(customJCheckBox.getId());
                        pojoParamsInfo.setTemplateInfo(templateInfo);
                        pojoParamsInfo.setDefaultPackageName(PojoParamsInfo.getDefaultPackageName(templateTypeEnum, StrUtil.underlineToCamelToLower(jdbcTableInfo.getTableName())));
                        pojoParamsInfo.setPackageRoot(pojoParamsInfo.getUseDefaultPackageRoot(customJFileChooserPanel.getFileName()));
                        pojoParamsInfo.setTableColumnInfos(jdbcMapJavaInfos);
                        this.checkTemplate(pojoParamsInfo);
                        templateParamsInfoMap.put(customJCheckBox.getId(), pojoParamsInfo);
                    }
                }
            } else {
                templateParamsInfoMap.remove(customJCheckBox.getId());
            }
        }
    }

    private String getTypePath(String packageRoot, String packageName, String pojoName) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(packageRoot)) {
            sb.append(packageRoot);
        }
        if (StringUtils.isNotBlank(packageName)) {
            sb.append(packageName);
            sb.append(".");
        }
        sb.append(pojoName);
        return sb.toString();
    }
}
