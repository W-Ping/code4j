package com.code4j.component.panel;

import com.code4j.config.Code4jConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * 文件地址选择器
 *
 * @author liu_wp
 * @date 2020/11/19
 * @see
 */
public class CustomJFileChooserPanel extends BasePanel {
    private JButton selectBtn;
    private JTextField selectFile;
    private JFileChooser jFileChooser;
    private Object extObject;
    private File file;

    public CustomJFileChooserPanel(Component parent, Boolean isMultiSelect, Integer mode) {
        super();
        this.jFileChooser = new JFileChooser();
        this.selectFile = new JTextField(Code4jConstants.CACHE_PROJECT_SELECTED_FILE.getAbsolutePath());
        this.selectFile.setPreferredSize(new Dimension(250, 30));
        this.selectFile.setEditable(false);
        this.selectBtn = new JButton("选择");
        selectBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                showFileOpenDialog(parent, mode, isMultiSelect);
            }
        });
        CommonPanel commonPanel = new CommonPanel();
        commonPanel.add(selectFile);
        commonPanel.add(selectBtn);

        this.add(commonPanel);
    }

    /**
     * @param parent
     * @param mode
     * @param isMultiSelect
     */
    public void showFileOpenDialog(Component parent, Integer mode, Boolean isMultiSelect) {
        // 设置默认显示的文件夹为当前文件夹
        jFileChooser.setCurrentDirectory(Code4jConstants.CACHE_PROJECT_SELECTED_FILE);
        // 设置文件选择的模式（只选文件、只选文件夹、文件和文件均可选）
        jFileChooser.setFileSelectionMode(mode == null ? JFileChooser.FILES_AND_DIRECTORIES : mode);
        // 设置是否允许多选
        jFileChooser.setMultiSelectionEnabled(isMultiSelect == null ? true : isMultiSelect);
        // 添加可用的文件过滤器（FileNameExtensionFilter 的第一个参数是描述, 后面是需要过滤的文件扩展名 可变参数）
//        this.addChoosableFileFilter(new FileNameExtensionFilter("zip(*.zip, *.rar)", "zip", "rar"));
        // 设置默认使用的文件过滤器
//        this.setFileFilter(new FileNameExtensionFilter("image(*.jpg, *.png, *.gif)", "jpg", "png", "gif"));
        // 打开文件选择框（线程将被阻塞, 直到选择框被关闭）
        int result = jFileChooser.showOpenDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            // 如果点击了"确定", 则获取选择的文件路径
            file = jFileChooser.getSelectedFile();
            Code4jConstants.CACHE_PROJECT_SELECTED_FILE = file;
            // 如果允许选择多个文件, 则通过下面方法获取选择的所有文件
            // File[] files = fileChooser.getSelectedFiles();
            selectFile.setText(file.getAbsolutePath());
        }
    }

    /**
     * @return
     */
    public String getSelectValue() {
        if (this.selectFile != null) {
            return this.selectFile.getText();
        }
        return null;
    }

    public File getFile() {
        return file;
    }

    public String getFileName() {
        return file == null ? null : file.getName();
    }

    public void setFile(final File file) {
        this.file = file;
    }

    public JButton getSelectBtn() {
        return selectBtn;
    }

    public void setSelectBtn(final JButton selectBtn) {
        this.selectBtn = selectBtn;
    }

    public JTextField getSelectFile() {
        return selectFile;
    }

    public void setSelectFile(final JTextField selectFile) {
        this.selectFile = selectFile;
    }

    public JFileChooser getjFileChooser() {
        return jFileChooser;
    }

    public void setjFileChooser(final JFileChooser jFileChooser) {
        this.jFileChooser = jFileChooser;
    }

    public Object getExtObject() {
        return extObject;
    }

    public void setExtObject(final Object extObject) {
        this.extObject = extObject;
    }

    @Override
    protected void init() {
        this.setVisible(true);
    }
}
