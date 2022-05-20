package com.code4j.connect.postgresql;

import com.code4j.connect.AbstractJDBCService;
import com.code4j.connect.DataSourceTypeEnum;
import com.code4j.pojo.JdbcSourceInfo;

/**
 * @author lwp
 * @date 2022-05-20
 */
public class PostgresqlJDBCServiceImpl extends AbstractJDBCService {

    public PostgresqlJDBCServiceImpl(final JdbcSourceInfo jdbcSourceInfo) {
        super(jdbcSourceInfo);
    }
    @Override
    protected DataSourceTypeEnum dataSourceTypeEnum() {
        return DataSourceTypeEnum.POSTGRESQL;
    }

    @Override
    protected String schemaPattern(String schemaPattern) {
        return "public";
    }
}
