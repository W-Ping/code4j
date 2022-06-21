package com.code4j.component.label;

import com.code4j.component.dialog.BaseDialog;
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
public class TemplateClickLabel<T extends BaseDialog> extends JLabel {
    private CommonPanel bindCommonPanel;
    private T showDialog;

    /**
     * @param text
     * @param title
     * @param templateTypeEnum
     */
    public TemplateClickLabel(String text, String title, TemplateTypeEnum templateTypeEnum, CommonPanel bindCommonPanel, boolean Visible) {
        super(text);
        this.bindCommonPanel = bindCommonPanel;
        this.setForeground(Color.BLUE);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.setFont(new Font("Helvetica", Font.PLAIN, 12));
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    if (TemplateTypeEnum.SERVICE_API.getTemplateId().equals(templateTypeEnum.getTemplateId())) {
                        showDialog = (T) CustomDialogUtil.showServiceApiConfigDialog(bindCommonPanel, title, templateTypeEnum);
                    } else if (TemplateTypeEnum.MAPPER.getTemplateId().equals(templateTypeEnum.getTemplateId())) {
                        showDialog = (T) CustomDialogUtil.showMapperConfigDialog(bindCommonPanel, title, templateTypeEnum);
                    } else if (TemplateTypeEnum.XML.getTemplateId().equals(templateTypeEnum.getTemplateId())) {
                        showDialog = (T) CustomDialogUtil.showXmlConfigDialog(bindCommonPanel, title, templateTypeEnum);
                    } else {
                        showDialog = (T) CustomDialogUtil.showTableConfigDialog(bindCommonPanel, title, templateTypeEnum);
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
        this.setVisible(Visible);
    }

    public CommonPanel getBindCommonPanel() {
        return bindCommonPanel;
    }

    public void setBindCommonPanel(CommonPanel bindCommonPanel) {
        this.bindCommonPanel = bindCommonPanel;
    }

    public T getShowDialog() {
        return showDialog;
    }

    public void setShowDialog(T showDialog) {
        this.showDialog = showDialog;
    }
}
