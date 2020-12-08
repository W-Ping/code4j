package com.code4j.component.panel;

import com.code4j.config.Code4jConstants;

import javax.swing.*;
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
        this.add(new JLabel("当前版本："+Code4jConstants.APP_VERSION));
        this.setLayout(new FlowLayout(FlowLayout.RIGHT));
    }
}
