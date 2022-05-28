package com.code4j.component.menu;

import com.code4j.util.SystemUtil;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;

/**
 * 自定义菜单
 *
 * @author lwp
 * @date 2022-03-12
 */
public class CustomJMenuItem extends JMenuItem {
    /**
     * 菜单绑定数据
     */
    private Object data;

    public CustomJMenuItem(String text, String icon, Object data) {
        this(text, icon, data, null);
    }

    public CustomJMenuItem(String text, String icon, CustomBasicMenuItemUI customBasicMenuItemUI) {
        this(text, icon, null, customBasicMenuItemUI);
    }

    public CustomJMenuItem(String text, String icon, Object data, CustomBasicMenuItemUI customBasicMenuItemUI) {
        super(text);
        this.data = data;
        this.setUI(customBasicMenuItemUI);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        if (StringUtils.isNotBlank(icon)) {
            this.setIcon(new ImageIcon(SystemUtil.getSystemResource(String.format("images/%s.png", icon))));
        }
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
