package com.code4j.component.panel;

import com.code4j.component.menu.CustomBasicMenuItemUI;
import com.code4j.component.menu.CustomJMenuItem;
import com.code4j.config.Code4jConstants;
import com.code4j.enums.DataSourceTypeEnum;
import com.code4j.pojo.ProjectCodeConfigInfo;
import com.code4j.util.CustomDialogUtil;
import com.code4j.util.SQLiteUtil;
import com.code4j.util.SystemUtil;
import org.apache.commons.collections4.CollectionUtils;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 顶部栏
 *
 * @author liu_wp
 * @date 2020/11/17
 * @see
 */
public class TopPanel extends BasePanel {

    private JMenu pcfMenu;

    public TopPanel(final Dimension dimension, BasePanel leftPanel) {
        super(null, dimension, leftPanel);
        JMenuBar jMenuBar = new JMenuBar();
        JMenu linkMenu = new JMenu("连接");
        linkMenu.setIcon(new ImageIcon(ClassLoader.getSystemResource("images/add_link.png")));
        CustomJMenuItem item = new CustomJMenuItem(DataSourceTypeEnum.MYSQL.typeName(), DataSourceTypeEnum.MYSQL.typeName() + "_close", new CustomBasicMenuItemUI(Code4jConstants.selectionBackground, Code4jConstants.selectionForeground));
        CustomJMenuItem item2 = new CustomJMenuItem(DataSourceTypeEnum.POSTGRESQL.typeName(), DataSourceTypeEnum.POSTGRESQL.typeName() + "_close", new CustomBasicMenuItemUI(Code4jConstants.selectionBackground, Code4jConstants.selectionForeground));
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        item2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        item.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                showDBConfigDialog("新增连接", DataSourceTypeEnum.MYSQL);
            }
        });
        item2.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                showDBConfigDialog("新增连接", DataSourceTypeEnum.POSTGRESQL);
            }
        });
        linkMenu.add(item);
        linkMenu.add(item2);
        pcfMenu = new JMenu("项目配置");
        pcfMenu.setIcon(new ImageIcon(ClassLoader.getSystemResource("images/config.png")));
        CustomJMenuItem m2Item = new CustomJMenuItem(Code4jConstants.CONFIG_NAME, "add", new CustomBasicMenuItemUI(Code4jConstants.selectionBackground, Code4jConstants.selectionForeground));
        m2Item.addActionListener((e) -> {
            showProjectConfigDialog("新增项目配置", null, false);
        });
        MatteBorder matteBorder = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black);
        m2Item.setBorder(matteBorder);
        m2Item.setForeground(new Color(0x0077FF));
        Font font = new Font(Font.DIALOG, Font.CENTER_BASELINE, 14);
        m2Item.setFont(font);
        pcfMenu.add(m2Item);
        // 加载已添加的项目配置
        final List<ProjectCodeConfigInfo> projectConfigPropertyValues = SQLiteUtil.select(new ProjectCodeConfigInfo());
        log.info(">>>>>> load project config count {}", projectConfigPropertyValues == null ? 0 : projectConfigPropertyValues.size());
        if (!CollectionUtils.isEmpty(projectConfigPropertyValues)) {
            projectConfigPropertyValues.forEach(v -> {
                CustomJMenuItem itm = new CustomJMenuItem(v.getProjectName(), "tp", v, new CustomBasicMenuItemUI(Code4jConstants.selectionBackground, Code4jConstants.selectionForeground));
                itm.addActionListener(e -> {
                    showProjectConfigDialog("编辑项目配置", itm.getData(), true);
                });
                pcfMenu.add(itm);
            });
        }

        JMenu helpMenu = new JMenu("帮助文档");
        helpMenu.setIcon(new ImageIcon(ClassLoader.getSystemResource("images/help.png")));
        CustomJMenuItem tutorial = new CustomJMenuItem("使用教程", "tutorial", new CustomBasicMenuItemUI(Code4jConstants.selectionBackground, Code4jConstants.selectionForeground));
        tutorial.addActionListener(e -> {
            InputStream resourceAsStream = TopPanel.class.getClassLoader().getResourceAsStream("README.md");
            String content = SystemUtil.readByStream(resourceAsStream);
            String html = Code4jConstants.SYS_TEMP_PATH + "\\代码生成工具说明文档.html";
            SystemUtil.writeFile(html, content);
            try {
                Desktop.getDesktop().open(new File(html));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        helpMenu.add(tutorial);

        jMenuBar.add(linkMenu);
        jMenuBar.add(pcfMenu);
        jMenuBar.add(helpMenu);
        CommonPanel commonPanel = new CommonPanel();
        commonPanel.add(jMenuBar);
//        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.setLayout(new BorderLayout());
        this.add(commonPanel, BorderLayout.WEST);
    }

    @Override
    protected void init() {
        MatteBorder matteBorder = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray);
        this.setBorder(matteBorder);
    }

    /**
     *
     */
    public void loadProjectCodeConfig(String option) {
        List<ProjectCodeConfigInfo> projectConfigPropertyValues = SQLiteUtil.select(new ProjectCodeConfigInfo());
        JPopupMenu popupMenu = pcfMenu.getPopupMenu();
        Component firstComponent = popupMenu.getComponent(0);
        pcfMenu.removeAll();
        pcfMenu.add(firstComponent);
        if (CollectionUtils.isNotEmpty(projectConfigPropertyValues)) {
            projectConfigPropertyValues.forEach(v -> {
                CustomJMenuItem itm = new CustomJMenuItem(v.getProjectName(), "tp", v, new CustomBasicMenuItemUI(Code4jConstants.selectionBackground, Code4jConstants.selectionForeground));
                itm.addActionListener(e -> {
                    showProjectConfigDialog("编辑项目配置", itm.getData(), true);
                });
                pcfMenu.add(itm);
            });
        }
    }


    /**
     * @param title
     * @param dataSourceTypeEnum
     */
    public void showDBConfigDialog(String title, DataSourceTypeEnum dataSourceTypeEnum) {
        CustomDialogUtil.showDBConfigDialog(this, title, dataSourceTypeEnum, null);
    }

    /**
     * @param title
     * @param extObj
     * @param isUpdate
     */
    public void showProjectConfigDialog(String title, Object extObj, boolean isUpdate) {
        CustomDialogUtil.showProjectConfigDialog(this, title, extObj, isUpdate);
    }

}
