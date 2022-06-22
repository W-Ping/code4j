package com.code4j.component;

import com.code4j.component.menu.CustomBasicMenuItemUI;
import com.code4j.component.menu.CustomJMenuItem;
import com.code4j.component.panel.BasePanel;
import com.code4j.component.panel.CommonPanel;
import com.code4j.component.panel.RightPanel;
import com.code4j.config.Code4jConstants;
import com.code4j.connect.JDBCService;
import com.code4j.connect.JdbcServiceFactory;
import com.code4j.pojo.JdbcDbInfo;
import com.code4j.pojo.JdbcSourceInfo;
import com.code4j.pojo.JdbcTableInfo;
import com.code4j.util.CustomDialogUtil;
import com.code4j.util.SQLiteUtil;
import com.code4j.util.SystemUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * @author lwp
 * @date 2022-05-25
 */
public class LinkTree extends JTree {
    private static final Logger log = LoggerFactory.getLogger(LinkTree.class);
    private int index;
    private RightPanel rightPanel;
    private DefaultMutableTreeNode root;
    private Color defaultBackgroundColor;


    public LinkTree(BasePanel rightPanel, DefaultMutableTreeNode root, int index, Color defaultBackgroundColor) {
        super(root);
        this.root = root;
        this.index = index;
        this.defaultBackgroundColor = defaultBackgroundColor;
        this.rightPanel = (RightPanel) rightPanel;
        this.setShowsRootHandles(false);
        this.setRootVisible(true);
        this.setEditable(false);
        this.setBackground(defaultBackgroundColor);
        this.addMouseListener(new LinkTreeMouse(this));
        this.addTreeSelectionListener(new LinkTreeSelectionListener(this));
        this.setCellRenderer(new LinkTreeDefaultTreeCellRenderer((JdbcSourceInfo) root.getUserObject(), defaultBackgroundColor));
    }


    /**
     * @param newJdbcSourceInfo
     */
    public boolean refresh(JdbcSourceInfo newJdbcSourceInfo) {
        if (newJdbcSourceInfo != null) {
            root.setUserObject(newJdbcSourceInfo);
        }
        this.updateUI();
        this.setBackground(defaultBackgroundColor);
        return true;
    }

    public static class LinkTreeMouse extends MouseAdapter {
        private LinkTree linkTree;

        public LinkTreeMouse(LinkTree linkTree) {
            this.linkTree = linkTree;

        }

        @Override
        public void mouseEntered(final MouseEvent evt) {
            linkTree.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        }

        @Override
        public void mouseExited(MouseEvent e) {
            linkTree.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

        @Override
        public void mouseClicked(final MouseEvent evt) {
            if (evt.getButton() == MouseEvent.BUTTON3) {
                TreePath path = linkTree.getPathForLocation(evt.getX(), evt.getY());
                if (path == null) {
                    return;
                }
                linkTree.setSelectionPath(path);
                DefaultMutableTreeNode selectNode = (DefaultMutableTreeNode) linkTree.getLastSelectedPathComponent();
                getJPopupMenu(selectNode, evt.getX(), evt.getY());
            }
        }

        public void getJPopupMenu(DefaultMutableTreeNode selectNode, int x, int y) {
            Object userObject = selectNode.getUserObject();
            boolean isCanSelect = userObject instanceof JdbcSourceInfo;
            if (isCanSelect) {
                JdbcSourceInfo jdbcSourceInfo = (JdbcSourceInfo) userObject;
                jdbcSourceInfo.setCurrTreeNode(selectNode);
                JPopupMenu jPopupMenu = new JPopupMenu();
                CustomJMenuItem editItem = new CustomJMenuItem("编 辑", "edit", new CustomBasicMenuItemUI(Code4jConstants.selectionBackground, Code4jConstants.selectionForeground));
                editItem.addActionListener(new AbstractAction() {
                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        CustomDialogUtil.showDBConfigDialog(linkTree, "编辑连接", jdbcSourceInfo.getDataSourceTypeEnum(), jdbcSourceInfo);
                    }
                });
                CustomJMenuItem delItem = new CustomJMenuItem("删 除", "delete", new CustomBasicMenuItemUI(Code4jConstants.selectionBackground, Code4jConstants.selectionForeground));
                delItem.addActionListener(new AbstractAction() {
                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        deleteTreeNode(jdbcSourceInfo);
                    }
                });
                CustomJMenuItem refreshItem = new CustomJMenuItem("刷 新", "refresh", new CustomBasicMenuItemUI(Code4jConstants.selectionBackground, Code4jConstants.selectionForeground));
                refreshItem.addActionListener(new AbstractAction() {
                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        refreshTreeNode(jdbcSourceInfo, selectNode);
                    }
                });
                jPopupMenu.add(refreshItem);
                jPopupMenu.add(editItem);
                jPopupMenu.add(delItem);
                jPopupMenu.show(linkTree, x, y);
            }
        }

        public void deleteTreeNode(JdbcSourceInfo jdbcSourceInfo) {
            int res = JOptionPane.showConfirmDialog(linkTree.getRightPanel(), "确认删除?【" + jdbcSourceInfo.getConnectName() + "】", "确认", JOptionPane.YES_NO_OPTION);
            if (res == JOptionPane.YES_OPTION) {
                final CommonPanel parent = (CommonPanel) linkTree.getParent();
                if (!SQLiteUtil.deleteByPk(jdbcSourceInfo.getId(), JdbcSourceInfo.class)) {
                    CustomDialogUtil.showError("删除连接失败");
                    return;
                } else {
                    parent.remove(linkTree);
                    parent.updateUI();
                }
            }
        }

        public void refreshTreeNode(JdbcSourceInfo jdbcSourceInfo, DefaultMutableTreeNode selectNode) {
            final List<JdbcDbInfo> jdbcTableInfos = JdbcServiceFactory.getJdbcService(jdbcSourceInfo).getAllJdbcDbInfo();
            jdbcSourceInfo.setJdbcDbInfos(jdbcTableInfos);
            selectNode.removeAllChildren();
            final DefaultMutableTreeNode defaultMutableTreeNode = new DefaultMutableTreeNode(jdbcSourceInfo);
            ((DefaultTreeModel) linkTree.getModel()).reload(defaultMutableTreeNode);
        }
    }

    public static class LinkTreeDefaultTreeCellRenderer extends DefaultTreeCellRenderer {
        private JdbcSourceInfo jdbcSourceInfo;

        private Color defaultBackgroundColor;

        public LinkTreeDefaultTreeCellRenderer(JdbcSourceInfo jdbcSourceInfo, Color defaultBackgroundColor) {
            this.jdbcSourceInfo = jdbcSourceInfo;
            this.defaultBackgroundColor = defaultBackgroundColor;
        }

        @Override
        public Color getBackgroundNonSelectionColor() {
            return defaultBackgroundColor;
        }

        /**
         * @param tree
         * @param value
         * @param sel
         * @param expanded
         * @param leaf
         * @param row
         * @param hasFocus
         * @return
         */
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                      boolean sel, boolean expanded, boolean leaf, int row,
                                                      boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
                    row, hasFocus);
            setText(value.toString());
            if (sel) {
                this.setBackgroundSelectionColor(new Color(0x99D79E));
                setForeground(getTextSelectionColor());
            } else {
                setForeground(getTextNonSelectionColor());
            }
            this.setBorderSelectionColor(defaultBackgroundColor);
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            final Object userObject = node.getUserObject();
            if (userObject instanceof JdbcSourceInfo) {
                this.setIcon(getIconPath(jdbcSourceInfo.getSourceType(), expanded));
                if (!expanded) {
                    this.setBackgroundSelectionColor(defaultBackgroundColor);
                }
            } else if (userObject instanceof JdbcDbInfo) {
                this.setIcon(getIconPath("db", sel || expanded));
            } else if (userObject instanceof JdbcTableInfo) {
                this.setIcon(getIconPath("table", sel || expanded));
            }
            return this;
        }

        private ImageIcon getIconPath(String pngName, boolean oc) {
            return new ImageIcon(SystemUtil.getSystemResource(String.format("images/%s_%s.png", pngName, oc ? "open" : "close")));
        }
    }

    public static class LinkTreeSelectionListener implements TreeSelectionListener {
        private LinkTree linkTree;

        public LinkTreeSelectionListener(LinkTree linkTree) {
            this.linkTree = linkTree;
        }

        @Override
        public void valueChanged(TreeSelectionEvent e) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) linkTree.getLastSelectedPathComponent();
            if (node == null) {
                return;
            }
            Object object = node.getUserObject();
            if (object instanceof JdbcSourceInfo) {
                log.debug("》》》选择连接：{}", object);
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
                    linkTree.getRightPanel().clearEmpty(jdbcSourceInfo.toString() + " 没有数据库");
                }
            } else if (object instanceof JdbcDbInfo) {
                log.debug("》》》选择数据库：{}", object);
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
                    linkTree.getRightPanel().clearEmpty(jdbcDbInfo.toString() + " 没有表信息");
                }
            } else if (object instanceof JdbcTableInfo) {
                //选择表
                log.debug("》》》选择表：{}", object);
                JdbcTableInfo jdbcTableInfo = (JdbcTableInfo) object;
                linkTree.getRightPanel().showGenerateView(jdbcTableInfo, jdbcTableInfo.getJdbcSourceInfo());
            }
        }

    }


    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public RightPanel getRightPanel() {
        return rightPanel;
    }

    public void setRightPanel(RightPanel rightPanel) {
        this.rightPanel = rightPanel;
    }


    public DefaultMutableTreeNode getRoot() {
        return root;
    }

    public void setRoot(DefaultMutableTreeNode root) {
        this.root = root;
    }
}
