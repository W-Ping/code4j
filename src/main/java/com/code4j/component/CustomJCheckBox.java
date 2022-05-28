package com.code4j.component;

import javax.swing.*;

/**
 * @author liu_wp
 * @date 2020/11/19
 * @see
 */
public class CustomJCheckBox extends JCheckBox {
    /**
     * 对应模板ID {@link  com.code4j.config.XmlSqlTemplateEnum}
     */
    private String id;
    private Object bindObject;
    private JComponent bindComponent;

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

    public Object getBindObject() {
        return bindObject;
    }

    public void setBindObject(final Object bindObject) {
        this.bindObject = bindObject;
    }

    public JComponent getBindComponent() {
        return bindComponent;
    }

    public void setBindComponent(final JComponent bindComponent) {
        this.bindComponent = bindComponent;
    }
}
