package com.code4j.component.dialog;

import com.code4j.component.panel.CommonPanel;
import com.code4j.component.panel.LeftPanel;
import com.code4j.component.panel.TopPanel;
import com.code4j.connect.DataSourceTypeEnum;
import com.code4j.connect.JDBCService;
import com.code4j.connect.JdbcServiceFactory;
import com.code4j.exception.Code4jException;
import com.code4j.pojo.JdbcDbInfo;
import com.code4j.pojo.JdbcSourceInfo;
import com.code4j.util.CustomDialogUtil;
import com.code4j.util.PropertiesUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * @author liu_wp
 * @date 2020/11/18
 * @see
 */
public class DBConfigDialog extends BaseDialog {
    private JTextField connectNameField;
    private JTextField connectHostField;
    private JTextField connectPortField;
    private JTextField nameField;
    private JPasswordField passwordField;
    private DataSourceTypeEnum dataSourceTypeEnum;

    public DBConfigDialog(final Component parentComponent, final String title, Object extObj, final DataSourceTypeEnum dataSourceTypeEnum) {
        super(parentComponent, title, true, extObj);
        this.dataSourceTypeEnum = dataSourceTypeEnum;
    }

    private JdbcSourceInfo getDefaultJdbcSourceInfo() {
        if (extObj != null && extObj instanceof JdbcSourceInfo) {
            return (JdbcSourceInfo) extObj;
        }
        return new JdbcSourceInfo();
//        List<JdbcSourceInfo> jdbcPropertyValues = PropertiesUtil.getJdbcPropertyValues();
//        return CollectionUtils.isNotEmpty(jdbcPropertyValues) ? jdbcPropertyValues.get(jdbcPropertyValues.size() - 1) : new JdbcSourceInfo();
    }


    @Override
    protected Component content() {
        JdbcSourceInfo jdbcSourceInfo = getDefaultJdbcSourceInfo();
        String port = String.valueOf(jdbcSourceInfo.getConnectPort() == null ? 3306 : jdbcSourceInfo.getConnectPort()).trim();
        Dimension inputDim = new Dimension(230, 30);
        CommonPanel c1 = new CommonPanel();
        JLabel l1 = new JLabel("连接名称：");
        connectNameField = new JTextField(jdbcSourceInfo.getConnectName());
        connectNameField.setPreferredSize(inputDim);
        c1.add(l1);
        c1.add(connectNameField);
        CommonPanel c2 = new CommonPanel();
        JLabel l2 = new JLabel("连接地址：");
        connectHostField = new JTextField(jdbcSourceInfo.getConnectHost());
        connectHostField.setPreferredSize(inputDim);
        c2.add(l2);
        c2.add(connectHostField);
        CommonPanel c3 = new CommonPanel();
        JLabel l3 = new JLabel("         端口：");
        connectPortField = new JTextField(port);
        connectPortField.setPreferredSize(inputDim);
        c3.add(l3);
        c3.add(connectPortField);
        CommonPanel c4 = new CommonPanel();
        JLabel l4 = new JLabel("     用户名：");
        nameField = new JTextField(jdbcSourceInfo.getUserName());
        nameField.setPreferredSize(inputDim);
        c4.add(l4);
        c4.add(nameField);
        CommonPanel c5 = new CommonPanel();
        JLabel l5 = new JLabel("         密码：");
        passwordField = new JPasswordField(jdbcSourceInfo.getPassword());
        passwordField.setPreferredSize(inputDim);
        c5.add(l5);
        c5.add(passwordField);
        Box box = Box.createVerticalBox();
        box.add(c1);
        box.add(c2);
        box.add(c3);
        box.add(c4);
        box.add(c5);
        CommonPanel commonPanel = new CommonPanel();
        commonPanel.add(box);
        return commonPanel;
    }

    @Override
    public void afterInit() {
        this.pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    @Override
    public CommonPanel bottomMid() {
        CommonPanel commonPanel = new CommonPanel();
        JButton jButton = new JButton("测试连接");
        jButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                getConnectInfo(false);
            }
        });
        commonPanel.add(jButton);
        return commonPanel;
    }

    @Override
    protected void okClick() {
        this.getConnectInfo(true);
    }

    private void getConnectInfo(boolean isSave) {
        this.checkParams();
        JdbcSourceInfo jdbcSourceInfo = new JdbcSourceInfo();
        jdbcSourceInfo.setConnectName(connectNameField.getText());
        jdbcSourceInfo.setConnectHost(connectHostField.getText());
        jdbcSourceInfo.setConnectPort(Integer.valueOf(connectPortField.getText()));
        jdbcSourceInfo.setUserName(nameField.getText());
        jdbcSourceInfo.setPassword(new String(passwordField.getPassword()));
        jdbcSourceInfo.setDataSourceTypeEnum(dataSourceTypeEnum);
        //编辑
        if (extObj != null) {
            jdbcSourceInfo.setCurrTreeNode(((JdbcSourceInfo) extObj).getCurrTreeNode());
        }
        ((CommonPanel) contentPanel).setBindObject(jdbcSourceInfo);
        JDBCService jdbcService = JdbcServiceFactory.getJdbcService(jdbcSourceInfo);
        if (!jdbcService.test()) {
            CustomDialogUtil.showError("连接失败！");
            return;
        }
        if (isSave) {
            //新增
            if (extObj == null) {
                List<JdbcSourceInfo> jdbcPropertyValues = PropertiesUtil.getJdbcPropertyValues();
                if (CollectionUtils.isEmpty(jdbcPropertyValues)) {
                    jdbcSourceInfo.setIndex(0);
                } else {
                    //最后一条
                    Integer index = jdbcPropertyValues.get(jdbcPropertyValues.size() - 1).getIndex();
                    jdbcSourceInfo.setIndex(++index);
                }
            } else {
                jdbcSourceInfo.setIndex(((JdbcSourceInfo) extObj).getIndex());
            }
            List<JdbcDbInfo> jdbcDbInfos = jdbcService.getAllJdbcDbInfo();
            if (parentComponent instanceof TopPanel || parentComponent instanceof LeftPanel) {
                LeftPanel leftPanel;
                if (parentComponent instanceof TopPanel) {
                    TopPanel topPanel = (TopPanel) parentComponent;
                    leftPanel = topPanel.getLeftPanel();
                } else {
                    leftPanel = (LeftPanel) parentComponent;
                }
                jdbcSourceInfo.setJdbcDbInfos(jdbcDbInfos);
                leftPanel.editTreeNode(jdbcSourceInfo);
                this.close();
            }
        } else {
            CustomDialogUtil.showOk("连接成功！");
        }
    }

    private void checkParams() {
        if (StringUtils.isBlank(connectNameField.getText())) {
            CustomDialogUtil.showError("连接名称不能为空");
            throw new Code4jException("连接名称不能为空!");
        }
        if (StringUtils.isBlank(connectHostField.getText())) {
            CustomDialogUtil.showError("连接地址不能为空");
            throw new Code4jException("连接地址不能为空!");
        }
        if (StringUtils.isBlank(connectPortField.getText())) {
            CustomDialogUtil.showError("端口不能为空");
            throw new Code4jException("端口不能为空!");
        }
        if (StringUtils.isBlank(nameField.getText())) {
            CustomDialogUtil.showError("用户名不能为空");
            throw new Code4jException("用户名不能为空!");
        }
        if (passwordField.getPassword() == null || passwordField.getPassword().length <= 0) {
            CustomDialogUtil.showError("密码不能为空");
            throw new Code4jException("密码不能为空!");
        }
    }

}
