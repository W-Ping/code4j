package com.code4j.component.panel;

import java.awt.*;

/**
 * @author liu_wp
 * @date 2020/11/17
 * @see
 */
public class CommonPanel extends BasePanel {

    private Object bindObject;

    public CommonPanel() {
        super();
    }

    public CommonPanel(Component... sbComponent) {
        super();
        this.addList(sbComponent);
    }

    public CommonPanel(final Dimension dimension) {
        super(dimension);
    }

    public CommonPanel(final LayoutManager layout) {
        super(layout);
    }

    public CommonPanel(final LayoutManager layout, final Dimension dimension) {
        this(layout, dimension,null);
    }
    public CommonPanel(final LayoutManager layout, final Dimension dimension,Object bindObject) {
        super(layout, dimension);
        this.bindObject=bindObject;
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

    public Object getBindObject() {
        return bindObject;
    }

    public void setBindObject(final Object bindObject) {
        this.bindObject = bindObject;
    }
}
