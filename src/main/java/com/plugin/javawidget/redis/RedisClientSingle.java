package com.plugin.javawidget.redis;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.plugin.json.Json;
import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.BitOP;
import redis.clients.jedis.BitPosParams;
import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.GeoRadiusResponse;
import redis.clients.jedis.GeoUnit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.ZParams;
import redis.clients.jedis.params.geo.GeoRadiusParam;
import redis.clients.jedis.params.sortedset.ZAddParams;
import redis.clients.jedis.params.sortedset.ZIncrByParams;

/**
 * Redis客户端-单机版
 * 
 *
 */
public class RedisClientSingle implements RedisClient {


	private class json extends Json {

		@Override
		protected String toJson(Object src) {
			return super.toJson(src);
		}

		@Override
		protected <T> T fromJson(String json, Type type) {
			return super.fromJson(json, type);
		}
	}

	private JedisPool jedisPool;

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }


    @Override
	public String set(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String result = jedis.set(key, value);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

    @Override
    public String set(String key, String value, String nxxx, String expx, long time) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String result = jedis.set(key, value, nxxx, expx, time);
            return result;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

	@Override
	public String get(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String result = jedis.get(key);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	/**
	 * 获取JSON并且转换为对象
	 * @param key
	 * @param classs
	 * @return
	 */
	@Override
	public <E> E get(String key,Class classs){
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String result = jedis.get(key);
			return new json().fromJson(result,classs);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long del(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long result = jedis.del(key);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long del(String key,List<String> keys, List<String> args) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Object result  = jedis.eval(key,keys,args);
			return (Long) result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long hset(String key, String field, String value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long result = jedis.hset(key, field, value);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public String hget(String key, String field) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String result = jedis.hget(key, field);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long hdel(String key, String... fields) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long result = jedis.hdel(key, fields);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long setnx(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long result = jedis.setnx(key, value);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long hsetnx(String key, String field, String value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long result = jedis.hsetnx(key, field, value);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long expire(String key, int seconds) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long result = jedis.expire(key, seconds);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long incr(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long result = jedis.incr(key);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long decr(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long result = jedis.decr(key);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Boolean exists(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Boolean result = jedis.exists(key);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long persist(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long result = jedis.persist(key);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long ttl(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long result = jedis.ttl(key);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long lpush(String key, String... strings) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long result = jedis.lpush(key, strings);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public String rpop(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String result = jedis.rpop(key);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long sadd(String key, String... strings) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long result = jedis.sadd(key, strings);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Set<String> smembers(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Set<String> result = jedis.smembers(key);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public List<String> lrange(String key, long start, long end) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			List<String> result = jedis.lrange(key, start, end);
			return result;
		} finally {
			if (jedis != null)
				jedis.close();
		}
	}

	@Override
	public Map<String, String> hgetAll(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Map<String, String> result = jedis.hgetAll(key);
			return result;
		} finally {
			if (jedis != null)
				jedis.close();
		}
	}

	@Override
	public Long geoadd(String key, double longitude, double latitude, String member) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long result = jedis.geoadd(key, longitude, latitude, member);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public List<GeoCoordinate> geopos(String key, String... members) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			List<GeoCoordinate> result = jedis.geopos(key, members);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Double geodist(String key, String member1, String member2) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Double result = jedis.geodist(key, member1, member2);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public List<GeoRadiusResponse> georadiusByMember(String key, String member, double radius, GeoUnit unit) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			List<GeoRadiusResponse> result = jedis.georadiusByMember(key, member, radius, unit);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Set<String> keys(String pattern) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Set<String> keys = jedis.keys(pattern);
			return keys;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long rpush(String key, String... strings) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long result = jedis.rpush(key, strings);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public String set(String key, String value, String nxxx) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String result = jedis.set(key, value, nxxx);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public String type(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String result = jedis.type(key);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long pexpire(String key, long milliseconds) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long result = jedis.pexpire(key, milliseconds);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long expireAt(String key, long unixTime) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long result = jedis.expireAt(key, unixTime);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long pexpireAt(String key, long millisecondsTimestamp) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long result = jedis.pexpireAt(key, millisecondsTimestamp);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long pttl(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long result = jedis.pttl(key);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Boolean setbit(String key, long offset, boolean value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Boolean result = jedis.setbit(key, offset, value);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Boolean setbit(String key, long offset, String value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Boolean result = jedis.setbit(key, offset, value);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Boolean getbit(String key, long offset) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Boolean result = jedis.getbit(key, offset);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long setrange(String key, long offset, String value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long result = jedis.setrange(key, offset, value);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public String getrange(String key, long startOffset, long endOffset) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String result = jedis.getrange(key, startOffset, endOffset);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public String getSet(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String result = jedis.getSet(key, value);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public String setex(String key, int seconds, String value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String result = jedis.setex(key, seconds, value);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	/**
	 *  将值 value 关联到 key ，并将 key 的过期时间设为 seconds (以秒为单位)
	 * @param key
	 * @param seconds (以秒为单位)
	 * @param value
	 * @return
	 */
	@Override
	public String setexObject(String key, int seconds, Object value){
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String result = jedis.setex(key, seconds, new json().toJson(value));
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}


	@Override
	public String psetex(String key, long milliseconds, String value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String result = jedis.psetex(key, milliseconds, value);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long decrBy(String key, long integer) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long result = jedis.decrBy(key, integer);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long incrBy(String key, long integer) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long result = jedis.incrBy(key, integer);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Double incrByFloat(String key, double value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Double result = jedis.incrByFloat(key, value);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long append(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long result = jedis.append(key, value);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public String substr(String key, int start, int end) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String result = jedis.substr(key, start, end);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public String hmset(String key, Map<String, String> hash) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String result = jedis.hmset(key, hash);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public List<String> hmget(String key, String... fields) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			List<String> result = jedis.hmget(key, fields);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long hincrBy(String key, String field, long value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long result = jedis.hincrBy(key, field, value);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Double hincrByFloat(String key, String field, double value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Double result = jedis.hincrByFloat(key, field, value);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Boolean hexists(String key, String field) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Boolean result = jedis.hexists(key, field);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long hlen(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long result = jedis.hlen(key);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Set<String> hkeys(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Set<String> result = jedis.hkeys(key);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public List<String> hvals(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			List<String> result = jedis.hvals(key);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long llen(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long result = jedis.llen(key);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public String ltrim(String key, long start, long end) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String result = jedis.ltrim(key, start, end);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public String lindex(String key, long index) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String result = jedis.lindex(key, index);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public String lset(String key, long index, String value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String result = jedis.lset(key, index, value);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long lrem(String key, long count, String value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long result = jedis.lrem(key, count, value);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public String lpop(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String result = jedis.lpop(key);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long srem(String key, String... member) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.srem(key, member);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public String spop(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.spop(key);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Set<String> spop(String key, long count) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.spop(key, count);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long scard(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.scard(key);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Boolean sismember(String key, String member) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.sismember(key, member);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public String srandmember(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.srandmember(key);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public List<String> srandmember(String key, int count) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.srandmember(key, count);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long strlen(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.strlen(key);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long zadd(String key, double score, String member) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.zadd(key, score, member);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long zadd(String key, double score, String member, ZAddParams params) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.zadd(key, score, member, params);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long zadd(String key, Map<String, Double> scoreMembers) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.zadd(key, scoreMembers);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long zadd(String key, Map<String, Double> scoreMembers, ZAddParams params) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.zadd(key, scoreMembers, params);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long zrem(String key, String... member) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.zrem(key, member);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Double zincrby(String key, double score, String member) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.zincrby(key, score, member);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Double zincrby(String key, double score, String member, ZIncrByParams params) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.zincrby(key, score, member, params);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long zrank(String key, String member) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.zrank(key, member);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long zrevrank(String key, String member) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.zrevrank(key, member);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Set<String> zrevrange(String key, long start, long end) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.zrevrange(key, start, end);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Set<Tuple> zrangeWithScores(String key, long start, long end) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.zrangeWithScores(key, start, end);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Set<Tuple> zrevrangeWithScores(String key, long start, long end) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.zrevrangeWithScores(key, start, end);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long zcard(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.zcard(key);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Double zscore(String key, String member) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.zscore(key, member);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public List<String> sort(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.sort(key);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public List<String> sort(String key, SortingParams sortingParameters) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.sort(key, sortingParameters);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long zcount(String key, double min, double max) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.zcount(key, min, max);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long zcount(String key, String min, String max) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.zcount(key, min, max);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Set<String> zrangeByScore(String key, double min, double max) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.zrangeByScore(key, min, max);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Set<String> zrangeByScore(String key, String min, String max) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.zrangeByScore(key, min, max);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Set<String> zrevrangeByScore(String key, double max, double min) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.zrangeByScore(key, min, max);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Set<String> zrangeByScore(String key, double min, double max, int offset, int count) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.zrangeByScore(key, min, max, offset, count);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Set<String> zrevrangeByScore(String key, String max, String min) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.zrangeByScore(key, min, max);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Set<String> zrangeByScore(String key, String min, String max, int offset, int count) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.zrangeByScore(key, min, max, offset, count);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.zrangeByScore(key, min, max, offset, count);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.zrangeByScoreWithScores(key, min, max);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.zrangeByScoreWithScores(key, min, max);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max, int offset, int count) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.zrangeByScoreWithScores(key, min, max, offset, count);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Set<String> zrevrangeByScore(String key, String max, String min, int offset, int count) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.zrevrangeByScore(key, max, min, offset, count);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.zrangeByScoreWithScores(key, min, max);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.zrangeByScoreWithScores(key, min, max);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max, int offset, int count) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.zrangeByScoreWithScores(key, min, max, offset, count);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.zrangeByScoreWithScores(key, min, max, offset, count);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min, int offset, int count) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.zrangeByScoreWithScores(key, min, max, offset, count);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long zremrangeByRank(String key, long start, long end) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.zremrangeByRank(key, start, end);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long zremrangeByScore(String key, double start, double end) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.zremrangeByScore(key, start, end);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long zremrangeByScore(String key, String start, String end) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.zremrangeByScore(key, start, end);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long zlexcount(String key, String min, String max) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.zlexcount(key, min, max);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Set<String> zrangeByLex(String key, String min, String max) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.zrangeByLex(key, min, max);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Set<String> zrangeByLex(String key, String min, String max, int offset, int count) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.zrangeByLex(key, min, max, offset, count);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Set<String> zrevrangeByLex(String key, String max, String min) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.zrevrangeByLex(key, max, min);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Set<String> zrevrangeByLex(String key, String max, String min, int offset, int count) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.zrevrangeByLex(key, max, min, offset, count);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long zremrangeByLex(String key, String min, String max) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.zremrangeByLex(key, min, max);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long linsert(String key, LIST_POSITION where, String pivot, String value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.linsert(key, where, pivot, value);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long lpushx(String key, String... string) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.lpushx(key, string);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long rpushx(String key, String... string) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.rpushx(key, string);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	@Deprecated
	public List<String> blpop(String arg) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.blpop(arg);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public List<String> blpop(int timeout, String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.blpop(timeout, key);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	@Deprecated
	public List<String> brpop(String arg) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.brpop(arg);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public List<String> brpop(int timeout, String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.brpop(timeout, key);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public String echo(String string) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.echo(string);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long move(String key, int dbIndex) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.move(key, dbIndex);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long bitcount(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.bitcount(key);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long bitcount(String key, long start, long end) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.bitcount(key, start, end);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long bitpos(String key, boolean value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.bitpos(key, value);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long bitpos(String key, boolean value, BitPosParams params) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.bitpos(key, value, params);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	@Deprecated
	public ScanResult<Entry<String, String>> hscan(String key, int cursor) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.hscan(key, cursor);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	@Deprecated
	public ScanResult<String> sscan(String key, int cursor) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.sscan(key, cursor);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	@Deprecated
	public ScanResult<Tuple> zscan(String key, int cursor) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.zscan(key, cursor);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public ScanResult<Entry<String, String>> hscan(String key, String cursor) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.hscan(key, cursor);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public ScanResult<Entry<String, String>> hscan(String key, String cursor, ScanParams params) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.hscan(key, cursor, params);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public ScanResult<String> sscan(String key, String cursor) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.sscan(key, cursor);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public ScanResult<String> sscan(String key, String cursor, ScanParams params) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.sscan(key, cursor, params);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public ScanResult<Tuple> zscan(String key, String cursor) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.zscan(key, cursor);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public ScanResult<Tuple> zscan(String key, String cursor, ScanParams params) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.zscan(key, cursor, params);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long pfadd(String key, String... elements) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.pfadd(key, elements);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public long pfcount(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.pfcount(key);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long geoadd(String key, Map<String, GeoCoordinate> memberCoordinateMap) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.geoadd(key, memberCoordinateMap);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Double geodist(String key, String member1, String member2, GeoUnit unit) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.geodist(key, member1, member2, unit);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public List<String> geohash(String key, String... members) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.geohash(key, members);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public List<GeoRadiusResponse> georadius(String key, double longitude, double latitude, double radius,
			GeoUnit unit) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.georadius(key, longitude, latitude, radius, unit);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public List<GeoRadiusResponse> georadius(String key, double longitude, double latitude, double radius, GeoUnit unit,
			GeoRadiusParam param) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.georadius(key, longitude, latitude, radius, unit, param);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public List<GeoRadiusResponse> georadiusByMember(String key, String member, double radius, GeoUnit unit,
			GeoRadiusParam param) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.georadiusByMember(key, member, radius, unit, param);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public List<Long> bitfield(String key, String... arguments) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.bitfield(key, arguments);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long del(String... keys) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.del(keys);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long exists(String... keys) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.exists(keys);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public List<String> blpop(int timeout, String... keys) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.blpop(timeout, keys);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public List<String> brpop(int timeout, String... keys) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.brpop(timeout, keys);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public List<String> blpop(String... args) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.blpop(args);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public List<String> brpop(String... args) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.brpop(args);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public List<String> mget(String... keys) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.mget(keys);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public String mset(String... keysvalues) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.mset(keysvalues);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long msetnx(String... keysvalues) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.msetnx(keysvalues);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public String rename(String oldkey, String newkey) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.rename(oldkey, newkey);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long renamenx(String oldkey, String newkey) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.renamenx(oldkey, newkey);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public String rpoplpush(String srckey, String dstkey) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.rpoplpush(srckey, dstkey);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Set<String> sdiff(String... keys) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.sdiff(keys);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long sdiffstore(String dstkey, String... keys) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.sdiffstore(dstkey, keys);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Set<String> sinter(String... keys) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.sinter(keys);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long sinterstore(String dstkey, String... keys) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.sinterstore(dstkey, keys);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long smove(String srckey, String dstkey, String member) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.smove(srckey, dstkey, member);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long sort(String key, SortingParams sortingParameters, String dstkey) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.sort(key, sortingParameters, dstkey);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long sort(String key, String dstkey) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.sort(key, dstkey);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Set<String> sunion(String... keys) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.sunion(keys);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long sunionstore(String dstkey, String... keys) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.sunionstore(dstkey, keys);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public String watch(String... keys) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.watch(keys);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public String unwatch() {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.unwatch();
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long zinterstore(String dstkey, String... sets) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.zinterstore(dstkey, sets);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long zinterstore(String dstkey, ZParams params, String... sets) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.zinterstore(dstkey, params, sets);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long zunionstore(String dstkey, String... sets) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.zunionstore(dstkey, sets);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long zunionstore(String dstkey, ZParams params, String... sets) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.zunionstore(dstkey, params, sets);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public String brpoplpush(String source, String destination, int timeout) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.brpoplpush(source, destination, timeout);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long publish(String channel, String message) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.publish(channel, message);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public void subscribe(JedisPubSub jedisPubSub, String... channels) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.subscribe(jedisPubSub, channels);
			;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}

	}

	@Override
	public void psubscribe(JedisPubSub jedisPubSub, String... patterns) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.psubscribe(jedisPubSub, patterns);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}

	}

	@Override
	public String randomKey() {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.randomKey();
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Long bitop(BitOP op, String destKey, String... srcKeys) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.bitop(op, destKey, srcKeys);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	@Deprecated
	public ScanResult<String> scan(int cursor) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.scan(cursor);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public ScanResult<String> scan(String cursor) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.scan(cursor);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public ScanResult<String> scan(String cursor, ScanParams params) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.scan(cursor, params);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public String pfmerge(String destkey, String... sourcekeys) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.pfmerge(destkey, sourcekeys);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public long pfcount(String... keys) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.pfcount(keys);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Set<String> zrange(String key, long start, long end) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.zrange(key,start,end);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}



}
