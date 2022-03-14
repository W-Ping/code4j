package com.code4j.component.label;

import com.code4j.pojo.ProjectCodeConfigInfo;
import com.code4j.util.CustomDialogUtil;
import com.code4j.util.PropertiesUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

/**
 * @author lwp
 * @date 2022-03-12
 */
public class ConfigLabel extends JLabel {
    public ConfigLabel(Component component, String text) {
        this.setText(text);
        this.setForeground(Color.BLUE);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.setFont(new Font("Helvetica", Font.PLAIN, 12));
        LoadConfig loadConfig = new LoadConfig(component, text);
        this.addMouseListener(loadConfig);
    }

    public class LoadConfig implements MouseListener {
        private Component bindCommonPanel;
        private String title;

        public LoadConfig(Component bindCommonPanel, String title) {
            this.bindCommonPanel = bindCommonPanel;
            this.title = title;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            List<ProjectCodeConfigInfo> projectConfigPropertyValues = PropertiesUtil.getProjectConfigPropertyValues();
            CustomDialogUtil.showSelectProjectConfigDialog(bindCommonPanel, title, projectConfigPropertyValues);
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
}
