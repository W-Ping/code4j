package com.code4j.component.dialog;

import com.code4j.component.CustomJRadioButton;
import com.code4j.component.panel.RightPanel;
import com.code4j.pojo.ProjectCodeConfigInfo;
import com.code4j.util.CustomDialogUtil;
import org.apache.commons.collections4.CollectionUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author lwp
 * @date 2022-03-12
 */
public class SelectProjectConfigDialog extends BaseDialog {

    private List<CustomJRadioButton> buttonList;

    public SelectProjectConfigDialog(Component parentComponent, String title, boolean modal, Object extObj) {
        super(parentComponent, title, modal, extObj);
    }

    @Override
    protected Component content() {
        Box box = Box.createHorizontalBox();
        buttonList = new ArrayList<>();
        if (extObj != null) {
            List<ProjectCodeConfigInfo> projectConfigPropertyValues = (List<ProjectCodeConfigInfo>) extObj;
            if (!CollectionUtils.isEmpty(projectConfigPropertyValues)) {
                ButtonGroup buttonGroup = new ButtonGroup();
                for (ProjectCodeConfigInfo value : projectConfigPropertyValues) {
                    CustomJRadioButton jRadioButton = new CustomJRadioButton(value.getProjectName(), value.getProjectName(), value);
                    buttonGroup.add(jRadioButton);
                    box.add(jRadioButton);
                    buttonList.add(jRadioButton);
                }
            } else {
                JLabel jLabel = new JLabel("还没有配置项目");
                box.add(jLabel);
            }
        }
        return box;
    }

    @Override
    protected void okClick() {
        if (buttonList != null) {
            Optional<CustomJRadioButton> first = buttonList.stream().filter(v -> v.isSelected()).findFirst();
            if (first.isPresent()) {
                CustomJRadioButton radioButton = first.get();
                ProjectCodeConfigInfo selectProjectCodeConfigInfo = (ProjectCodeConfigInfo) radioButton.getData();
                if (parentComponent != null) {
                    RightPanel rightPanel = (RightPanel) parentComponent;
                    rightPanel.loadProjectConfig(selectProjectCodeConfigInfo);
                }
            }else{
                CustomDialogUtil.showError("请选择项目配置");
            }
        }
    }
}
