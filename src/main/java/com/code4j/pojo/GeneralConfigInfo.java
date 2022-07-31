package com.code4j.pojo;

import com.code4j.annotation.Column;
import com.code4j.annotation.Table;

/**
 * @author lwp
 * @date 2022-07-29
 */
@Table(value = "general_config_info", uniqueKey = "version")
public class GeneralConfigInfo extends BaseInfo {
    /**
     * 版本信息
     */
    @Column("version")
    private String version;
    /**
     * 配置信息 JSON
     */
    @Column("config_data")
    private String configData;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getConfigData() {
        return configData;
    }

    public void setConfigData(String configData) {
        this.configData = configData;
    }

}
