package com.code4j.component.dialog;

import com.code4j.component.panel.CommonPanel;
import com.code4j.component.panel.TopPanel;
import com.code4j.config.Code4jConstants;
import com.code4j.config.TemplateTypeEnum;
import com.code4j.pojo.ProjectCodeConfigInfo;
import com.code4j.util.CustomDialogUtil;
import com.code4j.util.PropertiesUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * @author lwp
 * @date 2022-03-12
 */
public class ProjectConfigDialog extends BaseDialog {
    private CommonPanel projectComponent;
    private CommonPanel doComponent;
    private CommonPanel voComponent;
    private CommonPanel xmlComponent;
    private CommonPanel mapperComponent;
    private CommonPanel serviceComponent;


    public ProjectConfigDialog(Component parentComponent, String title, boolean modal, Object extObj, boolean isUpdate) {
        super(parentComponent, title, modal, extObj, isUpdate);
    }

    @Override
    protected Component content() {
        ProjectCodeConfigInfo projectCodeConfigInfo = extObj != null ? (ProjectCodeConfigInfo) extObj : new ProjectCodeConfigInfo();
        Dimension inputDim = new Dimension(230, 30);
        JTextField j1 = new JTextField(projectCodeConfigInfo.getProjectName());
        j1.setPreferredSize(inputDim);
        projectComponent = new CommonPanel(new JLabel("项目名称："), j1);
        doComponent = createItemComponent(TemplateTypeEnum.DO, projectCodeConfigInfo.getDoPath(), projectCodeConfigInfo.getDoPackageName());
        voComponent = createItemComponent(TemplateTypeEnum.VO, projectCodeConfigInfo.getVoPath(), projectCodeConfigInfo.getVoPackageName());
        xmlComponent = createItemComponent(TemplateTypeEnum.XML, projectCodeConfigInfo.getXmlPath(), projectCodeConfigInfo.getXmlPackageName());
        mapperComponent = createItemComponent(TemplateTypeEnum.MAPPER, projectCodeConfigInfo.getMapperPath(), projectCodeConfigInfo.getMapperPackageName());
        serviceComponent = createItemComponent(TemplateTypeEnum.SERVICE_API, projectCodeConfigInfo.getServiceApiPath(), projectCodeConfigInfo.getServiceApiPackageName());
        Box box = Box.createVerticalBox();
        box.add(projectComponent);
        box.add(doComponent);
        box.add(voComponent);
        box.add(xmlComponent);
        box.add(mapperComponent);
        box.add(serviceComponent);
        CommonPanel commonPanel = new CommonPanel(box);
        return commonPanel;
    }

    @Override
    protected void okClick() {
        List<ProjectCodeConfigInfo> projectConfigPropertyValues = PropertiesUtil.getProjectConfigPropertyValues();
        ProjectCodeConfigInfo projectCodeConfigInfo = getProjectCodeConfigInfo(this.extObj, projectConfigPropertyValues);
        if (StringUtils.isBlank(projectCodeConfigInfo.getProjectName())) {
            CustomDialogUtil.showError("项目名称不能为空");
            return;
        }
        if (Code4jConstants.CONFIG_NAME.equals(projectCodeConfigInfo.getProjectName())) {
            CustomDialogUtil.showError("项目名称不可用");
            return;
        }
        if (CollectionUtils.isNotEmpty(projectConfigPropertyValues)) {
            if ((!this.isUpdate &&
                    projectConfigPropertyValues.stream().anyMatch(v -> projectCodeConfigInfo.getProjectName().equals(v.getProjectName())))
                    || this.isUpdate && projectConfigPropertyValues.stream().filter(v -> !v.getIndex().equals(projectCodeConfigInfo.getIndex())).anyMatch(v -> projectCodeConfigInfo.getProjectName().equals(v.getProjectName()))) {
                CustomDialogUtil.showError("项目名称不能重复");
                return;
            }
        }
        if (!PropertiesUtil.setProjectConfigPropertyValues(projectCodeConfigInfo, isUpdate)) {
            CustomDialogUtil.showError("保存配置失败");
            return;
        } else {
            ((TopPanel) parentComponent).loadProjectCodeConfig();
            this.close();
        }

    }

    @Override
    public CommonPanel bottomMid() {
        if (this.isUpdate) {
            CommonPanel commonPanel = new CommonPanel();
            JButton jButton = new JButton("删除配置");
            ProjectCodeConfigInfo projectCodeConfigInfo = (ProjectCodeConfigInfo) this.extObj;
            ProjectConfigDialog projectConfigDialog = this;
            jButton.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    CustomDialogUtil.confirm(projectConfigDialog, "确认删除", (c) -> {
                        if (PropertiesUtil.deleteProjectConfigProperty(projectCodeConfigInfo)) {
                            ((TopPanel) parentComponent).loadProjectCodeConfig();
                            projectConfigDialog.close();
                        }
                        return null;
                    });
                }
            });
            commonPanel.add(jButton);
            return commonPanel;
        }
        return null;
    }

    @Override
    public void afterOkClick() {
        super.afterOkClick();
    }

    /**
     * @param extObj
     * @param projectConfigPropertyValues
     * @return
     */
    private ProjectCodeConfigInfo getProjectCodeConfigInfo(Object extObj, List<ProjectCodeConfigInfo> projectConfigPropertyValues) {
        ProjectCodeConfigInfo projectCodeConfigInfo;
        if (extObj != null) {
            projectCodeConfigInfo = (ProjectCodeConfigInfo) extObj;
        } else {
            projectCodeConfigInfo = new ProjectCodeConfigInfo();
            Integer index = 0;
            if (!CollectionUtils.isEmpty(projectConfigPropertyValues)) {
                index = projectConfigPropertyValues.get(projectConfigPropertyValues.size() - 1).getIndex();
                index++;
            }
            projectCodeConfigInfo.setIndex(index);
        }
        JTextField pj = (JTextField) projectComponent.getComponent(1);
        projectCodeConfigInfo.setProjectName(pj.getText());
        projectCodeConfigInfo.setVoPackageName(getPackText(voComponent));
        projectCodeConfigInfo.setVoPath(getPathText(voComponent));
        projectCodeConfigInfo.setDoPackageName(getPackText(doComponent));
        projectCodeConfigInfo.setDoPath(getPathText(doComponent));
        projectCodeConfigInfo.setXmlPackageName(getPackText(xmlComponent));
        projectCodeConfigInfo.setXmlPath(getPathText(xmlComponent));
        projectCodeConfigInfo.setMapperPackageName(getPackText(mapperComponent));
        projectCodeConfigInfo.setMapperPath(getPathText(mapperComponent));
        projectCodeConfigInfo.setServiceApiPackageName(getPackText(serviceComponent));
        projectCodeConfigInfo.setServiceApiPath(getPathText(serviceComponent));
        return projectCodeConfigInfo;
    }

    /**
     * @param templateTypeEnum
     * @param defaultPath
     * @param defaultPackage
     * @return
     */
    public CommonPanel createItemComponent(TemplateTypeEnum templateTypeEnum, String defaultPath, String defaultPackage) {
        Dimension boxDimension = new Dimension(650, 70);
        Dimension inputDim = new Dimension(230, 25);
        Border lineBorder = BorderFactory.createLineBorder(Color.WHITE, 2);
        JTextField j2 = new JTextField(defaultPackage);
        j2.setPreferredSize(inputDim);
        JTextField j3 = new JTextField(defaultPath);
        j3.setPreferredSize(inputDim);
        CommonPanel c2 = new CommonPanel(new JLabel("包名称："), j2);
        CommonPanel c3 = new CommonPanel(new JLabel("包路径："), j3);
        Box hBox = Box.createHorizontalBox();
        hBox.add(c2);
        hBox.add(c3);
        CommonPanel itemCP = new CommonPanel(new FlowLayout(FlowLayout.LEFT), boxDimension);
        itemCP.setBorder(BorderFactory.createTitledBorder(lineBorder, templateTypeEnum.getTemplateDesc()));
        itemCP.addList(hBox);
        return itemCP;
    }

    /**
     * @param commonPanel
     * @return
     */
    private String getPackText(CommonPanel commonPanel) {
        Box box = (Box) commonPanel.getComponent(0);
        CommonPanel packC = (CommonPanel) box.getComponent(0);
        JTextField textField = (JTextField) packC.getComponent(1);
        return textField.getText();
    }

    /**
     * @param commonPanel
     * @return
     */
    private String getPathText(CommonPanel commonPanel) {
        Box box = (Box) commonPanel.getComponent(0);
        CommonPanel packC = (CommonPanel) box.getComponent(1);
        JTextField textField = (JTextField) packC.getComponent(1);
        return textField.getText();
    }

}
