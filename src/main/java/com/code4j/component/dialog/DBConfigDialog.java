package com.code4j.component.dialog;

import com.code4j.component.panel.CommonPanel;
import com.code4j.component.panel.LeftPanel;
import com.code4j.component.panel.TopPanel;
import com.code4j.connect.DataSourceTypeEnum;
import com.code4j.connect.JDBCService;
import com.code4j.connect.JdbcServiceFactory;
import com.code4j.exception.Code4jException;
import com.code4j.pojo.JdbcSourceInfo;
import com.code4j.util.CustomDialogUtil;
import com.code4j.util.JSONUtil;
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

    public DBConfigDialog(final Component parentComponent, final String title, final DataSourceTypeEnum dataSourceTypeEnum) {
        super(parentComponent, title, true, dataSourceTypeEnum);
    }

    @Override
    protected Component content() {
        List<JdbcSourceInfo> jdbcPropertyValues = PropertiesUtil.getJdbcPropertyValues();
        JdbcSourceInfo jdbcSourceInfo = new JdbcSourceInfo();
        if (CollectionUtils.isNotEmpty(jdbcPropertyValues)) {
            jdbcSourceInfo = jdbcPropertyValues.get(jdbcPropertyValues.size() - 1);
        }
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
        JdbcSourceInfo JDBCSourceInfo = new JdbcSourceInfo();
        JDBCSourceInfo.setConnectName(connectNameField.getText());
        JDBCSourceInfo.setConnectHost(connectHostField.getText());
        JDBCSourceInfo.setConnectPort(Integer.valueOf(connectPortField.getText()));
        JDBCSourceInfo.setUserName(nameField.getText());
        JDBCSourceInfo.setPassword(new String(passwordField.getPassword()));
        JDBCSourceInfo.setDataSourceTypeEnum((DataSourceTypeEnum) extObj);
        ((CommonPanel) contentPanel).setBindObject(JDBCSourceInfo);
        JDBCService jdbcService = JdbcServiceFactory.getJdbcService(JDBCSourceInfo);
        if (!jdbcService.test()) {
            CustomDialogUtil.showError("连接失败！");
            return;
        }
        if (isSave) {
            List<String> allDbName = jdbcService.getAllDbName();
            System.out.println(JSONUtil.Object2JSON(allDbName));
            if (parentComponent instanceof TopPanel) {
                TopPanel topPanel = (TopPanel) parentComponent;
                LeftPanel leftPanel = topPanel.getLeftPanel();
                leftPanel.showTreeView(allDbName, JDBCSourceInfo);
                if (!PropertiesUtil.setJdbcPropertyValues(JDBCSourceInfo)) {
                    CustomDialogUtil.showError("保存数据连接失败");
                    return;
                }
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
