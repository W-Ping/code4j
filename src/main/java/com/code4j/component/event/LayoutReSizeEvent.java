package com.code4j.component.event;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * @author liu_wp
 * @date 2020/12/7
 * @see
 */
public class LayoutReSizeEvent extends MouseAdapter {
    @Override
    public void mouseClicked(final MouseEvent e) {
        System.out.println("mouseClicked");
        super.mouseClicked(e);
    }

    @Override
    public void mousePressed(final MouseEvent e) {
        System.out.println("mousePressed");
        Point locationOnScreen = e.getLocationOnScreen();
        System.out.println(locationOnScreen.getX());
        System.out.println(locationOnScreen.getY());
        super.mousePressed(e);
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
        System.out.println("mouseReleased");
        super.mouseReleased(e);
    }

    @Override
    public void mouseEntered(final MouseEvent e) {
        System.out.println("mouseEntered");
        super.mouseEntered(e);
    }

    @Override
    public void mouseExited(final MouseEvent e) {
        System.out.println("mouseExited");
        super.mouseExited(e);
    }

    @Override
    public void mouseWheelMoved(final MouseWheelEvent e) {
        System.out.println("mouseWheelMoved");
        super.mouseWheelMoved(e);
    }

    @Override
    public void mouseDragged(final MouseEvent e) {
        System.out.println("mouseDragged");
        super.mouseDragged(e);
    }

    @Override
    public void mouseMoved(final MouseEvent e) {
        System.out.println("mouseMoved");
        super.mouseMoved(e);
    }
}
