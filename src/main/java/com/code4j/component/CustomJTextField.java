package com.code4j.component;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

/**
 * @author lwp
 * @date 2022-06-16
 */
public class CustomJTextField extends JTextField {
    private Object bindObject;

    public CustomJTextField(String text, Dimension preferredSize) {
        this(text, preferredSize, null);
    }

    public CustomJTextField(String text, Dimension preferredSize, Object bindObject) {
        super(text);
        this.setPreferredSize(preferredSize);
        this.bindObject = bindObject;
        CustomJTextField that = this;
        this.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                that.setToolTipText(that.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                that.setToolTipText(that.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                that.setToolTipText(that.getText());
            }
        });

    }

    public Object getBindObject() {
        return bindObject;
    }

    public void setBindObject(Object bindObject) {
        this.bindObject = bindObject;
    }
}
