package com.code4j.component.dialog;

import com.code4j.component.CustomJCheckBox;
import com.code4j.component.panel.CommonPanel;
import com.code4j.exception.Code4jException;
import com.code4j.pojo.JdbcMapJavaInfo;
import com.code4j.util.CustomDialogUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liu_wp
 * @date 2020/11/23
 * @see
 */
public class TableConfigDialog extends BaseDialog {
    private List<CustomJCheckBox> selectCustomJCheckBoxList;

    public TableConfigDialog(final Component parentComponent, final String title, final boolean modal, final Object extObj) {
        super(parentComponent, title, modal, extObj);
    }

    @Override
    public void beforeInit() {
        selectCustomJCheckBoxList = new ArrayList<>();
    }

    @Override
    public void afterInit() {
        this.setPreferredSize(new Dimension(130 * 4 + 140, 500));
        this.pack();
        setLocationRelativeTo(null);
//        setResizable(false);
        setVisible(true);
    }

    @Override
    protected Component content() {
        CommonPanel commonPanel = new CommonPanel();
        Box box = Box.createVerticalBox();
        CommonPanel parentComponent = (CommonPanel) super.getParentComponent();
        List<JdbcMapJavaInfo> tableColumnInfos = (List<JdbcMapJavaInfo>) parentComponent.getBindObject();
        if (CollectionUtils.isNotEmpty(tableColumnInfos)) {
            Dimension dimension = new Dimension(130, 25);
            Dimension checkDimension = new Dimension(60, 25);
            CommonPanel c1 = new CommonPanel();
            JLabel j1 = new JLabel("数据库字段");
            JLabel j2 = new JLabel("类型");
            JLabel j3 = new JLabel("Java字段");
            JLabel j4 = new JLabel("类型");
            JLabel j5 = new JLabel("是否生成");
            j1.setPreferredSize(dimension);
            j2.setPreferredSize(dimension);
            j3.setPreferredSize(dimension);
            j4.setPreferredSize(dimension);
            j5.setPreferredSize(checkDimension);
            c1.addList(j1, j2, j3, j4, j5);
            box.add(c1);
            for (int i = 0; i < tableColumnInfos.size(); i++) {
                JdbcMapJavaInfo tableColumnInfo = tableColumnInfos.get(i);
                CommonPanel c2 = new CommonPanel();
                JTextField j1v = new JTextField(tableColumnInfo.getColumn());
                JTextField j2v = new JTextField(tableColumnInfo.getJdbcType());
                JTextField j3v = new JTextField(tableColumnInfo.getJavaProperty());
                JTextField j4v = new JTextField(tableColumnInfo.getJavaType());
                CustomJCheckBox j5v = new CustomJCheckBox("", !tableColumnInfo.isIgnore(), i + "");
                j5v.setBindObject(tableColumnInfo);
                j1v.setEditable(false);
                j2v.setEditable(false);
                j1v.setPreferredSize(dimension);
                j2v.setPreferredSize(dimension);
                j3v.setPreferredSize(dimension);
                j4v.setPreferredSize(dimension);
                j5v.setPreferredSize(checkDimension);
                j5v.setBindComponent(c2);
                selectCustomJCheckBoxList.add(j5v);
                c2.addList(j1v, j2v, j3v, j4v, j5v);
                box.add(c2);
            }
        }
        commonPanel.addList(box);
        JScrollPane jScrollPane = new JScrollPane(commonPanel);
//        JScrollPane jScrollPane = new JScrollPane(commonPanel) {
//            @Override
//            public Dimension getPreferredSize() {
//                return getJScrollPaneDimension();
//            }
//        };
        jScrollPane.setBorder(null);
        return jScrollPane;
    }

    public Dimension getJScrollPaneDimension() {
        Dimension preferredSize = this.getMaximumSize();
        return new Dimension((int) preferredSize.getWidth() - 5, (int) preferredSize.getHeight() - 5);
    }

    @Override
    protected void okClick() {
        List<JdbcMapJavaInfo> jdbcMapJavaInfos = new ArrayList<>();
        for (int i = 0; i < selectCustomJCheckBoxList.size(); i++) {
            CustomJCheckBox customJCheckBox = selectCustomJCheckBoxList.get(i);
            JdbcMapJavaInfo tableColumnInfo = (JdbcMapJavaInfo) customJCheckBox.getBindObject();
            checkData(tableColumnInfo);
            JdbcMapJavaInfo newTableColumnInfo = new JdbcMapJavaInfo();
            newTableColumnInfo.setColumn(tableColumnInfo.getColumn());
            newTableColumnInfo.setJdbcType(tableColumnInfo.getJdbcType());
            newTableColumnInfo.setComment(tableColumnInfo.getComment());
            if (customJCheckBox.isSelected()) {
                JComponent bindComponent = customJCheckBox.getBindComponent();
                JTextField javaPropertyT = (JTextField) bindComponent.getComponent(2);
                JTextField javaTypeT = (JTextField) bindComponent.getComponent(3);
                newTableColumnInfo.setJavaProperty(javaPropertyT.getText());
                newTableColumnInfo.setJavaType(javaTypeT.getText());
                newTableColumnInfo.setIgnore(false);
            } else {
                newTableColumnInfo.setJavaType(tableColumnInfo.getJavaType());
                newTableColumnInfo.setJavaProperty(tableColumnInfo.getJavaProperty());
                newTableColumnInfo.setIgnore(true);
            }
            checkData(newTableColumnInfo);
            jdbcMapJavaInfos.add(newTableColumnInfo);
        }
        CommonPanel parentComponent = (CommonPanel) super.getParentComponent();
        //重新绑定值
        parentComponent.setBindObject(jdbcMapJavaInfos);
        this.close();
    }

    private void checkData(JdbcMapJavaInfo jdbcMapJavaInfo) {
        if (StringUtils.isBlank(jdbcMapJavaInfo.getColumn())) {
            CustomDialogUtil.showError("数据库字段为空");
            throw new Code4jException("数据库字段类型为空");
        }
        if (StringUtils.isBlank(jdbcMapJavaInfo.getJdbcType())) {
            CustomDialogUtil.showError("【" + jdbcMapJavaInfo.getColumn() + "】数据库字段类型为空");
            throw new Code4jException("【" + jdbcMapJavaInfo.getColumn() + "】数据库字段类型为空");
        }
        if (StringUtils.isBlank(jdbcMapJavaInfo.getJavaProperty())) {
            CustomDialogUtil.showError("【" + jdbcMapJavaInfo.getColumn() + "】Java字段类型为空");
            throw new Code4jException("【" + jdbcMapJavaInfo.getColumn() + "】Java字段类型为空");
        }
        if (StringUtils.isBlank(jdbcMapJavaInfo.getJavaType())) {
            CustomDialogUtil.showError("【" + jdbcMapJavaInfo.getColumn() + "】Java字段类型为空");
            throw new Code4jException("【" + jdbcMapJavaInfo.getColumn() + "】Java字段类型为空");
        }
    }
}
