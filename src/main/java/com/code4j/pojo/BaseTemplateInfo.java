package com.code4j.pojo;

import com.code4j.util.StrUtil;
import org.apache.commons.lang3.StringUtils;

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

    public String getPojoName() {
        return pojoName;
    }

    /**
     * @param rootPath
     * @param suffix
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
}
