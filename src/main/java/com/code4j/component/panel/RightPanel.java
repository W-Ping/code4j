package com.code4j.component.panel;

import com.code4j.action.GenerateCodeAction;
import com.code4j.component.CustomJCheckBox;
import com.code4j.config.Code4jConstants;
import com.code4j.config.TemplateTypeEnum;
import com.code4j.pojo.JdbcSourceInfo;
import com.code4j.pojo.JdbcTableInfo;
import com.code4j.util.StrUtil;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author liu_wp
 * @date 2020/11/17
 * @see
 */
public class RightPanel extends BasePanel {

    private List<CustomJCheckBox> customJCheckBoxList = new ArrayList<>();

    public RightPanel(final Dimension dimension) {
        super(new FlowLayout(FlowLayout.LEFT), dimension);
    }


    @Override
    protected void init() {
    }


    public void showGenerateView(JdbcTableInfo jdbcTableInfo, JdbcSourceInfo jdbcSourceInfo) {
        this.removeAll();
        String tableName = jdbcTableInfo.getTableName();
        Dimension inputDimension = new Dimension(150, 30);
        Border lineBorder = BorderFactory.createLineBorder(Color.WHITE, 2);
        Dimension preferredSize = this.getPreferredSize();
        int avgH = (int) Math.floor(preferredSize.getHeight() / 8);
        Dimension dimensionP = new Dimension((int) preferredSize.getWidth() - 10, avgH);
        CommonPanel projectP = new CommonPanel(new FlowLayout(FlowLayout.LEFT), new Dimension((int) preferredSize.getWidth() - 10, avgH + 20));
        projectP.setBorder(BorderFactory.createTitledBorder(lineBorder, "项目地址"));
        JLabel p1 = new JLabel("项目地址：");
        CustomJFileChooserPanel p1V = new CustomJFileChooserPanel(this, false, JFileChooser.DIRECTORIES_ONLY);
        JLabel p2 = new JLabel("共用包名前缀：");
        JTextField p2V = new JTextField();
        p2V.setPreferredSize(inputDimension);
        p1V.setExtObject(p2V);
        projectP.addList(p1, p1V, p2, p2V);

        CommonPanel daoP = new CommonPanel(new FlowLayout(FlowLayout.LEFT), dimensionP);
        daoP.setBorder(BorderFactory.createTitledBorder(lineBorder, TemplateTypeEnum.DO.getTemplateDesc()));
        CommonPanel dp1 = new CommonPanel();
        JLabel d1 = new JLabel("名称：");
        JTextField d1v = new JTextField(getPojoName(tableName, Code4jConstants.DO_SUFFIX));
        d1v.setPreferredSize(inputDimension);
        dp1.addList(d1, d1v);
        CommonPanel dp2 = new CommonPanel();
        JLabel d2 = new JLabel("包名：");
        JTextField d2v = new JTextField(StrUtil.underlineToCamelToLower(tableName));
        d2v.setPreferredSize(inputDimension);
        dp2.addList(d2, d2v);
        CommonPanel dp3 = new CommonPanel();
        JLabel d3 = new JLabel("路径：");
        JTextField d3v = new JTextField(Code4jConstants.DEFAULT_PATH);
        d3v.setPreferredSize(inputDimension);
        dp3.addList(d3, d3v);
        daoP.addList(dp1, dp2, dp3);

        CommonPanel voP = new CommonPanel(new FlowLayout(FlowLayout.LEFT), dimensionP);
        voP.setBorder(BorderFactory.createTitledBorder(lineBorder, TemplateTypeEnum.VO.getTemplateDesc()));
        CommonPanel vp1 = new CommonPanel();
        JLabel v1 = new JLabel("名称：");
        JTextField v1v = new JTextField(getPojoName(tableName, Code4jConstants.VO_SUFFIX));
        v1v.setPreferredSize(inputDimension);
        vp1.addList(v1, v1v);
        CommonPanel vp2 = new CommonPanel();
        JLabel v2 = new JLabel("包名：");
        JTextField v2v = new JTextField(StrUtil.underlineToCamelToLower(tableName));
        v2v.setPreferredSize(inputDimension);
        vp2.addList(v2, v2v);
        CommonPanel vp3 = new CommonPanel();
        JLabel v3 = new JLabel("路径：");
        JTextField v3v = new JTextField(Code4jConstants.DEFAULT_PATH);
        v3v.setPreferredSize(inputDimension);
        vp3.addList(v3, v3v);
        voP.addList(vp1, vp2, vp3);

        CommonPanel diyP = new CommonPanel(new FlowLayout(FlowLayout.LEFT), dimensionP);
        diyP.setBorder(BorderFactory.createTitledBorder(lineBorder, TemplateTypeEnum.DIY.getTemplateDesc()));
        CommonPanel diyp1 = new CommonPanel();
        JLabel diy1 = new JLabel("名称：");
        JTextField diy1v = new JTextField();
        diy1v.setPreferredSize(inputDimension);
        diyp1.addList(diy1, diy1v);
        CommonPanel diyp2 = new CommonPanel();
        JLabel diy2 = new JLabel("包名：");
        JTextField diy2v = new JTextField(StrUtil.underlineToCamelToLower(tableName));
        diy2v.setPreferredSize(inputDimension);
        diyp2.addList(diy2, diy2v);
        CommonPanel diyp3 = new CommonPanel();
        JLabel diy3 = new JLabel("路径：");
        JTextField diy3v = new JTextField(Code4jConstants.DEFAULT_PATH);
        diy3v.setPreferredSize(inputDimension);
        diyp3.addList(diy3, diy3v);
        diyP.addList(diyp1, diyp2, diyp3);

        CommonPanel mapperP = new CommonPanel(new FlowLayout(FlowLayout.LEFT), dimensionP);
        mapperP.setBorder(BorderFactory.createTitledBorder(lineBorder, TemplateTypeEnum.MAPPER.getTemplateDesc()));
        CommonPanel mapperP1 = new CommonPanel();
        JLabel mapper1 = new JLabel("名称：");
        JTextField mapper1V = new JTextField(getPojoName(tableName, Code4jConstants.MAPPER_SUFFIX));
        mapper1V.setPreferredSize(inputDimension);
        mapperP1.addList(mapper1, mapper1V);
        CommonPanel mapperP2 = new CommonPanel();
        JLabel mapper2 = new JLabel("包名：");
        JTextField mapper2V = new JTextField(Code4jConstants.DEFAULT_MAPPER_PACKAGE + "." + StrUtil.underlineToCamelToLower(tableName));
        mapper2V.setPreferredSize(inputDimension);
        mapperP2.addList(mapper2, mapper2V);
        CommonPanel mapperP3 = new CommonPanel();
        JLabel mapper3 = new JLabel("路径：");
        JTextField mapper3V = new JTextField(Code4jConstants.DEFAULT_PATH);
        mapper3V.setPreferredSize(inputDimension);
        mapperP3.addList(mapper3, mapper3V);
        mapperP.addList(mapperP1, mapperP2, mapperP3);


        CommonPanel xmlP = new CommonPanel(new FlowLayout(FlowLayout.LEFT), dimensionP);
        xmlP.setBorder(BorderFactory.createTitledBorder(lineBorder, TemplateTypeEnum.XML.getTemplateDesc()));
        CommonPanel xml1P = new CommonPanel();
        JLabel xml1 = new JLabel("名称：");
        JTextField xml1v = new JTextField(getPojoName(tableName, Code4jConstants.MAPPER_SUFFIX));
        xml1v.setPreferredSize(inputDimension);
        xml1P.addList(xml1, xml1v);
        CommonPanel xml2P = new CommonPanel();
        JLabel xml2 = new JLabel("包名：");
        JTextField xml2v = new JTextField(Code4jConstants.DEFAULT_MAPPER_PACKAGE + "." + StrUtil.underlineToCamelToLower(tableName));
        xml2v.setPreferredSize(inputDimension);
        xml2P.addList(xml2, xml2v);

        CommonPanel xml3P = new CommonPanel();
        JLabel xml3 = new JLabel("路径：");
        JTextField xml3v = new JTextField(Code4jConstants.DEFAULT_XML_PATH);
        xml3v.setPreferredSize(inputDimension);
        xml3P.addList(xml3, xml3v);
        xmlP.addList(xml1P, xml2P, xml3P);
        CommonPanel optionP = new CommonPanel(new FlowLayout(FlowLayout.LEFT), dimensionP);
        CustomJCheckBox cb1 = new CustomJCheckBox("DO 配置", true, TemplateTypeEnum.DO.getTemplateId());
        cb1.setBindCommonPanel(daoP);
        CustomJCheckBox cb2 = new CustomJCheckBox(TemplateTypeEnum.VO.getTemplateDesc(), true, TemplateTypeEnum.VO.getTemplateId());
        cb2.setBindCommonPanel(voP);
        CustomJCheckBox cb3 = new CustomJCheckBox(TemplateTypeEnum.DIY.getTemplateDesc(), false, TemplateTypeEnum.DIY.getTemplateId());
        cb3.setBindCommonPanel(diyP);
        CustomJCheckBox cb4 = new CustomJCheckBox(TemplateTypeEnum.XML.getTemplateDesc(), true, TemplateTypeEnum.XML.getTemplateId());
        cb4.setBindCommonPanel(mapperP);
        CustomJCheckBox cb5 = new CustomJCheckBox(TemplateTypeEnum.MAPPER.getTemplateDesc(), true, TemplateTypeEnum.MAPPER.getTemplateId());
        cb5.setBindCommonPanel(xmlP);
        optionP.addList(cb1, cb2, cb3, cb4, cb5);
        customJCheckBoxList.addAll(Stream.of(cb1, cb2, cb3, cb4, cb5).collect(Collectors.toList()));
        CommonPanel btnP = new CommonPanel(new FlowLayout(FlowLayout.RIGHT), new Dimension((int) preferredSize.getWidth() - 10, avgH - 20));
        JButton generate = new JButton(" 确定 ");
        generate.addActionListener(new GenerateCodeAction(p1V, jdbcTableInfo, customJCheckBoxList, jdbcSourceInfo));
        btnP.add(generate);
        Box box = Box.createVerticalBox();
        box.add(projectP);
        box.add(daoP);
        box.add(voP);
        box.add(diyP);
        box.add(mapperP);
        box.add(xmlP);
        box.add(optionP);
        box.add(btnP);
        this.add(box);
        this.updateUI();
    }

    private String getPojoName(String str, String prefix) {
        return StrUtil.underlineToCamelFirstToUpper(str) + prefix;
    }

}
