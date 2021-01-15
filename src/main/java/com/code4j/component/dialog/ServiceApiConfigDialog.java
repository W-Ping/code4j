package com.code4j.component.dialog;

import com.code4j.component.CustomJCheckBox;
import com.code4j.component.panel.CommonPanel;
import com.code4j.config.ServiceApiTemplateEnum;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * service api 配置 对话框
 *
 * @author liu_wp
 * @date 2020/11/23
 * @see
 */
public class ServiceApiConfigDialog extends BaseDialog {
    private List<CustomJCheckBox> selectCustomJCheckBoxList;

    /**
     * @param parentComponent
     * @param title
     * @param modal
     * @param extObj
     */
    public ServiceApiConfigDialog(final Component parentComponent, final String title, final boolean modal, final Object extObj) {
        super(parentComponent, title, modal, extObj);
    }

    @Override
    public void beforeInit() {
        selectCustomJCheckBoxList = new ArrayList<>();
    }

    @Override
    protected Component content() {
        Box box = Box.createVerticalBox();
        CommonPanel cp1 = new CommonPanel();
        CustomJCheckBox cb1 = new CustomJCheckBox(ServiceApiTemplateEnum.INSERT.getTemplateDesc(), true, ServiceApiTemplateEnum.INSERT.getTemplateId());
        CustomJCheckBox cb2 = new CustomJCheckBox(ServiceApiTemplateEnum.INSERT_BATCH.getTemplateDesc(), true, ServiceApiTemplateEnum.INSERT_BATCH.getTemplateId());
        cp1.addList(cb1, cb2);
        CommonPanel cp2 = new CommonPanel();
        CustomJCheckBox cb3 = new CustomJCheckBox(ServiceApiTemplateEnum.UPDATE.getTemplateDesc(), true, ServiceApiTemplateEnum.UPDATE.getTemplateId());
        CustomJCheckBox cb4 = new CustomJCheckBox(ServiceApiTemplateEnum.DELETE.getTemplateDesc(), true, ServiceApiTemplateEnum.DELETE.getTemplateId());
        cp2.addList(cb3, cb4);
        CommonPanel cp3 = new CommonPanel();
        CustomJCheckBox cb5 = new CustomJCheckBox(ServiceApiTemplateEnum.SELECT.getTemplateDesc(), true, ServiceApiTemplateEnum.SELECT.getTemplateId());
        CustomJCheckBox cb6 = new CustomJCheckBox(ServiceApiTemplateEnum.SELECT_ONE.getTemplateDesc(), true, ServiceApiTemplateEnum.SELECT_ONE.getTemplateId());
        CustomJCheckBox cb7 = new CustomJCheckBox(ServiceApiTemplateEnum.SELECT_PAGE.getTemplateDesc(),
                true, ServiceApiTemplateEnum.SELECT_PAGE.getTemplateId());
        cp3.addList(cb5, cb6, cb7);
        box.add(cp1);
        box.add(cp2);
        box.add(cp3);
        selectCustomJCheckBoxList.addAll(Stream.of(cb1, cb2, cb3, cb4, cb5, cb6, cb7).collect(Collectors.toList()));
        return box;
    }

    @Override
    protected void okClick() {

    }
}
