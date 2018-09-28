/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 abel533@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.plugin.javawidget.mybatisplugins;


import com.plugin.javawidget.paging.Paginator;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.CachingExecutor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandlerRegistry;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * 目前只有支持MySQL分页支持
 *
 * @author lk
 * @version 1.0.0
 */
@Intercepts(
        {
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        }
)
//@SuppressWarnings()
public class QueryPaginatorInterceptor implements Interceptor {

    /**
     * FOUND_ROWS == true
     * COUNT(*)  == false
     */
    private boolean paginatorType = true;

    public void setPaginatorType(boolean Paginator) {
        this.paginatorType = Paginator;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        final Object[] queryArgs = invocation.getArgs();
        if (invocation.getTarget() instanceof CachingExecutor) {
            if (invocation.getTarget() instanceof Executor) {
                if (invocation.getMethod().getName().equals("query")) {
                    if (queryArgs.length == 4) {
                        final MappedStatement mappedStatement = (MappedStatement) queryArgs[0];
                        if (getinterfaceclass(mappedStatement)) {
                            final Object parameter = queryArgs[1];
                            final BoundSql boundSql = mappedStatement.getBoundSql(parameter);
                            MappedStatement newMs = ProxyResultSetHandler(mappedStatement, new BoundSqlSqlSource(getPagingSql(mappedStatement, boundSql)));
                            queryArgs[0] = newMs;
                            return getPaginator(invocation.proceed(), mappedStatement, boundSql, parameter);
                        }
                    }
                }
            }
        }

        return invocation.proceed();
    }

    /**
     * 返回分页sql语句
     *
     * @param mappedStatement
     * @param boundSql
     * @return
     */
    private BoundSql getPagingSql(MappedStatement mappedStatement, BoundSql boundSql) {
        class getsql {
            /**
             * <p>
             * select rowid, fid
             * from t_sysconfig_carspec_config tscc
             * WHERE fcar_name = #{car}
             * LIMIT
             * #{offset},#{rows}
             * </p>
             *
             * @param mappedStatement
             * @param boundSql
             * @return
             */
            private BoundSql getSqlCount(MappedStatement mappedStatement, BoundSql boundSql) {
                String sql = boundSql.getSql();
                String[] conditions = sql.split(" ", 2);
                //除了Select以外的语句
                String[] limits = conditions[1].split("LIMIT");
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < limits.length - 1; i++) {
                    stringBuilder.append(limits[i]);
                }
                String countSql = "; select count(*) " + stringBuilder.toString();
                List<ParameterMapping> newparameterMappings = new ArrayList<>();
                List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
                newparameterMappings.addAll(parameterMappings);
                //Limit 去两个参数，所有参数以Mapper顺序为准
                for (int i = 0; i < parameterMappings.size() - 2; i++) {
                    newparameterMappings.add(parameterMappings.get(i));
                }
                BoundSql countBoundSql = new BoundSql(mappedStatement.getConfiguration(), sql + countSql, newparameterMappings, boundSql.getParameterObject());
                return countBoundSql;
            }

            /**
             * <p>
             * select SQL_CALC_FOUND_ROWS rowid, fid
             * from t_sysconfig_carspec_config tscc
             * WHERE fcar_name = #{car}
             * LIMIT
             * #{offset},#{rows}
             * </p>
             *
             * @param mappedStatement
             * @param boundSql
             * @return
             */
            private BoundSql getSqlFoundRows(MappedStatement mappedStatement, BoundSql boundSql) {
                String sql = boundSql.getSql();
                sql = sql.replaceFirst("SQL_CALC_FOUND_ROWS", "").replaceFirst("(?i)select", "select SQL_CALC_FOUND_ROWS");
                String countSql = "; SELECT FOUND_ROWS() AS COUNT ; ";
                BoundSql countBoundSql = new BoundSql(mappedStatement.getConfiguration(), sql + countSql, boundSql.getParameterMappings(), boundSql.getParameterObject());
                setForeach(boundSql, countBoundSql);
                return countBoundSql;
            }
        }
        return paginatorType == true ? new getsql().getSqlFoundRows(mappedStatement, boundSql) : new getsql().getSqlCount(mappedStatement, boundSql);
    }


    /**
     * 构建对Foreach的支持
     *
     * @param boundSql
     * @param countBoundSql
     */
    private void setForeach(BoundSql boundSql, BoundSql countBoundSql) {
        try {
            Field[] boundSqlfield = boundSql.getClass().getDeclaredFields();
            Field[] countBoundSqlfields = countBoundSql.getClass().getDeclaredFields();
            Field.setAccessible(countBoundSqlfields, true);
            Field.setAccessible(boundSqlfield, true);
            for (Field f1 : boundSqlfield) {
                for (Field f2 : countBoundSqlfields) {
                    if ("additionalParameters".equals(f1.getName()) && "additionalParameters".equals(f2.getName())) {
                        f2.set(countBoundSql, f1.get(boundSql));
                    }
                    if ("metaParameters".equals(f1.getName()) && "metaParameters".equals(f2.getName())) {
                        f2.set(countBoundSql, f1.get(boundSql));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断返回参数是否为指定的分页对象
     *
     * @param mappedStatement
     * @return
     */
    private boolean getinterfaceclass(MappedStatement mappedStatement) {
        //class.forName("com.plugin.dao.SysconfigCarspecConfigMapper").getMethods()[0].getGenericReturnType
        StringBuilder stringBuilder = new StringBuilder();
        String[] oldpath = mappedStatement.getId().split("\\.");
        int oldpathlength = oldpath.length;
        String methodsname = "", classname = "";
        for (int i = 0; i < oldpathlength; i++) {
            if (i == oldpathlength - 1) {
                methodsname = oldpath[i];
            } else if (i < oldpathlength - 2) {
                stringBuilder.append(oldpath[i]).append(".");
            } else {
                stringBuilder.append(oldpath[i]);
            }
        }
        classname = stringBuilder.toString();
        try {
            Class classs = Class.forName(classname);
            Method[] methods = classs.getMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodsname)) {
                    PaginatorType jsonResultValue = method.getAnnotation(PaginatorType.class);
                    if (jsonResultValue != null) {
                        paginatorType = jsonResultValue.value();
                    }
                    if (method.getGenericReturnType() instanceof ParameterizedTypeImpl) {
                        Class typeImpl = ((ParameterizedTypeImpl) method.getGenericReturnType()).getRawType();
                        return typeImpl.equals(Paginator.class);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 构建返回参数
     *
     * @param invocation
     * @return
     */
    private List getPaginator(Object invocation, MappedStatement mappedStatement, BoundSql boundSql, Object parameter) {
        final Paginator paginator = getNamespaceSql(mappedStatement, boundSql, parameter);
        if (invocation instanceof ArrayList) {
            ArrayList resultList = (ArrayList) invocation;
            paginator.setData(resultList.get(0));
            paginator.setTotal((int) ((ArrayList) resultList.get(1)).get(0));
        }
        List<Paginator> list = new ArrayList<>();
        list.add(paginator);
        return list;
    }

    /**
     * 构建完整的SQL语句，获取到输入的Limt的值，从新返回到分页对象中
     *
     * @param mappedStatement
     * @param boundSql
     * @param params
     * @return
     */
    public Paginator getNamespaceSql(MappedStatement mappedStatement, BoundSql boundSql, Object params) {

        class replaceParameter {

            private String sql;

            /**
             * 根据类型替换参数
             * 仅作为数字和字符串两种类型进行处理，需要特殊处理的可以继续完善这里
             *
             * @param sql
             * @param value
             * @param jdbcType
             * @param javaType
             * @return
             */
            public replaceParameter(String sql, Object value, JdbcType jdbcType, Class javaType) {
                String strValue = String.valueOf(value);
                if (jdbcType != null) {
                    switch (jdbcType) {
                        //数字
                        case BIT:
                        case TINYINT:
                        case SMALLINT:
                        case INTEGER:
                        case BIGINT:
                        case FLOAT:
                        case REAL:
                        case DOUBLE:
                        case NUMERIC:
                        case DECIMAL:
                            break;
                        //日期
                        case DATE:
                        case TIME:
                        case TIMESTAMP:
                            //其他，包含字符串和其他特殊类型
                        default:
                            strValue = "'" + strValue + "'";


                    }
                } else if (Number.class.isAssignableFrom(javaType)) {
                    //不加单引号
                } else {
                    strValue = "'" + strValue + "'";
                }
                this.sql = sql.replaceFirst("\\?", strValue);
            }

            public String getSql() {
                return this.sql;
            }
        }

        final Configuration configuration = mappedStatement.getConfiguration();
        TypeHandlerRegistry typeHandlerRegistry = mappedStatement.getConfiguration().getTypeHandlerRegistry();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        Paginator<Object> paginator = new Paginator();
        if (parameterMappings != null) {

            String sql = boundSql.getSql();
            for (ParameterMapping parameterMapping : parameterMappings) {
                if (parameterMapping.getMode() != ParameterMode.OUT) {
                    String propertyName = parameterMapping.getProperty();
                    Object value;
                    if (boundSql.hasAdditionalParameter(propertyName)) {
                        value = boundSql.getAdditionalParameter(propertyName);
                    } else if (params == null) {
                        value = null;
                    } else if (typeHandlerRegistry.hasTypeHandler(params.getClass())) {
                        value = params;
                    } else {
                        MetaObject metaObject = configuration.newMetaObject(params);
                        value = metaObject.getValue(propertyName);
                    }
                    /*if (parameterMapping.getProperty().endsWith("offset")){
                        offset = (Integer) value;
                    }
                    if(parameterMapping.getProperty().endsWith("itemsPerPage")) {
                        itemsPerPage = (Integer) value;
                    }*/
                    JdbcType jdbcType = parameterMapping.getJdbcType();
                    if (value == null && jdbcType == null) {
                        jdbcType = configuration.getJdbcTypeForNull();
                    }
                    sql = new replaceParameter(sql, value, jdbcType, parameterMapping.getJavaType()).getSql();
                }
            }
            final String[] limits = sql.split("(?i)LIMIT");
            final String limitval = Arrays.asList(limits).get(limits.length - 1);
            final String[] split = limitval.split(",");
            class replace{
                String replaceall(String val){
                   return val.replaceAll("'", "").replaceAll(" ", "").replaceAll("(\r\n|\r|\n|\n\r)","");
                }
            }
            if (split.length == 2) {
                Integer offset = Integer.parseInt(new replace().replaceall(split[0]));
                Integer itemsPerPage = Integer.parseInt(new replace().replaceall(split[1]));
                paginator.setItemsPerPage(itemsPerPage);
                paginator.setPage(offset >= 0 && itemsPerPage > 0 ? offset / itemsPerPage + 1 : 0);
            }
        }
        return paginator;
    }


    /**
     * 新加Mapper结构
     *
     * @param ms
     * @param newSqlSource
     * @return
     */
    private MappedStatement ProxyResultSetHandler(MappedStatement ms, SqlSource newSqlSource) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length > 0) {
            builder.keyProperty(ms.getKeyProperties()[0]);
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(newResultMap(ms.getResultMaps()));//ms.getResultMaps()
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());
        return builder.build();
    }

    /**
     * 构建统一的返回值
     */
    private List<ResultMap> newResultMap(List<ResultMap> lrm) {
        ResultMap resultMap = new ResultMap.Builder(null, lrm.size() > 0 ? lrm.get(0).getId() : "", Integer.class, new ArrayList<ResultMapping>()).build();
        List<ResultMap> list = new ArrayList<>();
        if (lrm.size() > 0) {
            list.add(lrm.get(0));
        }
        list.add(resultMap);
        return list;
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Proxy.newProxyInstance(Interceptor.class.getClassLoader(), target.getClass().getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    return intercept(new Invocation(target, method, args));
                }
            });
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {
    }

    public static class BoundSqlSqlSource implements SqlSource {
        private BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        @Override
        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }

}
