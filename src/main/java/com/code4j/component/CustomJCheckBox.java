package com.code4j.component;

import com.code4j.enums.XmlSqlTemplateEnum;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.BiConsumer;

/**
 * @author liu_wp
 * @date 2020/11/19
 * @see
 */
public class CustomJCheckBox extends JCheckBox {
    /**
     * 对应模板ID {@link  XmlSqlTemplateEnum}
     */
    private String id;
    private Object bindObject;
    private JComponent bindComponent;

    public CustomJCheckBox(String text, boolean isSelect, String id, Object bindObject) {
        super(text, isSelect);
        this.id = id;
        this.bindObject = bindObject;
    }

    public CustomJCheckBox(String text, boolean isSelect, String id) {
        this(text, isSelect, id, null);
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

    public static class CustomJCheckBoxSelectedAction implements ActionListener {
        private BiConsumer<Object, JComponent[]> selected;
        private BiConsumer<Object, JComponent[]> unSelected;
        private JComponent[] jComponents;

        public CustomJCheckBoxSelectedAction(BiConsumer<Object, JComponent[]> selected, BiConsumer<Object, JComponent[]> unSelected, JComponent... jComponents) {
            this.selected = selected;
            this.unSelected = unSelected;
            this.jComponents = jComponents;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            AbstractButton abstractButton = (AbstractButton) e.getSource();
            CustomJCheckBox customJCheckBox = (CustomJCheckBox) abstractButton;
            if (abstractButton.getModel().isSelected()) {
                if (selected != null) {
                    selected.accept(customJCheckBox.getBindObject(), jComponents);
                }
            } else {
                unSelected.accept(customJCheckBox.getBindObject(), jComponents);
            }
        }
    }
}
