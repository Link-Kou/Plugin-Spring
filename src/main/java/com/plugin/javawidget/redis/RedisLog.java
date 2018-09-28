package com.plugin.javawidget.redis;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

/**
 *
 * 监控redis日志
 * 开启AspectJ 自动代理模式,如果不填proxyTargetClass=true，默认为false
 * @EnableAspectJAutoProxy(proxyTargetClass=true)
 */
@Aspect
@Component
public class RedisLog {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisLog.class);

    /**
     * AOP执行的方法
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.plugin.javawidget.redis.RedisClient+.*(..))")
    public Object logPrint(ProceedingJoinPoint pjp) throws Throwable {
        //执行
        Object proceed = pjp.proceed();
        Object[] params = pjp.getArgs();
        StringBuffer stringBuffer = new StringBuffer();
        if (params != null && params.length > 0) {
            for (Object object : params) {
                if (object instanceof String[]) {
                    String[] param = (String[]) object;
                    if (param != null && param.length > 0) {
                        stringBuffer.append("[");
                        for (Object object1 : param) {
                            stringBuffer.append(object1);
                            stringBuffer.append(",");
                        }
                        stringBuffer.replace(stringBuffer.lastIndexOf(",") , stringBuffer.lastIndexOf(",")+1, "");
                        stringBuffer.append("]");
                    }
                } else {
                    stringBuffer.append(object);
                    stringBuffer.append(",");
                }
            }
        }
        if (stringBuffer!=null&&"".equals(stringBuffer.toString().trim())&&stringBuffer.length()-stringBuffer.lastIndexOf(",")==1) {
            if (stringBuffer.lastIndexOf(",")!=-1) {
            	stringBuffer.replace(stringBuffer.lastIndexOf(",") , stringBuffer.lastIndexOf(",")+1, "");
			}
        }
        LOGGER.info("djcpsRedisLogger\ndjcpsRedisLogger==> Redis_Preparing: {} \ndjcpsRedisLogger==> Redis_Parameters: {} \ndjcpsRedisLogger<== Redis_Result: {}" ,pjp.getSignature(),stringBuffer.toString(), proceed);
        return proceed;
    }

}
