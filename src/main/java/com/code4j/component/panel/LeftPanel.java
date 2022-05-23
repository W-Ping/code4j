package com.code4j.component.panel;

import com.code4j.connect.JDBCService;
import com.code4j.connect.JdbcServiceFactory;
import com.code4j.pojo.JdbcDbInfo;
import com.code4j.pojo.JdbcSourceInfo;
import com.code4j.pojo.JdbcTableInfo;
import com.code4j.util.CustomDialogUtil;
import com.code4j.util.SQLiteUtil;
import org.apache.commons.collections4.CollectionUtils;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author liu_wp
 * @date 2020/11/17
 * @see
 */
public class LeftPanel extends BasePanel {
    private RightPanel rightPanel;
    private JTree tree;
    private JScrollPane treeJScrollPane;

    public LeftPanel(final Dimension dimension) {
        super(new FlowLayout(FlowLayout.LEFT), dimension);
    }

    @Override
    protected void init() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        root.setAllowsChildren(true);
        tree = new JTree(new DefaultTreeModel(root));
        tree.setShowsRootHandles(true);
        tree.setRootVisible(false);
        tree.setEditable(false);
        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(final MouseEvent evt) {
                tree.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                tree.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }

            @Override
            public void mouseClicked(final MouseEvent evt) {
                if (evt.getButton() == MouseEvent.BUTTON3) {
                    TreePath path = tree.getPathForLocation(evt.getX(), evt.getY());
                    if (path == null) {
                        return;
                    }
                    getJPopupMenu(path, evt.getX(), evt.getY());
                }
            }
        });

        this.setLayout(new BorderLayout());
        MatteBorder matteBorder = BorderFactory.createMatteBorder(0, 0, 0, 1, Color.gray);
        this.setBorder(matteBorder);
        this.showDbTree();
    }

    /**
     * @param jdbcPropertyArr
     */
    public void showDbTree(JdbcSourceInfo... jdbcPropertyArr) {
        List<JdbcSourceInfo> jdbcPropertyValues = SQLiteUtil.select(new JdbcSourceInfo());
        if (jdbcPropertyArr != null && jdbcPropertyArr.length > 0) {
            List<JdbcSourceInfo> newJdbcPropertyList = Stream.of(jdbcPropertyArr).collect(Collectors.toList());
            jdbcPropertyValues = Stream.of(jdbcPropertyValues, newJdbcPropertyList).flatMap(Collection::stream).distinct().collect(Collectors.toList());
        }
        if (jdbcPropertyValues != null && jdbcPropertyValues.size() > 0) {
            System.out.println("数据库连接数量:" + jdbcPropertyValues.size());
            DefaultTreeModel defaultTreeModel = (DefaultTreeModel) tree.getModel();
            DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) defaultTreeModel.getRoot();
            rootNode.removeAllChildren();
            for (final JdbcSourceInfo jdbcSourceInfo : jdbcPropertyValues) {
                DefaultMutableTreeNode top = new DefaultMutableTreeNode(jdbcSourceInfo);
                rootNode.add(top);
            }
            defaultTreeModel.reload();
            tree.addTreeSelectionListener(new TreeSelectionListener() {

                @Override
                public void valueChanged(final TreeSelectionEvent e) {

                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
                            .getLastSelectedPathComponent();
                    if (node == null) {
                        return;
                    }
                    Object object = node.getUserObject();
                    node.getParent().getAllowsChildren();
                    if (object instanceof JdbcSourceInfo) {
                        System.out.println("你选择连接：" + object.toString());
                        //选择数据链接
                        JdbcSourceInfo jdbcSourceInfo = (JdbcSourceInfo) object;
                        List<JdbcDbInfo> jdbcDbInfos = jdbcSourceInfo.getJdbcDbInfos();
                        if (CollectionUtils.isEmpty(jdbcDbInfos)) {
                            JDBCService jdbcService = JdbcServiceFactory.getJdbcService(jdbcSourceInfo);
                            jdbcDbInfos = jdbcService.getAllJdbcDbInfo();
                            jdbcSourceInfo.setJdbcDbInfos(jdbcDbInfos);
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
                        System.out.println("你选择数据库：" + object.toString());
                        JdbcDbInfo jdbcDbInfo = (JdbcDbInfo) object;
                        JDBCService jdbcService = JdbcServiceFactory.getJdbcService(jdbcDbInfo.getJdbcSourceInfo());
                        List<JdbcTableInfo> jdbcTableInfos = jdbcService.getJdbcTableInfo(jdbcDbInfo);
                        if (CollectionUtils.isNotEmpty(jdbcTableInfos)) {
                            for (final JdbcTableInfo tableInfo : jdbcTableInfos) {
                                DefaultMutableTreeNode dbNameNode = new DefaultMutableTreeNode(tableInfo, false);
                                if (node.getChildCount() > 0) {
                                    TreeNode lastChild = node.getLastChild();
                                    JdbcTableInfo jdbcTableInfo = (JdbcTableInfo) ((DefaultMutableTreeNode) lastChild).getUserObject();
                                    if (!jdbcTableInfo.getTableName().equals(tableInfo.getTableName())) {
                                        node.add(dbNameNode);
                                    }
                                } else {
                                    node.add(dbNameNode);
                                }
                            }
                        } else {
                            rightPanel.clearEmpty(jdbcDbInfo.toString() + " 没有表信息");
                        }
                    } else if (object instanceof JdbcTableInfo) {
                        //选择数据表
                        System.out.println("你选择表：" + object.toString());
                        JdbcTableInfo jdbcTableInfo = (JdbcTableInfo) object;
                        rightPanel.showGenerateView(jdbcTableInfo, jdbcTableInfo.getJdbcSourceInfo());
                    }
                }
            });
            treeJScrollPane = treeJScrollPane(tree);
        }

    }

    public JScrollPane treeJScrollPane(JTree tree) {
        if (treeJScrollPane != null) {
            return treeJScrollPane;
        }
        JScrollPane treeJScrollPane = new JScrollPane(tree) {
            @Override
            public Dimension getPreferredSize() {
                return getJScrollPaneDimension();
            }
        };
        treeJScrollPane.setBorder(null);
        this.add(treeJScrollPane);
        this.updateUI();
        return treeJScrollPane;
    }

    public void addTreeNode(JdbcSourceInfo jdbcSourceInfo) {
        this.showDbTree();
    }

    /**
     * @param newNode
     * @param jdbcService
     */
    @Deprecated
    public void addTreeNode(DefaultMutableTreeNode newNode, JDBCService jdbcService) {
        DefaultTreeModel defaultTreeModel = (DefaultTreeModel) tree.getModel();
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) defaultTreeModel.getRoot();
        Object userObject = newNode.getUserObject();
        if (userObject instanceof JdbcSourceInfo) {
            JdbcSourceInfo jdbcSourceInfo = (JdbcSourceInfo) userObject;
            List<JdbcDbInfo> jdbcDbInfos = jdbcSourceInfo.getJdbcDbInfos();
            if (!CollectionUtils.isEmpty(jdbcDbInfos)) {
                for (final JdbcDbInfo jdbcDbInfo : jdbcDbInfos) {
                    DefaultMutableTreeNode defaultMutableTreeNode = new DefaultMutableTreeNode(jdbcDbInfo);
                    List<JdbcTableInfo> jdbcTableInfos = jdbcService.getJdbcTableInfo(jdbcDbInfo);
                    if (CollectionUtils.isNotEmpty(jdbcTableInfos)) {
                        for (final JdbcTableInfo tableInfo : jdbcTableInfos) {
                            DefaultMutableTreeNode dbNameNode = new DefaultMutableTreeNode(tableInfo, false);
                            defaultMutableTreeNode.add(dbNameNode);
                        }
                    } else {
                        rightPanel.clearEmpty(jdbcDbInfo.toString() + " 没有表信息");
                    }
                    newNode.add(defaultMutableTreeNode);
                }
            }
        }
        rootNode.add(newNode);
        defaultTreeModel.reload();
        treeJScrollPane = treeJScrollPane(tree);
    }

    public void deleteTreeNode(JdbcSourceInfo jdbcSourceInfo, DefaultMutableTreeNode deleNode) {
        int res = JOptionPane.showConfirmDialog(null,
                "确认删除?【" + jdbcSourceInfo.getConnectName() + "】", "确认",
                JOptionPane.YES_NO_OPTION);
        if (res == JOptionPane.YES_OPTION) {
            ((DefaultTreeModel) tree.getModel()).removeNodeFromParent(deleNode);
            ((DefaultTreeModel) tree.getModel()).reload();
            if (!SQLiteUtil.deleteByPk(jdbcSourceInfo.getId(),JdbcSourceInfo.class)) {
//            if (!PropertiesUtil.deleteJdbcProperty(jdbcSourceInfo)) {
                CustomDialogUtil.showError("删除连接失败");
                return;
            }
        }
    }

    /**
     * @param jdbcSourceInfo
     */
    public void editTreeNode(JdbcSourceInfo jdbcSourceInfo) {
        if (!SQLiteUtil.insertOrUpdate(jdbcSourceInfo)) {
//        if (!PropertiesUtil.setJdbcPropertyValues(jdbcSourceInfo)) {
            CustomDialogUtil.showError("保存数据连接失败");
            return;
        }
        DefaultMutableTreeNode currTreeNode = jdbcSourceInfo.getCurrTreeNode();
        if (currTreeNode != null) {
            currTreeNode.setUserObject(jdbcSourceInfo);
            tree.updateUI();
        } else {
//            DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(jdbcSourceInfo);
//            jdbcSourceInfo.setCurrTreeNode(newNode);
//            this.addTreeNode(newNode, jdbcService);
            this.addTreeNode(jdbcSourceInfo);
        }

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

    private void getJPopupMenu(TreePath path, int x, int y) {
        tree.setSelectionPath(path);
        DefaultMutableTreeNode selectNode = (DefaultMutableTreeNode) tree
                .getLastSelectedPathComponent();
        Object userObject = selectNode.getUserObject();
        boolean isCanSelect = userObject instanceof JdbcSourceInfo;
        if (isCanSelect) {
            JdbcSourceInfo jdbcSourceInfo = (JdbcSourceInfo) userObject;
            jdbcSourceInfo.setCurrTreeNode(selectNode);
            JPopupMenu jPopupMenu = new JPopupMenu();
            LeftPanel leftPanel = this;
            JMenuItem editItem = new JMenuItem("编辑连接");
            editItem.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    CustomDialogUtil.showDBConfigDialog(leftPanel, "编辑连接", jdbcSourceInfo.getDataSourceTypeEnum(), jdbcSourceInfo);
                }
            });
            JMenuItem delItem = new JMenuItem("删除连接");
            delItem.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    deleteTreeNode(jdbcSourceInfo, selectNode);
                }
            });
            jPopupMenu.add(editItem);
            jPopupMenu.add(delItem);
            jPopupMenu.show(tree, x, y);
        }
    }
}
