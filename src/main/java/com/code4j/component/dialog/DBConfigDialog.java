package com.code4j.component.dialog;

import com.code4j.component.CustomJTextField;
import com.code4j.component.LinkTree;
import com.code4j.component.panel.CommonPanel;
import com.code4j.component.panel.LeftPanel;
import com.code4j.component.panel.TopPanel;
import com.code4j.connect.JDBCService;
import com.code4j.connect.JdbcServiceFactory;
import com.code4j.enums.DataSourceTypeEnum;
import com.code4j.exception.Code4jException;
import com.code4j.pojo.JdbcSourceInfo;
import com.code4j.util.CustomDialogUtil;
import com.code4j.util.JSONUtil;
import com.code4j.util.SQLiteUtil;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Optional;

/**
 * 数据库连接 对话框
 *
 * @author liu_wp
 * @date 2020/11/18
 * @see
 */
public class DBConfigDialog extends BaseDialog {


    private CustomJTextField connectNameField;
    private CustomJTextField connectHostField;
    private CustomJTextField connectPortField;
    private CustomJTextField nameField;

    private CustomJTextField initDbFiled;
    private JPasswordField passwordField;
    private JdbcSourceInfo defaultJdbcSourceInfo;

    public DBConfigDialog(final Component parentComponent, final String title, Object extObj) {
        super(parentComponent, title, true, extObj, true);
    }

    private JdbcSourceInfo getDefaultJdbcSourceInfo() {
        if (extObj != null && extObj instanceof JdbcSourceInfo) {
            return (JdbcSourceInfo) extObj;
        }
        return new JdbcSourceInfo();
    }

    @Override
    public void beforeInit() {
        this.setPreferredSize(new Dimension(350, 320));
    }

    @Override
    protected Component content() {
        defaultJdbcSourceInfo = getDefaultJdbcSourceInfo();
        String port = String.valueOf(defaultJdbcSourceInfo.getConnectPort() == null ? defaultJdbcSourceInfo.getDataSourceTypeEnum().defaultPort() : defaultJdbcSourceInfo.getConnectPort());
        Dimension leftDim = new Dimension(80, 28);
        Dimension rightDim = new Dimension(200, 28);
        CommonPanel c1 = new CommonPanel(new CommonPanel(leftDim, new JLabel("连接名称：")), connectNameField = new CustomJTextField(defaultJdbcSourceInfo.getConnectName(), rightDim));
        CommonPanel c2 = new CommonPanel(new CommonPanel(leftDim, new JLabel("连接地址：")), connectHostField = new CustomJTextField(defaultJdbcSourceInfo.getConnectHost(), rightDim));
        CommonPanel c3 = new CommonPanel(new CommonPanel(leftDim, new JLabel("端口：")), connectPortField = new CustomJTextField(port, rightDim));
        CommonPanel c5 = new CommonPanel(new CommonPanel(leftDim, new JLabel("用户名：")), nameField = new CustomJTextField(defaultJdbcSourceInfo.getUserName(), rightDim));
        CommonPanel c6 = new CommonPanel(new CommonPanel(leftDim, new JLabel("密码：")), passwordField = new JPasswordField(defaultJdbcSourceInfo.getPassword()));
        passwordField.setPreferredSize(rightDim);
        Box box = Box.createVerticalBox();
        box.add(c1);
        box.add(c2);
        box.add(c3);
        if (defaultJdbcSourceInfo.getDataSourceTypeEnum() == DataSourceTypeEnum.POSTGRESQL) {
            CommonPanel c4 = new CommonPanel(new CommonPanel(leftDim, new JLabel("初始数据库：")), initDbFiled = new CustomJTextField(Optional.ofNullable(defaultJdbcSourceInfo.getInitDb()).orElse(DataSourceTypeEnum.POSTGRESQL.defaultInitDb()), rightDim));
            box.add(c4);
        }
        box.add(c5);
        box.add(c6);
        return new CommonPanel(box);
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
        jdbcSourceInfo.setId(defaultJdbcSourceInfo.getId());
        jdbcSourceInfo.setConnectName(connectNameField.getText().trim());
        jdbcSourceInfo.setConnectHost(connectHostField.getText().trim());
        jdbcSourceInfo.setConnectPort(Integer.valueOf(connectPortField.getText()));
        jdbcSourceInfo.setUserName(nameField.getText().trim());
        jdbcSourceInfo.setPassword(new String(passwordField.getPassword()));
        jdbcSourceInfo.setSourceType(defaultJdbcSourceInfo.getDataSourceTypeEnum().typeName());
        jdbcSourceInfo.setDataSourceTypeEnum(defaultJdbcSourceInfo.getDataSourceTypeEnum());
        if (initDbFiled != null && initDbFiled.getText() != null) {
            jdbcSourceInfo.setInitDb(initDbFiled.getText().trim());
        }
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
            // 编辑
            boolean isSuccess = false;
            if (parentComponent instanceof LinkTree) {
                if (jdbcSourceInfo.getId() >= 0) {
                    if (!SQLiteUtil.checkUnique(jdbcSourceInfo, true)) {
                        CustomDialogUtil.showError("保存失败！已存在相同连接");
                        log.error("保存更新失败！已存在相同连接{}", JSONUtil.Object2JSON(jdbcSourceInfo));
                        return;
                    }
                    if (!SQLiteUtil.insertOrUpdate(jdbcSourceInfo)) {
                        CustomDialogUtil.showError("编辑更新失败!");
                        log.error("编辑更新失败！{}", JSONUtil.Object2JSON(jdbcSourceInfo));
                        return;
                    }
                    isSuccess = ((LinkTree) parentComponent).refresh(jdbcSourceInfo);
                }
            } else if (parentComponent instanceof TopPanel || parentComponent instanceof LeftPanel) {
                if (!SQLiteUtil.checkUnique(jdbcSourceInfo, false)) {
                    log.error("保存新增失败！已存在相同连接{}", JSONUtil.Object2JSON(jdbcSourceInfo));
                    CustomDialogUtil.showError("保存失败！已存在相同连接");
                    return;
                }
                //新增
                LeftPanel leftPanel = null;
                if (parentComponent instanceof TopPanel) {
                    TopPanel topPanel = (TopPanel) parentComponent;
                    leftPanel = (LeftPanel) topPanel.getBindPanel();
                } else {
                    leftPanel = (LeftPanel) parentComponent;
                }
                jdbcSourceInfo.setJdbcDbInfos(jdbcService.getAllJdbcDbInfo());
                isSuccess = leftPanel.addLinkTree(jdbcSourceInfo);

            }
            if (isSuccess) {
                CustomDialogUtil.showOk("保存成功！", true, (bol) -> {
                    this.close();
                });
            } else {
                CustomDialogUtil.showError("保存失败！");
                log.error("保存失败{}", JSONUtil.Object2JSON(jdbcSourceInfo));
            }
        } else {
            CustomDialogUtil.showOk("连接成功！", false, null);
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
