package com.code4j.action;

import com.code4j.component.CustomJCheckBox;
import com.code4j.component.CustomJTextField;
import com.code4j.component.panel.CustomJFileChooserPanel;
import com.code4j.config.Code4jConstants;
import com.code4j.enums.ControllerApiTemplateEnum;
import com.code4j.enums.TemplateTypeEnum;
import com.code4j.enums.XmlSqlTemplateEnum;
import com.code4j.exception.Code4jException;
import com.code4j.pojo.*;
import com.code4j.util.*;
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
                    } else if (TemplateTypeEnum.CONTROLLER.getTemplateId().equals(templateId)) {
                        dataMap = this.convertControllerTemplateData(baseTemplateInfo);
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
     * Controller 模板数据
     *
     * @param baseTemplateInfo
     * @return
     */
    private Map<String, Object> convertControllerTemplateData(BaseTemplateInfo baseTemplateInfo) {
        Map<String, Object> dataMap = new HashMap<>(4);
        ControllerParamsInfo controllerParamsInfo = (ControllerParamsInfo) baseTemplateInfo;
        PojoParamsInfo doPojo = (PojoParamsInfo) templateParamsInfoMap.get(TemplateTypeEnum.DO.getTemplateId());
        PojoParamsInfo voPojo = (PojoParamsInfo) templateParamsInfoMap.get(TemplateTypeEnum.VO.getTemplateId());
        ServiceParamsInfo interfaceParamsInfo = (ServiceParamsInfo) templateParamsInfoMap.get(TemplateTypeEnum.SERVICE_API.getTemplateId());
        if (interfaceParamsInfo != null) {
            controllerParamsInfo.setInterfaceParamsInfo(interfaceParamsInfo.getInterfaceParamsInfo());
        } else {
            controllerParamsInfo.setInterfaceParamsInfo(null);
        }
        final List<ControllerApiParamsInfo> controllerApiParamsInfos = controllerParamsInfo.getControllerApiParamsInfos();
        final Set<String> packages = this.getControllerPackages(controllerParamsInfo, doPojo, voPojo, controllerApiParamsInfos);
        final Set<String> superPojoInfoPackages = this.getSuperPojoInfoPackages(controllerParamsInfo.getSuperPojoInfo(), doPojo, controllerParamsInfo, mybatisPlus.isSelected(), null);
        if (!CollectionUtils.isEmpty(superPojoInfoPackages)) {
            packages.addAll(superPojoInfoPackages);
        }
        dataMap.put("baseInfo", controllerParamsInfo);
        dataMap.put("packages", packages);
        dataMap.put("apiInfos", controllerApiParamsInfos);
        dataMap.put("isSwagger", swagger.isSelected());
        return dataMap;
    }

    /**
     * Controller 引用的包路径
     *
     * @param controllerParamsInfo
     * @param doPojo
     * @param voPojo
     * @param controllerApiParamsInfos
     * @return
     */
    private Set<String> getControllerPackages(ControllerParamsInfo controllerParamsInfo, PojoParamsInfo doPojo, PojoParamsInfo voPojo, List<ControllerApiParamsInfo> controllerApiParamsInfos) {
        Set<String> packages = new HashSet<>();
        //引用service
        final InterfaceParamsInfo interfaceParamsInfo = controllerParamsInfo.getInterfaceParamsInfo();
        if (interfaceParamsInfo != null) {
            packages.add(interfaceParamsInfo.getPackagePath());
            packages.add("javax.annotation.Resource");
        }
        if (!CollectionUtils.isEmpty(controllerApiParamsInfos)) {
            JdbcMapJavaInfo tablePrimaryKey = controllerParamsInfo.getTablePrimaryKey();
            //响应参数类型
            final String resultClass = controllerParamsInfo.getResultClass();
            String resultName = null;
            if (StringUtils.isNotBlank(resultClass)) {
                packages.add(resultClass);
                resultName = controllerParamsInfo.getResultName();
            }
            for (ControllerApiParamsInfo info : controllerApiParamsInfos) {
                if (CollectionUtils.isNotEmpty(info.getParameterInfos())) {
                    //清除上一次执行的缓存
                    info.getParameterInfos().clear();
                }
                if (StringUtils.isNotBlank(resultName)) {
                    if (ControllerApiTemplateEnum.isObjectListResultType(info.getTemplateId())) {
                        packages.add("java.util.List");
                        info.setResultType(String.format("%s<List<%s>>", resultName, voPojo != null ? voPojo.getPojoName() : (doPojo != null ? doPojo.getPojoName() : "Object")));
                    } else if (ControllerApiTemplateEnum.isObjectResultType(info.getTemplateId())) {
                        info.setResultType(String.format("%s<%s>", resultName, voPojo != null ? voPojo.getPojoName() : (doPojo != null ? doPojo.getPojoName() : "Object")));
                    } else if (ControllerApiTemplateEnum.isBooleanResultType(info.getTemplateId())) {
                        info.setResultType(String.format("%s<Boolean>", resultName));
                    } else if (ControllerApiTemplateEnum.isPageResultType(info.getTemplateId())) {
                        info.setResultType(String.format("%s<Page<%s>>", resultName, voPojo != null ? voPojo.getPojoName() : (doPojo != null ? doPojo.getPojoName() : "Object")));
                        if (mybatisPlus.isSelected()) {
                            packages.add("com.baomidou.mybatisplus.extension.plugins.pagination.Page");
                        } else {
                            //自定义分页插件路径 实际项目中需替换
                            packages.add(Code4jConstants.DEFAULT_PAGEINFO_PACKAGE);
                        }
                    }
                } else {
                    info.setResultType("Object");
                }
                if (ControllerApiTemplateEnum.isIdPathVariable(info.getControllerApiTemplateEnum())) {
                    final Set<ControllerApiParamsInfo.ParameterInfo> parameterInfos = Optional.ofNullable(info.getParameterInfos()).orElse(new HashSet<>());
                    ControllerApiParamsInfo.ParameterInfo parameterInfo = new ControllerApiParamsInfo.ParameterInfo();
                    parameterInfo.setParameterName("id");
                    parameterInfo.setAnnotations(String.format("@PathVariable(value =\"%s\" )", parameterInfo.getParameterName()));
                    parameterInfo.setParameterType(tablePrimaryKey != null ? this.getShortPkJdbcType(tablePrimaryKey.getJavaType()) : "long");
                    parameterInfos.add(parameterInfo);
                    info.setParameterInfos(parameterInfos);
                }
                if (ControllerApiTemplateEnum.isRequestBody(info.getControllerApiTemplateEnum())) {
                    final Set<ControllerApiParamsInfo.ParameterInfo> parameterInfos = Optional.ofNullable(info.getParameterInfos()).orElse(new HashSet<>());
                    ControllerApiParamsInfo.ParameterInfo parameterInfo = new ControllerApiParamsInfo.ParameterInfo();
                    parameterInfo.setAnnotations(voPojo != null || doPojo != null ? "@Validated @RequestBody" : "@RequestBody");
                    packages.add("org.springframework.validation.annotation.Validated");
                    //优先使用voPojo
                    if (voPojo != null) {
                        parameterInfo.setParameterType(voPojo.getPojoName());
                        parameterInfo.setParameterName(StrUtil.underlineToCamelToLower(voPojo.getPojoName()));
                        packages.add(voPojo.getPackagePath());
                    } else {
                        parameterInfo.setParameterType(doPojo != null ? doPojo.getPojoName() : "Object");
                        parameterInfo.setParameterName(doPojo != null ? StrUtil.underlineToCamelToLower(doPojo.getPojoName()) : "object");
                        if (doPojo != null) {
                            packages.add(doPojo.getPackagePath());
                        }
                    }
                    parameterInfos.add(parameterInfo);
                    info.setParameterInfos(parameterInfos);
                }
            }
        }
        return packages;
    }

    /**
     * Mapper 模板数据
     *
     * @param baseTemplateInfo
     * @return
     */
    private Map<String, Object> convertMapperTemplateData(BaseTemplateInfo baseTemplateInfo) {
        Map<String, Object> dataMap = new HashMap<>(3);
        PojoParamsInfo doPojo = (PojoParamsInfo) templateParamsInfoMap.get(TemplateTypeEnum.DO.getTemplateId());
        MapperParamsInfo mapperParamsInfo = (MapperParamsInfo) baseTemplateInfo;
        List<MapperApiParamsInfo> mapperApiParamsInfos = mapperParamsInfo.getMapperApiParamsInfos();
        dataMap.put("baseInfo", mapperParamsInfo);
        final Set<String> packages = this.getMapperPackages(mapperParamsInfo, doPojo, mapperApiParamsInfos);
        dataMap.put("packages", packages);
        dataMap.put("apiInfos", mybatisPlus.isSelected() ? null : mapperApiParamsInfos);
        return dataMap;
    }

    /**
     * Mapper 模板引用包路径
     *
     * @param mapperParamsInfo
     * @param doPojo
     * @param mapperApiParamsInfos
     * @return
     */
    private Set<String> getMapperPackages(MapperParamsInfo mapperParamsInfo, PojoParamsInfo doPojo, List<MapperApiParamsInfo> mapperApiParamsInfos) {
        Set<String> packages = new HashSet<>();
        SuperPojoInfo superPojoInfo = mapperParamsInfo.getSuperPojoInfo();
        Set<String> superPackages = this.getSuperPojoInfoPackages(superPojoInfo, doPojo, mapperParamsInfo, mybatisPlus.isSelected(), null);
        if (!CollectionUtils.isEmpty(superPackages)) {
            packages.addAll(superPackages);
        }
        if (CollectionUtils.isNotEmpty(mapperApiParamsInfos) && !mybatisPlus.isSelected()) {
            JdbcMapJavaInfo tablePrimaryKey = mapperParamsInfo.getTablePrimaryKey();
            if (tablePrimaryKey != null) {
                packages.add(tablePrimaryKey.getJavaType());
            }
            String resultTypePath = null;
            if (doPojo != null) {
                resultTypePath = this.getTypePath(doPojo.getPackageRoot(), doPojo.getPackageName(), doPojo.getPojoName());
            } else {
                resultTypePath = "Object";
            }
            //mapper 接口 需引入的包路径
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
                    mapperApiParamsInfo.setParameterType(doPojo != null ? doPojo.getPojoName() : "Object");
                } else if (XmlSqlTemplateEnum.isObjectParameterType(mapperApiParamsInfo.getTemplateId())) {
                    mapperApiParamsInfo.setParameterType(doPojo != null ? doPojo.getPojoName() : "Object");
                } else if (XmlSqlTemplateEnum.isParameterPk(mapperApiParamsInfo.getTemplateId()) && tablePrimaryKey != null) {
                    if (tablePrimaryKey.getJdbcType() != null) {
                        mapperApiParamsInfo.setParameterType(this.getShortPkJdbcType(tablePrimaryKey.getJavaType()));
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
     * Pojo（Entity,VO） 模板数据
     *
     * @param baseTemplateInfo
     * @return
     */
    private Map<String, Object> convertPojoTemplateData(BaseTemplateInfo baseTemplateInfo) {
        PojoParamsInfo pojoParamsInfo = (PojoParamsInfo) baseTemplateInfo;
        Map<String, Object> dataMap = new HashMap<>(5);
        final Set<String> packages = columnPackage(pojoParamsInfo);
        dataMap.put("packages", packages);
        dataMap.put("pojoInfo", pojoParamsInfo);
        dataMap.put("isLombok", lombok.isSelected());
        dataMap.put("isSwagger", swagger.isSelected());
        dataMap.put("isMybatisPlus", mybatisPlus.isSelected());
        return dataMap;
    }

    /**
     * Service  模板数据
     *
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
        dataMap.put("service_value", StrUtil.firstCharOnlyToLower(serviceParamsInfo.getPojoName()));
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
     * Service 引用包路径
     *
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
     * Service Api 模板数据
     *
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
     * 获取父类引用包路径
     *
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
                if (superPojoInfo != null) {
                    packages.add(superPojoInfo.getGenericPagePath());
                }
            }
        }
        return packages;
    }

    /**
     * xml 模板数据
     *
     * @param baseTemplateInfo
     * @return
     */
    private Map<String, Object> convertXmlTemplateData(BaseTemplateInfo baseTemplateInfo) {
        BaseTemplateInfo doPojo = templateParamsInfoMap.get(TemplateTypeEnum.DO.getTemplateId());
        BaseTemplateInfo mapperPojo = templateParamsInfoMap.get(TemplateTypeEnum.MAPPER.getTemplateId());
        XmlParamsInfo xmlParamsInfo = (XmlParamsInfo) baseTemplateInfo;
        if (mapperPojo != null) {
            xmlParamsInfo.setNamespace(StringUtils.isNotBlank(mapperPojo.getPackageRoot()) ? mapperPojo.getPackageRoot() + mapperPojo.getPackagePath() : mapperPojo.getPackagePath());
//            MapperParamsInfo mapperParamsInfo = (MapperParamsInfo) mapperPojo;
            // mapper 选择了自定义接口 需要生成对应的SQL
//            xmlParamsInfo.defaultXmlApiParamsInfos(mapperParamsInfo.getMapperApiParamsInfos());
        } else {
            xmlParamsInfo.setNamespace(null);
        }
        if (doPojo != null) {
            //doPojo替换默认的 tableColumnInfos
            xmlParamsInfo.setTableColumnInfos(doPojo.getTableColumnInfos());
            xmlParamsInfo.setResultMapType(StringUtils.isNotBlank(doPojo.getPackageRoot()) ? doPojo.getPackageRoot() + doPojo.getPackagePath() : doPojo.getPackagePath());
        } else {
            xmlParamsInfo.setResultMapType(null);
        }
        HashMap<String, Object> dataMap = new HashMap<>(2);
        List<XmlApiParamsInfo> xmlApiParamsInfos = xmlParamsInfo.getXmlApiParamsInfos();
        dataMap.put("xmlMap", xmlParamsInfo);
        dataMap.put("xmlApiParamsInfos", mybatisPlus.isSelected() ? null : xmlApiParamsInfos);
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
        //获取前端通用配置
        final Map<String, String> currGeneralConfigInfo = SQLiteUtil.getCurrGeneralConfigInfo();
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
                    interfaceParamsInfo.setAuthor(this.getAuthor(currGeneralConfigInfo));
                    interfaceParamsInfo.setJdbcTableInfo(jdbcTableInfo);
                    interfaceParamsInfo.setSuperPojoInfo(this.getSuperPojoName(apiSuperPath.getText().trim(), true));
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
                    final SuperPojoInfo superPojoName = this.getSuperPojoName(implSuper, true);
                    serviceParamsInfo.setSuperPojoInfo(superPojoName);
                    serviceParamsInfo.setPojoDesc(templateTypeEnum.getTemplateDesc());
                    serviceParamsInfo.setAuthor(this.getAuthor(currGeneralConfigInfo));
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
                        //xml模板 参数
                        XmlParamsInfo xmlParamsInfo = (XmlParamsInfo) modelBoxInfo.getBindObject();
                        List<JdbcMapJavaInfo> jdbcMapJavaInfos = xmlParamsInfo.getTableColumnInfos();
                        xmlParamsInfo.setPojoType(customJCheckBox.getId());
                        xmlParamsInfo.setPojoName(packNameT.getText());
                        xmlParamsInfo.setPackageName(packageT.getText());
                        xmlParamsInfo.setPojoPath(pojoPathT.getText());
                        xmlParamsInfo.setAuthor(this.getAuthor(currGeneralConfigInfo));
                        xmlParamsInfo.setJdbcTableInfo(jdbcTableInfo);
                        xmlParamsInfo.setPojoDesc(TemplateTypeEnum.XML.getTemplateDesc());
                        TemplateInfo templateInfo = new TemplateInfo(customJCheckBox.getId());
                        xmlParamsInfo.setTemplateInfo(templateInfo);
                        xmlParamsInfo.setPackageRoot(xmlParamsInfo.getUseDefaultPackageRoot(customJFileChooserPanel.getFileName()));
                        xmlParamsInfo.setTemplateTypeEnum(templateTypeEnum);
                        this.checkTemplate(xmlParamsInfo);
                        List<XmlApiParamsInfo> xmlApiParamsInfos = xmlParamsInfo.getXmlApiParamsInfos();
                        if (CollectionUtils.isNotEmpty(xmlApiParamsInfos)) {
                            for (final XmlApiParamsInfo xmlApiParamsInfo : xmlApiParamsInfos) {
                                if (XmlSqlTemplateEnum.isObjectListResultType(xmlApiParamsInfo.getTemplateId()) || XmlSqlTemplateEnum.isObjectResultType(xmlApiParamsInfo.getTemplateId())) {
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
                        if (TemplateTypeEnum.MAPPER.getTemplateId().equals(customJCheckBox.getId())) {
                            //mapper模板 参数
                            MapperParamsInfo mapperParamsInfo = (MapperParamsInfo) modelBoxInfo.getBindObject();
                            mapperParamsInfo.setSuperPojoInfo(this.getSuperPojoName(superPathT.getText().trim(), true));
                            mapperParamsInfo.setPojoDesc(templateTypeEnum.getTemplateDesc());
                            mapperParamsInfo.setPojoType(customJCheckBox.getId());
                            mapperParamsInfo.setPackageName(packageT.getText());
                            mapperParamsInfo.setPojoName(packNameT.getText());
                            mapperParamsInfo.setPojoPath(pojoPathT.getText());
                            mapperParamsInfo.setAuthor(this.getAuthor(currGeneralConfigInfo));
                            mapperParamsInfo.setJdbcTableInfo(jdbcTableInfo);
                            mapperParamsInfo.setTemplateTypeEnum(templateTypeEnum);
                            TemplateInfo templateInfo = new TemplateInfo(customJCheckBox.getId());
                            mapperParamsInfo.setTemplateInfo(templateInfo);
                            mapperParamsInfo.setDefaultPackageName(PojoParamsInfo.getDefaultPackageName(templateTypeEnum, StrUtil.underlineToCamelToLower(jdbcTableInfo.getTableName())));
                            mapperParamsInfo.setPackageRoot(mapperParamsInfo.getUseDefaultPackageRoot(customJFileChooserPanel.getFileName()));
                            this.checkTemplate(mapperParamsInfo);
                            templateParamsInfoMap.put(customJCheckBox.getId(), mapperParamsInfo);
                        } else if (TemplateTypeEnum.CONTROLLER.getTemplateId().equals(customJCheckBox.getId())) {
                            ControllerParamsInfo controllerParamsInfo = (ControllerParamsInfo) modelBoxInfo.getBindObject();
                            controllerParamsInfo.setAuthor(this.getAuthor(currGeneralConfigInfo));
                            controllerParamsInfo.setJdbcTableInfo(jdbcTableInfo);
                            controllerParamsInfo.setPojoDesc(templateTypeEnum.getTemplateDesc());
                            controllerParamsInfo.setPojoType(customJCheckBox.getId());
                            controllerParamsInfo.setPackageName(packageT.getText());
                            controllerParamsInfo.setPojoName(packNameT.getText());
                            controllerParamsInfo.setPojoPath(pojoPathT.getText());
                            controllerParamsInfo.setTemplateTypeEnum(templateTypeEnum);
                            TemplateInfo templateInfo = new TemplateInfo(customJCheckBox.getId());
                            final String tableName = jdbcTableInfo.getTableName();
                            controllerParamsInfo.setRootRequestMapping("/" + StrUtil.underlineToCamelToLower(tableName));
                            controllerParamsInfo.setTemplateInfo(templateInfo);
                            controllerParamsInfo.setDefaultPackageName(PojoParamsInfo.getDefaultPackageName(templateTypeEnum, StrUtil.underlineToCamelToLower(jdbcTableInfo.getTableName())));
                            controllerParamsInfo.setPackageRoot(controllerParamsInfo.getUseDefaultPackageRoot(customJFileChooserPanel.getFileName()));
                            controllerParamsInfo.setSuperPojoInfo(this.getSuperPojoName(superPathT.getText().trim(), false));
                            final CustomJTextField resultClassField = modelBoxInfo.getResultClassField();
                            if (resultClassField != null) {
                                controllerParamsInfo.setResultClass(resultClassField.getText().trim());
                            }
                            templateParamsInfoMap.put(customJCheckBox.getId(), controllerParamsInfo);
                        } else {
                            PojoParamsConfigInfo pojoParamsConfigInfo = (PojoParamsConfigInfo) modelBoxInfo.getBindObject();
                            List<JdbcMapJavaInfo> jdbcMapJavaInfos = pojoParamsConfigInfo.getTableColumnInfos();
                            //do、vo模板参数
                            PojoParamsInfo pojoParamsInfo = new PojoParamsInfo();
                            pojoParamsInfo.setSuperPojoInfo(this.getSuperPojoName(superPathT.getText().trim(), true));
                            pojoParamsInfo.setPojoDesc(templateTypeEnum.getTemplateDesc());
                            pojoParamsInfo.setPojoType(customJCheckBox.getId());
                            pojoParamsInfo.setPackageName(packageT.getText());
                            pojoParamsInfo.setPojoName(packNameT.getText());
                            pojoParamsInfo.setPojoPath(pojoPathT.getText());
                            pojoParamsInfo.setAuthor(this.getAuthor(currGeneralConfigInfo));
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

    /**
     * @param map
     * @return
     */
    private String getAuthor(Map<String, String> map) {
        final String author = map.get(Code4jConstants.AUTHOR_KEY);
        return Optional.ofNullable(author).orElse(Code4jConstants.DEFAULT_AUTHOR);
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
     * @param genericFlag
     * @return
     */
    private SuperPojoInfo getSuperPojoName(String superPojo, boolean genericFlag) {
        if (StringUtils.isNotBlank(superPojo)) {
            SuperPojoInfo superPojoInfo = new SuperPojoInfo();
            if (superPojo.indexOf(".") > 0) {
                String pojoName = superPojo.substring(superPojo.lastIndexOf(".") + 1);
                superPojoInfo.setPojoName(pojoName);
            } else {
                superPojoInfo.setPojoName(superPojo);
            }
            superPojoInfo.setPackageName(superPojo);
            superPojoInfo.setGenericFlag(genericFlag);
            return superPojoInfo;
        }
        return null;
    }
}
