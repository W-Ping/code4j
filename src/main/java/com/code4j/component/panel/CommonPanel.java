package com.code4j.component.panel;

import com.code4j.pojo.BaseInfo;

import java.awt.*;

/**
 * @author liu_wp
 * @date 2020/11/17
 * @see
 */
public class CommonPanel<T extends BaseInfo> extends BasePanel {
    protected T bindValue;

    public CommonPanel() {
        super();
    }

    public CommonPanel(final Dimension dimension) {
        super(dimension);
    }

    public CommonPanel(final LayoutManager layout) {
        super(layout);
    }

    public CommonPanel(final LayoutManager layout, final Dimension dimension) {
        super(layout, dimension);
    }

    @Override
    protected void init() {

    }

    public void addList(Component... components) {
        if (components != null) {
            for (int i = 0; i < components.length; i++) {
                this.add(components[i], i);
            }
        }
    }

    public T getBindValue() {
        return bindValue;
    }

    public void setBindValue(final T bindValue) {
        this.bindValue = bindValue;
    }
}
