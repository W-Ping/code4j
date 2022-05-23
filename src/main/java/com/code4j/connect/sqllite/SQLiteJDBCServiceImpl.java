package com.code4j.connect.sqllite;

import com.code4j.connect.AbstractJDBCService;
import com.code4j.connect.DataSourceTypeEnum;
import com.code4j.pojo.BaseInfo;
import com.code4j.pojo.DbTableInfo;
import com.code4j.pojo.JdbcSourceInfo;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author lwp
 * @date 2022-05-20
 */
public class SQLiteJDBCServiceImpl<T extends BaseInfo> extends AbstractJDBCService<T> {
    public SQLiteJDBCServiceImpl(final JdbcSourceInfo jdbcSourceInfo) {
        super(jdbcSourceInfo);
    }

    @Override
    protected DataSourceTypeEnum dataSourceTypeEnum() {
        return DataSourceTypeEnum.SQLITE;
    }

    @Override
    protected String createTableSql(String tableName, List<DbTableInfo> dbTableInfos) {
        StringBuilder sb = new StringBuilder("CREATE TABLE ");
        sb.append(tableName);
        sb.append("(");
        for (int i = 0; i < dbTableInfos.size(); i++) {
            final DbTableInfo dbTableInfo = dbTableInfos.get(i);
            sb.append(dbTableInfo.getColumn());
            sb.append(" ");
            sb.append(dbTableInfo.getJdbcType());
            if (dbTableInfo.getPrimaryKey() != null && dbTableInfo.getPrimaryKey()) {
                sb.append(" PRIMARY KEY ");
            }
            if (dbTableInfo.getAutoIncrement() != null && dbTableInfo.getAutoIncrement()) {
                sb.append(" AUTOINCREMENT ");
            }
            if (dbTableInfo.getCanNull() != null && !dbTableInfo.getCanNull()) {
                sb.append(" NOT NULL ");
            }
            if (StringUtils.isNotBlank(dbTableInfo.getDefaultValue())) {
                sb.append(" DEFAULT ");
                sb.append(dbTableInfo.getDefaultValue());
            }
            if (i != dbTableInfos.size() - 1) {
                sb.append(",");
            }
        }
        sb.append(")");
        System.out.println(sb.toString());
        return sb.toString();
    }
}
