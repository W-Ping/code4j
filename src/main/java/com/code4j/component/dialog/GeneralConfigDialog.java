package com.code4j.component.dialog;

import com.code4j.component.CustomJTextField;
import com.code4j.component.label.LinkLabel;
import com.code4j.component.panel.CommonPanel;
import com.code4j.component.panel.CustomJFileChooserPanel;
import com.code4j.component.select.ProjectConfigSelect;
import com.code4j.config.Code4jConstants;
import com.code4j.pojo.GeneralConfigInfo;
import com.code4j.util.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author lwp
 * @date 2022-07-29
 */
public class GeneralConfigDialog extends BaseDialog {
    private static final Dimension INPUT_DIMENSION = new Dimension(80, 28);
    private CustomJTextField customJTextField;
    private GeneralConfigInfo generalConfigInfo;
    private JLabel authorLabel;
    private static final Map<String, String> CONFIG_DATA_MAP = new HashMap<>();
    ;

    /**
     * @param parentComponent
     * @param title
     * @param modal
     * @param extObj
     */
    public GeneralConfigDialog(Component parentComponent, String title, boolean modal, Object extObj) {
        super(parentComponent, title, modal, extObj);
    }

    @Override
    public void beforeInit() {
        CONFIG_DATA_MAP.clear();
        this.setPreferredSize(new Dimension(400, 320));
    }

    @Override
    protected Component content() {
        GeneralConfigInfo configInfo = new GeneralConfigInfo();
        configInfo.setVersion(Code4jConstants.APP_VERSION);
        final List<GeneralConfigInfo> configInfos = SQLiteUtil.select(configInfo);
        if (!CollectionUtils.isEmpty(configInfos)) {
            generalConfigInfo = configInfos.get(0);
        } else {
            generalConfigInfo = new GeneralConfigInfo();
        }
        CommonPanel commonPanel = new CommonPanel(new VFlowLayout());
        CommonPanel cp0 = new CommonPanel(new JLabel("源码地址："), new LinkLabel("Gitee", Color.BLUE, (e) -> {
            SystemUtil.open(Code4jConstants.GITEE_URL);
        }));
        CommonPanel cp1 = new CommonPanel(new JLabel("版本："), new JLabel(Code4jConstants.APP_VERSION));

        customJTextField = new CustomJTextField("", INPUT_DIMENSION);
        final String configData = generalConfigInfo.getConfigData();
        // 代码作者
        String author = Code4jConstants.DEFAULT_AUTHOR;
        Long defaultSelected = null;
        String defaultPath = null;
        if (StringUtils.isNotBlank(configData)) {
            Map<String, String> configDataMap = JSONUtil.jsonToMap(configData);
            CONFIG_DATA_MAP.putAll(configDataMap);
            if (configDataMap.containsKey(Code4jConstants.AUTHOR_KEY)) {
                author = configDataMap.get(Code4jConstants.AUTHOR_KEY);
            }
            if (configDataMap.containsKey(Code4jConstants.DEFAULT_SELECTED_CONFIG_KEY)) {
                defaultSelected = Long.valueOf(configDataMap.get(Code4jConstants.DEFAULT_SELECTED_CONFIG_KEY));
            }
            if (configDataMap.containsKey(Code4jConstants.DEFAULT_PATH_KEY)) {
                defaultPath = configDataMap.get(Code4jConstants.DEFAULT_PATH_KEY);
            }
        }
        CommonPanel cp2 = new CommonPanel(new JLabel("项目地址："), this.geDefaultPath(defaultPath));
        CommonPanel cp3 = new CommonPanel(new JLabel("项目配置："), this.getDefaultProjectConfig(defaultSelected));
        CommonPanel cp4 = new CommonPanel(new JLabel("代码作者："));
        customJTextField.setText(author);
        authorLabel = new JLabel(author);
        AtomicReference<LinkLabel> saveLabel = new AtomicReference<>();
        cp4.add(authorLabel);
        cp4.add(new LinkLabel(Code4jConstants.EDIT_BTN_TEXT, Color.BLUE, e -> {
            if (Code4jConstants.EDIT_BTN_TEXT.equals(e.getText())) {
                cp4.remove(authorLabel);
                cp4.add(customJTextField, 1);
                saveLabel.set(new LinkLabel("保存", Color.BLUE, ee -> {
                    save(cp4, saveLabel, e);
                }));
                e.setText("取消");
                e.setForeground(Color.RED);
                cp4.add(saveLabel.get(), 3);
            } else {
                cancel(cp4, saveLabel, e);
            }
            updateUI();
        }));
        CommonPanel docWebCp = new CommonPanel(new JLabel("下载地址："),new LinkLabel(Code4jConstants.WEB_DOC_URL, Color.BLACK, (e) -> {
            SystemUtil.open(Code4jConstants.WEB_DOC_URL);
        }));
        CommonPanel bottomCp=new CommonPanel(cp0,cp1);
        commonPanel.add(cp2);
        commonPanel.add(cp3);
        commonPanel.add(cp4);
        commonPanel.add(docWebCp);
        commonPanel.add(bottomCp);
        return commonPanel;
    }

    /**
     * @param defaultSelected
     * @return
     */
    private ProjectConfigSelect getDefaultProjectConfig(Long defaultSelected) {
        ProjectConfigSelect projectConfigSelect = new ProjectConfigSelect(this, null, defaultSelected, (c, item) -> {
            CONFIG_DATA_MAP.put(Code4jConstants.DEFAULT_SELECTED_CONFIG_KEY, item.getId().toString());
            generalConfigInfo.setConfigData(JSONUtil.Object2JSON(CONFIG_DATA_MAP));
            SQLiteUtil.insertOrUpdate(generalConfigInfo);
        });
        return projectConfigSelect;
    }

    private CustomJFileChooserPanel geDefaultPath(String defaultPath) {
        CustomJFileChooserPanel jFileChooser = new CustomJFileChooserPanel(this, false, JFileChooser.DIRECTORIES_ONLY, defaultPath, e -> {
            CONFIG_DATA_MAP.put(Code4jConstants.DEFAULT_PATH_KEY, e.getSelectValue());
            generalConfigInfo.setConfigData(JSONUtil.Object2JSON(CONFIG_DATA_MAP));
            SQLiteUtil.insertOrUpdate(generalConfigInfo);
        });
        jFileChooser.setSelectFilePreferredSize(new Dimension(180, 30));
        return jFileChooser;
    }

    private void cancel(CommonPanel commonPanel, AtomicReference<LinkLabel> save, LinkLabel label) {
        commonPanel.remove(customJTextField);
        commonPanel.remove(save.get());
        commonPanel.add(authorLabel, 1);
        label.setText("编辑");
        label.setForeground(Color.BLUE);
    }

    private void updateUI() {
        //添加或删除组件后,更新窗口
        SwingUtilities.updateComponentTreeUI(this);
    }

    @Override
    public boolean visibleOkBtn() {
        return false;
    }

    private void save(CommonPanel cp2, AtomicReference<LinkLabel> saveLabel, LinkLabel label) {
        final String author = customJTextField.getText().trim();
        if (StringUtils.isNotBlank(author)) {
            CONFIG_DATA_MAP.put(Code4jConstants.AUTHOR_KEY, author);
        }
        if (CONFIG_DATA_MAP.size() > 0) {
            final String configData = JSONUtil.Object2JSON(CONFIG_DATA_MAP);
            generalConfigInfo.setConfigData(configData);
            if (SQLiteUtil.insertOrUpdate(generalConfigInfo)) {
                CustomDialogUtil.showOk("保存成功！", true, e -> {
                    authorLabel.setText(author);
                    cancel(cp2, saveLabel, label);
                });
            } else {
                CustomDialogUtil.showError("保存失败！");
            }
        } else {
            CustomDialogUtil.showError("请填写配置信息！");
        }
    }

    @Override
    protected void okClick() {
    }
}
