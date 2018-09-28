package com.plugin.javawidget.redis.aop;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

/**
 * 全局分布式锁
 */
//注解会在class字节码文件中存在，在运行时可以通过反射获取到
@Retention(RetentionPolicy.RUNTIME)
//定义注解的作用目标
@Target({ElementType.METHOD})
//说明该注解将被包含在javadoc中
@Documented
//最高优先级
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface RedisLock {
    /**
     * 转换的实体类名称
     * **/
    Value value();

}
