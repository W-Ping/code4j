package com.code4j.component.panel;

import com.code4j.pojo.BaseInfo;

import javax.swing.*;
import java.awt.*;

/**
 * @author liu_wp
 * @date 2020/11/17
 * @see
 */
public abstract class BasePanel<T extends BaseInfo> extends JPanel {


    public BasePanel() {
        super();
        init();
    }

    public BasePanel(Dimension dimension) {
        super();
        setPreferredSize(dimension);
        init();
    }

    public BasePanel(LayoutManager layout) {
        super(layout);
        init();
    }

    public BasePanel(LayoutManager layout, Dimension dimension) {
        super(layout);
        setPreferredSize(dimension);
        init();
    }

    /**
     * 初始化组件
     */
    protected abstract void init();


}
