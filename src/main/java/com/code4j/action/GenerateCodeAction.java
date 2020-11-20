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
        this.pojoParamsInfoMap.clear();
        this.customJFileChooserPanel = customJFileChooserPanel;
        this.customJCheckBoxes = customJCheckBoxes;
        this.jdbcTableInfo = jdbcTableInfo;
        this.jdbcSourceInfo = jdbcSourceInfo;
        this.jdbcService = JdbcServiceFactory.getJdbcService(jdbcSourceInfo);
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
        List<JdbcMapJavaInfo> tableColumnInfos = jdbcService.getTableColumnInfo(jdbcTableInfo.getDbName(), jdbcTableInfo.getTableName());
        for (Map.Entry<String, ? extends BaseTemplateInfo> mp : pojoParamsInfoMap.entrySet()) {
            String templateId = mp.getKey();
            TemplateInfo templateInfo = new TemplateInfo();
            templateInfo.setTemplateId(templateId);
            BaseTemplateInfo baseTemplateInfo = mp.getValue();
            Map<String, Object> dataMap;
            String packageRoot = null;
            JTextField field = (JTextField) customJFileChooserPanel.getExtObject();
            if (StringUtils.isNotBlank(field.getText())) {
                packageRoot = field.getText().trim() + ".";
            } else {
                if (customJFileChooserPanel.getFile() != null) {
                    packageRoot = Code4jConstants.DEFAULT_ROOT_PACKAGE + "." + customJFileChooserPanel.getFile().getName() + ".";
                }
            }
            if (TemplateTypeEnum.XML.getTemplateId().equals(templateId)) {
                dataMap = convertXmlTemplateData(baseTemplateInfo, packageRoot, tableColumnInfos);
            } else {
                baseTemplateInfo.setPackageRoot(packageRoot);
                dataMap = convertPojoTemplateData(baseTemplateInfo, tableColumnInfos);
            }
            String codePath = FreemarkerUtil.generateCodeByTemplate(projectPath, templateInfo, mp.getValue(), dataMap);
            //恢复
            baseTemplateInfo.setPackageRoot(null);
            System.out.println("templateId:" + codePath);
        }
    }

    /**
     * @param baseTemplateInfo
     * @param tableColumnInfos
     * @return
     */
    private Map<String, Object> convertPojoTemplateData(BaseTemplateInfo baseTemplateInfo, List<JdbcMapJavaInfo> tableColumnInfos) {
        PojoParamsInfo pojoParamsInfo = (PojoParamsInfo) baseTemplateInfo;
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("columnPackages", columnPackage(tableColumnInfos));
        dataMap.put("tableColumnInfos", tableColumnInfos);
        dataMap.put("pojo", pojoParamsInfo);
        return dataMap;
    }

    /**
     * @param baseTemplateInfo
     * @param packageRoot
     * @param tableColumnInfos
     * @return
     */
    private Map<String, Object> convertXmlTemplateData(BaseTemplateInfo baseTemplateInfo, String packageRoot, List<JdbcMapJavaInfo> tableColumnInfos) {
        BaseTemplateInfo doPojo = pojoParamsInfoMap.get(TemplateTypeEnum.DO.getTemplateId());
        BaseTemplateInfo mapperPojo = pojoParamsInfoMap.get(TemplateTypeEnum.MAPPER.getTemplateId());
        XMLParamsInfo xmlParamsInfo = (XMLParamsInfo) baseTemplateInfo;
        xmlParamsInfo.setTableColumnInfos(tableColumnInfos);
        if (mapperPojo != null) {
            xmlParamsInfo.setNamespace(StringUtils.isNotBlank(packageRoot) ? packageRoot + mapperPojo.getPackagePath() : mapperPojo.getPackagePath());
        }
        if (doPojo != null) {
            xmlParamsInfo.setResultMapType(StringUtils.isNotBlank(packageRoot) ? packageRoot + doPojo.getPackagePath() : doPojo.getPackagePath());
        }
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("xmlMap", xmlParamsInfo);
        return dataMap;
    }

    private List<String> columnPackage(List<JdbcMapJavaInfo> tableColumnInfos) {
        List<String> packageList = new ArrayList<>();
        for (final JdbcMapJavaInfo tableColumnInfo : tableColumnInfos) {
            String javaType = tableColumnInfo.getJavaType();
            if (javaType.contains(".")) {
                if (javaType.startsWith("java.lang")) {
                    continue;
                } else {
                    packageList.add(javaType);
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
     * @param jdbcTableInfo
     * @param jdbcSourceInfo
     */
    private void setPojoParams() {
        for (final CustomJCheckBox customJCheckBox : customJCheckBoxes) {
            if (customJCheckBox.isSelected()) {
                CommonPanel commonPanel = customJCheckBox.getBindCommonPanel();
                JTextField packNameT = (JTextField) ((CommonPanel) commonPanel.getComponent(0)).getComponent(1);
                JTextField packageT = (JTextField) ((CommonPanel) commonPanel.getComponent(1)).getComponent(1);
                JTextField pojoPathT = (JTextField) ((CommonPanel) commonPanel.getComponent(2)).getComponent(1);
                if (TemplateTypeEnum.XML.getTemplateId().equals(customJCheckBox.getId())) {
                    XMLParamsInfo xmlParamsInfo = new XMLParamsInfo();
                    xmlParamsInfo.setPojoType(customJCheckBox.getId());
                    xmlParamsInfo.setPojoName(packNameT.getText());
                    xmlParamsInfo.setPackageName(packageT.getText());
                    xmlParamsInfo.setPojoPath(pojoPathT.getText());
                    xmlParamsInfo.setAuthor(jdbcSourceInfo.getCreator());
                    xmlParamsInfo.setJdbcTableInfo(jdbcTableInfo);
                    xmlParamsInfo.setPojoDesc(TemplateTypeEnum.XML.getTemplateDesc());
                    this.checkTemplate(xmlParamsInfo);
                    pojoParamsInfoMap.put(customJCheckBox.getId(), xmlParamsInfo);
                } else {
                    PojoParamsInfo pojoParamsInfo = new PojoParamsInfo();
                    TemplateTypeEnum templateTypeEnum = TemplateTypeEnum.getTemplateTypeEnum(customJCheckBox.getId());
                    pojoParamsInfo.setPojoDesc(templateTypeEnum.getTemplateDesc());
                    pojoParamsInfo.setPojoType(customJCheckBox.getId());
                    pojoParamsInfo.setPojoName(packNameT.getText());
                    pojoParamsInfo.setPackageName(packageT.getText());
                    pojoParamsInfo.setPojoPath(pojoPathT.getText());
                    pojoParamsInfo.setAuthor(jdbcSourceInfo.getCreator());
                    pojoParamsInfo.setJdbcTableInfo(jdbcTableInfo);
                    this.checkTemplate(pojoParamsInfo);
                    pojoParamsInfoMap.put(customJCheckBox.getId(), pojoParamsInfo);
                }
            } else {
                pojoParamsInfoMap.remove(customJCheckBox.getId());
            }
        }
    }
}
