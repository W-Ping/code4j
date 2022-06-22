package com.code4j.component.menu;

import javax.swing.*;
import javax.swing.plaf.basic.BasicMenuItemUI;
import java.awt.*;

/**
 * @author lwp
 * @date 2022-05-27
 */
public class CustomBasicMenuItemUI extends BasicMenuItemUI {
    private float alpha;
    private static float selectionAlpha = 0.9f;

    public CustomBasicMenuItemUI(Color bgColor, Color fgColor) {
        this(bgColor, fgColor, null);
    }

    public CustomBasicMenuItemUI(Color bgColor, Color fgColor, Float alpha) {
        super.selectionBackground = bgColor;
        super.selectionForeground = fgColor;
        this.alpha = alpha != null ? alpha : 0.7f;
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        // 设为透明,这样才会更新菜单低下的内容(这个请了解Opaque的作用)
        menuItem.setOpaque(false);
    }

    @Override
    public void paint(Graphics g, JComponent comp) {
        // 创建一个graphics的副本
        Graphics2D gx = (Graphics2D) g.create();
        // alpha 就是透明度.值越小 透明度越大
        gx.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        // 将控件画到半透明 的画布上
        super.paint(gx, comp);
        // graphics使用后要销毁,这是良好习惯
        gx.dispose();
    }

    /**
     * 重写了背景的绘制方法,不管Opaque的属性,只按不透明方式画菜单背景
     */
    @Override
    protected void paintBackground(Graphics g, JMenuItem menuItem, Color bgColor) {
        ButtonModel model = menuItem.getModel();
        Color oldColor = g.getColor();
        int menuWidth = menuItem.getWidth();
        int menuHeight = menuItem.getHeight();
        if (model.isArmed() || (menuItem instanceof JMenu && model.isSelected())) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, selectionAlpha));
            g2.setColor(bgColor);
            g2.fillRect(0, 0, menuWidth, menuHeight);
        } else {
            g.setColor(menuItem.getBackground());
            g.fillRect(0, 0, menuWidth, menuHeight);
        }
        g.setColor(oldColor);
    }

    @Override
    protected void paintText(Graphics g, JMenuItem menuItem, Rectangle textRect, String text) {
        ButtonModel model = menuItem.getModel();
        // 鼠标移动菜单时文字不透明
        if (model.isArmed() || model.isSelected()) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            super.paintText(g2, menuItem, textRect, text);
            g2.dispose();
        } else {
            super.paintText(g, menuItem, textRect, text);
        }
    }

    /**
     * 设置选中时是否透明
     *
     * @param alpha
     */
    public static void setSelectionAlpha(float alpha) {
        selectionAlpha = alpha;
    }
}
