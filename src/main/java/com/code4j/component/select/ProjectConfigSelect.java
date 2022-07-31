package com.code4j.component.select;

import com.code4j.component.panel.RightPanel;
import com.code4j.pojo.ProjectCodeConfigInfo;
import com.code4j.util.SQLiteUtil;
import org.apache.commons.collections4.CollectionUtils;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

/**
 * @author :lwp
 * @date :Created in 2022-03-14
 */
public class ProjectConfigSelect extends JComboBox {
    public static final Dimension SIZE = new Dimension(100, 20);

    public ProjectConfigSelect(Component component, String tableName, Long defaultSelected, BiConsumer<Component, ProjectCodeConfigInfo> function) {
        ProjectConfigSelect projectConfigSelect = this;
        ProjectCodeConfigInfo defaultConfig = null;
        if (defaultSelected != null && defaultSelected != -1L) {
            ProjectCodeConfigInfo projectCodeConfigInfo = new ProjectCodeConfigInfo();
            projectCodeConfigInfo.setId(defaultSelected);
            List<ProjectCodeConfigInfo> items = SQLiteUtil.select(projectCodeConfigInfo);
            if (CollectionUtils.isNotEmpty(items)) {
                defaultConfig = this.getDefaultItem(items, defaultSelected);
            }
        }
        this.setPreferredSize(SIZE);
        if (defaultConfig == null) {
            defaultConfig = new ProjectCodeConfigInfo(tableName);
        }
        if (component != null && component instanceof RightPanel) {
            ((RightPanel) component).loadProjectConfig(defaultConfig);
        }
        this.addItem(defaultConfig);
        this.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                List<ProjectCodeConfigInfo> items = SQLiteUtil.select(new ProjectCodeConfigInfo());
                if (CollectionUtils.isNotEmpty(items)) {
                    projectConfigSelect.removeAllItems();
                    projectConfigSelect.addItem(new ProjectCodeConfigInfo(tableName));
                    items.forEach(v -> projectConfigSelect.addItem(v));
                }

            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {

            }
        });
        this.addItemListener(new SelectItem(component, function));
    }

    /**
     * @param items
     * @param defaultSelected
     * @return
     */
    private ProjectCodeConfigInfo getDefaultItem(List<ProjectCodeConfigInfo> items, Long defaultSelected) {
        if (defaultSelected != null) {
            Optional<ProjectCodeConfigInfo> optional = items.stream().filter(v -> v.getId().equals(defaultSelected)).findFirst();
            if (optional.isPresent()) {
                return optional.get();
            }
        }
        return null;
    }

    public class SelectItem implements ItemListener {
        private Component component;
        private BiConsumer<Component, ProjectCodeConfigInfo> function;

        public SelectItem(Component component, BiConsumer<Component, ProjectCodeConfigInfo> function) {
            this.component = component;
            this.function = function;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            Object item = e.getItem();
            if (e.getStateChange() == ItemEvent.SELECTED) {
                function.accept(component, (ProjectCodeConfigInfo) item);
            }
        }
    }
}
