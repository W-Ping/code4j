package com.code4j.util;

import com.code4j.component.dialog.DBConfigDialog;
import com.code4j.component.dialog.ServiceApiConfigDialog;
import com.code4j.component.dialog.TableConfigDialog;
import com.code4j.config.TemplateTypeEnum;
import com.code4j.connect.DataSourceTypeEnum;
import sun.awt.AppContext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * @author liu_wp
 * @date 2020/11/18
 * @see
 */
public class CustomDialogUtil {
    private static final Object sharedFrameKey = CustomDialogUtil.class;
    private static final Object sharedOwnerFrameKey =
            new StringBuffer("CustomDialogUtil.customerSharedOwnerFrame");

    /**
     * @param parentComponent
     * @param title
     * @return
     */
    public static DBConfigDialog showDBConfigDialog(final Component parentComponent, final String title, DataSourceTypeEnum dataSourceTypeEnum) {
        DBConfigDialog dbConfigDialog = new DBConfigDialog(parentComponent, title, dataSourceTypeEnum);
        return dbConfigDialog;
    }

    /**
     * @param parentComponent
     * @param title
     * @param templateTypeEnum
     * @return
     */
    public static TableConfigDialog showTableConfigDialog(final Component parentComponent, final String title, TemplateTypeEnum templateTypeEnum) {
        TableConfigDialog tableConfigDialog = new TableConfigDialog(parentComponent, title, true, templateTypeEnum);
        return tableConfigDialog;
    }

    /**
     * @param parentComponent
     * @param title
     * @param templateTypeEnum
     * @return
     */
    public static ServiceApiConfigDialog showServiceApiConfigDialog(final Component parentComponent, final String title, TemplateTypeEnum templateTypeEnum) {
        ServiceApiConfigDialog serviceApiConfigDialog = new ServiceApiConfigDialog(parentComponent, title, true, templateTypeEnum);
        return serviceApiConfigDialog;
    }

    public static void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "错误", JOptionPane.ERROR_MESSAGE);
    }

    public static void showOk(String message) {
        JOptionPane.showMessageDialog(null, message, "成功", JOptionPane.INFORMATION_MESSAGE, null);
    }

    static void appContextPut(Object key, Object value) {
        AppContext.getAppContext().put(key, value);
    }

    public static Frame getRootFrame() throws HeadlessException {
        Frame sharedFrame =
                (Frame) CustomDialogUtil.appContextGet(sharedFrameKey);
        if (sharedFrame == null) {
            sharedFrame = CustomDialogUtil.getSharedOwnerFrame();
            CustomDialogUtil.appContextPut(sharedFrameKey, sharedFrame);
        }
        return sharedFrame;
    }

    static Frame getSharedOwnerFrame() throws HeadlessException {
        Frame sharedOwnerFrame =
                (Frame) CustomDialogUtil.appContextGet(sharedOwnerFrameKey);
        if (sharedOwnerFrame == null) {
            sharedOwnerFrame = new CustomDialogUtil.CustomerSharedOwnerFrame();
            CustomDialogUtil.appContextPut(sharedOwnerFrameKey,
                    sharedOwnerFrame);
        }
        return sharedOwnerFrame;
    }

    public static Frame setShareFrame(String key) {
        Frame sharedFrame = (Frame) AppContext.getAppContext().get(key);
        if (sharedFrame == null) {
            CustomerSharedOwnerFrame sharedOwnerFrame = new CustomerSharedOwnerFrame();
            AppContext.getAppContext().put(key, sharedOwnerFrame);
        }
        return sharedFrame;
    }

    static Object appContextGet(Object key) {
        return AppContext.getAppContext().get(key);
    }

    static class CustomerSharedOwnerFrame extends Frame implements WindowListener {
        public void addNotify() {
            super.addNotify();
            installListeners();
        }

        /**
         * Install window listeners on owned windows to watch for displayability changes
         */
        void installListeners() {
            Window[] windows = getOwnedWindows();
            for (Window window : windows) {
                if (window != null) {
                    window.removeWindowListener(this);
                    window.addWindowListener(this);
                }
            }
        }

        /**
         * Watches for displayability changes and disposes shared instance if there are no
         * displayable children left.
         */
        public void windowClosed(WindowEvent e) {
            synchronized (getTreeLock()) {
                Window[] windows = getOwnedWindows();
                for (Window window : windows) {
                    if (window != null) {
                        if (window.isDisplayable()) {
                            return;
                        }
                        window.removeWindowListener(this);
                    }
                }
                dispose();
            }
        }

        public void windowOpened(WindowEvent e) {
        }

        public void windowClosing(WindowEvent e) {
        }

        public void windowIconified(WindowEvent e) {
        }

        public void windowDeiconified(WindowEvent e) {
        }

        public void windowActivated(WindowEvent e) {
        }

        public void windowDeactivated(WindowEvent e) {
        }

        public void show() {
            // This frame can never be shown
        }

        public void dispose() {
            try {
                getToolkit().getSystemEventQueue();
                super.dispose();
            } catch (Exception e) {
                // untrusted code not allowed to dispose
            }
        }
    }
}
