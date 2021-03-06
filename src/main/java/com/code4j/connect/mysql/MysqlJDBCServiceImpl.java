package com.code4j.connect.mysql;

import com.code4j.connect.AbstractJDBCService;
import com.code4j.connect.DataSourceTypeEnum;
import com.code4j.pojo.JdbcSourceInfo;

/**
 * @author liu_wp
 * @date 2020/11/18
 * @see
 */
public class MysqlJDBCServiceImpl extends AbstractJDBCService {
    public MysqlJDBCServiceImpl(final JdbcSourceInfo jdbcSourceInfo) {
        super(jdbcSourceInfo);
    }

    @Override
    protected DataSourceTypeEnum dataSourceTypeEnum() {
        return DataSourceTypeEnum.MYSQL;
    }
}
