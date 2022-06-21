package com.code4j.action;

import com.code4j.component.CustomJCheckBox;
import com.code4j.component.CustomJTextField;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liu_wp
 * @date 2020/11/19
 * @see
 */
public class GenerateCodeAction implements ActionListener {
    private static final Logger log = LoggerFactory.getLogger(GenerateCodeAction.class);
    private CustomJFileChooserPanel customJFileChooserPanel;
    private Map<String, BaseTemplateInfo> templateParamsInfoMap = new ConcurrentHashMap<>();
    private JdbcTableInfo jdbcTableInfo;
    private JdbcSourceInfo jdbcSourceInfo;
    private Component parentComponent;
    /**
     * 选中需要生成的代码的配置项
     */
    private List<CustomJCheckBox> customJCheckBoxes;
    private CustomJCheckBox lombok;
    private CustomJCheckBox swagger;
    private CustomJCheckBox mybatisPlus;

    public GenerateCodeAction(Component parentComponent, CustomJFileChooserPanel customJFileChooserPanel, JdbcTableInfo jdbcTableInfo, List<CustomJCheckBox> customJCheckBoxes, JdbcSourceInfo jdbcSourceInfo, CustomJCheckBox lombok, CustomJCheckBox mybatisPlus, CustomJCheckBox swagger) {
        this.clearData();
        this.customJFileChooserPanel = customJFileChooserPanel;
        this.customJCheckBoxes = customJCheckBoxes;
        this.lombok = lombok;
        this.swagger = swagger;
        this.mybatisPlus = mybatisPlus;
        this.jdbcTableInfo = jdbcTableInfo;
        this.jdbcSourceInfo = jdbcSourceInfo;
        this.parentComponent = parentComponent;
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
                        log.info("interface path:【{}】", interfacePath);
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
                log.info("template path:【{}】", codePath);
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
        Map<String, Object> dataMap = new HashMap<>(3);
        PojoParamsInfo doPojo = (PojoParamsInfo) templateParamsInfoMap.get(TemplateTypeEnum.DO.getTemplateId());
        MapperParamsInfo mapperParamsInfo = (MapperParamsInfo) baseTemplateInfo;
        List<MapperApiParamsInfo> mapperApiParamsInfos = mapperParamsInfo.getMapperApiParamsInfos();
        dataMap.put("baseInfo", mapperParamsInfo);
        dataMap.put("packages", getMapperPackages(mapperParamsInfo, doPojo, mapperApiParamsInfos));
        dataMap.put("apiInfos", mybatisPlus.isSelected() ? null : mapperApiParamsInfos);
        //还原选择
        mapperParamsInfo.setMapperApiParamsInfos(null);
        return dataMap;
    }

    private Set<String> getMapperPackages(MapperParamsInfo mapperParamsInfo, PojoParamsInfo doPojo, List<MapperApiParamsInfo> mapperApiParamsInfos) {
        Set<String> packages = new HashSet<>();
        SuperPojoInfo superPojoInfo = mapperParamsInfo.getSuperPojoInfo();
        Set<String> superPackages = this.getSuperPojoInfoPackages(superPojoInfo, doPojo, mapperParamsInfo, mybatisPlus.isSelected(), null);
        if (!CollectionUtils.isEmpty(superPackages)) {
            packages.addAll(superPackages);
        }
        JdbcMapJavaInfo tablePrimaryKey = mapperParamsInfo.getTablePrimaryKey();
        if (tablePrimaryKey != null) {
            packages.add(tablePrimaryKey.getJavaType());
        }
        if (CollectionUtils.isNotEmpty(mapperApiParamsInfos) && !mybatisPlus.isSelected()) {
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
        Map<String, Object> dataMap = new HashMap<>(5);
        dataMap.put("packages", columnPackage(pojoParamsInfo));
        dataMap.put("pojoInfo", pojoParamsInfo);
        dataMap.put("isLombok", lombok.isSelected());
        dataMap.put("isSwagger", swagger.isSelected());
        dataMap.put("isMybatisPlus", mybatisPlus.isSelected());
        return dataMap;
    }

    /**
     * @param baseTemplateInfo
     * @return
     */
    private Map<String, Object> convertServicePojoTemplateData(BaseTemplateInfo baseTemplateInfo) {
        ServiceParamsInfo serviceParamsInfo = (ServiceParamsInfo) baseTemplateInfo;
        final SuperPojoInfo superPojoInfo = serviceParamsInfo.getSuperPojoInfo();
        final PojoParamsInfo doPojo = (PojoParamsInfo) templateParamsInfoMap.get(TemplateTypeEnum.DO.getTemplateId());
        final MapperParamsInfo mapper = (MapperParamsInfo) templateParamsInfoMap.get(TemplateTypeEnum.MAPPER.getTemplateId());
        HashMap<String, Object> dataMap = new HashMap<>(2);
        dataMap.put("pojo", serviceParamsInfo);
        dataMap.put("isMybatisPlus", mybatisPlus.isSelected());
        final Set<String> packages = this.serviceColumnPackages(serviceParamsInfo);
        Set<String> superPackages = this.getSuperPojoInfoPackages(superPojoInfo, doPojo, serviceParamsInfo, mybatisPlus.isSelected(), mapper);
        if (!CollectionUtils.isEmpty(superPackages)) {
            packages.addAll(superPackages);
        }
        dataMap.put("packages", packages);
        return dataMap;
    }

    /**
     * @param baseTemplateInfo
     * @return
     */
    private Map<String, Object> convertServiceApiPojoTemplateData(BaseTemplateInfo baseTemplateInfo) {
        InterfaceParamsInfo interfaceParamsInfo = (InterfaceParamsInfo) baseTemplateInfo;
        HashMap<String, Object> dataMap = new HashMap<>(1);
        dataMap.put("pojo", interfaceParamsInfo);
        dataMap.put("isMybatisPlus", mybatisPlus.isSelected());
        SuperPojoInfo superPojoInfo = interfaceParamsInfo.getSuperPojoInfo();
        PojoParamsInfo doPojo = (PojoParamsInfo) templateParamsInfoMap.get(TemplateTypeEnum.DO.getTemplateId());
        Set<String> packages = this.getSuperPojoInfoPackages(superPojoInfo, doPojo, interfaceParamsInfo, mybatisPlus.isSelected(), null);
        dataMap.put("packages", packages);
        return dataMap;
    }

    /**
     * @param superPojoInfo
     * @param doPojo
     * @param baseTemplateInfo
     * @param isMybatisPlus
     * @return
     */
    private Set<String> getSuperPojoInfoPackages(SuperPojoInfo superPojoInfo, PojoParamsInfo doPojo, BaseTemplateInfo baseTemplateInfo, boolean isMybatisPlus, MapperParamsInfo mapper) {
        Set<String> packages = new HashSet<>();
        if (superPojoInfo != null) {
            packages.add(superPojoInfo.getPackageName());
            if (superPojoInfo.isGenericFlag()) {
                if (doPojo != null) {
                    superPojoInfo.setGenericPojoName(doPojo.getPojoName());
                    superPojoInfo.setGenericPackageName(doPojo.getPackageName());
                    packages.add(superPojoInfo.getGenericPagePath());
                }
                if (mapper != null) {
                    superPojoInfo.setGenericMapperName(mapper.getPojoName());
                    superPojoInfo.setGenericMapperPackageName(mapper.getPackageName());
                    packages.add(superPojoInfo.getGenericMapperPagePath());
                }
            }
        } else {
            if (isMybatisPlus) {
                String pojoName = doPojo != null ? doPojo.getPojoName() : "XXXEntity";
                String packageName = doPojo != null ? doPojo.getPackageName() : Code4jConstants.DEFAULT_DO_PACKAGE;
                if (baseTemplateInfo instanceof InterfaceParamsInfo) {
                    //默认使用mybatisplus IService
                    superPojoInfo = new SuperPojoInfo("IService", pojoName, packageName);
                    ((InterfaceParamsInfo) baseTemplateInfo).setSuperPojoInfo(superPojoInfo);
                    packages.add("com.baomidou.mybatisplus.extension.service.IService");
                } else if (baseTemplateInfo instanceof MapperParamsInfo) {
                    //默认使用mybatisplus BaseMapper
                    superPojoInfo = new SuperPojoInfo("BaseMapper", pojoName, packageName);
                    ((MapperParamsInfo) baseTemplateInfo).setSuperPojoInfo(superPojoInfo);
                    packages.add("com.baomidou.mybatisplus.core.mapper");
                } else if (baseTemplateInfo instanceof ServiceParamsInfo) {
                    String mapperName = mapper != null ? mapper.getPojoName() : "XXXMapper";
                    String mapperPackageName = mapper != null ? mapper.getPackageName() : Code4jConstants.DEFAULT_MAPPER_PACKAGE;
                    //默认使用 默认使用mybatisplus ServiceImpl
                    superPojoInfo = new SuperPojoInfo("ServiceImpl", pojoName, packageName, mapperName, mapperPackageName);
                    ((ServiceParamsInfo) baseTemplateInfo).setSuperPojoInfo(superPojoInfo);
                    packages.add(superPojoInfo.getGenericMapperPackageName());
                    packages.add("com.baomidou.mybatisplus.extension.service.impl.ServiceImpl");
                }
                packages.add(superPojoInfo.getGenericPagePath());
            }
        }
        return packages;
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
        HashMap<String, Object> dataMap = new HashMap<>(2);
        List<XmlApiParamsInfo> xmlApiParamsInfos = xmlParamsInfo.getXmlApiParamsInfos();

        dataMap.put("xmlMap", xmlParamsInfo);
        dataMap.put("xmlApiParamsInfos", mybatisPlus.isSelected() ? null : xmlApiParamsInfos);
        //还原选择
        xmlParamsInfo.setXmlApiParamsInfos(null);
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
                if (TemplateTypeEnum.SERVICE_API.getTemplateId().equals(customJCheckBox.getId())) {
                    ServiceModelBox serviceModelBox = (ServiceModelBox) customJCheckBox.getBindObject();
                    final ModelBoxInfo serviceApi = serviceModelBox.getServiceApi();
                    final ModelBoxInfo serviceImpl = serviceModelBox.getServiceImpl();
                    //获取名称
                    CustomJTextField apiPackName = serviceApi.getPackNameFiled();
                    //获取包名
                    CustomJTextField apiPackage = serviceApi.getPackageField();
                    //获取路径
                    CustomJTextField apiPojoPath = serviceApi.getPojoPathField();
                    //获取父类
                    CustomJTextField apiSuperPath = serviceApi.getSuperPathField();
                    //service api 模板 参数
                    InterfaceParamsInfo interfaceParamsInfo = new InterfaceParamsInfo();
                    interfaceParamsInfo.setPojoName(apiPackName.getText().trim());
                    interfaceParamsInfo.setPojoPath(apiPojoPath.getText().trim());
                    interfaceParamsInfo.setPackageName(apiPackage.getText().trim());
                    interfaceParamsInfo.setDefaultPackageName(StrUtil.underlineToCamelToLower(jdbcTableInfo.getTableName()));
                    interfaceParamsInfo.setPackageRoot(interfaceParamsInfo.getUseDefaultPackageRoot(customJFileChooserPanel.getFileName()));
                    interfaceParamsInfo.setAuthor(jdbcSourceInfo.getCreator());
                    interfaceParamsInfo.setJdbcTableInfo(jdbcTableInfo);
                    interfaceParamsInfo.setSuperPojoInfo(this.getSuperPojoName(apiSuperPath.getText().trim()));
                    TemplateInfo interfaceTp = new TemplateInfo(customJCheckBox.getId());
                    interfaceParamsInfo.setTemplateInfo(interfaceTp);
                    this.checkTemplate(interfaceParamsInfo);
                    //service impl 目标参数优先参数 自定义>根据api生成
                    final CustomJTextField packNameFiled = serviceImpl.getPackNameFiled();
                    final CustomJTextField packageField = serviceImpl.getPackageField();
                    final CustomJTextField pojoPathField = serviceImpl.getPojoPathField();
                    final CustomJTextField superPathField = serviceImpl.getSuperPathField();
                    final String implPackName = packNameFiled.getText().trim();
                    final String implPackage = packageField.getText().trim();
                    final String implPojoPath = pojoPathField.getText().trim();
                    final String implSuper = superPathField.getText().trim();
                    ServiceParamsInfo serviceParamsInfo = new ServiceParamsInfo();
                    serviceParamsInfo.setInterfaceParamsInfo(interfaceParamsInfo);
                    serviceParamsInfo.setPojoPath(StringUtils.isNotBlank(implPojoPath) ? implPojoPath : interfaceParamsInfo.getPojoPath());
                    //实现类名称
                    if (StringUtils.isBlank(implPackName)) {
                        if (interfaceParamsInfo.getPojoName().startsWith("I")) {
                            serviceParamsInfo.setPojoName(interfaceParamsInfo.getPojoName().substring(1) + "Impl");
                        } else {
                            serviceParamsInfo.setPojoName(interfaceParamsInfo.getPojoName() + "Impl");
                        }
                    } else {
                        serviceParamsInfo.setPojoName(implPackName);
                    }
                    //实现类得包路径
                    if (StringUtils.isBlank(implPackage)) {
                        serviceParamsInfo.setPackageName(interfaceParamsInfo.getPackageName() + ".impl");
                        serviceParamsInfo.setPackageRoot(interfaceParamsInfo.getPackageRoot());
                    } else {
                        serviceParamsInfo.setPackageName(implPackage);
                        serviceParamsInfo.setPackageRoot(null);
                    }
                    //父类
                    final SuperPojoInfo superPojoName = this.getSuperPojoName(implSuper);
                    serviceParamsInfo.setSuperPojoInfo(superPojoName);
                    serviceParamsInfo.setPojoDesc(templateTypeEnum.getTemplateDesc());
                    serviceParamsInfo.setAuthor(jdbcSourceInfo.getCreator());
                    serviceParamsInfo.setJdbcTableInfo(jdbcTableInfo);
                    TemplateInfo templateInfo = new TemplateInfo(TemplateTypeEnum.SERVICE.getTemplateId());
                    serviceParamsInfo.setTemplateInfo(templateInfo);
                    serviceParamsInfo.setTemplateTypeEnum(TemplateTypeEnum.SERVICE);
                    this.checkTemplate(serviceParamsInfo);
                    templateParamsInfoMap.put(customJCheckBox.getId(), serviceParamsInfo);
                } else {
                    //绑定的配置容器daoP、voP、xmlP、mapperP
                    ModelBoxInfo modelBoxInfo = (ModelBoxInfo) customJCheckBox.getBindObject();
                    //获取名称
                    CustomJTextField packNameT = modelBoxInfo.getPackNameFiled();
                    //获取包名
                    CustomJTextField packageT = modelBoxInfo.getPackageField();
                    //获取路径
                    CustomJTextField pojoPathT = modelBoxInfo.getPojoPathField();
                    if (TemplateTypeEnum.XML.getTemplateId().equals(customJCheckBox.getId())) {
                        XmlParamsInfo xmlParamsInfo = (XmlParamsInfo) modelBoxInfo.getBindObject();
                        List<JdbcMapJavaInfo> jdbcMapJavaInfos = xmlParamsInfo.getTableColumnInfos();
                        //xml模板 参数
//                        XmlParamsInfo xmlParamsInfo = (XmlParamsInfo) commonPanel.getBindObject();
                        xmlParamsInfo.setPojoType(customJCheckBox.getId());
                        xmlParamsInfo.setPojoName(packNameT.getText());
                        xmlParamsInfo.setPackageName(packageT.getText());
                        xmlParamsInfo.setPojoPath(pojoPathT.getText());
                        xmlParamsInfo.setAuthor(jdbcSourceInfo.getCreator());
                        xmlParamsInfo.setJdbcTableInfo(jdbcTableInfo);
                        xmlParamsInfo.setPojoDesc(TemplateTypeEnum.XML.getTemplateDesc());
                        TemplateInfo templateInfo = new TemplateInfo(customJCheckBox.getId());
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
                        CustomJTextField superPathT = modelBoxInfo.getSuperPathField();
//                        CustomJTextField superPathT = (CustomJTextField) ((CommonPanel) commonPanel.getComponent(3)).getComponent(1);
                        if (TemplateTypeEnum.MAPPER.getTemplateId().equals(customJCheckBox.getId())) {
                            //mapper模板 参数
                            MapperParamsInfo mapperParamsInfo = (MapperParamsInfo) modelBoxInfo.getBindObject();
                            mapperParamsInfo.setSuperPojoInfo(this.getSuperPojoName(superPathT.getText().trim()));
                            mapperParamsInfo.setPojoDesc(templateTypeEnum.getTemplateDesc());
                            mapperParamsInfo.setPojoType(customJCheckBox.getId());
                            mapperParamsInfo.setPackageName(packageT.getText());
                            mapperParamsInfo.setPojoName(packNameT.getText());
                            mapperParamsInfo.setPojoPath(pojoPathT.getText());
                            mapperParamsInfo.setAuthor(jdbcSourceInfo.getCreator());
                            mapperParamsInfo.setJdbcTableInfo(jdbcTableInfo);
                            mapperParamsInfo.setTemplateTypeEnum(templateTypeEnum);
                            TemplateInfo templateInfo = new TemplateInfo(customJCheckBox.getId());
                            mapperParamsInfo.setTemplateInfo(templateInfo);
                            mapperParamsInfo.setDefaultPackageName(PojoParamsInfo.getDefaultPackageName(templateTypeEnum, StrUtil.underlineToCamelToLower(jdbcTableInfo.getTableName())));
                            mapperParamsInfo.setPackageRoot(mapperParamsInfo.getUseDefaultPackageRoot(customJFileChooserPanel.getFileName()));
                            this.checkTemplate(mapperParamsInfo);
                            templateParamsInfoMap.put(customJCheckBox.getId(), mapperParamsInfo);
                        } else {
                            List<JdbcMapJavaInfo> jdbcMapJavaInfos = (List<JdbcMapJavaInfo>) modelBoxInfo.getBindObject();
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
                            TemplateInfo templateInfo = new TemplateInfo(customJCheckBox.getId());
                            pojoParamsInfo.setTemplateInfo(templateInfo);
                            pojoParamsInfo.setDefaultPackageName(PojoParamsInfo.getDefaultPackageName(templateTypeEnum, StrUtil.underlineToCamelToLower(jdbcTableInfo.getTableName())));
                            pojoParamsInfo.setPackageRoot(pojoParamsInfo.getUseDefaultPackageRoot(customJFileChooserPanel.getFileName()));
                            pojoParamsInfo.setTableColumnInfos(jdbcMapJavaInfos);
                            this.checkTemplate(pojoParamsInfo);
                            templateParamsInfoMap.put(customJCheckBox.getId(), pojoParamsInfo);
                        }
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
