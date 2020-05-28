package com.linkkou.spring.collectors.sql;

import com.linkkou.spring.driven.converters.JsonResultException;
import com.linkkou.spring.enums.SystemMsgEnums;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lk
 * @version 1.0
 * @date 2020/5/21 17:53
 */
public class SqlQuery {

    /**
     * 构建的SQL语句列表
     */
    private final List<String> lineSql = new ArrayList<>();

    /**
     * 参数
     */
    private final Map<String, Object> mapvalue = new HashMap<>();

    /**
     * 字段
     */
    private final SqlQueryField sqlQueryField;

    public SqlQuery(SqlQueryField sqlQueryField) {
        this.sqlQueryField = sqlQueryField;
    }

    private void in(String paramname, String sqlkey, Object value, int index, Pair<String, SqlQueryFieldChange> contrastField, SqlQueryProvider queryProvider) {
        final SqlQueryProviderSymbolEnum symbol = queryProvider.getExtend().getSymbol();
        final SqlQueryProviderLinkEnum link = queryProvider.getLink();
        if (symbol == SqlQueryProviderSymbolEnum.IN || symbol == SqlQueryProviderSymbolEnum.NotIN) {
            List<String> insql = new ArrayList<>();
            if (value instanceof List) {
                List<?> values = (List<?>) value;
                String dbField = contrastField.getValue0();
                for (int i = 0; i < values.size(); i++) {
                    String sqlkeyitem = String.format("%s%s", sqlkey, i);
                    Object dbValue = contrastField.getValue1().change(values.get(i));
                    if (dbValue != null) {
                        mapvalue.put(sqlkeyitem, dbValue);
                        if (i == values.size() - 1) {
                            insql.add(String.format("#{%s.%s} ", paramname, sqlkeyitem));
                        } else {
                            insql.add(String.format("#{%s.%s} , ", paramname, sqlkeyitem));
                        }
                    }
                }
                if (insql.size() > 0) {
                    final String collect = String.join("", insql);
                    if (index == 0) {
                        lineSql.add(String.format(" %s %s ( %s )", dbField, symbol.getType(), collect));
                    } else {
                        lineSql.add(String.format(" %s %s %s ( %s ) ", link.getType(), dbField, symbol.getType(), collect));
                    }
                }
            } else {
                throw new JsonResultException(SystemMsgEnums.OPS_SQL_QUERY);
            }
        }
    }

    private void symbol(String paramname, String sqlkey, Object value, int index, Pair<String, SqlQueryFieldChange> contrastField, SqlQueryProvider queryProvider) {
        final SqlQueryProviderSymbolEnum symbol = queryProvider.getExtend().getSymbol();
        final SqlQueryProviderLinkEnum link = queryProvider.getLink();
        if (symbol == SqlQueryProviderSymbolEnum.IN || symbol == SqlQueryProviderSymbolEnum.NotIN) {
            in(paramname, sqlkey, value, index, contrastField, queryProvider);
        } else {
            String dbField = contrastField.getValue0();
            Object dbValue = contrastField.getValue1().change(value);
            if (dbValue != null) {
                if (index == 0) {
                    mapvalue.put(sqlkey, dbValue);
                    lineSql.add(String.format(" %s %s ( #{%s.%s} ) ", dbField, symbol.getType(), paramname, sqlkey));
                } else {
                    mapvalue.put(sqlkey, dbValue);
                    lineSql.add(String.format(" %s %s %s ( #{%s.%s} ) ", link.getType(), dbField, symbol.getType(), paramname, sqlkey));
                }
            }
        }
    }

    private void recursion(String paramname, List<SqlQueryProvider> sql) {
        for (int i = 0; i < sql.size(); i++) {
            final SqlQueryProvider queryProvider = sql.get(i);
            if (queryProvider.getType() == SqlQueryProviderTypeEnum.Expression) {
                final String field = queryProvider.getExtend().getField();
                final SqlQueryProviderLinkEnum link = queryProvider.getLink();
                if (link == SqlQueryProviderLinkEnum.UNKNOWN) {
                    throw new JsonResultException(SystemMsgEnums.OPS_SQL_QUERY);
                }
                final Pair<String, SqlQueryFieldChange> contrastField = sqlQueryField.get(field);
                if (contrastField == null) {
                    throw new JsonResultException(SystemMsgEnums.OPS_SQL_QUERY);
                }
                final SqlQueryProviderSymbolEnum symbol = queryProvider.getExtend().getSymbol();
                if (symbol == SqlQueryProviderSymbolEnum.UNKNOWN) {
                    throw new JsonResultException(SystemMsgEnums.OPS_SQL_QUERY);
                }
                final Object value = queryProvider.getExtend().getValue();
                final String sqlkey = String.format("a%s", lineSql.size());
                symbol(paramname, sqlkey, value, i, contrastField, queryProvider);
            }
            if (queryProvider.getType() == SqlQueryProviderTypeEnum.Group) {
                final SqlQueryProviderLinkEnum link = queryProvider.getLink();
                lineSql.add(String.format(" %s ( ", link.getType()));
                recursion(paramname, queryProvider.getChildren());
                lineSql.add(" ) ");
            }
        }
    }


    public Pair<Map<String, Object>, String> build(String paramname, List<SqlQueryProvider> sql) {
        recursion(paramname, sql);
        return Pair.with(mapvalue, lineSql.stream().collect(Collectors.joining()));
    }
}
