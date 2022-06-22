package com.code4j.component.panel;

import com.code4j.component.LinkTree;
import com.code4j.pojo.JdbcSourceInfo;
import com.code4j.util.SQLiteUtil;
import com.code4j.util.VFlowLayout;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.util.List;

/**
 * @author liu_wp
 * @date 2020/11/17
 * @see
 */
public class LeftPanel extends BasePanel {

    private CommonPanel linkTreePanel;

    public LeftPanel(final Dimension dimension, BasePanel basePanel) {
        super(new FlowLayout(FlowLayout.LEFT), dimension, basePanel);
    }

    @Override
    protected void init() {
        this.setLayout(new BorderLayout());
        MatteBorder matteBorder = BorderFactory.createMatteBorder(0, 0, 0, 1, Color.gray);
        this.setBorder(matteBorder);
        List<JdbcSourceInfo> jdbcPropertyValues = SQLiteUtil.select(new JdbcSourceInfo());
        log.info(">>>>>> load jdbc link count {}", jdbcPropertyValues == null ? 0 : jdbcPropertyValues.size());
        linkTreePanel = new CommonPanel();
        linkTreePanel.setLayout(new VFlowLayout());
        linkTreePanel.setBackground(this.getBackground());
        for (int i = 0; i < jdbcPropertyValues.size(); i++) {
            final JdbcSourceInfo jdbcSourceInfo = jdbcPropertyValues.get(i);
            LinkTree tree = new LinkTree(this.bindPanel, new DefaultMutableTreeNode(jdbcSourceInfo), i, this.getBackground());
            linkTreePanel.add(tree);
        }
        JScrollPane treeJScrollPane = new JScrollPane(linkTreePanel);
        treeJScrollPane.setBackground(this.getBackground());
        this.add(treeJScrollPane);

    }

    /**
     * @param jdbcSourceInfo
     * @return
     */
    public boolean addLinkTree(JdbcSourceInfo jdbcSourceInfo) {
        if (SQLiteUtil.insertOrUpdate(jdbcSourceInfo)) {
            log.info("保存连接成功！【{}】", jdbcSourceInfo.getConnectName());
            final Component[] components = linkTreePanel.getComponents();
            LinkTree tree = new LinkTree(this.bindPanel, new DefaultMutableTreeNode(jdbcSourceInfo), components.length, this.getBackground());
            linkTreePanel.add(tree);
            linkTreePanel.updateUI();
            return true;
        }
        return false;
    }

}
