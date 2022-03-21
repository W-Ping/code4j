package com.code4j.component.dialog;

import com.code4j.component.CustomJCheckBox;
import com.code4j.component.panel.CommonPanel;
import com.code4j.config.XmlSqlTemplateEnum;
import com.code4j.pojo.MapperApiParamsInfo;
import com.code4j.pojo.MapperParamsInfo;
import org.apache.commons.collections4.CollectionUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Mapper 配置 对话框
 *
 * @author liu_wp
 * @date 2020/12/22
 * @see
 */
public class MapperConfigDialog extends BaseDialog {
    private List<CustomJCheckBox> selectCustomJCheckBoxList;

    public MapperConfigDialog(final Component parentComponent, final String title, final boolean modal, final Object extObj) {
        super(parentComponent, title, modal, extObj);
    }

    @Override
    public void beforeInit() {
        selectCustomJCheckBoxList = new ArrayList<>();
        this.setPreferredSize(new Dimension(400, 300));
    }

    @Override
    protected Component content() {
        Box box = Box.createVerticalBox();
        CommonPanel cp1 = new CommonPanel();
        CustomJCheckBox cb1 = new CustomJCheckBox(XmlSqlTemplateEnum.INSERT.getApiId(), false, XmlSqlTemplateEnum.INSERT.getTemplateId());
//        CustomJCheckBox cb2 = new CustomJCheckBox(XmlSqlTemplateEnum.INSERT_BATCH.getTemplateDesc(), false, XmlSqlTemplateEnum.INSERT_BATCH.getTemplateId());
        CustomJCheckBox cb8 = new CustomJCheckBox(XmlSqlTemplateEnum.INSERT_DUPLICATEKEY.getApiId(), false, XmlSqlTemplateEnum.INSERT_DUPLICATEKEY.getTemplateId());
        cp1.addList(cb1, cb8);
        CommonPanel cp2 = new CommonPanel();
        CustomJCheckBox cb3 = new CustomJCheckBox(XmlSqlTemplateEnum.UPDATE.getApiId(), false, XmlSqlTemplateEnum.UPDATE.getTemplateId());
        CustomJCheckBox cb31 = new CustomJCheckBox(XmlSqlTemplateEnum.UPDATE_NOT_NULL.getApiId(), false, XmlSqlTemplateEnum.UPDATE_NOT_NULL.getTemplateId());
        cp2.addList(cb3, cb31);
        CommonPanel cp3 = new CommonPanel();
        CustomJCheckBox cb5 = new CustomJCheckBox(XmlSqlTemplateEnum.SELECT.getApiId(), false, XmlSqlTemplateEnum.SELECT.getTemplateId());
        CustomJCheckBox cb6 = new CustomJCheckBox(XmlSqlTemplateEnum.SELECT_ONE.getApiId(), false, XmlSqlTemplateEnum.SELECT_ONE.getTemplateId());
//        CustomJCheckBox cb7 = new CustomJCheckBox(XmlSqlTemplateEnum.SELECT_PAGE.getTemplateDesc(),
//                false, XmlSqlTemplateEnum.SELECT_PAGE.getTemplateId());
        cp3.addList(cb5, cb6);
        CommonPanel cp4 = new CommonPanel();
//        CustomJCheckBox cb9 = new CustomJCheckBox(XmlSqlTemplateEnum.INSERT_DUPLICATEKEY_BATCH.getTemplateDesc(), false, XmlSqlTemplateEnum.INSERT_DUPLICATEKEY_BATCH.getTemplateId());
        CustomJCheckBox cb4 = new CustomJCheckBox(XmlSqlTemplateEnum.DELETE.getApiId(), false, XmlSqlTemplateEnum.DELETE.getTemplateId());
        cp4.addList(cb4);
        selectCustomJCheckBoxList.addAll(Stream.of(cb1, cb3, cb31, cb4, cb5, cb6, cb8).collect(Collectors.toList()));
        box.add(cp1);
        box.add(cp2);
        box.add(cp3);
        box.add(cp4);
        return box;
    }

    @Override
    protected void okClick() {
        List<MapperApiParamsInfo> mapperApiParamsInfos = new ArrayList<>();
        for (final CustomJCheckBox customJCheckBox : selectCustomJCheckBoxList) {
            if (customJCheckBox.isSelected()) {
                XmlSqlTemplateEnum xmlSqlTemplateEnum = XmlSqlTemplateEnum.getXmlSqlTemplateEnumById(customJCheckBox.getId());
                mapperApiParamsInfos.add(new MapperApiParamsInfo(xmlSqlTemplateEnum));
            }
        }
        if (CollectionUtils.isNotEmpty(mapperApiParamsInfos)) {
            CommonPanel parentComponent = (CommonPanel) super.getParentComponent();
            MapperParamsInfo bindObject = (MapperParamsInfo) parentComponent.getBindObject();
            bindObject.setMapperApiParamsInfos(mapperApiParamsInfos);
            //重新绑定值
            parentComponent.setBindObject(bindObject);
        }
        this.close();
    }


}
