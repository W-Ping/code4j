package com.code4j.pojo;

import org.apache.commons.lang3.StringUtils;

/**
 * @author liu_wp
 * @date 2020/11/24
 * @see
 */
public class PropertyInfo {
    private String key;
    private String value;

    public PropertyInfo(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * @param obj
     * @return
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        PropertyInfo propertyInfo = (PropertyInfo) obj;
        if (StringUtils.isNotBlank(this.getKey())
                && this.getKey().equals(propertyInfo.getKey())) {
            return true;
        }
        return false;
    }


    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }
}
