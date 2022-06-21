package com.code4j.component.dialog;

import com.code4j.component.CustomJCheckBox;
import com.code4j.component.panel.CommonPanel;
import com.code4j.enums.ControllerApiTemplateEnum;
import com.code4j.pojo.ControllerApiParamsInfo;
import com.code4j.pojo.ControllerParamsInfo;
import org.apache.commons.collections4.CollectionUtils;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author lwp
 * @date 2022-06-21
 */
public class ControllerApiConfigDialog extends BaseDialog {
    private List<CustomJCheckBox> selectCustomJCheckBoxList;

    /**
     * @param parentComponent
     * @param title
     * @param modal
     * @param extObj
     */
    public ControllerApiConfigDialog(final Component parentComponent, final String title, final boolean modal, final Object extObj) {
        super(parentComponent, title, modal, extObj);
    }

    @Override
    public void beforeInit() {
        selectCustomJCheckBoxList = new ArrayList<>();
        this.setPreferredSize(new Dimension(250, 200));
    }

    @Override
    protected Component content() {
        Map<ControllerApiTemplateEnum, ControllerApiParamsInfo> selectedMap = getSelectedMap();
        Box box = Box.createVerticalBox();
        CustomJCheckBox c1 = new CustomJCheckBox(ControllerApiTemplateEnum.SAVE.getTemplateId(), selectedMap.containsKey(ControllerApiTemplateEnum.SAVE), ControllerApiTemplateEnum.SAVE.getTemplateId());
        CustomJCheckBox c2 = new CustomJCheckBox(ControllerApiTemplateEnum.UPDATE.getTemplateId(), selectedMap.containsKey(ControllerApiTemplateEnum.UPDATE), ControllerApiTemplateEnum.UPDATE.getTemplateId());
        CustomJCheckBox c3 = new CustomJCheckBox(ControllerApiTemplateEnum.DELETE.getTemplateId(), selectedMap.containsKey(ControllerApiTemplateEnum.DELETE), ControllerApiTemplateEnum.DELETE.getTemplateId());
        CustomJCheckBox c4 = new CustomJCheckBox(ControllerApiTemplateEnum.DETAIL.getTemplateId(), selectedMap.containsKey(ControllerApiTemplateEnum.DETAIL), ControllerApiTemplateEnum.DETAIL.getTemplateId());
        CustomJCheckBox c5 = new CustomJCheckBox(ControllerApiTemplateEnum.SEARCH.getTemplateId(), selectedMap.containsKey(ControllerApiTemplateEnum.SEARCH), ControllerApiTemplateEnum.SEARCH.getTemplateId());
        CustomJCheckBox c6 = new CustomJCheckBox(ControllerApiTemplateEnum.PAGE_SEARCH.getTemplateId(), selectedMap.containsKey(ControllerApiTemplateEnum.PAGE_SEARCH), ControllerApiTemplateEnum.PAGE_SEARCH.getTemplateId());
        CommonPanel cp1 = new CommonPanel(c1, c2, c3);
        CommonPanel cp2 = new CommonPanel(c4, c5, c6);
        selectCustomJCheckBoxList.addAll(Stream.of(c1, c2, c3, c4, c5, c6).collect(Collectors.toList()));
        box.add(cp1);
        box.add(cp2);
        return box;
    }

    @Override
    protected void okClick() {
        List<ControllerApiParamsInfo> controllerApiParamsInfos = new ArrayList<>();
        for (final CustomJCheckBox customJCheckBox : selectCustomJCheckBoxList) {
            if (customJCheckBox.isSelected()) {
                ControllerApiTemplateEnum xmlSqlTemplateEnum = ControllerApiTemplateEnum.getControllerApiTemplateEnumById(customJCheckBox.getId());
                controllerApiParamsInfos.add(new ControllerApiParamsInfo(xmlSqlTemplateEnum));
            }
        }
        CommonPanel parentComponent = (CommonPanel) super.getParentComponent();
        ControllerParamsInfo bindObject = (ControllerParamsInfo) parentComponent.getBindObject();
        bindObject.setControllerApiParamsInfos(controllerApiParamsInfos);
        //重新绑定值
        parentComponent.setBindObject(bindObject);
        this.close();
    }

    private Map<ControllerApiTemplateEnum, ControllerApiParamsInfo> getSelectedMap() {
        CommonPanel parentComponent = (CommonPanel) super.getParentComponent();
        final Object bindObject = parentComponent.getBindObject();
        Map<ControllerApiTemplateEnum, ControllerApiParamsInfo> selectedMap = new HashMap<>();
        if (bindObject != null) {
            ControllerParamsInfo controllerParamsInfo = (ControllerParamsInfo) bindObject;
            final List<ControllerApiParamsInfo> controllerApiParamsInfos = controllerParamsInfo.getControllerApiParamsInfos();
            if (!CollectionUtils.isEmpty(controllerApiParamsInfos)) {
                selectedMap = controllerApiParamsInfos.stream().collect(Collectors.toMap(ControllerApiParamsInfo::getControllerApiTemplateEnum, Function.identity()));
            }
        }
        return selectedMap;
    }
}
