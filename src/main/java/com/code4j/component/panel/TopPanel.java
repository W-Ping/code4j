package com.code4j.component.panel;

import com.code4j.connect.DataSourceTypeEnum;
import com.code4j.util.CustomDialogUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author liu_wp
 * @date 2020/11/17
 * @see
 */
public class TopPanel extends BasePanel {
    /**
     *
     */
    private LeftPanel leftPanel;
    public TopPanel(final Dimension dimension) {
        super(dimension);
        JMenuBar jMenuBar = new JMenuBar();
        JMenu m1 = new JMenu("数据源");
        JMenuItem item = new JMenuItem("MySQL");
        item.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                showDialog("MySQL配置管理", DataSourceTypeEnum.MYSQL);
            }
        });
        m1.add(item);
        jMenuBar.add(m1);
        this.add(jMenuBar);
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
    }

    @Override
    protected void init() {
        setBackground(Color.LIGHT_GRAY);
    }

    public void showDialog(String title, DataSourceTypeEnum dataSourceTypeEnum) {
        CustomDialogUtil.showDBConfigDialog(this, title, dataSourceTypeEnum);
    }

    public LeftPanel getLeftPanel() {
        return leftPanel;
    }

    public void setLeftPanel(final LeftPanel leftPanel) {
        this.leftPanel = leftPanel;
    }
}
