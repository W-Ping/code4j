package com.code4j.component.label;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

/**
 * @author lwp
 * @date 2022-07-28
 */
public class LinkLabel extends JLabel {

    /**
     * @param text
     * @param consumer
     */
    public LinkLabel(String text, Color color, Consumer<LinkLabel> consumer) {
        super(text);
        this.setForeground(color);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.setFont(new Font("Helvetica", Font.PLAIN, 12));
        LinkLabel that = this;
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    if (consumer != null) {
                        consumer.accept(that);
                    }
                }
            }
        });
    }
}
