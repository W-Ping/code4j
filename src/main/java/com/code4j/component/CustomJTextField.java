package com.code4j.component;

import javax.swing.*;
import java.awt.*;

/**
 * @author lwp
 * @date 2022-06-16
 */
public class CustomJTextField extends JTextField {
    public CustomJTextField(String text, Dimension preferredSize) {
        super(text);
        this.setPreferredSize(preferredSize);
    }
}
