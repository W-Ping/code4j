package com.code4j.component.dialog;

import com.code4j.component.CustomJTextField;
import com.code4j.component.label.LinkLabel;
import com.code4j.component.panel.CommonPanel;
import com.code4j.enums.TemplateTypeEnum;
import com.code4j.pojo.ProjectCodeConfigInfo;
import com.code4j.util.VFlowLayout;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author lwp
 * @date 2022-07-28
 */
public class PojoIgFieldDialog extends BaseDialog {
    private static final Dimension INPUT_DIMENSION = new Dimension(80, 28);
    private JScrollPane jScrollPane;
    private Box box;
    private List<CommonPanel> fieldPanelList;

    private ProjectCodeConfigInfo projectCodeConfigInfo;

    public PojoIgFieldDialog(Component parentComponent, String title, boolean modal, Object extObj) {
        super(parentComponent, title, modal, extObj);
    }

    @Override
    public void beforeInit() {
        this.setPreferredSize(new Dimension(250, 350));
    }

    @Override
    protected Component content() {
        this.initPojoIgFields((TemplateTypeEnum) extObj);
        box = Box.createVerticalBox();
        for (CommonPanel commonPanel : fieldPanelList) {
            box.add(commonPanel);
        }
        LinkLabel addLabel = new LinkLabel("新 增", Color.BLUE, (e) -> {
            final CommonPanel commonPanel = this.fieldPanel(null, true);
            fieldPanelList.add(commonPanel);
            box.add(commonPanel);
            updateUI();
        });
        final CommonPanel addCp = new CommonPanel(new FlowLayout(FlowLayout.RIGHT));
        addCp.add(addLabel);
        CommonPanel commonPanel = new CommonPanel(new VFlowLayout());
        commonPanel.setBorder(null);
        commonPanel.add(box);
        commonPanel.add(addCp);
        jScrollPane = new JScrollPane(commonPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane.setBorder(null);
        return jScrollPane;
    }

    private void initPojoIgFields(TemplateTypeEnum templateTypeEnum) {
        fieldPanelList = Optional.ofNullable(fieldPanelList).orElse(new ArrayList<>());
        projectCodeConfigInfo = (ProjectCodeConfigInfo) ((ProjectConfigDialog) parentComponent).getExtObj();
        String igFields = null;
        if (templateTypeEnum == TemplateTypeEnum.DO) {
            igFields = projectCodeConfigInfo.getDoIgFields();
        } else if (templateTypeEnum == TemplateTypeEnum.VO) {
            igFields = projectCodeConfigInfo.getVoIgFields();
        }
        if (StringUtils.isNotBlank(igFields)) {
            List<String> igFieldSet = Arrays.stream(igFields.split(",")).distinct().collect(Collectors.toList());
            for (int i = 0; i < igFieldSet.size(); i++) {
                fieldPanelList.add(this.fieldPanel(igFieldSet.get(i), true));
            }
        } else {
            fieldPanelList.add(this.fieldPanel(null, true));
            fieldPanelList.add(this.fieldPanel(null, true));
            fieldPanelList.add(this.fieldPanel(null, true));
            fieldPanelList.add(this.fieldPanel(null, true));
            fieldPanelList.add(this.fieldPanel(null, true));
            fieldPanelList.add(this.fieldPanel(null, true));
        }

    }

    @Override
    protected void okClick() {
        final TemplateTypeEnum templateTypeEnum = (TemplateTypeEnum) extObj;
        StringBuilder sb = new StringBuilder();
        for (CommonPanel commonPanel : fieldPanelList) {
            final CustomJTextField customJTextField = (CustomJTextField) commonPanel.getBindObject();
            final String text = customJTextField.getText();
            if (StringUtils.isNotBlank(text)) {
                sb.append(text);
                sb.append(",");
            }
        }
        String fields = "";
        if (sb.length() > 0) {
            fields = sb.substring(0, sb.lastIndexOf(","));
        }
        if ("null".equals(fields)) {
            fields = "";
        }
        if (TemplateTypeEnum.VO.equals(templateTypeEnum)) {
            projectCodeConfigInfo.setVoIgFields(fields);
        } else if (TemplateTypeEnum.DO.equals(templateTypeEnum)) {
            projectCodeConfigInfo.setDoIgFields(fields);
        }
        this.close();
    }

    private void updateUI() {
        //添加或删除组件后,更新窗口
        SwingUtilities.updateComponentTreeUI(this);
        //得到垂直滚动条
        JScrollBar jsb = jScrollPane.getVerticalScrollBar();
        //把滚动条位置设置到最下面
        jsb.setValue(jsb.getMaximum());

    }

    private CommonPanel fieldPanel(String field, boolean addDel) {
        final CustomJTextField customJTextField = new CustomJTextField(field, INPUT_DIMENSION);
        CommonPanel cp = new CommonPanel(new JLabel("字段"), customJTextField);
        cp.setBindObject(customJTextField);
        if (addDel) {
            LinkLabel addLabel = new LinkLabel("删 除", Color.RED, (text) -> {
                box.remove(cp);
                fieldPanelList.remove(cp);
                updateUI();
            });
            cp.add(addLabel);
        }
        return cp;
    }
}
