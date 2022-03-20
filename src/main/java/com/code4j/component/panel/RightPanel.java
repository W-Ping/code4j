package com.code4j.component.panel;

import com.code4j.action.GenerateCodeAction;
import com.code4j.component.CustomJCheckBox;
import com.code4j.component.dialog.MapperConfigDialog;
import com.code4j.component.label.TemplateClickLabel;
import com.code4j.component.select.ProjectConfigSelect;
import com.code4j.config.Code4jConstants;
import com.code4j.config.TemplateTypeEnum;
import com.code4j.connect.JdbcServiceFactory;
import com.code4j.pojo.*;
import com.code4j.util.StrUtil;
import org.apache.commons.lang3.StringUtils;

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
    private ProjectCodeConfigInfo projectCodeConfigInfo;

    private JTextField doPackName;
    private JTextField doPackPath;

    private JTextField voPackName;
    private JTextField voPackPath;

    private JTextField xmlPackName;
    private JTextField xmlPackPath;

    private JTextField mapperPackName;
    private JTextField mapperPackPath;

    private JTextField serviceApiPackName;
    private JTextField serviceApiPackPath;

    public RightPanel(final Dimension dimension) {
        super(new FlowLayout(FlowLayout.LEFT), dimension);
    }


    @Override
    protected void init() {
        this.setLayout(new BorderLayout());
    }

    public void clearEmpty(String promptMsg) {
        this.removeAll();
        if (StringUtils.isNotBlank(promptMsg)) {
            JLabel jLabel = new JLabel(promptMsg, JLabel.CENTER);
            jLabel.setFont(jLabel.getFont().deriveFont(30.0f));
            jLabel.setForeground(Color.gray);
            this.add(jLabel, BorderLayout.CENTER);
        }
        this.updateUI();
    }

    /**
     * @param jdbcTableInfo
     * @param jdbcSourceInfo
     */
    public void showGenerateView(JdbcTableInfo jdbcTableInfo, JdbcSourceInfo jdbcSourceInfo) {
        this.clearEmpty(null);
        List<JdbcMapJavaInfo> tableColumnInfos = JdbcServiceFactory.getJdbcService(jdbcSourceInfo).getTableColumnInfo(jdbcTableInfo.getDbName(), jdbcTableInfo.getTableName());
        String tableName = jdbcTableInfo.getTableName();
        Dimension inputDimension = new Dimension(200, 30);
        Dimension packageDimension = new Dimension(200, 30);
        Dimension pathDimension = new Dimension(120, 30);
        Border lineBorder = BorderFactory.createLineBorder(Color.WHITE, 2);
        Dimension preferredSize = this.getPreferredSize();
        int avgH = (int) Math.floor(preferredSize.getHeight() / 8);
        Dimension dimensionP = new Dimension((int) preferredSize.getWidth() - 10, avgH);
        CommonPanel projectP = new CommonPanel(new FlowLayout(FlowLayout.LEFT), new Dimension((int) preferredSize.getWidth() - 10, avgH + 20));
        projectP.setBorder(BorderFactory.createTitledBorder(lineBorder, "项目配置（" + jdbcTableInfo.getDbName() + "." + tableName + "）"));
        JLabel p1 = new JLabel("项目地址：");
        CustomJFileChooserPanel p1V = new CustomJFileChooserPanel(this, false, JFileChooser.DIRECTORIES_ONLY);
//        ConfigLabel jLabel = new ConfigLabel(this, "选择项目配置");
        //do 配置
        CommonPanel daoP = new CommonPanel(new FlowLayout(FlowLayout.LEFT));
        daoP.setBindObject(tableColumnInfos);
        daoP.setBorder(BorderFactory.createTitledBorder(lineBorder, TemplateTypeEnum.DO.getTemplateDesc()));
        CommonPanel dp1 = new CommonPanel();
        JLabel d1 = new JLabel("名称：");
        JTextField d1v = new JTextField(this.getPojoName(tableName, Code4jConstants.DO_SUFFIX));
        d1v.setPreferredSize(inputDimension);
        dp1.addList(d1, d1v);
        CommonPanel dp2 = new CommonPanel();
        JLabel d2 = new JLabel("包名：");
        doPackName = new JTextField(BaseTemplateInfo.getDefaultPackageName(TemplateTypeEnum.DO, this.getPackageName(tableName)));
        doPackName.setPreferredSize(packageDimension);
        dp2.addList(d2, doPackName);
        CommonPanel dp3 = new CommonPanel();
        JLabel d3 = new JLabel("路径：");
        doPackPath = new JTextField(Code4jConstants.DEFAULT_PATH);
        doPackPath.setPreferredSize(pathDimension);
        dp3.addList(d3, doPackPath);
        JTextField spText = new JTextField();
        spText.setPreferredSize(inputDimension);
        CommonPanel sp = new CommonPanel(new JLabel("父类："), spText);
        CommonPanel dp4 = new CommonPanel(new TemplateClickLabel("字段配置", jdbcTableInfo.getTableName() + " DO字段配置", TemplateTypeEnum.DO, daoP));
        daoP.addList(dp1, dp2, dp3, sp, dp4);
        //vo 配置
        CommonPanel voP = new CommonPanel(new FlowLayout(FlowLayout.LEFT), dimensionP);
        voP.setBindObject(tableColumnInfos);
        voP.setBorder(BorderFactory.createTitledBorder(lineBorder, TemplateTypeEnum.VO.getTemplateDesc()));
        CommonPanel vp1 = new CommonPanel();
        JLabel v1 = new JLabel("名称：");
        JTextField v1v = new JTextField(this.getPojoName(tableName, Code4jConstants.VO_SUFFIX));
        v1v.setPreferredSize(inputDimension);
        vp1.addList(v1, v1v);
        CommonPanel vp2 = new CommonPanel();
        JLabel v2 = new JLabel("包名：");
        voPackName = new JTextField(BaseTemplateInfo.getDefaultPackageName(TemplateTypeEnum.VO, this.getPackageName(tableName)));
        voPackName.setPreferredSize(packageDimension);
        vp2.addList(v2, voPackName);
        CommonPanel vp3 = new CommonPanel();
        JLabel v3 = new JLabel("路径：");
        voPackPath = new JTextField(Code4jConstants.DEFAULT_PATH);
        voPackPath.setPreferredSize(pathDimension);
        vp3.addList(v3, voPackPath);
        JTextField voSpText = new JTextField();
        voSpText.setPreferredSize(inputDimension);
        CommonPanel voSp = new CommonPanel(new JLabel("父类："), voSpText);
        CommonPanel vp4 = new CommonPanel(new TemplateClickLabel("字段配置", jdbcTableInfo.getTableName() + " VO字段配置", TemplateTypeEnum.DO, voP));
        voP.addList(vp1, vp2, vp3, voSp, vp4);
        //service 配置
        CommonPanel serviceP = new CommonPanel(new FlowLayout(FlowLayout.LEFT), dimensionP);
        serviceP.setBorder(BorderFactory.createTitledBorder(lineBorder, TemplateTypeEnum.SERVICE_API.getTemplateDesc()));
        CommonPanel serviceP1 = new CommonPanel();
        JLabel service1 = new JLabel("名称：");
        JTextField service1v = new JTextField("I" + this.getPojoName(tableName, Code4jConstants.SERVICE_SUFFIX));
        service1v.setPreferredSize(inputDimension);
        serviceP1.addList(service1, service1v);
        CommonPanel serviceP2 = new CommonPanel();
        JLabel service2 = new JLabel("包名：");
        serviceApiPackName = new JTextField(BaseTemplateInfo.getDefaultPackageName(TemplateTypeEnum.SERVICE_API, this.getPackageName(tableName)));
        serviceApiPackName.setPreferredSize(packageDimension);
        serviceP2.addList(service2, serviceApiPackName);
        CommonPanel serviceP3 = new CommonPanel();
        JLabel service3 = new JLabel("路径：");
        serviceApiPackPath = new JTextField(Code4jConstants.DEFAULT_PATH);
        serviceApiPackPath.setPreferredSize(pathDimension);
        serviceP3.addList(service3, serviceApiPackPath);
        JTextField serviceSpText = new JTextField();
        serviceSpText.setPreferredSize(inputDimension);
        CommonPanel serviceSp = new CommonPanel(new JLabel("父类："), serviceSpText);
//        CommonPanel serviceP4 = new CommonPanel(new TemplateClickLabel("配置", jdbcTableInfo.getTableName() + " service配置", TemplateTypeEnum.SERVICE_API, serviceP));
//        TemplateClickLabel serviceP41 = ;
//        serviceP4.addList(serviceP41);
        serviceP.addList(serviceP1, serviceP2, serviceP3, serviceSp);
        // mapper 配置
        CommonPanel mapperP = new CommonPanel(new FlowLayout(FlowLayout.LEFT), dimensionP);
        MapperParamsInfo mapperParamsInfo = new MapperParamsInfo();
        mapperP.setBindObject(mapperParamsInfo);
        mapperP.setBorder(BorderFactory.createTitledBorder(lineBorder, TemplateTypeEnum.MAPPER.getTemplateDesc()));
        CommonPanel mapperP1 = new CommonPanel();
        JLabel mapper1 = new JLabel("名称：");
        JTextField mapper1V = new JTextField(this.getPojoName(tableName, Code4jConstants.MAPPER_SUFFIX));
        mapper1V.setPreferredSize(inputDimension);
        mapperP1.addList(mapper1, mapper1V);
        CommonPanel mapperP2 = new CommonPanel();
        JLabel mapper2 = new JLabel("包名：");
        mapperPackName = new JTextField(BaseTemplateInfo.getDefaultPackageName(TemplateTypeEnum.MAPPER, this.getPackageName(tableName)));
        mapperPackName.setPreferredSize(packageDimension);
        mapperP2.addList(mapper2, mapperPackName);
        CommonPanel mapperP3 = new CommonPanel();
        JLabel mapper3 = new JLabel("路径：");
        mapperPackPath = new JTextField(Code4jConstants.DEFAULT_PATH);
        mapperPackPath.setPreferredSize(pathDimension);
        mapperP3.addList(mapper3, mapperPackPath);
        JTextField mapperSpText = new JTextField(Code4jConstants.MAPPER_SUPER_CLASS);
        mapperSpText.setPreferredSize(inputDimension);
        CommonPanel mapperSp = new CommonPanel(new JLabel("父类："), mapperSpText);
        TemplateClickLabel<MapperConfigDialog> mapperP4 = new TemplateClickLabel("配 置", jdbcTableInfo.getTableName() + " Mapper配置", TemplateTypeEnum.MAPPER, mapperP);
        mapperP.addList(mapperP1, mapperP2, mapperP3,mapperSp, mapperP4);
        //xml 配置
        CommonPanel xmlP = new CommonPanel(new FlowLayout(FlowLayout.LEFT), dimensionP);
        XmlParamsInfo xmlParamsInfo = new XmlParamsInfo();
        xmlParamsInfo.setTableColumnInfos(tableColumnInfos);
        xmlP.setBindObject(xmlParamsInfo);
        xmlP.setBorder(BorderFactory.createTitledBorder(lineBorder, TemplateTypeEnum.XML.getTemplateDesc()));
        CommonPanel xml1P = new CommonPanel();
        JLabel xml1 = new JLabel("名称：");
        JTextField xml1v = new JTextField(this.getPojoName(tableName, Code4jConstants.MAPPER_SUFFIX));
        xml1v.setPreferredSize(inputDimension);
        xml1P.addList(xml1, xml1v);
        CommonPanel xml2P = new CommonPanel();
        JLabel xml2 = new JLabel("包名：");
        xmlPackName = new JTextField(BaseTemplateInfo.getDefaultPackageName(TemplateTypeEnum.XML, this.getPackageName(tableName)));
        xmlPackName.setPreferredSize(packageDimension);
        xml2P.addList(xml2, xmlPackName);
        CommonPanel xml3P = new CommonPanel();
        JLabel xml3 = new JLabel("路径：");
        xmlPackPath = new JTextField(Code4jConstants.DEFAULT_XML_PATH);
        xmlPackPath.setPreferredSize(pathDimension);
        xml3P.addList(xml3, xmlPackPath);
        TemplateClickLabel xml4v = new TemplateClickLabel("XML配置", jdbcTableInfo.getTableName() + " XML配置", TemplateTypeEnum.XML, xmlP);
        xmlP.addList(xml1P, xml2P, xml3P, xml4v);

        CommonPanel optionP = new CommonPanel(new FlowLayout(FlowLayout.LEFT), dimensionP);
        CustomJCheckBox cb1 = new CustomJCheckBox(TemplateTypeEnum.DO.getTemplateDesc(), true, TemplateTypeEnum.DO.getTemplateId());
        cb1.setBindObject(daoP);
        CustomJCheckBox cb2 = new CustomJCheckBox(TemplateTypeEnum.VO.getTemplateDesc(), true, TemplateTypeEnum.VO.getTemplateId());
        cb2.setBindObject(voP);
        CustomJCheckBox cb4 = new CustomJCheckBox(TemplateTypeEnum.XML.getTemplateDesc(), true, TemplateTypeEnum.XML.getTemplateId());
        cb4.setBindObject(xmlP);
        CustomJCheckBox cb5 = new CustomJCheckBox(TemplateTypeEnum.MAPPER.getTemplateDesc(), true, TemplateTypeEnum.MAPPER.getTemplateId());
        cb5.setBindObject(mapperP);

        CustomJCheckBox cb6 = new CustomJCheckBox(TemplateTypeEnum.SERVICE_API.getTemplateDesc(), false, TemplateTypeEnum.SERVICE_API.getTemplateId());
        cb6.setBindObject(serviceP);
        optionP.addList(cb1, cb2, cb4, cb5, cb6);
        //添加绑定要生成代码的容器 有序添加 0：do配置；1：vo配置；2：mapper配置；3：service api配置
        customJCheckBoxList.addAll(Stream.of(cb1, cb2, cb5, cb4, cb6).collect(Collectors.toList()));
        CommonPanel btnP = new CommonPanel(new FlowLayout(FlowLayout.RIGHT), new Dimension((int) preferredSize.getWidth() - 10, avgH - 20));
        JButton generate = new JButton(" 生成代码 ");
        generate.addActionListener(new GenerateCodeAction(p1V, jdbcTableInfo, customJCheckBoxList, jdbcSourceInfo));
        btnP.add(generate);
        ProjectConfigSelect projectConfigSelect = new ProjectConfigSelect(this, (c, item) -> {
            ((RightPanel) c).loadProjectConfig(item);
        });
        projectP.addList(p1, p1V, projectConfigSelect);
        Box box = Box.createVerticalBox();
        box.add(projectP);
        box.add(daoP);
        box.add(voP);
        box.add(serviceP);
        box.add(mapperP);
        box.add(xmlP);
        box.add(optionP);
        box.add(btnP);
        this.add(box);
        this.updateUI();
    }

    /**
     * @param info
     */
    public void loadProjectConfig(ProjectCodeConfigInfo info) {
        if (info != null) {
            this.setProjectCodeConfigInfo(info);
            doPackName.setText(info.getDoPackageName());
            doPackPath.setText(info.getDoPath());
            voPackName.setText(info.getVoPackageName());
            voPackPath.setText(info.getVoPath());
            xmlPackName.setText(info.getXmlPackageName());
            xmlPackPath.setText(info.getXmlPath());
            mapperPackName.setText(info.getMapperPackageName());
            mapperPackPath.setText(info.getMapperPath());
            serviceApiPackName.setText(info.getServiceApiPackageName());
            serviceApiPackPath.setText(info.getServiceApiPath());
        }
    }

    private String getPojoName(String str, String prefix) {
        if (str.startsWith("t_")) {
            str = StrUtil.subFirstStr(str, "t_");
        }
        return StrUtil.underlineToCamelFirstToUpper(str) + prefix;
    }

    private String getPackageName(String str) {
        if (str.startsWith("t_")) {
            str = StrUtil.subFirstStr(str, "t_");
        }
        return StrUtil.underlineToCamelToLower(str);
    }

    public ProjectCodeConfigInfo getProjectCodeConfigInfo() {
        return projectCodeConfigInfo;
    }

    public void setProjectCodeConfigInfo(ProjectCodeConfigInfo projectCodeConfigInfo) {
        this.projectCodeConfigInfo = projectCodeConfigInfo;
    }
}
