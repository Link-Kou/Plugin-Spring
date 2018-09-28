package com.plugin.javawidget.redis.aop;


import com.plugin.configproperty.Config;
import com.plugin.configproperty.ConfigUtils;
import com.plugin.configproperty.ConfigValue;
import com.plugin.javawidget.driven.converters.JsonResult;
import com.plugin.javawidget.enums.SystemMsgEnums;
import com.plugin.javawidget.redis.RedisClient;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;


/**
 * AOP锁，
 * <p>注意：存在不一定可以解锁，执行异常的时候解锁存在极低概率锁死60秒</p>
 * @author LK
 * @date 2018/4/27
 */
@Aspect
@Component
public class RedisLockAopImpl {


	private static Logger logger = LoggerFactory.getLogger("RedisLogger");

	@ConfigValue(@Value("${SystemMsgEnums.ERROR.REDISLOCKAOPIMPL.REDISLOCKAOP.msg}"))
	private transient Config<String> REDISLOCKAOP;

	@ConfigValue(@Value("${SystemMsgEnums.ERROR.REDISLOCKAOPIMPL.REDISLOCKLOCKAOP.msg}"))
	private transient Config<String> REDISLOCKLOCKAOP;

	private final static String LOCK_SUCCESS = "OK";

	@Autowired
	private RedisClient redisClient;

	private final static String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

	/**
	 * 环绕通知
	 **/
	@Around("@annotation(redisLock)")
	public Object around(ProceedingJoinPoint pjp, RedisLock redisLock) throws Throwable {
		Value msg = redisLock.value();
		String key = new ConfigUtils(msg.value()).getString();
		try {
			if (!LOCK_SUCCESS.equals(redisClient.set(key, LOCK_SUCCESS, "NX", "EX", 60L))) {
				return new JsonResult(SystemMsgEnums.OPS_REDIS_LOCK_DATA);
			}
		}catch (Exception e){
			e.printStackTrace();
			logger.error(REDISLOCKLOCKAOP.get()+e.getMessage());
			throw e;
		}
		try {
			return pjp.proceed();
		} catch (Exception e) {
			e.printStackTrace();
			redisClient.del(script, Collections.singletonList(key), Collections.singletonList(LOCK_SUCCESS));
			logger.error(REDISLOCKAOP.get()+e.getMessage());
			throw e;
		}
	}

}
