package com.code4j.util;

import com.code4j.config.Code4jConstants;
import com.code4j.enums.JdbcType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @author liu_wp
 * @date 2020/11/13
 * @see
 */
public class SystemUtil {
    protected static final Logger log = LoggerFactory.getLogger(SystemUtil.class);

    /**
     * @param path
     * @return
     */
    public static URL getSystemResource(String path) {
        try {
            return ClassLoader.getSystemResource(path);
        } catch (Exception e) {
            log.error("加载资源失败！【{}】{}", path, e.getMessage());
        }
        return null;
    }

    /**
     * @param path
     * @return
     */
    public static String readByLines(String path) {
        try {
            return readByStream(new FileInputStream(path));
        } catch (FileNotFoundException e) {
            log.error("readByLines 读取数据失败！【{}】{}", path, e.getMessage());
        }
        return null;
    }

    /**
     * @param inputStream
     * @return
     */
    public static String readByStream(InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }
        String content = null;
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            StringBuffer sb = new StringBuffer();
            String temp = null;
            while ((temp = bufferedReader.readLine()) != null) {
                String s = MarkdownUtil.markdownToHtmlExtensitons(temp);
                sb.append(s);
            }
            content = sb.toString();
            bufferedReader.close();
        } catch (UnsupportedEncodingException e) {
            log.error("readByStream 执行失败！{}", e.getMessage());
        } catch (IOException e) {
            log.error("readByStream 执行失败！{}", e.getMessage());
        }
        return content;
    }

    /**
     * @param path
     * @param content
     */
    public static void writeFile(String path, String content) {
        try {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            bufferedWriter.write(content);
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            log.error("writeFile 执行失败！{}{}{}", path, content, e.getMessage());
        }
    }

    public static void open(String outDir) {
        try {
            String osName = System.getProperty("os.name");
            if (osName != null) {
                if (osName.contains("Mac")) {
                    Runtime.getRuntime().exec("open " + outDir);
                } else if (osName.contains("Windows")) {
                    Runtime.getRuntime().exec("cmd /c start " + outDir);
                } else {
                    log.debug("文件输出目录:" + outDir);
                }
            }
        } catch (IOException var3) {
            var3.printStackTrace();
        }
    }

    public static JComponent createContent() {
//        JPanel contentPane = new JPanel(new BorderLayout());
//        JPanel webBrowserPanel = new JPanel(new BorderLayout());
//        webBrowserPanel.setBorder(BorderFactory.createTitledBorder("Native Web Browser component"));
//        final JWebBrowser webBrowser = new JWebBrowser();
//        webBrowser.navigate("http://www.google.com");
//        webBrowserPanel.add(webBrowser, BorderLayout.CENTER);
//        contentPane.add(webBrowserPanel, BorderLayout.CENTER);
//        // Create an additional bar allowing to show/hide the menu bar of the web browser.
//        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 4));
//        JCheckBox menuBarCheckBox = new JCheckBox("Menu Bar", webBrowser.isMenuBarVisible());
//        menuBarCheckBox.addItemListener(new ItemListener()
//        {
//            public void itemStateChanged(ItemEvent e)
//            {
//                webBrowser.setMenuBarVisible(e.getStateChange() == ItemEvent.SELECTED);
//            }
//        });
//        buttonPanel.add(menuBarCheckBox);
//        contentPane.add(buttonPanel, BorderLayout.SOUTH);
//        return contentPane;
        return null;
    }

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

    /**
     * @param dataType
     * @return
     */
    public static String formatDataType(String dataType) {
        dataType = dataType.toUpperCase();
        if ("CHAR".equals(dataType) || "TEXT".equals(dataType) || "VARCHAR".equals(dataType) || "TINYTEXT".equals(dataType) || "LONGTEXT".equals(dataType) || "JSON".equals(dataType) || "XML".equals(dataType)) {
            dataType = "java.lang.String";
        } else if ("BIGINT".equals(dataType) || "INT8".equals(dataType) || "BIGSERIAL".equals(dataType)) {
            dataType = "java.lang.Long";
        } else if ("INT".equals(dataType) || "INTEGER".equals(dataType) || "MEDIUMINT".equals(dataType) || "TINYINT".equals(dataType) || "SMALLINT".equals(dataType)
                || "INT2".equals(dataType) || "INT4".equals(dataType)) {
            dataType = "java.lang.Integer";
        } else if ("FLOAT".equals(dataType) || "FLOAT4".equals(dataType)) {
            dataType = "java.lang.Float";
        } else if ("DOUBLE".equals(dataType) || "FLOAT8".equals(dataType) || "MONEY".equals(dataType)) {
            dataType = "java.lang.Double";
        } else if ("NUMERIC".equals(dataType) || "DECIMAL".equals(dataType) || "numeric".equals(dataType)) {
            dataType = "java.math.BigDecimal";
        } else if ("DATE".equals(dataType) || "YEAR".equals(dataType)) {
            return "java.util.Date";
        } else if ("TIMESTAMP".equals(dataType) || "DATETIME".equals(dataType)) {
            return "java.sql.Timestamp";
        } else if ("BIT".equals(dataType) || "BOOL".equals(dataType)) {
            return "java.lang.Boolean";
        } else if ("BLOB".equals(dataType)) {
            return "java.lang.byte[]";
        } else if ("CLOB".equals(dataType)) {
            dataType = "java.sql.Clob";
        } else if ("TIME".equals(dataType)) {
            dataType = "java.sql.Time";
        } else {
            dataType = "java.lang.Object";
        }
        return dataType;
    }

    /**
     * @param jdbcType
     * @param columnCount
     * @return
     */
    public static String convertJdbcType(String jdbcType, Integer columnCount) {
        if (columnCount != null) {
            final JdbcType jt = JdbcType.forCode(columnCount.intValue());
            return jt.name();
        }
        return jdbcType;
    }
}
