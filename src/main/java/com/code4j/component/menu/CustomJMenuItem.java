package com.code4j.component.menu;

import javax.swing.*;

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

    public CustomJMenuItem(String text, Object data) {
        super(text);
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
