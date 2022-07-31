package com.code4j.component.button;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * @author lwp
 * @date 2022-06-20
 */
public class CustomJButton extends JButton {
    public CustomJButton(String text, ActionListener listener) {
        super(text);
        this.addActionListener(listener);
    }

}
