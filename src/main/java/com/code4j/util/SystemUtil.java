package com.code4j.util;

import com.code4j.config.Code4jConstants;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author liu_wp
 * @date 2020/11/13
 * @see
 */
public class SystemUtil {
    /**
     * 复制
     *
     * @param str
     */
    public static void setClipboardString(String str) {
        //获取协同剪贴板，单例
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        //封装文本内容
        Transferable trans = new StringSelection(str);
        //把文本内容设置到系统剪贴板上
        clipboard.setContents(trans, null);
    }

    /**
     * 粘贴
     *
     * @return
     */
    public static String getClipboardString() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable trans = clipboard.getContents(null);
        if (trans != null) {
            if (trans.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                String clipboardStr = null;
                try {
                    clipboardStr = (String) trans.getTransferData(DataFlavor.stringFlavor);
                } catch (UnsupportedFlavorException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return clipboardStr;
            }
        }
        return null;
    }

    public static void browseWebUrl(String webUrl) {
        try {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(new URI(webUrl));
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (URISyntaxException e1) {
                e1.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void restart() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", Code4jConstants.APPLICATION_JAR);
                try {
                    processBuilder.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        System.exit(0);
    }

    public static String formatDataType(String dataType) {
        dataType = dataType.toUpperCase();
        if (dataType.equals("CHAR")
                || dataType.equals("TEXT")
                || dataType.equals("VARCHAR")
                || dataType.equals("TINYTEXT")
                || dataType.equals("LONGTEXT")) {
            dataType = "java.lang.String";
        } else if (dataType.equals("BIGINT")) {
            dataType = "java.lang.Long";
        } else if (dataType.equals("INT")
                || dataType.equals("INTEGER")
                || dataType.equals("MEDIUMINT")
                || dataType.equals("TINYINT")
                || dataType.equals("SMALLINT")) {
            dataType = "java.lang.Integer";
        } else if (dataType.equals("FLOAT")) {
            dataType = "java.lang.Float";
        } else if (dataType.equals("DOUBLE")) {
            dataType = "java.lang.Double";
        } else if (dataType.equals("NUMERIC")
                || dataType.equals("DECIMAL")) {
            dataType = "java.math.BigDecimal";
        } else if (dataType.equals("DATE")
                || dataType.equals("YEAR")
                || dataType.equals("TIME")) {
            return "java.util.Date";
        } else if (dataType.equals("TIMESTAMP")
                || dataType.equals("DATETIME")) {
            return "java.sql.Timestamp";
        } else if (dataType.equals("BIT")) {
            return "java.lang.Boolean";
        } else if (dataType.equals("BLOB")) {
            return "java.lang.byte[]";
        } else if (dataType.equals("CLOB")) {
            dataType = "java.sql.Clob";
        } else {
            dataType = "java.lang.Object";
        }
        return dataType;
    }
}
