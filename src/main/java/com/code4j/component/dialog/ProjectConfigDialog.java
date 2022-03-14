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
        serviceComponent = createItemComponent(TemplateTypeEnum.SERVICE_API, projectCodeConfigInfo.getServiceApiPath(), projectCodeConfigInfo.getServiceApiPath());
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
        Integer index = 0;
        if (!CollectionUtils.isEmpty(projectConfigPropertyValues)) {
            index = projectConfigPropertyValues.get(projectConfigPropertyValues.size() - 1).getIndex();
            if (!this.isUpdate) {
                index++;
            }
        }
        ProjectCodeConfigInfo projectCodeConfigInfo = getProjectCodeConfigInfo(index);
        if (StringUtils.isBlank(projectCodeConfigInfo.getProjectName())) {
            CustomDialogUtil.showError("项目名称不能为空");
            return;
        }
        if (Code4jConstants.CONFIG_NAME.equals(projectCodeConfigInfo.getProjectName())) {
            CustomDialogUtil.showError("项目名称不可用");
            return;
        }
        TopPanel topPanel = (TopPanel) parentComponent;
        topPanel.addProjectCodeConfig(this, projectCodeConfigInfo, projectConfigPropertyValues);

    }


    /**
     * @param index
     * @return
     */
    public ProjectCodeConfigInfo getProjectCodeConfigInfo(Integer index) {
        ProjectCodeConfigInfo projectCodeConfigInfo = new ProjectCodeConfigInfo();
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
        projectCodeConfigInfo.setIndex(index);
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
