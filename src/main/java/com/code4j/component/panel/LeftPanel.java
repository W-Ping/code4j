package com.code4j.component.panel;

import com.code4j.component.LinkTree;
import com.code4j.pojo.JdbcSourceInfo;
import com.code4j.util.SQLiteUtil;
import com.code4j.util.VFlowLayout;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        if (CollectionUtils.isNotEmpty(jdbcPropertyValues)) {
            linkTreePanel.removeAll();
            for (int i = 0; i < jdbcPropertyValues.size(); i++) {
                final JdbcSourceInfo jdbcSourceInfo = jdbcPropertyValues.get(i);
                LinkTree tree = new LinkTree(this.bindPanel, new DefaultMutableTreeNode(jdbcSourceInfo), i, this.getBackground());
                linkTreePanel.add(tree);
            }
        }
        JScrollPane treeJScrollPane = new JScrollPane(linkTreePanel);
        treeJScrollPane.setBackground(this.getBackground());
        this.add(treeJScrollPane);

    }

    /**
     * @param promptMsg
     */
    public void clearEmpty(String promptMsg) {
        this.removeAll();
        this.linkTreePanel = null;
        JLabel jLabel = this.defaultLabel(promptMsg);
        if (jLabel != null) {
            this.add(jLabel, BorderLayout.CENTER);
        }
        this.updateUI();
    }

    private JLabel defaultLabel(String promptMsg) {
        if (StringUtils.isNotBlank(promptMsg)) {
            JLabel jLabel = new JLabel(promptMsg, JLabel.CENTER);
            jLabel.setFont(jLabel.getFont().deriveFont(30.0f));
            jLabel.setForeground(Color.gray);
            return jLabel;
        }
        return null;
    }

    /**
     * @param jdbcSourceInfo
     * @return
     */
    public boolean addLinkTree(JdbcSourceInfo jdbcSourceInfo) {
        if (SQLiteUtil.insertOrUpdate(jdbcSourceInfo)) {
            log.info("保存连接成功！【{}】", jdbcSourceInfo.getConnectName());
            if (linkTreePanel == null) {
                this.removeAll();
                this.init();
                this.updateUI();
                return true;
            } else {
                final Component[] components = linkTreePanel.getComponents();
                LinkTree tree = new LinkTree(this.bindPanel, new DefaultMutableTreeNode(jdbcSourceInfo), components.length, this.getBackground());
                linkTreePanel.add(tree);
                linkTreePanel.updateUI();
                return true;
            }
        }
        return false;
    }

}
