package com.code4j.component.dialog;

import com.code4j.component.panel.CommonPanel;
import com.code4j.component.panel.TopPanel;
import com.code4j.config.Code4jConstants;
import com.code4j.config.TemplateTypeEnum;
import com.code4j.pojo.ProjectCodeConfigInfo;
import com.code4j.util.CustomDialogUtil;
import com.code4j.util.SQLiteUtil;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;

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
        ProjectCodeConfigInfo projectCodeConfigInfo = extObj != null ? (ProjectCodeConfigInfo) extObj : new ProjectCodeConfigInfo(true);
        Dimension inputDim = new Dimension(230, 30);
        JTextField j1 = new JTextField(projectCodeConfigInfo.getProjectName());
        j1.setPreferredSize(inputDim);
        projectComponent = new CommonPanel(new JLabel("项目名称："), j1);
        doComponent = createItemComponent(TemplateTypeEnum.DO, projectCodeConfigInfo.getDoPath(), projectCodeConfigInfo.getDoPackageName(), projectCodeConfigInfo.getDoSuperClass());
        voComponent = createItemComponent(TemplateTypeEnum.VO, projectCodeConfigInfo.getVoPath(), projectCodeConfigInfo.getVoPackageName(), projectCodeConfigInfo.getVoSuperClass());
        xmlComponent = createItemComponent(TemplateTypeEnum.XML, projectCodeConfigInfo.getXmlPath(), projectCodeConfigInfo.getXmlPackageName(), null);
        mapperComponent = createItemComponent(TemplateTypeEnum.MAPPER, projectCodeConfigInfo.getMapperPath(), projectCodeConfigInfo.getMapperPackageName(), projectCodeConfigInfo.getMapperSuperClass());
        serviceComponent = createItemComponent(TemplateTypeEnum.SERVICE_API, projectCodeConfigInfo.getServiceApiPath(), projectCodeConfigInfo.getServiceApiPackageName(), projectCodeConfigInfo.getServiceSuperClass());
        Box box = Box.createVerticalBox();
        box.add(projectComponent);
        box.add(doComponent);
        box.add(voComponent);
        box.add(serviceComponent);
        box.add(mapperComponent);
        box.add(xmlComponent);
        CommonPanel commonPanel = new CommonPanel(box);
        return commonPanel;
    }

    @Override
    protected void okClick() {
        ProjectCodeConfigInfo projectCodeConfigInfo = getProjectCodeConfigInfo(this.extObj);
        if (StringUtils.isBlank(projectCodeConfigInfo.getProjectName())) {
            CustomDialogUtil.showError("项目名称不能为空");
            return;
        }
        if (Code4jConstants.CONFIG_NAME.equals(projectCodeConfigInfo.getProjectName())) {
            CustomDialogUtil.showError("项目名称不可用");
            return;
        }
        if (!SQLiteUtil.insertOrUpdate(projectCodeConfigInfo)) {
            CustomDialogUtil.showError("保存配置失败！");
            return;
        } else {
            CustomDialogUtil.showOk("保存成功！", true, (bol) -> {
                ((TopPanel) parentComponent).loadProjectCodeConfig("save");
                this.close();
            });
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
                        if (SQLiteUtil.deleteByPk(projectCodeConfigInfo.getId(), ProjectCodeConfigInfo.class)) {
                            ((TopPanel) parentComponent).loadProjectCodeConfig("delete");
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
     * @return
     */
    private ProjectCodeConfigInfo getProjectCodeConfigInfo(Object extObj) {
        ProjectCodeConfigInfo projectCodeConfigInfo;
        if (extObj != null) {
            projectCodeConfigInfo = (ProjectCodeConfigInfo) extObj;
        } else {
            projectCodeConfigInfo = new ProjectCodeConfigInfo();
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
        projectCodeConfigInfo.setDoSuperClass(getSupperText(doComponent));
        projectCodeConfigInfo.setVoSuperClass(getSupperText(voComponent));
        projectCodeConfigInfo.setMapperSuperClass(getSupperText(mapperComponent));
        projectCodeConfigInfo.setServiceSuperClass(getSupperText(serviceComponent));
        return projectCodeConfigInfo;
    }

    /**
     * @param templateTypeEnum
     * @param defaultPath
     * @param defaultPackage
     * @return
     */
    public CommonPanel createItemComponent(TemplateTypeEnum templateTypeEnum, String defaultPath, String defaultPackage, String superClass) {
        Dimension boxDimension = new Dimension(820, 70);
        Dimension inputDim = new Dimension(230, 25);
        Border lineBorder = BorderFactory.createLineBorder(Color.WHITE, 2);
        JTextField j2 = new JTextField(defaultPackage);
        j2.setPreferredSize(inputDim);
        JTextField j3 = new JTextField(defaultPath);
        j3.setPreferredSize(new Dimension(150, 25));
        CommonPanel c2 = new CommonPanel(new JLabel("包 名："), j2);
        CommonPanel c3 = new CommonPanel(new JLabel("包路径："), j3);
        Box hBox = Box.createHorizontalBox();
        hBox.add(c2);
        hBox.add(c3);
        if (TemplateTypeEnum.XML != templateTypeEnum) {
            JTextField j4 = new JTextField(superClass);
            j4.setPreferredSize(inputDim);
            j4.setToolTipText("父类全路径");
            CommonPanel c4 = new CommonPanel(new JLabel("父 类："), j4);
            hBox.add(c4);
        }
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

    /**
     * @param commonPanel
     * @return
     */
    private String getSupperText(CommonPanel commonPanel) {
        Box box = (Box) commonPanel.getComponent(0);
        CommonPanel packC = (CommonPanel) box.getComponent(2);
        JTextField textField = (JTextField) packC.getComponent(1);
        return textField.getText();
    }
}
