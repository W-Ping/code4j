package com.code4j.action;

import com.code4j.component.CustomJCheckBox;
import com.code4j.component.panel.CommonPanel;
import com.code4j.component.panel.CustomJFileChooserPanel;
import com.code4j.config.Code4jConstants;
import com.code4j.config.TemplateTypeEnum;
import com.code4j.connect.JDBCService;
import com.code4j.connect.JdbcServiceFactory;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liu_wp
 * @date 2020/11/19
 * @see
 */
public class GenerateCodeAction implements ActionListener {
    private CustomJFileChooserPanel customJFileChooserPanel;
    private Map<String, BaseTemplateInfo> pojoParamsInfoMap = new ConcurrentHashMap<>();
    private JdbcTableInfo jdbcTableInfo;
    private JdbcSourceInfo jdbcSourceInfo;
    private JDBCService jdbcService;
    private List<CustomJCheckBox> customJCheckBoxes;

    public GenerateCodeAction(CustomJFileChooserPanel customJFileChooserPanel, JdbcTableInfo jdbcTableInfo, List<CustomJCheckBox> customJCheckBoxes, JdbcSourceInfo jdbcSourceInfo) {
        this.clearData();
        this.customJFileChooserPanel = customJFileChooserPanel;
        this.customJCheckBoxes = customJCheckBoxes;
        this.jdbcTableInfo = jdbcTableInfo;
        this.jdbcSourceInfo = jdbcSourceInfo;
        this.jdbcService = JdbcServiceFactory.getJdbcService(jdbcSourceInfo);
    }

    public void clearData() {
        this.pojoParamsInfoMap.clear();
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        this.setPojoParams();
        String projectPath = customJFileChooserPanel.getSelectValue();
        if (StringUtils.isBlank(projectPath)) {
            CustomDialogUtil.showError("项目地址不能为空");
            return;
        }
        if (pojoParamsInfoMap == null || pojoParamsInfoMap.isEmpty()) {
            CustomDialogUtil.showError("生成代码配置不能为空");
            return;
        }
        List<GenerateResultInfo> result = new ArrayList<>();
        for (Map.Entry<String, ? extends BaseTemplateInfo> mp : pojoParamsInfoMap.entrySet()) {
            String templateId = mp.getKey();
            BaseTemplateInfo baseTemplateInfo = mp.getValue();
            Map<String, Object> dataMap;
            if (TemplateTypeEnum.XML.getTemplateId().equals(templateId)) {
                dataMap = this.convertXmlTemplateData(baseTemplateInfo);
            } else {
                if (TemplateTypeEnum.SERVICE_API.getTemplateId().equals(templateId)) {
                    ServiceParamsInfo serviceParamsInfo = (ServiceParamsInfo) baseTemplateInfo;
                    InterfaceInfo interfaceInfo = serviceParamsInfo.getInterfaceInfo();
                    Map<String, Object> interfaceDataMap = this.convertServiceApiPojoTemplateData(interfaceInfo);
                    String interfacePath = FreemarkerUtil.generateCodeByTemplate(projectPath, interfaceInfo, interfaceDataMap);
                    System.out.println("interface path:" + interfacePath);
                    dataMap = convertServicePojoTemplateData(baseTemplateInfo);
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
     * @param tableColumnInfos
     * @return
     */
    private Map<String, Object> convertPojoTemplateData(BaseTemplateInfo baseTemplateInfo) {
        List<JdbcMapJavaInfo> tableColumnInfos = baseTemplateInfo.getTableColumnInfos();
        PojoParamsInfo pojoParamsInfo = (PojoParamsInfo) baseTemplateInfo;
        HashMap<String, Object> dataMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(tableColumnInfos)) {
            dataMap.put("columnPackages", columnPackage(tableColumnInfos));
            dataMap.put("tableColumnInfos", tableColumnInfos);
        }
        dataMap.put("pojo", pojoParamsInfo);
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
        InterfaceInfo interfaceInfo = (InterfaceInfo) baseTemplateInfo;
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("pojo", interfaceInfo);
        return dataMap;
    }

    /**
     * @param baseTemplateInfo
     * @return
     */
    private Map<String, Object> convertXmlTemplateData(BaseTemplateInfo baseTemplateInfo) {
        BaseTemplateInfo doPojo = pojoParamsInfoMap.get(TemplateTypeEnum.DO.getTemplateId());
        BaseTemplateInfo mapperPojo = pojoParamsInfoMap.get(TemplateTypeEnum.MAPPER.getTemplateId());
        XMLParamsInfo xmlParamsInfo = (XMLParamsInfo) baseTemplateInfo;

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
     *
     */
    private void setPojoParams() {
        this.clearData();
        for (final CustomJCheckBox customJCheckBox : customJCheckBoxes) {
            if (customJCheckBox.isSelected()) {
                TemplateTypeEnum templateTypeEnum = TemplateTypeEnum.getTemplateTypeEnum(customJCheckBox.getId());
                CommonPanel commonPanel = (CommonPanel) customJCheckBox.getBindObject();
                JTextField packNameT = (JTextField) ((CommonPanel) commonPanel.getComponent(0)).getComponent(1);
                JTextField packageT = (JTextField) ((CommonPanel) commonPanel.getComponent(1)).getComponent(1);
                JTextField pojoPathT = (JTextField) ((CommonPanel) commonPanel.getComponent(2)).getComponent(1);
                List<JdbcMapJavaInfo> jdbcMapJavaInfos = (List<JdbcMapJavaInfo>) commonPanel.getBindObject();
                if (TemplateTypeEnum.XML.getTemplateId().equals(customJCheckBox.getId())) {
                    XMLParamsInfo xmlParamsInfo = new XMLParamsInfo();
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
                    pojoParamsInfoMap.put(customJCheckBox.getId(), xmlParamsInfo);
                } else {
                    if (TemplateTypeEnum.SERVICE_API.getTemplateId().equals(customJCheckBox.getId())) {
                        InterfaceInfo interfaceInfo = new InterfaceInfo();
                        interfaceInfo.setPojoName(packNameT.getText());
                        interfaceInfo.setPojoPath(pojoPathT.getText());
                        interfaceInfo.setPackageName(packageT.getText());
                        interfaceInfo.setDefaultPackageName(StrUtil.underlineToCamelToLower(jdbcTableInfo.getTableName()));
                        interfaceInfo.setPackageRoot(interfaceInfo.getUseDefaultPackageRoot(customJFileChooserPanel.getFileName()));
                        interfaceInfo.setAuthor(jdbcSourceInfo.getCreator());
                        TemplateInfo interfaceTp = new TemplateInfo();
                        interfaceTp.setTemplateId(customJCheckBox.getId());
                        interfaceInfo.setTemplateInfo(interfaceTp);
                        ServiceParamsInfo serviceParamsInfo = new ServiceParamsInfo();
                        serviceParamsInfo.setInterfaceInfo(interfaceInfo);
                        serviceParamsInfo.setPojoPath(interfaceInfo.getPojoPath());
                        if (interfaceInfo.getPojoName().startsWith("I")) {
                            serviceParamsInfo.setPojoName(interfaceInfo.getPojoName().substring(1) + "Impl");
                        } else {
                            serviceParamsInfo.setPojoName(interfaceInfo.getPojoName() + "Impl");
                        }
                        serviceParamsInfo.setPackageName(interfaceInfo.getPackageName() + ".impl");
                        serviceParamsInfo.setPojoDesc(templateTypeEnum.getTemplateDesc());
                        serviceParamsInfo.setAuthor(interfaceInfo.getAuthor());
                        serviceParamsInfo.setPackageRoot(interfaceInfo.getPackageRoot());
                        TemplateInfo templateInfo = new TemplateInfo();
                        templateInfo.setTemplateId(TemplateTypeEnum.SERVICE.getTemplateId());
                        serviceParamsInfo.setTemplateInfo(templateInfo);
                        this.checkTemplate(serviceParamsInfo);
                        pojoParamsInfoMap.put(customJCheckBox.getId(), serviceParamsInfo);
                    } else {
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
                        pojoParamsInfoMap.put(customJCheckBox.getId(), pojoParamsInfo);
                    }
                }
            } else {
                pojoParamsInfoMap.remove(customJCheckBox.getId());
            }
        }
    }
}
