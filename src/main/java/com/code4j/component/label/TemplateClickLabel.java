package com.code4j.component.label;

import com.code4j.component.panel.CommonPanel;
import com.code4j.config.TemplateTypeEnum;
import com.code4j.util.CustomDialogUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author liu_wp
 * @date 2020/11/23
 * @see
 */
public class TemplateClickLabel extends JLabel {
    private CommonPanel bindCommonPanel;

    /**
     * @param text
     * @param title
     * @param templateTypeEnum
     */
    public TemplateClickLabel(String text, final String title, TemplateTypeEnum templateTypeEnum, CommonPanel bindCommonPanel) {
        super(text);
        this.bindCommonPanel = bindCommonPanel;
        this.setForeground(Color.BLUE);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.setFont(new Font("Helvetica", Font.PLAIN, 12));
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    if (TemplateTypeEnum.SERVICE_API.equals(templateTypeEnum.getTemplateId())) {
                        CustomDialogUtil.showServiceApiConfigDialog(bindCommonPanel, title, templateTypeEnum);
                    } else {
                        CustomDialogUtil.showTableConfigDialog(bindCommonPanel, title, templateTypeEnum);
                    }
                }
            }

            @Override
            public void mouseEntered(final MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        setVisible(true);
    }
}
