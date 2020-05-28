package com.linkkou.spring.collectors.sql;

import com.linkkou.spring.collectors.sql.change.IntChange;
import com.linkkou.spring.collectors.sql.change.VarcharChange;
import org.javatuples.Pair;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lk
 * @version 1.0
 * @date 2020/5/22 08:25
 */
public class SqlQueryField {

    private Map<String, Pair<String, SqlQueryFieldChange>> change = new HashMap<>();

    private Map<SqlQueryType, SqlQueryFieldChange> changeMap;


    public SqlQueryField() {
        this.changeMap = new HashMap<>();
        this.changeMap.put(SqlQueryType.VARCHAR, new VarcharChange());
        this.changeMap.put(SqlQueryType.INT, new IntChange());
        this.changeMap.put(SqlQueryType.DECIMAL, new VarcharChange());
    }

    /**
     * 统计字段类型
     *
     * @param aliasField   别名
     * @param dbField      字段
     * @param sqlQueryType 类型
     * @return
     */
    public SqlQueryField put(String aliasField, String dbField, SqlQueryType sqlQueryType) {
        final SqlQueryFieldChange sqlQueryFieldChange = changeMap.get(sqlQueryType);
        change.put(aliasField, Pair.with(dbField, sqlQueryFieldChange));
        return this;
    }

    /**
     * 获取到字段
     *
     * @param aliasField
     * @return
     */
    public Pair<String, SqlQueryFieldChange> get(String aliasField) {
        return change.get(aliasField);
    }

}
