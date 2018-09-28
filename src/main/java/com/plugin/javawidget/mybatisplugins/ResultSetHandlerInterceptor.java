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


import com.plugin.javawidget.mybatisplugins.Type.MyGsonEnum;
import com.plugin.javawidget.paging.Paginator;
import com.plugin.json.serializer.GsonEnum;
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

import java.lang.reflect.*;
import java.sql.Connection;
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
                @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class}),
                @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        }
)
//@SuppressWarnings()
public class ResultSetHandlerInterceptor implements Interceptor {

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
                        //final MappedStatement mappedStatement = (MappedStatement) queryArgs[0];
                        final Object proceed = invocation.proceed();
                        if (proceed instanceof ArrayList) {
                            final ArrayList proceed1 = (ArrayList) proceed;
                            proceed1.stream().forEach((x) -> {
                                Field[] fields = x.getClass().getDeclaredFields();
                                for (Field f : fields) {
                                    f.setAccessible(true);
                                    if (f.getType().equals(GsonEnum.class)) {
                                        if (null != f.getGenericType()) {
                                            try {
                                                final Object o = f.get(x);
                                                if(MyGsonEnum.class.equals(o.getClass())){
                                                    final MyGsonEnum o1 = (MyGsonEnum) o;
                                                    final ParameterizedTypeImpl genericType = (ParameterizedTypeImpl) f.getGenericType();
                                                    for (Type actualTypeArgument : genericType.getActualTypeArguments()) {
                                                        final Class<?> aClass = Class.forName(actualTypeArgument.getTypeName());
                                                        final Method[] methods = aClass.getMethods();
                                                        for (Method method : methods) {
                                                            if ("parse".equals(method.getName())) {
                                                                final Object invoke = method.invoke(null, o1.getValue());
                                                                f.set(x,invoke);
                                                            }
                                                        }
                                                    }
                                                }
                                            } catch (IllegalAccessException | ClassNotFoundException | InvocationTargetException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            });
                        }
                        return proceed;
                    }
                }
            }
        }

        return invocation.proceed();
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


}
