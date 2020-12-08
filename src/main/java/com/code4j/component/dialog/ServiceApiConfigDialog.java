package com.code4j.component.dialog;

import java.awt.*;

/**
 * @author liu_wp
 * @date 2020/11/23
 * @see
 */
public class ServiceApiConfigDialog extends BaseDialog {
    /**
     * @param parentComponent
     * @param title
     * @param modal
     * @param extObj
     */
    public ServiceApiConfigDialog(final Component parentComponent, final String title, final boolean modal, final Object extObj) {
        super(parentComponent, title, modal, extObj);
    }

    @Override
    protected Component content() {
        return null;
    }

    @Override
    protected void okClick() {

    }
}
