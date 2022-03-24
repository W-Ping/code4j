package com.code4j.component.panel;

import com.code4j.component.menu.CustomJMenuItem;
import com.code4j.config.Code4jConstants;
import com.code4j.connect.DataSourceTypeEnum;
import com.code4j.pojo.ProjectCodeConfigInfo;
import com.code4j.util.CustomDialogUtil;
import com.code4j.util.PropertiesUtil;
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
    /**
     *
     */
    private LeftPanel leftPanel;
    private JMenu m2;

    public TopPanel(final Dimension dimension) {
        super(dimension);
        JMenuBar jMenuBar = new JMenuBar();
        JMenu m1 = new JMenu("数据源");
        JMenuItem item = new JMenuItem("MySQL");
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        item.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                showDBConfigDialog("新增连接", DataSourceTypeEnum.MYSQL);
            }
        });
        m1.add(item);
        m2 = new JMenu("项目配置");

        CustomJMenuItem m2Item = new CustomJMenuItem(Code4jConstants.CONFIG_NAME + "+", null);
        m2Item.addActionListener((e) -> {
            showProjectConfigDialog("新增项目配置", null, false);
        });
        MatteBorder matteBorder = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black);
        m2Item.setBorder(matteBorder);
        m2Item.setToolTipText(Code4jConstants.CONFIG_NAME);
        Font font = new Font(Font.DIALOG, Font.CENTER_BASELINE, 14);
        m2Item.setFont(font);
        m2Item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        m2Item.setForeground(Color.BLUE);
        m2.add(m2Item);
        // 加载已添加的项目配置
        List<ProjectCodeConfigInfo> projectConfigPropertyValues = PropertiesUtil.getProjectConfigPropertyValues();
        if (!CollectionUtils.isEmpty(projectConfigPropertyValues)) {
            projectConfigPropertyValues.forEach(v -> {
                CustomJMenuItem itm = new CustomJMenuItem(v.getProjectName(), v);
                itm.addActionListener(e -> {
                    showProjectConfigDialog("编辑项目配置", itm.getData(), true);
                });
                m2.add(itm);
            });
        }
        jMenuBar.add(m1);
        jMenuBar.add(m2);
        JMenu m3 = new JMenu("文档说明");
        JMenuItem m31 = new JMenuItem("使用教程");
        m31.addActionListener(e -> {
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
        m3.add(m31);
        jMenuBar.add(m3);
        CommonPanel commonPanel = new CommonPanel();
        commonPanel.add(jMenuBar);
//        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.setLayout(new BorderLayout());
        this.add(commonPanel, BorderLayout.WEST);
    }

    @Override
    protected void init() {
//        setBackground(Color.LIGHT_GRAY);
        MatteBorder matteBorder = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray);
        this.setBorder(matteBorder);
    }

    /**
     *
     */
    public void loadProjectCodeConfig() {
        List<ProjectCodeConfigInfo> projectConfigPropertyValues = PropertiesUtil.getProjectConfigPropertyValues();
        JPopupMenu popupMenu = m2.getPopupMenu();
        Component firstComponent = popupMenu.getComponent(0);
        m2.removeAll();
        m2.add(firstComponent);
        if (CollectionUtils.isNotEmpty(projectConfigPropertyValues)) {
            projectConfigPropertyValues.forEach(v -> {
                CustomJMenuItem itm = new CustomJMenuItem(v.getProjectName(), v);
                itm.addActionListener(e -> {
                    showProjectConfigDialog("编辑项目配置", itm.getData(), true);
                });
                m2.add(itm);
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

    public LeftPanel getLeftPanel() {
        return leftPanel;
    }

    public void setLeftPanel(final LeftPanel leftPanel) {
        this.leftPanel = leftPanel;
    }
}
