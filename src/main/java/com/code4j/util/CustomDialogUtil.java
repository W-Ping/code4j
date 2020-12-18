package com.code4j.util;

import com.code4j.component.dialog.DBConfigDialog;
import com.code4j.component.dialog.GenerateResultDialog;
import com.code4j.component.dialog.ServiceApiConfigDialog;
import com.code4j.component.dialog.TableConfigDialog;
import com.code4j.config.TemplateTypeEnum;
import com.code4j.connect.DataSourceTypeEnum;
import com.code4j.pojo.GenerateResultInfo;
import com.code4j.pojo.JdbcSourceInfo;
import sun.awt.AppContext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

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
    public static DBConfigDialog showDBConfigDialog(final Component parentComponent, final String title, DataSourceTypeEnum dataSourceTypeEnum, JdbcSourceInfo jdbcSourceInfo) {
        DBConfigDialog dbConfigDialog = new DBConfigDialog(parentComponent, title, jdbcSourceInfo, dataSourceTypeEnum);
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

    /**
     * @param parentComponent
     * @param title
     * @param generateResultInfos
     * @return
     */
    public static GenerateResultDialog showGenerateResultDialog(final Component parentComponent, String title, List<GenerateResultInfo> generateResultInfos) {
        GenerateResultDialog generateResultDialog = new GenerateResultDialog(parentComponent, title, generateResultInfos);
        return generateResultDialog;
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
        @Override
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
        @Override
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

        @Override
        public void windowOpened(WindowEvent e) {
        }

        @Override
        public void windowClosing(WindowEvent e) {
        }

        @Override
        public void windowIconified(WindowEvent e) {
        }

        @Override
        public void windowDeiconified(WindowEvent e) {
        }

        @Override
        public void windowActivated(WindowEvent e) {
        }

        @Override
        public void windowDeactivated(WindowEvent e) {
        }

        @Override
        public void show() {
            // This frame can never be shown
        }

        @Override
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
