package com.code4j.component.panel;

import com.code4j.config.Code4jConstants;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

/**
 * 底部栏
 *
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
        MatteBorder matteBorder = BorderFactory.createMatteBorder(1, 0, 0, 0, Color.gray);
        this.setBorder(matteBorder);
        CommonPanel commonPanel = new CommonPanel();
        final JLabel jLabel = new JLabel("当前版本：" + Code4jConstants.APP_VERSION);
        Font font = new Font(Font.MONOSPACED, Font.CENTER_BASELINE, 10);
        jLabel.setFont(font);
        commonPanel.add(jLabel);
        this.setLayout(new BorderLayout());
        this.add(commonPanel, BorderLayout.EAST);
    }
}
