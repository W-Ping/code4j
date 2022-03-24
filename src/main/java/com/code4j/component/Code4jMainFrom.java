package com.code4j.component;

import com.code4j.component.panel.*;
import com.code4j.config.Code4jConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/**
 * @author liu_wp
 * @date 2020/11/17
 * @see
 */
public class Code4jMainFrom extends JFrame {
    private String title;
    private TopPanel topPanel;
    private LeftPanel leftPanel;
    private RightPanel rightPanel;
    private BottomPanel bottomPanel;

    private Code4jMainFrom(Code4jMainFrom.Builder builder) {
        super(builder.title);
        this.title = builder.title;
        this.topPanel = builder.topPanel;
        this.leftPanel = builder.leftPanel;
        this.rightPanel = builder.rightPanel;
        this.bottomPanel = builder.bottomPanel;
        init();
    }

    private void init() {
        Box vBox2 = Box.createHorizontalBox();
        vBox2.add(leftPanel);
        vBox2.add(rightPanel);
        this.add(topPanel, BorderLayout.NORTH);
        this.add(vBox2, BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);
        this.setPreferredSize(Code4jConstants.FROM_MIN_DEFAULT_SIZE);
        this.setVisible(true);
        this.pack();
        this.setResizable(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
//        this.setAlwaysOnTop(true);
    }

    public static Code4jMainFrom.Builder Builder() {
        return new Code4jMainFrom.Builder();
    }

    public static class Builder {
        private String title;
        private TopPanel topPanel;
        private LeftPanel leftPanel;
        private RightPanel rightPanel;
        private BottomPanel bottomPanel;


        public Code4jMainFrom.Builder title(String title) {
            this.title = title;
            return this;
        }

        public Code4jMainFrom.Builder topPanel(TopPanel topPanel) {
            this.topPanel = topPanel;
            return this;
        }

        public Code4jMainFrom.Builder leftPanel(LeftPanel leftPanel) {
            this.leftPanel = leftPanel;
            return this;
        }

        public Code4jMainFrom.Builder rightPanel(RightPanel rightPanel) {
            this.rightPanel = rightPanel;
            return this;
        }

        public Code4jMainFrom.Builder bottomPanel(BottomPanel bottomPanel) {
            this.bottomPanel = bottomPanel;
            return this;
        }

        public Code4jMainFrom build() {
            return new Code4jMainFrom(this);
        }
    }
}
