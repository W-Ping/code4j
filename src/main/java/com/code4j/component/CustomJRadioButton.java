package com.code4j.component;

import javax.swing.*;

/**
 * @author lwp
 * @date 2022-03-12
 */
public class CustomJRadioButton extends JRadioButton {
    private String id;
    private Object data;

    public CustomJRadioButton(String text, String id, Object data) {
        setText(text);
        this.id = id;
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
