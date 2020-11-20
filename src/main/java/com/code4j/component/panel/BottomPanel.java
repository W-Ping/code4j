package com.code4j.component.panel;

import java.awt.*;

/**
 * @author liu_wp
 * @date 2020/11/17
 * @see
 */
public class BottomPanel extends BasePanel {
    public BottomPanel() {
        super();
    }

    public BottomPanel(final Dimension dimension) {
        super(dimension);
    }

    public BottomPanel(final LayoutManager layout, final Dimension dimension) {
        super(layout, dimension);
    }

    @Override
    protected void init() {
        setBackground(Color.LIGHT_GRAY);
    }
}
