package com.code4j.component.panel;

import com.code4j.action.GenerateCodeAction;
import com.code4j.component.CustomJCheckBox;
import com.code4j.component.CustomJTextField;
import com.code4j.component.button.CustomJButton;
import com.code4j.component.label.TemplateClickLabel;
import com.code4j.component.select.ProjectConfigSelect;
import com.code4j.config.Code4jConstants;
import com.code4j.enums.TemplateTypeEnum;
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
    /**
     * 垂直布局容器总数量
     */
    private final static int V_CONTAINERS_COUNT = 10;
    private final static Dimension INPUT_DIMENSION = new Dimension(200, 30);
    private final static Dimension PACKAGE_DIMENSION = new Dimension(200, 30);
    private final static Dimension PATH_DIMENSION = new Dimension(120, 30);
    private final List<CustomJCheckBox> customJCheckBoxList = new ArrayList<>();
    private ProjectCodeConfigInfo projectCodeConfigInfo;

    private CustomJTextField doPackName;
    private CustomJTextField doPackPath;

    private CustomJTextField voPackName;
    private CustomJTextField voPackPath;

    private CustomJTextField xmlPackName;
    private CustomJTextField xmlPackPath;

    private CustomJTextField contrPackName;
    private CustomJTextField contrPackPath;
    private CustomJTextField mapperPackName;
    private CustomJTextField mapperPackPath;

    private CustomJTextField serviceApiPackName;
    private CustomJTextField serviceApiPackPath;

    private CustomJTextField serviceImplPackName;
    private CustomJTextField serviceImplPackPath;
    private CustomJTextField voSpText;
    private CustomJTextField serviceSpText;
    private CustomJTextField serviceImplSpText;
    private CustomJTextField mapperSpText;

    private CustomJTextField contrSpText;
    private CustomJTextField doSpText;

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

        //获取选择的表信息
        List<JdbcMapJavaInfo> tableColumnInfos = JdbcServiceFactory.getJdbcService(jdbcSourceInfo).getTableColumnInfo(jdbcTableInfo.getDbName(), jdbcTableInfo.getTableName());
        //表名
        String tableName = jdbcTableInfo.getTableName();
        Border lineBorder = BorderFactory.createLineBorder(Color.WHITE, 2);
        //获取容器尺寸
        Dimension preferredSize = this.getPreferredSize();
        //组件平均高度 总高度/容器数量
        int avgH = (int) Math.floor(preferredSize.getHeight() / V_CONTAINERS_COUNT);
        //组件平均尺寸 width=容器宽度-10 height=avgH
        Dimension avgW = new Dimension((int) preferredSize.getWidth() - 10, avgH);

        //----------------------------------------------项目配置---------------------------------------------
        //项目配置容器尺寸  width=容器宽度-10 height=avgH+20
        CommonPanel projectP = new CommonPanel(new FlowLayout(FlowLayout.LEFT), new Dimension((int) preferredSize.getWidth() - 10, avgH + 20));
        projectP.setBorder(BorderFactory.createTitledBorder(lineBorder, "项目配置（" + jdbcTableInfo.getDbName() + "." + tableName + "）"));
        JLabel p1 = new JLabel("项目地址：");
        CustomJFileChooserPanel p1V = new CustomJFileChooserPanel(this, false, JFileChooser.DIRECTORIES_ONLY);
        CustomJCheckBox lombokCb = new CustomJCheckBox("lombok", true, "lombok");
        CustomJCheckBox swaggerCb = new CustomJCheckBox("swagger", true, "swagger");
        CustomJCheckBox mybatisplusCb = new CustomJCheckBox("mybatis-plus", true, "mybatisplus");

        //项目配置
        ProjectConfigSelect projectConfigSelect = new ProjectConfigSelect(this, tableName, (c, item) -> {
            ((RightPanel) c).loadProjectConfig(item);
        });
        projectP.addList(p1, p1V, projectConfigSelect, lombokCb, swaggerCb, mybatisplusCb);
        //----------------------------------------------DO 配置---------------------------------------------
        CommonPanel daoP = new CommonPanel(new FlowLayout(FlowLayout.LEFT), null, tableColumnInfos);
        daoP.setBorder(BorderFactory.createTitledBorder(lineBorder, TemplateTypeEnum.DO.getTemplateDesc()));
        CommonPanel dp1 = new CommonPanel(new JLabel("名称："), new CustomJTextField(this.getPojoName(tableName, Code4jConstants.DO_SUFFIX), INPUT_DIMENSION));
        CommonPanel dp2 = new CommonPanel(new JLabel("包名："), doPackName = new CustomJTextField(null, PACKAGE_DIMENSION));
        CommonPanel dp3 = new CommonPanel(new JLabel("路径："), doPackPath = new CustomJTextField(null, PATH_DIMENSION));
        CommonPanel sp = new CommonPanel(new JLabel("父类："), doSpText = new CustomJTextField(Code4jConstants.DO_SUPER_CLASS, INPUT_DIMENSION));
        CommonPanel dp4 = new CommonPanel(new TemplateClickLabel("字段配置", jdbcTableInfo.getTableName() + " " + TemplateTypeEnum.DO.getTemplateDesc(), TemplateTypeEnum.DO, daoP, true));
        daoP.addList(dp1, dp2, dp3, sp, dp4);

        //----------------------------------------------vo 配置---------------------------------------------
        CommonPanel voP = new CommonPanel(new FlowLayout(FlowLayout.LEFT), avgW, tableColumnInfos);
        voP.setBorder(BorderFactory.createTitledBorder(lineBorder, TemplateTypeEnum.VO.getTemplateDesc()));
        CommonPanel vp1 = new CommonPanel(new JLabel("名称："), new CustomJTextField(this.getPojoName(tableName, Code4jConstants.VO_SUFFIX), INPUT_DIMENSION));
        CommonPanel vp2 = new CommonPanel(new JLabel("包名："), voPackName = new CustomJTextField(null, PACKAGE_DIMENSION));
        CommonPanel vp3 = new CommonPanel(new JLabel("路径："), voPackPath = new CustomJTextField(null, PATH_DIMENSION));
        CommonPanel voSp = new CommonPanel(new JLabel("父类："), voSpText = new CustomJTextField(null, INPUT_DIMENSION));
        CommonPanel vp4 = new CommonPanel(new TemplateClickLabel("字段配置", jdbcTableInfo.getTableName() + " " + TemplateTypeEnum.VO.getTemplateDesc(), TemplateTypeEnum.DO, voP, true));
        voP.addList(vp1, vp2, vp3, voSp, vp4);
        //----------------------------------------------mapper 配置---------------------------------------------
        CommonPanel mapperP = new CommonPanel(new FlowLayout(FlowLayout.LEFT), avgW, new MapperParamsInfo(tableColumnInfos));
        mapperP.setBorder(BorderFactory.createTitledBorder(lineBorder, TemplateTypeEnum.MAPPER.getTemplateDesc()));
        CommonPanel mapperP1 = new CommonPanel(new JLabel("名称："), new CustomJTextField(this.getPojoName(tableName, Code4jConstants.MAPPER_SUFFIX), INPUT_DIMENSION));
        CommonPanel mapperP2 = new CommonPanel(new JLabel("包名："), mapperPackName = new CustomJTextField(null, PACKAGE_DIMENSION));
        CommonPanel mapperP3 = new CommonPanel(new JLabel("路径："), mapperPackPath = new CustomJTextField(null, PATH_DIMENSION));
        CommonPanel mapperSp = new CommonPanel(new JLabel("父类："), mapperSpText = new CustomJTextField(Code4jConstants.MAPPER_SUPER_CLASS, INPUT_DIMENSION));
        TemplateClickLabel mapperP4 = new TemplateClickLabel("配 置", jdbcTableInfo.getTableName() + " " + TemplateTypeEnum.MAPPER.getTemplateDesc(), TemplateTypeEnum.MAPPER, mapperP, !mybatisplusCb.isSelected());
        mapperP.addList(mapperP1, mapperP2, mapperP3, mapperSp, mapperP4);
        //----------------------------------------------xml 配置---------------------------------------------
        CommonPanel xmlP = new CommonPanel(new FlowLayout(FlowLayout.LEFT), avgW, new XmlParamsInfo(tableColumnInfos));
        xmlP.setBorder(BorderFactory.createTitledBorder(lineBorder, TemplateTypeEnum.XML.getTemplateDesc()));
        CommonPanel xml1P = new CommonPanel(new JLabel("名称："), new CustomJTextField(this.getPojoName(tableName, Code4jConstants.XML_SUFFIX), INPUT_DIMENSION));
        CommonPanel xml2P = new CommonPanel(new JLabel("包名："), xmlPackName = new CustomJTextField(null, PACKAGE_DIMENSION));
        CommonPanel xml3P = new CommonPanel(new JLabel("路径："), xmlPackPath = new CustomJTextField(null, PATH_DIMENSION));
        TemplateClickLabel xml4v = new TemplateClickLabel("XML配置", jdbcTableInfo.getTableName() + " " + TemplateTypeEnum.XML.getTemplateDesc(), TemplateTypeEnum.XML, xmlP, !mybatisplusCb.isSelected());
        xmlP.addList(xml1P, xml2P, xml3P, xml4v);
        //----------------------------------------------service 配置---------------------------------------------
        CommonPanel serviceP = new CommonPanel(new FlowLayout(FlowLayout.LEFT), avgW);
        serviceP.setBorder(BorderFactory.createTitledBorder(lineBorder, TemplateTypeEnum.SERVICE_API.getTemplateDesc()));
        CommonPanel serviceP1 = new CommonPanel(new JLabel("名称："), new CustomJTextField("I" + this.getPojoName(tableName, Code4jConstants.SERVICE_SUFFIX), INPUT_DIMENSION));
        CommonPanel serviceP2 = new CommonPanel(new JLabel("包名："), serviceApiPackName = new CustomJTextField(null, PACKAGE_DIMENSION));
        CommonPanel serviceP3 = new CommonPanel(new JLabel("路径："), serviceApiPackPath = new CustomJTextField(Code4jConstants.DEFAULT_PATH, PATH_DIMENSION));
        CommonPanel serviceSp = new CommonPanel(new JLabel("父类："), serviceSpText = new CustomJTextField(null, INPUT_DIMENSION));
        serviceP.addList(serviceP1, serviceP2, serviceP3, serviceSp);
        //----------------------------------------------service 配置---------------------------------------------
        CommonPanel serviceImpl = new CommonPanel(new FlowLayout(FlowLayout.LEFT), avgW);
        serviceImpl.setBorder(BorderFactory.createTitledBorder(lineBorder, TemplateTypeEnum.SERVICE.getTemplateDesc()));
        CommonPanel serviceImpl1 = new CommonPanel(new JLabel("名称："), new CustomJTextField(this.getPojoName(tableName, Code4jConstants.SERVICE_SUFFIX) + "Impl", INPUT_DIMENSION));
        CommonPanel serviceImpl2 = new CommonPanel(new JLabel("包名："), serviceImplPackName = new CustomJTextField(null, PACKAGE_DIMENSION));
        CommonPanel serviceImpl3 = new CommonPanel(new JLabel("路径："), serviceImplPackPath = new CustomJTextField(Code4jConstants.DEFAULT_PATH, PATH_DIMENSION));
        CommonPanel serviceImpl4 = new CommonPanel(new JLabel("父类："), serviceImplSpText = new CustomJTextField(null, INPUT_DIMENSION));
        serviceImpl.addList(serviceImpl1, serviceImpl2, serviceImpl3, serviceImpl4);

        //----------------------------------------------control 配置---------------------------------------------
        CommonPanel contrP = new CommonPanel(new FlowLayout(FlowLayout.LEFT), avgW, new ControllerParamsInfo(tableColumnInfos));
        contrP.setBorder(BorderFactory.createTitledBorder(lineBorder, TemplateTypeEnum.CONTROLLER.getTemplateDesc()));
        CommonPanel contr1P = new CommonPanel(new JLabel("名称："), new CustomJTextField(this.getPojoName(tableName, Code4jConstants.CONTROLLER_SUFFIX), INPUT_DIMENSION));
        CommonPanel contr2P = new CommonPanel(new JLabel("包名："), contrPackName = new CustomJTextField(null, PACKAGE_DIMENSION));
        CommonPanel contr3P = new CommonPanel(new JLabel("路径："), contrPackPath = new CustomJTextField(null, PATH_DIMENSION));
        CommonPanel contrSp = new CommonPanel(new JLabel("父类："), contrSpText = new CustomJTextField(Code4jConstants.CONTROLLER_SUPER_CLASS, INPUT_DIMENSION));
        TemplateClickLabel contrCf = new TemplateClickLabel("配置", TemplateTypeEnum.CONTROLLER.getTemplateDesc(), TemplateTypeEnum.CONTROLLER, contrP, true);
        contrP.addList(contr1P, contr2P, contr3P, contrSp, contrCf);
        //----------------------------------------------CheckBox 配置---------------------------------------------
        CommonPanel optionP = new CommonPanel(new FlowLayout(FlowLayout.LEFT), avgW);
        CustomJCheckBox cb1 = new CustomJCheckBox(TemplateTypeEnum.DO.getTemplateDesc(), true, TemplateTypeEnum.DO.getTemplateId(), this.convertModelBox(daoP, true));
        CustomJCheckBox cb2 = new CustomJCheckBox(TemplateTypeEnum.VO.getTemplateDesc(), true, TemplateTypeEnum.VO.getTemplateId(), this.convertModelBox(voP, true));
        CustomJCheckBox cb4 = new CustomJCheckBox(TemplateTypeEnum.XML.getTemplateDesc(), true, TemplateTypeEnum.XML.getTemplateId(), this.convertModelBox(xmlP, false));
        CustomJCheckBox cb5 = new CustomJCheckBox(TemplateTypeEnum.MAPPER.getTemplateDesc(), true, TemplateTypeEnum.MAPPER.getTemplateId(), this.convertModelBox(mapperP, true));
        CustomJCheckBox cb6 = new CustomJCheckBox("Service(Api，Impl)配置", true, TemplateTypeEnum.SERVICE_API.getTemplateId(), new ServiceModelBox(this.convertModelBox(serviceP, true), this.convertModelBox(serviceImpl, true)));
        CustomJCheckBox cb7 = new CustomJCheckBox(TemplateTypeEnum.CONTROLLER.getTemplateDesc(), true, TemplateTypeEnum.CONTROLLER.getTemplateId(), this.convertModelBox(contrP, true));
        final List<CustomJCheckBox> cbList = Stream.of(cb1, cb2, cb5, cb4, cb6, cb7).collect(Collectors.toList());
        optionP.addList(cbList.toArray(new CustomJCheckBox[0]));
        //添加绑定要生成代码的容器 有序添加 0：do配置；1：vo配置；2：mapper配置；3：service api配置
        customJCheckBoxList.addAll(cbList);
        //mybatispuls 插件 关联mapperP4 和xml4v
        mybatisplusCb.addActionListener(new CustomJCheckBox.CustomJCheckBoxSelectedAction((v, c) -> {
            for (JComponent jComponent : c) {
                jComponent.setVisible(false);
            }
        }, (v, c) -> {
            for (JComponent jComponent : c) {
                jComponent.setVisible(true);
            }
        }, mapperP4, xml4v));

        CommonPanel btnP = new CommonPanel(new FlowLayout(FlowLayout.RIGHT), new Dimension((int) preferredSize.getWidth() - 10, avgH - 20));
        CustomJButton generate = new CustomJButton(" 生成代码 ", new GenerateCodeAction(this, p1V, jdbcTableInfo, customJCheckBoxList, jdbcSourceInfo, lombokCb, mybatisplusCb, swaggerCb));
        btnP.add(generate);
        //布局
        Box box = Box.createVerticalBox();
        box.add(projectP);
        box.add(daoP);
        box.add(voP);
        box.add(mapperP);
        box.add(xmlP);
        box.add(serviceP);
        box.add(serviceImpl);
        box.add(contrP);
        box.add(optionP);
        box.add(btnP);
        this.add(box);
        this.defaultInit(tableName);
        this.updateUI();
    }

    public void defaultInit(String tableName) {
        this.loadProjectConfig(new ProjectCodeConfigInfo(tableName));
    }

    /**
     * @param commonPanel
     * @return
     */
    private ModelBoxInfo convertModelBox(CommonPanel commonPanel, boolean haveSuperClass) {
        //获取名称
        CustomJTextField packNameT = (CustomJTextField) ((CommonPanel) commonPanel.getComponent(0)).getComponent(1);
        //获取包名
        CustomJTextField packageT = (CustomJTextField) ((CommonPanel) commonPanel.getComponent(1)).getComponent(1);
        //获取路径
        CustomJTextField pojoPathT = (CustomJTextField) ((CommonPanel) commonPanel.getComponent(2)).getComponent(1);
        //获取父类路径
        CustomJTextField superPathT = null;
        if (haveSuperClass) {
            superPathT = (CustomJTextField) ((CommonPanel) commonPanel.getComponent(3)).getComponent(1);
        }

        return new ModelBoxInfo(packNameT, packageT, pojoPathT, superPathT, commonPanel.getBindObject());
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
            serviceImplPackName.setText(info.getServiceImplPackageName());
            serviceImplPackPath.setText(info.getServiceImplPath());
            serviceApiPackName.setText(info.getServiceApiPackageName());
            serviceApiPackPath.setText(info.getServiceApiPath());
            contrPackName.setText(info.getContrPackageName());
            contrPackPath.setText(info.getContrPath());
            doSpText.setText(info.getDoSuperClass());
            voSpText.setText(info.getVoSuperClass());
            mapperSpText.setText(info.getMapperSuperClass());
            contrSpText.setText(info.getContrSuperClass());
            serviceSpText.setText(info.getServiceSuperClass());
            serviceImplSpText.setText(info.getServiceImplSuperClass());
        }
    }

    private String getPojoName(String str, String prefix) {
        if (str.startsWith("t_")) {
            str = StrUtil.subFirstStr(str, "t_");
        }
        return StrUtil.underlineToCamelFirstToUpper(str) + prefix;
    }


    public ProjectCodeConfigInfo getProjectCodeConfigInfo() {
        return projectCodeConfigInfo;
    }

    public void setProjectCodeConfigInfo(ProjectCodeConfigInfo projectCodeConfigInfo) {
        this.projectCodeConfigInfo = projectCodeConfigInfo;
    }
}
