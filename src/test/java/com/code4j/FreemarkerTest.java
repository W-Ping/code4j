package com.code4j;

import com.code4j.config.Code4jConstants;
import com.code4j.config.TemplateTypeEnum;
import com.code4j.pojo.*;
import com.code4j.util.FreemarkerUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author liu_wp
 * @date 2020/11/20
 * @see
 */
public class FreemarkerTest {


    @Before
    public void before() throws IOException {
    }

    @Test
    public void testDoTemplate() {
        String rootPath = "D:\\iris-work\\code4j";
        PojoParamsInfo pojoParamsInfo = new PojoParamsInfo();
        pojoParamsInfo.setPojoPath("src/main/java");
        pojoParamsInfo.setPackageName("com.code4j.templates");
        pojoParamsInfo.setPojoName("TestInfoDO");
        JdbcTableInfo jdbcTableInfo = new JdbcTableInfo();
        jdbcTableInfo.setDbName("t_text_info");
        jdbcTableInfo.setRemark("测试表");
        pojoParamsInfo.setAuthor("刘伟平");
        pojoParamsInfo.setJdbcTableInfo(jdbcTableInfo);
        SuperPojoInfo superPojoInfo = new SuperPojoInfo();
        superPojoInfo.setPojoName("BaseInfo");
        superPojoInfo.setPackageName("com.code4j.pojo");
        pojoParamsInfo.setSuperPojoInfo(superPojoInfo);
        List<JdbcMapJavaInfo> tableColumnInfos = new ArrayList<>();
        JdbcMapJavaInfo j1 = new JdbcMapJavaInfo();
        j1.setJavaProperty("userName");
        j1.setComment("用户信息");
        j1.setJavaType("String");
        j1.setColumn("user_name");
        JdbcMapJavaInfo j2 = new JdbcMapJavaInfo();
        j2.setJavaProperty("userAge");
        j2.setComment("用户年龄");
        j2.setJavaType("Integer");
        j2.setColumn("user_age");
        JdbcMapJavaInfo j3 = new JdbcMapJavaInfo();
        j3.setJavaProperty("money");
        j3.setComment("年薪");
        j3.setJavaType("java.math.BigDecimal");
        j3.setColumn("money");
        tableColumnInfos.add(j1);
        tableColumnInfos.add(j2);
        tableColumnInfos.add(j3);
        TemplateInfo templateInfo = new TemplateInfo();
        templateInfo.setTemplateId(TemplateTypeEnum.DO.getTemplateId());
        templateInfo.setTemplatePath(Code4jConstants.TEMPLATE_PATH);
        pojoParamsInfo.setTemplateInfo(templateInfo);
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("tableColumnInfos", tableColumnInfos);
        dataMap.put("columnPackages", columnPackage(tableColumnInfos));
        dataMap.put("pojo", pojoParamsInfo);
        String pojoPath = FreemarkerUtil.generateCodeByTemplate(rootPath, pojoParamsInfo, dataMap);
        System.out.println(pojoPath);
    }

    @Test
    public void testVoTemplate() {
        String rootPath = "D:\\iris-work\\code4j";
        PojoParamsInfo pojoParamsInfo = new PojoParamsInfo();
        pojoParamsInfo.setPojoPath("src/main/java");
        pojoParamsInfo.setPackageName("com.code4j.templates");
        pojoParamsInfo.setPojoName("TestInfoVO");
        JdbcTableInfo jdbcTableInfo = new JdbcTableInfo();
        jdbcTableInfo.setDbName("t_text_info");
        jdbcTableInfo.setRemark("测试表");
        pojoParamsInfo.setAuthor("刘伟平");
        pojoParamsInfo.setJdbcTableInfo(jdbcTableInfo);
        List<JdbcMapJavaInfo> tableColumnInfos = new ArrayList<>();
        JdbcMapJavaInfo j1 = new JdbcMapJavaInfo();
        j1.setJavaProperty("userName");
        j1.setComment("用户信息");
        j1.setJavaType("String");
        j1.setColumn("user_name");
        JdbcMapJavaInfo j2 = new JdbcMapJavaInfo();
        j2.setJavaProperty("userAge");
        j2.setComment("用户年龄");
        j2.setJavaType("Integer");
        j2.setColumn("user_age");
        JdbcMapJavaInfo j3 = new JdbcMapJavaInfo();
        j3.setJavaProperty("money");
        j3.setComment("年薪");
        j3.setJavaType("java.math.BigDecimal");
        j3.setColumn("money");
        tableColumnInfos.add(j1);
        tableColumnInfos.add(j2);
        tableColumnInfos.add(j3);
        TemplateInfo templateInfo = new TemplateInfo();
        templateInfo.setTemplateId(TemplateTypeEnum.VO.getTemplateId());
        templateInfo.setTemplatePath(Code4jConstants.TEMPLATE_PATH);
        pojoParamsInfo.setTemplateInfo(templateInfo);
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("tableColumnInfos", tableColumnInfos);
        dataMap.put("columnPackages", columnPackage(tableColumnInfos));
        dataMap.put("pojo", pojoParamsInfo);
        String pojoPath = FreemarkerUtil.generateCodeByTemplate(rootPath, pojoParamsInfo, dataMap);
        System.out.println(pojoPath);
    }

    @Test
    public void testMapperTemplate() {
        String rootPath = "D:\\iris-work\\code4j";
        PojoParamsInfo pojoParamsInfo = new PojoParamsInfo();
        pojoParamsInfo.setPojoPath("src/main/java");
        pojoParamsInfo.setPackageName("com.code4j.templates.mapper");
        pojoParamsInfo.setPojoName("TestInfoMapper");
        JdbcTableInfo jdbcTableInfo = new JdbcTableInfo();
        jdbcTableInfo.setDbName("t_text_info");
        jdbcTableInfo.setRemark("测试表");
        pojoParamsInfo.setAuthor("刘伟平");
        pojoParamsInfo.setJdbcTableInfo(jdbcTableInfo);
        TemplateInfo templateInfo = new TemplateInfo();
        templateInfo.setTemplateId(TemplateTypeEnum.MAPPER.getTemplateId());
        templateInfo.setTemplatePath(Code4jConstants.TEMPLATE_PATH);
        pojoParamsInfo.setTemplateInfo(templateInfo);
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("pojo", pojoParamsInfo);
        String pojoPath = FreemarkerUtil.generateCodeByTemplate(rootPath, pojoParamsInfo, dataMap);
        System.out.println(pojoPath);
    }

    @Test
    public void testXmlTemplate() {
        String rootPath = "D:\\iris-work\\code4j";
        PojoParamsInfo pojoParamsInfo = new PojoParamsInfo();
        pojoParamsInfo.setPojoPath("src/main/java");
        pojoParamsInfo.setPackageName("com.code4j.templates");
        pojoParamsInfo.setPojoName("TestInfoDO");
        JdbcTableInfo jdbcTableInfo = new JdbcTableInfo();
        jdbcTableInfo.setDbName("t_text_info");
        jdbcTableInfo.setRemark("测试表");
        pojoParamsInfo.setAuthor("刘伟平");
        pojoParamsInfo.setJdbcTableInfo(jdbcTableInfo);
        List<JdbcMapJavaInfo> tableColumnInfos = new ArrayList<>();
        JdbcMapJavaInfo j1 = new JdbcMapJavaInfo();
        j1.setJavaProperty("userName");
        j1.setComment("用户信息");
        j1.setJavaType("String");
        j1.setColumn("user_name");
        j1.setJdbcType("VARCHAR");
        JdbcMapJavaInfo j2 = new JdbcMapJavaInfo();
        j2.setJavaProperty("userAge");
        j2.setComment("用户年龄");
        j2.setJavaType("Integer");
        j2.setColumn("user_age");
        j2.setJdbcType("VARCHAR");
        JdbcMapJavaInfo j3 = new JdbcMapJavaInfo();
        j3.setJavaProperty("money");
        j3.setComment("年薪");
        j3.setJavaType("java.math.BigDecimal");
        j3.setColumn("money");
        j3.setJdbcType("DECIMAL");
        tableColumnInfos.add(j1);
        tableColumnInfos.add(j2);
        tableColumnInfos.add(j3);
        TemplateInfo templateInfo = new TemplateInfo();
        templateInfo.setTemplateId(TemplateTypeEnum.XML.getTemplateId());
        templateInfo.setTemplatePath(Code4jConstants.TEMPLATE_PATH);

        XmlParamsInfo xmlParamsInfo = new XmlParamsInfo(tableColumnInfos);
        xmlParamsInfo.setNamespace("com.mapper");
        xmlParamsInfo.setResultMapType(pojoParamsInfo.getPackagePath());
        xmlParamsInfo.setPojoName("testInfoMapper");
        xmlParamsInfo.setPojoPath("src/main/java");
        xmlParamsInfo.setPackageName("com.code4j.templates");
        xmlParamsInfo.setAuthor("刘伟平");
        xmlParamsInfo.setTemplateInfo(templateInfo);
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("xmlMap", xmlParamsInfo);
        String pojoPath = FreemarkerUtil.generateCodeByTemplate(rootPath, xmlParamsInfo, dataMap);
        System.out.println(pojoPath);
    }

    private List<String> columnPackage(List<JdbcMapJavaInfo> tableColumnInfos) {
        List<String> packageList = new ArrayList<>();
        for (final JdbcMapJavaInfo tableColumnInfo : tableColumnInfos) {
            String javaType = tableColumnInfo.getJavaType();
            if (javaType.contains(".")) {
                if (javaType.startsWith("java.lang")) {
                    continue;
                } else {
                    packageList.add(javaType);
                }
            }
        }
        return packageList;
    }
}
