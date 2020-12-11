package com.code4j;

import com.code4j.component.Code4jMainFrom;
import com.code4j.component.panel.*;
import com.code4j.config.Code4jConstants;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;

/**
 * @author liu_wp
 * @date 2020/11/17
 * @see
 */
public class Code4jApplication {

    public static void main(String[] args) throws Exception {
        TopPanel topPanel = new TopPanel(Code4jConstants.TOP_MIN_DEFAULT_SIZE);
        LeftPanel leftPanel = new LeftPanel(Code4jConstants.LEFT_MIN_DEFAULT_SIZE);
        RightPanel rightPanel = new RightPanel(Code4jConstants.RIGHT_MIN_DEFAULT_SIZE);
        BottomPanel bottomPanel = new BottomPanel(Code4jConstants.BOTTOM_MIN_DEFAULT_SIZE);
        leftPanel.setRightPanel(rightPanel);
        topPanel.setLeftPanel(leftPanel);
        LayoutDropTargetListener listener = new LayoutDropTargetListener(leftPanel);
        DropTarget dropTarget = new DropTarget(leftPanel, DnDConstants.ACTION_COPY_OR_MOVE, listener, true);
        Code4jMainFrom.Builder()
                .title("代码生成工具")
                .topPanel(topPanel)
                .leftPanel(leftPanel)
                .rightPanel(rightPanel)
                .bottomPanel(bottomPanel)
                .build();
    }
}
