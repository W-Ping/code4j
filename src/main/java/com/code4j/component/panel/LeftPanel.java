package com.code4j.component.panel;

import com.code4j.connect.JDBCService;
import com.code4j.connect.JdbcServiceFactory;
import com.code4j.pojo.JdbcDbInfo;
import com.code4j.pojo.JdbcSourceInfo;
import com.code4j.pojo.JdbcTableInfo;
import com.code4j.util.PropertiesUtil;
import org.apache.commons.collections4.CollectionUtils;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.List;

/**
 * @author liu_wp
 * @date 2020/11/17
 * @see
 */
public class LeftPanel extends BasePanel {
    private RightPanel rightPanel;
    private JTree tree;

    public LeftPanel(final Dimension dimension) {
        super(new FlowLayout(FlowLayout.LEFT), dimension);
    }

    @Override
    protected void init() {
        this.setLayout(new BorderLayout());
        MatteBorder matteBorder = BorderFactory.createMatteBorder(0, 0, 0, 1, Color.gray);
        this.setBorder(matteBorder);
        List<JdbcSourceInfo> jdbcPropertyValues = PropertiesUtil.getJdbcPropertyValues();
        showDbTree(jdbcPropertyValues);
    }

    /**
     * @param jdbcPropertyValues
     */
    public void showDbTree(List<JdbcSourceInfo> jdbcPropertyValues) {
        if (CollectionUtils.isNotEmpty(jdbcPropertyValues)) {
            System.out.println("数据库连接数量:" + jdbcPropertyValues.size());
            DefaultMutableTreeNode root = new DefaultMutableTreeNode();
            for (final JdbcSourceInfo jdbcSourceInfo : jdbcPropertyValues) {
                JDBCService jdbcService = JdbcServiceFactory.getJdbcService(jdbcSourceInfo);
                List<JdbcDbInfo> allJdbcDbInfo = jdbcService.getAllJdbcDbInfo();
                jdbcSourceInfo.setJdbcDbInfos(allJdbcDbInfo);
                DefaultMutableTreeNode top = new DefaultMutableTreeNode(jdbcSourceInfo);
                root.add(top);
            }
            tree = new JTree(new DefaultTreeModel(root));
            tree.setShowsRootHandles(true);
            tree.setRootVisible(false);
            tree.setEditable(false);
            tree.addTreeSelectionListener(new TreeSelectionListener() {
                @Override
                public void valueChanged(final TreeSelectionEvent e) {
                    rightPanel.clearEmpty(null);
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
                            .getLastSelectedPathComponent();
                    if (node == null) {
                        return;
                    }
                    Object object = node.getUserObject();
                    System.out.println("你选择了：" + object.toString());
                    node.getParent().getAllowsChildren();
                    if (object instanceof JdbcSourceInfo) {
                        //选择数据链接
                        JdbcSourceInfo jdbcSourceInfo = (JdbcSourceInfo) object;
                        List<JdbcDbInfo> jdbcDbInfos = jdbcSourceInfo.getJdbcDbInfos();
                        if (CollectionUtils.isEmpty(jdbcDbInfos)) {
                            JDBCService jdbcService = JdbcServiceFactory.getJdbcService(jdbcSourceInfo);
                            jdbcDbInfos = jdbcService.getAllJdbcDbInfo();
                        }
                        if (CollectionUtils.isNotEmpty(jdbcDbInfos)) {
                            for (final JdbcDbInfo jdbcDbInfo : jdbcDbInfos) {
                                DefaultMutableTreeNode dbNameNode = new DefaultMutableTreeNode(jdbcDbInfo, true);
                                node.add(dbNameNode);
                            }
                        } else {
                            rightPanel.clearEmpty(jdbcSourceInfo.toString() + " 没有数据库");
                        }
                    } else if (object instanceof JdbcDbInfo) {
                        //选择数据库
                        JdbcDbInfo jdbcDbInfo = (JdbcDbInfo) object;
                        JDBCService jdbcService = JdbcServiceFactory.getJdbcService(jdbcDbInfo.getJdbcSourceInfo());
                        List<JdbcTableInfo> jdbcTableInfos = jdbcService.getJdbcTableInfo(jdbcDbInfo);
                        if (CollectionUtils.isNotEmpty(jdbcTableInfos)) {
                            for (final JdbcTableInfo tableInfo : jdbcTableInfos) {
                                DefaultMutableTreeNode dbNameNode = new DefaultMutableTreeNode(tableInfo, false);
                                node.add(dbNameNode);
                            }
                        } else {
                            rightPanel.clearEmpty(jdbcDbInfo.toString() + " 没有表信息");
                        }
                    } else if (object instanceof JdbcTableInfo) {
                        //选择数据表
                        JdbcTableInfo jdbcTableInfo = (JdbcTableInfo) object;
                        rightPanel.showGenerateView(jdbcTableInfo, jdbcTableInfo.getJdbcSourceInfo());
                    }
                }
            });
            JScrollPane jScrollPane = new JScrollPane(tree) {
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

    public void addTreeNode(DefaultMutableTreeNode newNode) {
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) tree.getModel().getRoot();
        rootNode.add(newNode);
        Object userObject = newNode.getUserObject();
        if (userObject instanceof JdbcSourceInfo) {
            JdbcSourceInfo jdbcSourceInfo = (JdbcSourceInfo) userObject;
            List<JdbcDbInfo> jdbcDbInfos = jdbcSourceInfo.getJdbcDbInfos();
            if (!CollectionUtils.isEmpty(jdbcDbInfos)) {
                for (final JdbcDbInfo jdbcDbInfo : jdbcDbInfos) {
                    DefaultMutableTreeNode defaultMutableTreeNode = new DefaultMutableTreeNode(jdbcDbInfo);
                    newNode.add(defaultMutableTreeNode);
                }
            }
        }
        TreePath treePath = new TreePath(newNode);
        System.out.println("-------:" + treePath.getPath().length);
        System.out.println("-------:" + treePath.getPathCount());
        tree.expandPath(treePath);
        tree.updateUI();
    }

    /**
     * @param dbNames
     * @param jdbcSourceInfo
     */
    public void showTreeView(JdbcSourceInfo jdbcSourceInfo) {
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(jdbcSourceInfo);
        addTreeNode(top);

//        DefaultMutableTreeNode top = new DefaultMutableTreeNode(jdbcSourceInfo.getConnectName() + "(" + jdbcSourceInfo.getConnectHost() + ")");
//        for (final String dbName : dbNames) {
//            DefaultMutableTreeNode node = new DefaultMutableTreeNode(dbName);
//            top.add(node);
//        }
//        final JTree tree = new JTree(top);
//        tree.addTreeSelectionListener(new TreeSelectionListener() {
//            @Override
//            public void valueChanged(final TreeSelectionEvent e) {
//                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
//                        .getLastSelectedPathComponent();
//                if (node == null) {
//                    return;
//                }
//                Object object = node.getUserObject();
//                System.out.println("你选择了：" + object.toString());
//                if (node.isLeaf()) {
//                    JDBCService jdbcService = JdbcServiceFactory.getJdbcService(jdbcSourceInfo);
//                    if (node.getAllowsChildren() && !(object instanceof JdbcTableInfo)) {
//                        JdbcTableInfo jdbcTableInfo = (JdbcTableInfo) object;
//                        JdbcDbInfo jdbcDbInfo = new JdbcDbInfo(jdbcTableInfo.getDbName(), jdbcTableInfo.getJdbcSourceInfo());
//                        List<JdbcTableInfo> jdbcTableInfos = jdbcService.getJdbcTableInfo(jdbcDbInfo);
//                        for (final JdbcTableInfo table : jdbcTableInfos) {
//                            DefaultMutableTreeNode defaultMutableTreeNode = new DefaultMutableTreeNode(table, false);
//                            node.add(defaultMutableTreeNode);
//                        }
//                    } else {
//                        if (rightPanel != null) {
//                            JdbcTableInfo jdbcTableInfo = (JdbcTableInfo) object;
//                            rightPanel.showGenerateView(jdbcTableInfo, jdbcSourceInfo);
//                        }
//                    }
//                }
//
//            }
//        });
//        CommonPanel commonPanel = new CommonPanel(new FlowLayout(FlowLayout.LEFT));
//        commonPanel.add(tree);
//        JScrollPane jScrollPane = new JScrollPane(commonPanel) {
//            @Override
//            public Dimension getPreferredSize() {
//                return getJScrollPaneDimension();
//            }
//        };
//        jScrollPane.setBorder(null);
//        this.add(jScrollPane);
//        this.updateUI();
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
