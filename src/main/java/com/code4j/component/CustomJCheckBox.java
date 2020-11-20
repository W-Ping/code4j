package com.code4j.component;

import com.code4j.component.panel.CommonPanel;

import javax.swing.*;

/**
 * @author liu_wp
 * @date 2020/11/19
 * @see
 */
public class CustomJCheckBox extends JCheckBox {
    private String id;
    private CommonPanel bindCommonPanel;
    public CustomJCheckBox(String text, boolean isSelect, String id) {
        super(text, isSelect);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public CommonPanel getBindCommonPanel() {
        return bindCommonPanel;
    }

    public void setBindCommonPanel(final CommonPanel bindCommonPanel) {
        this.bindCommonPanel = bindCommonPanel;
    }
}
