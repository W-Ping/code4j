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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PipedReader;
import java.util.*;
import java.util.List;
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
    private Component parentComponent;
    /**
     * 选中需要生成的代码的配置项
     */
    private List<CustomJCheckBox> customJCheckBoxes;

    public GenerateCodeAction(Component parentComponent, CustomJFileChooserPanel customJFileChooserPanel, JdbcTableInfo jdbcTableInfo, List<CustomJCheckBox> customJCheckBoxes, JdbcSourceInfo jdbcSourceInfo) {
        this.clearData();
        this.customJFileChooserPanel = customJFileChooserPanel;
        this.customJCheckBoxes = customJCheckBoxes;
        this.jdbcTableInfo = jdbcTableInfo;
        this.jdbcSourceInfo = jdbcSourceInfo;
        this.parentComponent=parentComponent;
    }

    public void clearData() {
        this.templateParamsInfoMap.clear();
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        CustomDialogUtil.confirm(parentComponent, "确认生成代码？", (c) -> {
            this.setTemplateParams();
            String projectPath = customJFileChooserPanel.getSelectValue();
            if (StringUtils.isBlank(projectPath)) {
                CustomDialogUtil.showError("项目地址不能为空");
                return null;
            }
            if (templateParamsInfoMap == null || templateParamsInfoMap.isEmpty()) {
                CustomDialogUtil.showError("生成代码配置不能为空");
                return null;
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
                //表主键
                JdbcMapJavaInfo tablePrimaryKey = baseTemplateInfo.getTablePrimaryKey();
                dataMap.put("tablePK", tablePrimaryKey);
                String codePath = FreemarkerUtil.generateCodeByTemplate(projectPath, mp.getValue(), dataMap);
                GenerateResultInfo generateResultInfo = new GenerateResultInfo(baseTemplateInfo.getTemplateTypeEnum().getTemplateDesc(), StringUtils.isNotBlank(codePath) ? Code4jConstants.SUCCESS : Code4jConstants.FAIL, codePath);
                result.add(generateResultInfo);
                System.out.println("template path:" + codePath);
                //恢复
                baseTemplateInfo.setPackageRoot(null);
            }
            CustomDialogUtil.showGenerateResultDialog(customJFileChooserPanel, "代码生成结果", result);
            return null;
        });
    }

    /**
     * @param baseTemplateInfo
     * @return
     */
    private Map<String, Object> convertMapperTemplateData(BaseTemplateInfo baseTemplateInfo) {
        Map<String, Object> dataMap = new HashMap<>();
        PojoParamsInfo doPojo = (PojoParamsInfo) templateParamsInfoMap.get(TemplateTypeEnum.DO.getTemplateId());
        MapperParamsInfo mapperParamsInfo = (MapperParamsInfo) baseTemplateInfo;
        dataMap.put("baseInfo", mapperParamsInfo);
        dataMap.put("packages", getMapperPackages(mapperParamsInfo, doPojo));
        dataMap.put("apiInfos", mapperParamsInfo.getMapperApiParamsInfos());
        return dataMap;
    }

    private Set<String> getMapperPackages(MapperParamsInfo mapperParamsInfo, PojoParamsInfo doPojo) {
        List<MapperApiParamsInfo> mapperApiParamsInfos = mapperParamsInfo.getMapperApiParamsInfos();
        Set<String> packages = new HashSet<>();
        SuperPojoInfo superPojoInfo = mapperParamsInfo.getSuperPojoInfo();
        if (superPojoInfo != null) {
            packages.add(superPojoInfo.getPackageName());
            if (superPojoInfo.isGenericFlag()) {
                if (doPojo != null) {
                    superPojoInfo.setGenericPojoName(doPojo.getPojoName());
                    superPojoInfo.setGenericPackageName(doPojo.getPackageName());
                    packages.add(superPojoInfo.getGenericPackageName() + "." + superPojoInfo.getGenericPojoName());
                }
            }
        }
        JdbcMapJavaInfo tablePrimaryKey = mapperParamsInfo.getTablePrimaryKey();
        if (tablePrimaryKey != null) {
            packages.add(tablePrimaryKey.getJavaType());
        }
        if (CollectionUtils.isNotEmpty(mapperApiParamsInfos)) {
            String resultTypePath = getTypePath(doPojo.getPackageRoot(), doPojo.getPackageName(), doPojo.getPojoName());
            for (final MapperApiParamsInfo mapperApiParamsInfo : mapperApiParamsInfos) {
                //方法返回结果类型
                if (XmlSqlTemplateEnum.isObjectListResultType(mapperApiParamsInfo.getTemplateId())) {
                    packages.add("java.util.List");
                    packages.add("org.apache.ibatis.annotations.Param");
                    mapperApiParamsInfo.setResultTypePath(resultTypePath);
                    packages.add(mapperApiParamsInfo.getResultTypePath());
                    mapperApiParamsInfo.setResultType(doPojo != null ? "List<" + doPojo.getPojoName() + ">" : "List<Object>");
                } else if (XmlSqlTemplateEnum.isObjectResultType(mapperApiParamsInfo.getTemplateId())) {
                    mapperApiParamsInfo.setResultTypePath(resultTypePath);
                    packages.add(mapperApiParamsInfo.getResultTypePath());
                    mapperApiParamsInfo.setResultType(doPojo != null ? doPojo.getPojoName() : "Object");
                } else if (XmlSqlTemplateEnum.isIntResultType(mapperApiParamsInfo.getTemplateId())) {
                    mapperApiParamsInfo.setResultType("int");
                } else {
                    mapperApiParamsInfo.setResultType("void");
                }
                //方法参数类型
                if (XmlSqlTemplateEnum.isObjectListParameterType(mapperApiParamsInfo.getTemplateId())) {
                    mapperApiParamsInfo.setParameterType(doPojo.getPojoName());
                } else if (XmlSqlTemplateEnum.isObjectParameterType(mapperApiParamsInfo.getTemplateId())) {
                    mapperApiParamsInfo.setParameterType(doPojo.getPojoName());
                } else if (XmlSqlTemplateEnum.isParameterPk(mapperApiParamsInfo.getTemplateId()) && tablePrimaryKey != null) {
                    if (tablePrimaryKey.getJdbcType() != null) {
                        mapperApiParamsInfo.setParameterType(getShortPkJdbcType(tablePrimaryKey.getJavaType()));
                        packages.add(tablePrimaryKey.getJavaType());
                    }
                }
                if (StringUtils.isNotBlank(mapperApiParamsInfo.getParameterTypePath()) && mapperApiParamsInfo.getParameterTypePath().indexOf(".") > 0) {
                    packages.add(mapperApiParamsInfo.getParameterTypePath());
                }
            }
        }
        return packages;
    }

    private String getShortPkJdbcType(String jdbcType) {
        if (StringUtils.isNotBlank(jdbcType) && jdbcType.indexOf(".") > 0) {
            return jdbcType.substring(jdbcType.lastIndexOf(".") + 1);
        }
        return jdbcType;
    }

    /**
     * @param baseTemplateInfo
     * @return
     */
    private Map<String, Object> convertPojoTemplateData(BaseTemplateInfo baseTemplateInfo) {
        PojoParamsInfo pojoParamsInfo = (PojoParamsInfo) baseTemplateInfo;
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("packages", columnPackage(pojoParamsInfo));
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
        dataMap.put("packages", this.serviceColumnPackages(serviceParamsInfo));
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
            MapperParamsInfo mapperParamsInfo = (MapperParamsInfo) mapperPojo;
            xmlParamsInfo.setNamespace(StringUtils.isNotBlank(mapperPojo.getPackageRoot()) ? mapperPojo.getPackageRoot() + mapperPojo.getPackagePath() : mapperPojo.getPackagePath());
            if (mapperParamsInfo.getSuperPojoInfo() != null) {
                xmlParamsInfo.defaultXmlApiParamsInfos();
            }
        }
        if (doPojo != null) {
            xmlParamsInfo.setTableColumnInfos(doPojo.getTableColumnInfos());
            xmlParamsInfo.setResultMapType(StringUtils.isNotBlank(doPojo.getPackageRoot()) ? doPojo.getPackageRoot() + doPojo.getPackagePath() : doPojo.getPackagePath());
        }
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("xmlMap", xmlParamsInfo);
        return dataMap;
    }

    private Set<String> columnPackage(PojoParamsInfo pojoParamsInfo) {
        Set<String> packageList = new HashSet<>();
        SuperPojoInfo superPojoInfo = pojoParamsInfo.getSuperPojoInfo();
        if (superPojoInfo != null) {
            packageList.add(superPojoInfo.getPackageName());
        }
        List<JdbcMapJavaInfo> tableColumnInfos = pojoParamsInfo.getTableColumnInfos();
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
     * @param serviceParamsInfo
     * @return
     */
    private Set<String> serviceColumnPackages(ServiceParamsInfo serviceParamsInfo) {
        Set<String> packages = new HashSet<>();
        InterfaceParamsInfo interfaceParamsInfo = serviceParamsInfo.getInterfaceParamsInfo();
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(interfaceParamsInfo.getPackageRoot())) {
            sb.append(interfaceParamsInfo.getPackageRoot());
            sb.append(".");
        }
        sb.append(interfaceParamsInfo.getPackageName());
        sb.append(".");
        sb.append(interfaceParamsInfo.getPojoName());
        packages.add(sb.toString());
        SuperPojoInfo superPojoInfo = serviceParamsInfo.getSuperPojoInfo();
        if (superPojoInfo != null) {
            packages.add(superPojoInfo.getPackageName());
        }
        return packages;
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
                    //获取父类
                    JTextField superPathT = (JTextField) ((CommonPanel) commonPanel.getComponent(3)).getComponent(1);
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
                        //service impl 目标参数
                        ServiceParamsInfo serviceParamsInfo = new ServiceParamsInfo();
                        serviceParamsInfo.setInterfaceParamsInfo(interfaceParamsInfo);
                        serviceParamsInfo.setPojoPath(interfaceParamsInfo.getPojoPath());
                        if (interfaceParamsInfo.getPojoName().startsWith("I")) {
                            serviceParamsInfo.setPojoName(interfaceParamsInfo.getPojoName().substring(1) + "Impl");
                        } else {
                            serviceParamsInfo.setPojoName(interfaceParamsInfo.getPojoName() + "Impl");
                        }
                        //实现类得包名称
                        serviceParamsInfo.setPackageName(interfaceParamsInfo.getPackageName() + ".impl");
                        serviceParamsInfo.setPojoDesc(templateTypeEnum.getTemplateDesc());
                        serviceParamsInfo.setAuthor(interfaceParamsInfo.getAuthor());
                        serviceParamsInfo.setPackageRoot(interfaceParamsInfo.getPackageRoot());
                        serviceParamsInfo.setSuperPojoInfo(this.getSuperPojoName(superPathT.getText().trim()));
                        TemplateInfo templateInfo = new TemplateInfo();
                        templateInfo.setTemplateId(TemplateTypeEnum.SERVICE.getTemplateId());
                        serviceParamsInfo.setTemplateInfo(templateInfo);
                        serviceParamsInfo.setTemplateTypeEnum(TemplateTypeEnum.SERVICE);
                        this.checkTemplate(serviceParamsInfo);
                        templateParamsInfoMap.put(customJCheckBox.getId(), serviceParamsInfo);
                    } else if (TemplateTypeEnum.MAPPER.getTemplateId().equals(customJCheckBox.getId())) {
                        //mapper模板 参数
                        MapperParamsInfo mapperParamsInfo = (MapperParamsInfo) commonPanel.getBindObject();
                        mapperParamsInfo.setSuperPojoInfo(this.getSuperPojoName(superPathT.getText().trim()));
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
                        templateParamsInfoMap.put(customJCheckBox.getId(), mapperParamsInfo);
                    } else {
                        List<JdbcMapJavaInfo> jdbcMapJavaInfos = (List<JdbcMapJavaInfo>) commonPanel.getBindObject();
                        //do、vo模板参数
                        PojoParamsInfo pojoParamsInfo = new PojoParamsInfo();
                        pojoParamsInfo.setSuperPojoInfo(this.getSuperPojoName(superPathT.getText().trim()));
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

    /**
     * @param superPojo
     * @return
     */
    private SuperPojoInfo getSuperPojoName(String superPojo) {
        if (StringUtils.isNotBlank(superPojo)) {
            SuperPojoInfo superPojoInfo = new SuperPojoInfo();
            if (superPojo.indexOf(".") > 0) {
                String pojoName = superPojo.substring(superPojo.lastIndexOf(".") + 1);
                superPojoInfo.setPojoName(pojoName);
            } else {
                superPojoInfo.setPojoName(superPojo);
            }
            superPojoInfo.setPackageName(superPojo);
            superPojoInfo.setGenericFlag(true);
            return superPojoInfo;
        }
        return null;
    }
}
