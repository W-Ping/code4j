package com.code4j.component.dialog;

import com.code4j.component.CustomJCheckBox;
import com.code4j.component.panel.CommonPanel;
import com.code4j.enums.XmlSqlTemplateEnum;
import com.code4j.pojo.XmlApiParamsInfo;
import com.code4j.pojo.XmlParamsInfo;
import org.apache.commons.collections4.CollectionUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.function.Function;
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
        this.setPreferredSize(new Dimension(400, 300));
    }

    @Override
    protected Component content() {
        Map<XmlSqlTemplateEnum, XmlApiParamsInfo> selectedMap = getSelectedMap();
        Box box = Box.createVerticalBox();
        CommonPanel cp1 = new CommonPanel();
        CustomJCheckBox cb1 = new CustomJCheckBox(XmlSqlTemplateEnum.INSERT.getApiId(), selectedMap.containsKey(XmlSqlTemplateEnum.INSERT), XmlSqlTemplateEnum.INSERT.getTemplateId());
//        CustomJCheckBox cb2 = new CustomJCheckBox(XmlSqlTemplateEnum.INSERT_BATCH.getTemplateDesc(), false, XmlSqlTemplateEnum.INSERT_BATCH.getTemplateId());
        CustomJCheckBox cb8 = new CustomJCheckBox(XmlSqlTemplateEnum.INSERT_DUPLICATEKEY.getApiId(), selectedMap.containsKey(XmlSqlTemplateEnum.INSERT_DUPLICATEKEY), XmlSqlTemplateEnum.INSERT_DUPLICATEKEY.getTemplateId());
        cp1.addList(cb1, cb8);
        CommonPanel cp2 = new CommonPanel();
        CustomJCheckBox cb3 = new CustomJCheckBox(XmlSqlTemplateEnum.UPDATE.getApiId(), selectedMap.containsKey(XmlSqlTemplateEnum.UPDATE), XmlSqlTemplateEnum.UPDATE.getTemplateId());
        CustomJCheckBox cb31 = new CustomJCheckBox(XmlSqlTemplateEnum.UPDATE_NOT_NULL.getApiId(), selectedMap.containsKey(XmlSqlTemplateEnum.UPDATE_NOT_NULL), XmlSqlTemplateEnum.UPDATE_NOT_NULL.getTemplateId());
        cp2.addList(cb3, cb31);
        CommonPanel cp3 = new CommonPanel();
        CustomJCheckBox cb5 = new CustomJCheckBox(XmlSqlTemplateEnum.SELECT.getApiId(), selectedMap.containsKey(XmlSqlTemplateEnum.SELECT), XmlSqlTemplateEnum.SELECT.getTemplateId());
        CustomJCheckBox cb6 = new CustomJCheckBox(XmlSqlTemplateEnum.SELECT_ONE.getApiId(), selectedMap.containsKey(XmlSqlTemplateEnum.SELECT_ONE), XmlSqlTemplateEnum.SELECT_ONE.getTemplateId());
//        CustomJCheckBox cb7 = new CustomJCheckBox(XmlSqlTemplateEnum.SELECT_PAGE.getTemplateDesc(),
//                false, XmlSqlTemplateEnum.SELECT_PAGE.getTemplateId());
        cp3.addList(cb5, cb6);
        CommonPanel cp4 = new CommonPanel();
//        CustomJCheckBox cb9 = new CustomJCheckBox(XmlSqlTemplateEnum.INSERT_DUPLICATEKEY_BATCH.getTemplateDesc(), false, XmlSqlTemplateEnum.INSERT_DUPLICATEKEY_BATCH.getTemplateId());
        CustomJCheckBox cb4 = new CustomJCheckBox(XmlSqlTemplateEnum.DELETE.getApiId(), selectedMap.containsKey(XmlSqlTemplateEnum.DELETE), XmlSqlTemplateEnum.DELETE.getTemplateId());
        cp4.addList(cb4);
        box.add(cp1);
        box.add(cp2);
        box.add(cp3);
        box.add(cp4);
        selectCustomJCheckBoxList.addAll(Stream.of(cb1, cb3, cb31, cb4, cb5, cb6, cb8).collect(Collectors.toList()));
        return box;
    }

    @Override
    protected void okClick() {
        List<XmlApiParamsInfo> xmlApiParamsInfos = new ArrayList<>();
        for (final CustomJCheckBox customJCheckBox : selectCustomJCheckBoxList) {
            if (customJCheckBox.isSelected()) {
                XmlSqlTemplateEnum xmlSqlTemplateEnum = XmlSqlTemplateEnum.getXmlSqlTemplateEnumById(customJCheckBox.getId());
                XmlApiParamsInfo xmlApiParamsInfo = new XmlApiParamsInfo(xmlSqlTemplateEnum);

                xmlApiParamsInfos.add(xmlApiParamsInfo);
            }
        }
        CommonPanel parentComponent = (CommonPanel) super.getParentComponent();
        XmlParamsInfo bindObject = (XmlParamsInfo) parentComponent.getBindObject();
        bindObject.setXmlApiParamsInfos(xmlApiParamsInfos);
        //重新绑定值
        parentComponent.setBindObject(bindObject);
        this.close();
    }

    private Map<XmlSqlTemplateEnum, XmlApiParamsInfo> getSelectedMap() {
        CommonPanel parentComponent = (CommonPanel) super.getParentComponent();
        final Object bindObject = parentComponent.getBindObject();
        Map<XmlSqlTemplateEnum, XmlApiParamsInfo> selectedMap = new HashMap<>();
        if (bindObject != null) {
            XmlParamsInfo xmlParamsInfo = (XmlParamsInfo) bindObject;
            final List<XmlApiParamsInfo> xmlApiParamsInfos = xmlParamsInfo.getXmlApiParamsInfos();
            if (!CollectionUtils.isEmpty(xmlApiParamsInfos)) {
                selectedMap = xmlApiParamsInfos.stream().collect(Collectors.toMap(XmlApiParamsInfo::getXmlSqlTemplateEnum, Function.identity()));
            }
        }
        return selectedMap;
    }
}
