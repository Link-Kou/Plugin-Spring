package com.plugin.javawidgetTest.redis;

import com.linkkou.spring.redis.locality.LockResult;
import com.linkkou.spring.redis.locality.RedisLock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author lk
 * @version 1.0
 * @date 2019/2/13 20:43
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:/config/spring/spring-mvc.xml", "classpath*:/config/spring/spring-redis.xml"})
public class test {


    @Test
    public void RedisLock() {
        boolean b = RedisLock.lock("key", "key", (x) -> {
            return x;
        }, (x) -> {
            return false;
        }, (x) -> {
            return false;
        });
        boolean b2 = RedisLock.lock("key", "key", (x) -> {
            return x;
        }, (x) -> {
            return false;
        }, (x) -> {
            return false;
        });

        boolean b3 = RedisLock.lock("key", "key", new LockResult<Boolean>() {
            @Override
            public Boolean success() {
                return true;
            }

            @Override
            public Boolean failure() {
                return false;
            }

            @Override
            public Boolean unlock(boolean status) {
                return status;
            }
        });
        assertTrue("锁失败", b);
        assertFalse("未锁住", b2);
    }
}
