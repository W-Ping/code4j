package com.code4j.component.select;

import com.code4j.pojo.ProjectCodeConfigInfo;
import com.code4j.util.PropertiesUtil;
import org.apache.commons.collections4.CollectionUtils;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * @author :lwp
 * @date :Created in 2022-03-14
 */
public class ProjectConfigSelect extends JComboBox {

    public ProjectConfigSelect(Component component, BiConsumer<Component, ProjectCodeConfigInfo> function) {
        ProjectConfigSelect projectConfigSelect = this;
        this.addItem(new ProjectCodeConfigInfo("---默认配置---", -1));
        this.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                List<ProjectCodeConfigInfo> items = PropertiesUtil.getProjectConfigPropertyValues();
                ProjectCodeConfigInfo selectedItem = (ProjectCodeConfigInfo) projectConfigSelect.getSelectedItem();
                projectConfigSelect.removeAllItems();
                projectConfigSelect.addItem(new ProjectCodeConfigInfo("---默认配置---", -1));
                if (CollectionUtils.isNotEmpty(items)) {
                    for (ProjectCodeConfigInfo item : items) {
                        if (selectedItem.getIndex().equals(item.getIndex())) {
                            selectedItem = item;
                        }
                        projectConfigSelect.addItem(item);
                    }
                    projectConfigSelect.setSelectedItem(selectedItem);
                    SelectItem selectItem = new SelectItem(component, function);
                    projectConfigSelect.addItemListener(selectItem);
                }
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {

            }
        });
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
            if (e.getStateChange() == ItemEvent.SELECTED && item instanceof ProjectCodeConfigInfo) {
                function.accept(component, (ProjectCodeConfigInfo) item);
            }
        }
    }
}
