package com.code4j.component.dialog;

import com.code4j.component.panel.CommonPanel;
import com.code4j.pojo.BaseInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author liu_wp
 * @date 2020/11/18
 * @see
 */
public abstract class BaseDialog<T extends BaseInfo> extends JDialog {
    private String title;
    protected Component parentComponent;
    protected CommonPanel<T> contentPanel;
    protected Object extObj;

    public BaseDialog(Component parentComponent, String title, boolean modal, Object extObj) {
        super((Frame) SwingUtilities.windowForComponent(parentComponent));
        this.parentComponent = parentComponent;
        this.extObj = extObj;
        setTitle(title);
        setModal(modal);
        init();
    }

    public void init() {
        Box box = Box.createVerticalBox();
        box.add(contentPanel = this.content());
        box.add(this.bottom());
        this.setContentPane(box);
        this.pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    protected abstract CommonPanel<T> content();

    protected CommonPanel bottom() {
        CommonPanel commonPanel = new CommonPanel();
        JButton cancelBtn = new JButton("取消");
        cancelBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                cancelClick();
            }
        });
        JButton okBtn = new JButton("确定");
        okBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                okClick();
            }
        });
        commonPanel.add(cancelBtn);
        CommonPanel bottomMid = bottomMid();
        if (bottomMid != null) {
            commonPanel.add(bottomMid);
        }
        commonPanel.add(okBtn);
        return commonPanel;
    }

    public CommonPanel bottomMid() {
        return null;
    }

    protected abstract void okClick();

    /**
     *
     */
    public void cancelClick() {
        this.close();
    }

    public void close() {
        this.dispose();
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(final String title) {
        this.title = title;
    }

    public Component getParentComponent() {
        return parentComponent;
    }

    public void setParentComponent(final Component parentComponent) {
        this.parentComponent = parentComponent;
    }

    public CommonPanel<T> getContentPanel() {
        return contentPanel;
    }

    public void setContentPanel(final CommonPanel<T> contentPanel) {
        this.contentPanel = contentPanel;
    }
}
