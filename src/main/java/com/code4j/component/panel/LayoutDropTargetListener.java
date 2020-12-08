package com.code4j.component.panel;

import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;

/**
 * @author liu_wp
 * @date 2020/12/7
 * @see
 */
public class LayoutDropTargetListener implements DropTargetListener {
    public BasePanel panel;

    public LayoutDropTargetListener(BasePanel panel) {
        this.panel = panel;
    }

    @Override
    public void dragEnter(final DropTargetDragEvent dtde) {
        System.out.println("dragEnter");
    }

    @Override
    public void dragOver(final DropTargetDragEvent dtde) {
        System.out.println("dragOver");

    }

    @Override
    public void dropActionChanged(final DropTargetDragEvent dtde) {
        System.out.println("dropActionChanged");

    }

    @Override
    public void dragExit(final DropTargetEvent dte) {
        System.out.println("dragExit");

    }

    @Override
    public void drop(final DropTargetDropEvent dtde) {
        System.out.println("drop");

    }
}
