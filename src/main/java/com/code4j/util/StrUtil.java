package com.code4j.util;

import com.google.common.base.CaseFormat;
import org.apache.commons.lang3.StringUtils;

/**
 * @author liu_wp
 * @date 2020/11/19
 * @see
 */
public class StrUtil {

    /**
     * 下划线转驼峰
     * 例如：user_name
     *
     * @param str
     * @return
     */
    public static String underlineToCamel(String str) {
        return underlineToCamel(str, false);
    }

    /**
     * @param str
     * @return
     */
    public static String underlineToCamelToLower(String str) {
        return underlineToCamel(str, false).toLowerCase();
    }

    /**
     * @param str
     * @return
     */
    public static String underlineToCamelFirstToUpper(String str) {
        return underlineToCamel(str, true);
    }

    /**
     * @param str
     * @param isUpper 是否大写
     * @return
     */
    public static String underlineToCamel(String str, boolean isUpper) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        if (StringUtils.containsAny(str, "_")) {
            return CaseFormat.LOWER_UNDERSCORE.to(isUpper ? CaseFormat.UPPER_CAMEL : CaseFormat.LOWER_CAMEL, str);
        }
        return str;
    }

    /**
     * @param str
     * @return
     */
    public static String firstCharOnlyToUpper(String str) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL,str);
    }
    public static String firstCharOnlyToLower(String str) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL,str);
    }
    /**
     * 中划线逻辑符转驼峰
     * 例如：user-name
     *
     * @param str
     * @param isUpper 是否大写
     * @return
     */
    public static String hyphenToCamel(String str, boolean isUpper) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        if (StringUtils.containsAny(str, "-")) {
            return CaseFormat.LOWER_HYPHEN.to(isUpper ? CaseFormat.UPPER_CAMEL : CaseFormat.LOWER_CAMEL, str);
        }
        return str;
    }

    /**
     * @param str
     * @return
     */
    public static String hyphenToCamel(String str) {
        return hyphenToCamel(str, false);
    }

    /**
     * @param str
     * @param repChar
     * @param replacement
     * @return
     */
    public static String replaceAll(String str, String repChar, String replacement) {
        if (StringUtils.isNotBlank(str) && StringUtils.isNotBlank(repChar)) {
            return str.replaceAll("[" + repChar + "]", replacement);
        }
        return str;
    }

    public static String subStrLast(String str, String substr) {
        if (StringUtils.isNotBlank(str) && StringUtils.isNotBlank(substr)
                && str.indexOf(substr) >= 0) {
            String[] split = str.split("[" + substr + "]");
            return split[split.length - 1];
        }
        return null;
    }

    public static String subFirstStr(String str, String substr) {
        if (StringUtils.isNotBlank(str) && StringUtils.isNotBlank(substr)
                && str.indexOf(substr) >= 0) {
            return str.substring(str.indexOf(substr) + substr.length());
        }
        return null;
    }

}
