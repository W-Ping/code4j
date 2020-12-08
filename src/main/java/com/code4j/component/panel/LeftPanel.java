package com.code4j.component.panel;

import com.code4j.connect.JDBCService;
import com.code4j.connect.JdbcServiceFactory;
import com.code4j.pojo.JdbcSourceInfo;
import com.code4j.pojo.JdbcTableInfo;
import com.code4j.util.PropertiesUtil;
import org.apache.commons.collections4.CollectionUtils;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.util.List;

/**
 * @author liu_wp
 * @date 2020/11/17
 * @see
 */
public class LeftPanel extends BasePanel {

    private RightPanel rightPanel;

    public LeftPanel(final Dimension dimension) {
        super(new FlowLayout(FlowLayout.LEFT), dimension);
    }

    @Override
    protected void init() {
        List<JdbcSourceInfo> jdbcPropertyValues = PropertiesUtil.getJdbcPropertyValues();
//        showDbTree(jdbcPropertyValues);

    }

    /**
     * @param jdbcPropertyValues
     */
    public void showDbTree(List<JdbcSourceInfo> jdbcPropertyValues) {
        if (CollectionUtils.isNotEmpty(jdbcPropertyValues)) {
            CommonPanel commonPanel = new CommonPanel(new FlowLayout(FlowLayout.LEFT));
            Box box = Box.createVerticalBox();
            for (final JdbcSourceInfo jdbcSourceInfo : jdbcPropertyValues) {
                DefaultMutableTreeNode top = new DefaultMutableTreeNode(jdbcSourceInfo.getConnectName() + "(" + jdbcSourceInfo.getConnectHost() + ")");
                final JTree tree = new JTree(top);
                box.add(tree);
            }
            commonPanel.addList(box);
            JScrollPane jScrollPane = new JScrollPane(commonPanel) {
                @Override
                public Dimension getPreferredSize() {
                    return getJScrollPaneDimension();
                }
            };
            jScrollPane.setBorder(null);
            this.add(jScrollPane);
            this.updateUI();
        }
    }

    /**
     * @param dbNames
     * @param jdbcSourceInfo
     */
    public void showTreeView(List<String> dbNames, JdbcSourceInfo jdbcSourceInfo) {
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(jdbcSourceInfo.getConnectName() + "(" + jdbcSourceInfo.getConnectHost() + ")");
        for (final String dbName : dbNames) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(dbName);
            top.add(node);
        }
        final JTree tree = new JTree(top);
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(final TreeSelectionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
                        .getLastSelectedPathComponent();
                if (node == null) {
                    return;
                }
                Object object = node.getUserObject();
                System.out.println("你选择了：" + object.toString());
                if (node.isLeaf()) {
                    JDBCService jdbcService = JdbcServiceFactory.getJdbcService(jdbcSourceInfo);
                    if (node.getAllowsChildren() && !(object instanceof JdbcTableInfo)) {
                        List<JdbcTableInfo> jdbcTableInfos = jdbcService.getJdbcTableInfo(object.toString());
                        for (final JdbcTableInfo table : jdbcTableInfos) {
                            DefaultMutableTreeNode defaultMutableTreeNode = new DefaultMutableTreeNode(table, false);
                            node.add(defaultMutableTreeNode);
                        }
                    } else {
                        if (rightPanel != null) {
                            JdbcTableInfo jdbcTableInfo = (JdbcTableInfo) object;
                            rightPanel.showGenerateView(jdbcTableInfo, jdbcSourceInfo);
                        }
                    }
                }

            }
        });
        CommonPanel commonPanel = new CommonPanel(new FlowLayout(FlowLayout.LEFT));
        commonPanel.add(tree);
        JScrollPane jScrollPane = new JScrollPane(commonPanel) {
            @Override
            public Dimension getPreferredSize() {
                return getJScrollPaneDimension();
            }
        };
        jScrollPane.setBorder(null);
        this.add(jScrollPane);
        this.updateUI();
    }

    public Dimension getJScrollPaneDimension() {
        Dimension preferredSize = this.getPreferredSize();
        return new Dimension((int) preferredSize.getWidth() - 5, (int) preferredSize.getHeight() - 5);
    }

    public RightPanel getRightPanel() {
        return rightPanel;
    }

    public void setRightPanel(final RightPanel rightPanel) {
        this.rightPanel = rightPanel;
    }
}
