package com.code4j.pojo;

import com.code4j.connect.DataSourceTypeEnum;

/**
 * @author liu_wp
 * @date 2020/11/17
 * @see
 */
public class JdbcSourceInfo extends BaseInfo implements Cloneable {
    /**
     * 连接名称
     */
    private String connectName;
    /**
     * 连接地址（域名或IP）
     */
    private String connectHost;
    /**
     * 连接端口
     */
    private Integer connectPort;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户密码
     */
    private String password;

    private String creator = "code4j";
    private DataSourceTypeEnum dataSourceTypeEnum;

    @Override
    public JdbcSourceInfo clone() {
        try {
            JdbcSourceInfo jdbcSourceInfo = new JdbcSourceInfo();
            jdbcSourceInfo.setConnectName(this.getConnectName());
            jdbcSourceInfo.setConnectHost(this.getConnectHost());
            jdbcSourceInfo.setConnectPort(this.getConnectPort());
            jdbcSourceInfo.setUserName(this.getUserName());
            jdbcSourceInfo.setPassword(this.getPassword());
            jdbcSourceInfo.setCreator(this.getCreator());
            jdbcSourceInfo.setDataSourceTypeEnum(this.getDataSourceTypeEnum());
            return jdbcSourceInfo;
        } catch (Exception e) {
            return null;
        }
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
        JdbcSourceInfo jdbcSourceInfo = (JdbcSourceInfo) obj;
        if (jdbcSourceInfo.getConnectHost() != null && jdbcSourceInfo.getConnectHost().equals(this.getConnectHost())
                && jdbcSourceInfo.getConnectPort() != null && jdbcSourceInfo.getConnectPort().equals(this.getConnectPort())) {
            return true;
        }
        return false;
    }

    public String getConnectName() {
        return connectName;
    }

    public void setConnectName(final String connectName) {
        this.connectName = connectName;
    }

    public String getConnectHost() {
        return connectHost;
    }

    public void setConnectHost(final String connectHost) {
        this.connectHost = connectHost;
    }

    public Integer getConnectPort() {
        return connectPort;
    }

    public void setConnectPort(final Integer connectPort) {
        this.connectPort = connectPort;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public DataSourceTypeEnum getDataSourceTypeEnum() {
        return dataSourceTypeEnum;
    }

    public void setDataSourceTypeEnum(final DataSourceTypeEnum dataSourceTypeEnum) {
        this.dataSourceTypeEnum = dataSourceTypeEnum;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(final String creator) {
        this.creator = creator;
    }
}
