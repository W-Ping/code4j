package com.code4j.util;

import com.code4j.component.dialog.*;
import com.code4j.enums.TemplateTypeEnum;
import com.code4j.enums.DataSourceTypeEnum;
import com.code4j.pojo.GenerateResultInfo;
import com.code4j.pojo.JdbcSourceInfo;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import sun.awt.AppContext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

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
     * 数据库配置窗口
     *
     * @param parentComponent
     * @param title
     * @return
     */
    public static DBConfigDialog showDBConfigDialog(final Component parentComponent, final String title, DataSourceTypeEnum dataSourceTypeEnum, JdbcSourceInfo jdbcSourceInfo) {
        if (dataSourceTypeEnum != null) {
            jdbcSourceInfo = Optional.ofNullable(jdbcSourceInfo).orElse(new JdbcSourceInfo());
            jdbcSourceInfo.setDataSourceTypeEnum(dataSourceTypeEnum);
        }
        DBConfigDialog dbConfigDialog = new DBConfigDialog(parentComponent, title + "【" + jdbcSourceInfo.getDataSourceTypeEnum().typeName() + "】", jdbcSourceInfo);
        return dbConfigDialog;
    }

    public static ProjectConfigDialog showProjectConfigDialog(final Component parentComponent, final String title, Object extObj, boolean isUpdate) {
        return new ProjectConfigDialog(parentComponent, title, true, extObj, isUpdate);
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
    public static XmlConfigDialog showXmlConfigDialog(final Component parentComponent, final String title, TemplateTypeEnum templateTypeEnum) {
        XmlConfigDialog xmlConfigDialog = new XmlConfigDialog(parentComponent, title, true, templateTypeEnum);
        return xmlConfigDialog;
    }

    /**
     * @param parentComponent
     * @param title
     * @param templateTypeEnum
     * @return
     */
    public static MapperConfigDialog showMapperConfigDialog(final Component parentComponent, final String title, TemplateTypeEnum templateTypeEnum) {
        MapperConfigDialog mapperConfigDialog = new MapperConfigDialog(parentComponent, title, true, templateTypeEnum);
        return mapperConfigDialog;
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
     * @param templateTypeEnum
     * @return
     */
    public static ControllerApiConfigDialog showControllerApiConfigDialog(final Component parentComponent, final String title, TemplateTypeEnum templateTypeEnum) {
        ControllerApiConfigDialog controllerApiConfigDialog = new ControllerApiConfigDialog(parentComponent, title, true, templateTypeEnum);
        return controllerApiConfigDialog;
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


    /**
     * @param parentComponent
     * @param message
     * @param function
     */
    public static void confirm(final Component parentComponent, String message, Function<Component, Void> function) {
        int res = JOptionPane.showConfirmDialog(parentComponent,
                message, "确认",
                JOptionPane.YES_NO_OPTION);
        if (res == JOptionPane.YES_OPTION) {
            function.apply(parentComponent);
        }
    }

    public static void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "错误", JOptionPane.ERROR_MESSAGE);
    }

    public static void showOk(String message, boolean autoClose, Consumer<Boolean> consumer) {
        final JOptionPane op = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
        final JDialog dialog = op.createDialog(null, "成功");
        if (autoClose) {
            ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
                    new BasicThreadFactory.Builder().namingPattern("dialog-schedule-pool-%d").daemon(true).build());
            executorService.scheduleAtFixedRate(() -> {
                dialog.setVisible(false);
                dialog.dispose();
                if (!executorService.isShutdown()) {
                    executorService.shutdown();
                }
                if (consumer != null) {
                    consumer.accept(true);
                }
            }, 800, 800, TimeUnit.MILLISECONDS);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setAlwaysOnTop(true);
            dialog.setModal(false);
            dialog.setVisible(true);

        } else {
            JOptionPane.showMessageDialog(null, message, "成功", JOptionPane.INFORMATION_MESSAGE, null);
        }

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

//    public static Frame setShareFrame(String key) {
//        Frame sharedFrame = (Frame) AppContext.getAppContext().get(key);
//        if (sharedFrame == null) {
//            CustomerSharedOwnerFrame sharedOwnerFrame = new CustomerSharedOwnerFrame();
//            AppContext.getAppContext().put(key, sharedOwnerFrame);
//        }
//        return sharedFrame;
//    }

    public static Object appContextGet(Object key) {
        return AppContext.getAppContext().get(key);
    }

    public static class CustomerSharedOwnerFrame extends Frame implements WindowListener {
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
