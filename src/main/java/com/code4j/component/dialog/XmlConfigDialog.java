package com.code4j.component.dialog;

import com.code4j.component.CustomJCheckBox;
import com.code4j.component.panel.CommonPanel;
import com.code4j.config.XmlSqlTemplateEnum;
import com.code4j.pojo.XmlApiParamsInfo;
import com.code4j.pojo.XmlParamsInfo;
import org.apache.commons.collections4.CollectionUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * xml 配置 对话框
 *
 * @author liu_wp
 * @date 2020/12/22
 * @see
 */
public class XmlConfigDialog extends BaseDialog {
    private List<CustomJCheckBox> selectCustomJCheckBoxList;

    public XmlConfigDialog(final Component parentComponent, final String title, final boolean modal, final Object extObj) {
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
        CustomJCheckBox cb1 = new CustomJCheckBox(XmlSqlTemplateEnum.INSERT.getTemplateDesc(), false, XmlSqlTemplateEnum.INSERT.getTemplateId());
        CustomJCheckBox cb2 = new CustomJCheckBox(XmlSqlTemplateEnum.INSERT_BATCH.getTemplateDesc(), false, XmlSqlTemplateEnum.INSERT_BATCH.getTemplateId());
        cp1.addList(cb1, cb2);
        CommonPanel cp2 = new CommonPanel();
        CustomJCheckBox cb3 = new CustomJCheckBox(XmlSqlTemplateEnum.UPDATE.getTemplateDesc(), false, XmlSqlTemplateEnum.UPDATE.getTemplateId());
        CustomJCheckBox cb4 = new CustomJCheckBox(XmlSqlTemplateEnum.DELETE.getTemplateDesc(), false, XmlSqlTemplateEnum.DELETE.getTemplateId());
        cp2.addList(cb3, cb4);
        CommonPanel cp3 = new CommonPanel();
        CustomJCheckBox cb5 = new CustomJCheckBox(XmlSqlTemplateEnum.SELECT.getTemplateDesc(), false, XmlSqlTemplateEnum.SELECT.getTemplateId());
        CustomJCheckBox cb6 = new CustomJCheckBox(XmlSqlTemplateEnum.SELECT_ONE.getTemplateDesc(), false, XmlSqlTemplateEnum.SELECT_ONE.getTemplateId());
        CustomJCheckBox cb7 = new CustomJCheckBox(XmlSqlTemplateEnum.SELECT_PAGE.getTemplateDesc(),
                false, XmlSqlTemplateEnum.SELECT_PAGE.getTemplateId());
        cp3.addList(cb5, cb6, cb7);
        CommonPanel cp4 = new CommonPanel();
        CustomJCheckBox cb8 = new CustomJCheckBox(XmlSqlTemplateEnum.INSERT_DUPLICATEKEY.getTemplateDesc(), false, XmlSqlTemplateEnum.INSERT_DUPLICATEKEY.getTemplateId());
        CustomJCheckBox cb9 = new CustomJCheckBox(XmlSqlTemplateEnum.INSERT_DUPLICATEKEY_BATCH.getTemplateDesc(), false, XmlSqlTemplateEnum.INSERT_DUPLICATEKEY_BATCH.getTemplateId());
        cp4.addList(cb8, cb9);
        box.add(cp4);
        box.add(cp1);
        box.add(cp2);
        box.add(cp3);
        selectCustomJCheckBoxList.addAll(Stream.of(cb1, cb2, cb3, cb4, cb5, cb6, cb7, cb8, cb9).collect(Collectors.toList()));
        return box;
    }

    @Override
    protected void okClick() {
        List<XmlApiParamsInfo> xmlApiParamsInfos = new ArrayList<>();
        for (final CustomJCheckBox customJCheckBox : selectCustomJCheckBoxList) {
            if (customJCheckBox.isSelected()) {
                XmlApiParamsInfo xmlApiParamsInfo = new XmlApiParamsInfo();
                XmlSqlTemplateEnum xmlSqlTemplateEnum = XmlSqlTemplateEnum.getXmlSqlTemplateEnumById(customJCheckBox.getId());
                xmlApiParamsInfo.setTemplateId(customJCheckBox.getId());
                xmlApiParamsInfo.setApiId(xmlSqlTemplateEnum.getApiId());
                xmlApiParamsInfos.add(xmlApiParamsInfo);
            }
        }
        if(CollectionUtils.isNotEmpty(xmlApiParamsInfos)) {
            CommonPanel parentComponent = (CommonPanel) super.getParentComponent();
            XmlParamsInfo bindObject = (XmlParamsInfo) parentComponent.getBindObject();
            bindObject.setXmlApiParamsInfos(xmlApiParamsInfos);
            //重新绑定值
            parentComponent.setBindObject(bindObject);
        }
        this.close();
    }
}
