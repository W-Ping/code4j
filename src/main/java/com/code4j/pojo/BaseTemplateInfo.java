package com.code4j.pojo;

import com.code4j.config.Code4jConstants;
import com.code4j.config.TemplateTypeEnum;
import com.code4j.util.StrUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * @author liu_wp
 * @date 2020/11/20
 * @see
 */
public class BaseTemplateInfo {
    /**
     * 作者
     */
    private String author;
    /**
     * 对象名称
     */
    private String pojoName;
    /**
     * 对象包
     */
    private String packageName;
    /**
     * 默认对象包
     */
    private String defaultPackageName;
    /**
     * 对象路径
     */
    private String pojoPath;
    /**
     * 对象类型
     */
    private String pojoType;

    private String packageRoot;
    /**
     * 表信息
     */
    private JdbcTableInfo jdbcTableInfo;

    private String pojoDesc;

    private TemplateTypeEnum templateTypeEnum;
    /**
     *
     */
    private TemplateInfo templateInfo;
    /**
     *
     */
    private List<JdbcMapJavaInfo> tableColumnInfos;

    /**
     *
     */
    private String primaryKey;

    public String getPojoName() {
        return pojoName;
    }

    /**
     * @param rootPath
     * @return
     */
    public String getGeneratePojoFolder(String rootPath) {
        StringBuilder sb = new StringBuilder();
        sb.append(rootPath);
        if (!rootPath.endsWith("\\")) {
            sb.append("\\");
        }
        if (StringUtils.isNotBlank(pojoPath)) {
            String pojoPathTo = StrUtil.replaceAll(pojoPath, "/", "\\\\");
            sb.append(pojoPathTo);
            if (!pojoPathTo.endsWith("\\")) {
                sb.append("\\");
            }
        }
        if (StringUtils.isNotBlank(packageRoot)) {
            String packageRootTo = StrUtil.replaceAll(packageRoot, ".", "\\\\");
            sb.append(packageRootTo);
            if (!packageRootTo.endsWith("\\")) {
                sb.append("\\");
            }
        }
        if (StringUtils.isNotBlank(packageName)) {
            String packageNameTo = StrUtil.replaceAll(packageName, ".", "\\\\");
            sb.append(packageNameTo);
        }
        return sb.toString();
    }

    /**
     * @return
     */
    public JdbcMapJavaInfo getTablePrimaryKey() {
        if (CollectionUtils.isNotEmpty(tableColumnInfos)) {
            Optional<JdbcMapJavaInfo> first = tableColumnInfos.stream().filter(JdbcMapJavaInfo::isPrimaryKey).findFirst();
            if (first.isPresent()) {
                return first.get();
            }
        }
        return null;
    }

    /**
     * @return
     */
    public String getPackagePath() {
        if (StringUtils.isNotBlank(this.packageName)
                && StringUtils.isNotBlank(this.pojoName)) {
            return this.packageName + "." + this.pojoName;
        }
        return null;
    }

    public String getPackageRoot() {
        return packageRoot;
    }

    public void setPackageRoot(final String packageRoot) {
        this.packageRoot = packageRoot;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(final String author) {
        this.author = author;
    }

    public void setPojoName(final String pojoName) {
        this.pojoName = pojoName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(final String packageName) {
        this.packageName = packageName;
    }

    public String getPojoPath() {
        return pojoPath;
    }

    public void setPojoPath(final String pojoPath) {
        this.pojoPath = pojoPath;
    }

    public String getPojoType() {
        return pojoType;
    }

    public void setPojoType(final String pojoType) {
        this.pojoType = pojoType;
    }

    public JdbcTableInfo getJdbcTableInfo() {
        return jdbcTableInfo;
    }

    public void setJdbcTableInfo(final JdbcTableInfo jdbcTableInfo) {
        this.jdbcTableInfo = jdbcTableInfo;
    }

    public String getPojoDesc() {
        return pojoDesc;
    }

    public void setPojoDesc(final String pojoDesc) {
        this.pojoDesc = pojoDesc;
    }

    public TemplateTypeEnum getTemplateTypeEnum() {
        return templateTypeEnum;
    }

    public void setTemplateTypeEnum(final TemplateTypeEnum templateTypeEnum) {
        this.templateTypeEnum = templateTypeEnum;
    }

    public String getDefaultPackageName() {
        return defaultPackageName;
    }

    public void setDefaultPackageName(final String defaultPackageName) {
        this.defaultPackageName = defaultPackageName;
    }

    public TemplateInfo getTemplateInfo() {
        return templateInfo;
    }

    public void setTemplateInfo(final TemplateInfo templateInfo) {
        this.templateInfo = templateInfo;
    }

    /**
     * @param fileName
     * @return
     */
    public String getUseDefaultPackageRoot(String fileName) {
        if (StringUtils.isNotBlank(this.defaultPackageName) && StringUtils.isNotBlank(this.packageName)
                && this.defaultPackageName.equals(this.packageName) && StringUtils.isNotBlank(fileName)) {

//            return packageRoot = Code4jConstants.DEFAULT_ROOT_PACKAGE + "." + fileName + ".";
            return packageRoot = Code4jConstants.DEFAULT_ROOT_PACKAGE + ".";
        }
        return null;
    }

    public static String getDefaultPackageName(TemplateTypeEnum templateTypeEnum, String packageName) {
        if (TemplateTypeEnum.MAPPER.equals(templateTypeEnum)) {
            return Code4jConstants.DEFAULT_MAPPER_PACKAGE + "." + packageName;
        } else if (TemplateTypeEnum.XML.equals(templateTypeEnum)) {
            return Code4jConstants.DEFAULT_SQL_XML_PACKAGE + "." + packageName;
        } else if (TemplateTypeEnum.VO.equals(templateTypeEnum)) {
            return Code4jConstants.DEFAULT_VO_PACKAGE + "." + packageName;
        } else if (TemplateTypeEnum.DO.equals(templateTypeEnum)) {
            return Code4jConstants.DEFAULT_DO_PACKAGE + "." + packageName;
        } else if (TemplateTypeEnum.SERVICE_API.equals(templateTypeEnum)) {
            return packageName;
        }
        return null;
    }

    public List<JdbcMapJavaInfo> getTableColumnInfos() {
        return tableColumnInfos;
    }

    public void setTableColumnInfos(final List<JdbcMapJavaInfo> tableColumnInfos) {
        this.tableColumnInfos = tableColumnInfos;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }
}
