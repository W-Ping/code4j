package com.code4j.component.panel;

import com.code4j.pojo.BaseInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

/**
 * @author liu_wp
 * @date 2020/11/17
 * @see
 */
public abstract class BasePanel<T extends BaseInfo> extends JPanel {
    protected static final Logger log = LoggerFactory.getLogger(BasePanel.class);

    protected BasePanel bindPanel;

    public BasePanel() {
        this(null, null, null);
    }

    public BasePanel(Dimension dimension) {
        this(null, dimension, null);
    }

    public BasePanel(LayoutManager layout) {
        this(layout, null, null);
    }

    public BasePanel(LayoutManager layout, Dimension dimension) {
        this(layout, dimension, null);
    }

    public BasePanel(LayoutManager layout, Dimension dimension, BasePanel bindPanel) {
        super(layout != null ? layout : new FlowLayout());
        if (dimension != null) {
            setPreferredSize(dimension);
        }
        this.bindPanel = bindPanel;
        init();
    }

    /**
     * 初始化组件
     */
    protected abstract void init();

    public BasePanel getBindPanel() {
        return bindPanel;
    }

    public void setBindPanel(BasePanel bindPanel) {
        this.bindPanel = bindPanel;
    }
}
