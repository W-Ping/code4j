package com.code4j.util;

import com.code4j.component.dialog.DBConfigDialog;
import com.code4j.connect.DataSourceTypeEnum;

import javax.swing.*;
import java.awt.*;

/**
 * @author liu_wp
 * @date 2020/11/18
 * @see
 */
public class CustomDialogUtil {

    /**
     * @param parentComponent
     * @param title
     * @return
     */
    public static DBConfigDialog showDBConfigDialog(final Component parentComponent, final String title, DataSourceTypeEnum dataSourceTypeEnum) {
        DBConfigDialog dbConfigDialog = new DBConfigDialog(parentComponent, title, dataSourceTypeEnum);
        return dbConfigDialog;
    }

    public static void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "错误", JOptionPane.ERROR_MESSAGE);
    }
}
