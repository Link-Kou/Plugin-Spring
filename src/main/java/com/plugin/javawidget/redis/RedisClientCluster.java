package com.plugin.javawidget.redis;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


import com.google.gson.JsonIOException;

import com.plugin.json.Json;
import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.BitOP;
import redis.clients.jedis.BitPosParams;
import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.GeoRadiusResponse;
import redis.clients.jedis.GeoUnit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
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
 * Redis客户端-集群版
 */
public class RedisClientCluster implements RedisClient {

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


    private JedisCluster jedisCluster;

    public void setJedisCluster(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }

    @Override
    public String set(String key, String value) {
        String result = jedisCluster.set(key, value);
        return result;
    }

    @Override
    public String get(String key) {
        String result = jedisCluster.get(key);
        return result;
    }

    /**
     * 获取JSON并且转换为对象
     * @param key
     * @param classs
     * @return
     */
    @Override
    public <E> E get(String key,Class classs){
        String result = jedisCluster.get(key);
        return new json().fromJson(result,classs);
    }

    @Override
    public Long del(String key) {
        Long result = jedisCluster.del(key);
        return result;
    }

    @Override
    public Long del(String key,List<String> keys, List<String> args) {
        Object result  = jedisCluster.eval(key,keys,args);
        return (Long) result;
    }


    @Override
    public Long hset(String key, String field, String value) {
        Long result = jedisCluster.hsetnx(key, field, value);
        return result;
    }

    @Override
    public String hget(String key, String field) {
        String result = jedisCluster.hget(key, field);
        return result;
    }

    @Override
    public Long hdel(String key, String... fields) {
        Long result = jedisCluster.hdel(key, fields);
        return result;
    }

    @Override
    public Long setnx(String key, String value) {
        Long result = jedisCluster.setnx(key, value);
        return result;
    }

    @Override
    public Long hsetnx(String key, String field, String value) {
        Long result = jedisCluster.hsetnx(key, field, value);
        return result;
    }

    @Override
    public Long expire(String key, int seconds) {
        Long result = jedisCluster.expire(key, seconds);
        return result;
    }

    @Override
    public Long incr(String key) {
        Long result = jedisCluster.incr(key);
        return result;
    }

    @Override
    public Long decr(String key) {
        Long result = jedisCluster.decr(key);
        return result;
    }

    @Override
    public Boolean exists(String key) {
        Boolean result = jedisCluster.exists(key);
        return result;
    }

    @Override
    public Long persist(String key) {
        Long result = jedisCluster.persist(key);
        return result;
    }

    @Override
    public Long ttl(String key) {
        Long result = jedisCluster.ttl(key);
        return result;
    }

    @Override
    public Long lpush(String key, String... strings) {
        Long result = jedisCluster.lpush(key, strings);
        return result;
    }

    @Override
    public String rpop(String key) {
        String result = jedisCluster.rpop(key);
        return result;
    }

    @Override
    public Long sadd(String key, String... strings) {
        Long result = jedisCluster.sadd(key, strings);
        return result;
    }

    @Override
    public Set<String> smembers(String key) {
        Set<String> result = jedisCluster.smembers(key);
        return result;
    }

    @Override
    public List<String> lrange(String key, long start, long end) {
        List<String> result = jedisCluster.lrange(key, start, end);
        return result;
    }

    @Override
    public Map<String, String> hgetAll(String key) {
        Map<String, String> result = jedisCluster.hgetAll(key);
        return result;
    }

    @Override
    public Long geoadd(String key, double longitude, double latitude, String member) {
        Long result = jedisCluster.geoadd(key, longitude, latitude, member);
        return result;
    }

    @Override
    public List<GeoCoordinate> geopos(String key, String... members) {
        List<GeoCoordinate> result = jedisCluster.geopos(key, members);
        return result;
    }

    @Override
    public Double geodist(String key, String member1, String member2) {
        Double result = jedisCluster.geodist(key, member1, member2);
        return result;
    }

    @Override
    public List<GeoRadiusResponse> georadiusByMember(String key, String member, double radius, GeoUnit unit) {
        List<GeoRadiusResponse> result = jedisCluster.georadiusByMember(key, member, radius, unit);
        return result;
    }

    @Override
    public Set<String> keys(String pattern) {
        Map<String, JedisPool> clusterNodes = jedisCluster.getClusterNodes();
        Collection<JedisPool> nodes = clusterNodes.values();
        Iterator<JedisPool> iterator = nodes.iterator();
        Set<String> keys = new HashSet<>();
        while (iterator.hasNext()) {
            Jedis jedis = null;
            try {
                JedisPool jedisPool = (JedisPool) iterator.next();
                jedis = jedisPool.getResource();
                keys.addAll(jedis.keys(pattern));
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
        }
        return keys;
    }

    @Override
    public Set<String> zrange(String key, long start, long end) {
        Set<String> zrange = jedisCluster.zrange(key, start, end);
        return zrange;
    }

    @Override
    public Long rpush(String key, String... strings) {
        Long result = jedisCluster.rpush(key, strings);
        return result;
    }

    @Override
    public String set(String key, String value, String nxxx, String expx, long time) {
        return jedisCluster.set(key, value, nxxx, expx, time);

    }

    @Override
    @Deprecated
    public String set(String key, String value, String nxxx) {
        return jedisCluster.set(key, value, nxxx);

    }

    @Override
    public String type(String key) {
        return jedisCluster.type(key);
    }

    @Override
    public Long pexpire(String key, long milliseconds) {
        return jedisCluster.pexpire(key, milliseconds);

    }

    @Override
    public Long expireAt(String key, long unixTime) {
        return jedisCluster.expireAt(key, unixTime);
    }

    @Override
    public Long pexpireAt(String key, long millisecondsTimestamp) {
        return jedisCluster.pexpireAt(key, millisecondsTimestamp);
    }

    @Override
    public Long pttl(String key) {
        return jedisCluster.pttl(key);

    }

    @Override
    public Boolean setbit(String key, long offset, boolean value) {
        return jedisCluster.setbit(key, offset, value);

    }

    @Override
    public Boolean setbit(String key, long offset, String value) {
        return jedisCluster.setbit(key, offset, value);
    }

    @Override
    public Boolean getbit(String key, long offset) {
        return jedisCluster.getbit(key, offset);
    }

    @Override
    public Long setrange(String key, long offset, String value) {
        return jedisCluster.setrange(key, offset, value);
    }

    @Override
    public String getrange(String key, long startOffset, long endOffset) {
        return jedisCluster.getrange(key, startOffset, endOffset);
    }

    @Override
    public String getSet(String key, String value) {
        return jedisCluster.getSet(key, value);
    }

    @Override
    public String setex(String key, int seconds, String value) {
        return jedisCluster.setex(key, seconds, value);
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
        return jedisCluster.setex(key, seconds, new json().toJson(value));
    }

    @Override
    public String psetex(String key, long milliseconds, String value) {
        return jedisCluster.psetex(key, milliseconds, value);
    }

    @Override
    public Long decrBy(String key, long integer) {
        return jedisCluster.decrBy(key, integer);
    }

    @Override
    public Long incrBy(String key, long integer) {
        return jedisCluster.incrBy(key, integer);
    }

    @Override
    public Double incrByFloat(String key, double value) {
        return jedisCluster.incrByFloat(key, value);
    }

    @Override
    public Long append(String key, String value) {
        return jedisCluster.append(key, value);
    }

    @Override
    public String substr(String key, int start, int end) {
        return jedisCluster.substr(key, start, end);
    }

    @Override
    public String hmset(String key, Map<String, String> hash) {
        return jedisCluster.hmset(key, hash);
    }

    @Override
    public List<String> hmget(String key, String... fields) {
        return jedisCluster.hmget(key, fields);
    }

    @Override
    public Long hincrBy(String key, String field, long value) {
        return jedisCluster.hincrBy(key, field, value);
    }

    @Override
    public Double hincrByFloat(String key, String field, double value) {
        return jedisCluster.hincrByFloat(key, field, value);
    }

    @Override
    public Boolean hexists(String key, String field) {
        return jedisCluster.hexists(key, field);
    }

    @Override
    public Long hlen(String key) {
        return jedisCluster.hlen(key);
    }

    @Override
    public Set<String> hkeys(String key) {
        return jedisCluster.hkeys(key);
    }

    @Override
    public List<String> hvals(String key) {
        return jedisCluster.hvals(key);
    }

    @Override
    public Long llen(String key) {
        return jedisCluster.llen(key);
    }

    @Override
    public String ltrim(String key, long start, long end) {
        return jedisCluster.ltrim(key, start, end);
    }

    @Override
    public String lindex(String key, long index) {
        return jedisCluster.lindex(key, index);
    }

    @Override
    public String lset(String key, long index, String value) {
        return jedisCluster.lset(key, index, value);
    }

    @Override
    public Long lrem(String key, long count, String value) {
        return jedisCluster.lrem(key, count, value);
    }

    @Override
    public String lpop(String key) {
        return jedisCluster.lpop(key);
    }

    @Override
    public Long srem(String key, String... member) {
        return jedisCluster.srem(key, member);
    }

    @Override
    public String spop(String key) {
        return jedisCluster.spop(key);
    }

    @Override
    public Set<String> spop(String key, long count) {
        return jedisCluster.spop(key, count);
    }

    @Override
    public Long scard(String key) {
        return jedisCluster.scard(key);
    }

    @Override
    public Boolean sismember(String key, String member) {
        return jedisCluster.sismember(key, member);
    }

    @Override
    public String srandmember(String key) {
        return jedisCluster.srandmember(key);
    }

    @Override
    public List<String> srandmember(String key, int count) {
        return jedisCluster.srandmember(key, count);
    }

    @Override
    public Long strlen(String key) {
        return jedisCluster.strlen(key);
    }

    @Override
    public Long zadd(String key, double score, String member) {
        return jedisCluster.zadd(key, score, member);
    }

    @Override
    public Long zadd(String key, double score, String member, ZAddParams params) {
        return jedisCluster.zadd(key, score, member, params);
    }

    @Override
    public Long zadd(String key, Map<String, Double> scoreMembers) {
        return jedisCluster.zadd(key, scoreMembers);
    }

    @Override
    public Long zadd(String key, Map<String, Double> scoreMembers, ZAddParams params) {
        return jedisCluster.zadd(key, scoreMembers, params);
    }

    @Override
    public Long zrem(String key, String... member) {
        return jedisCluster.zrem(key, member);
    }

    @Override
    public Double zincrby(String key, double score, String member) {
        return jedisCluster.zincrby(key, score, member);
    }

    @Override
    public Double zincrby(String key, double score, String member, ZIncrByParams params) {
        return jedisCluster.zincrby(key, score, member, params);
    }

    @Override
    public Long zrank(String key, String member) {
        return jedisCluster.zrank(key, member);
    }

    @Override
    public Long zrevrank(String key, String member) {
        return jedisCluster.zrevrank(key, member);
    }

    @Override
    public Set<String> zrevrange(String key, long start, long end) {
        return jedisCluster.zrevrange(key, start, end);
    }

    @Override
    public Set<Tuple> zrangeWithScores(String key, long start, long end) {
        return jedisCluster.zrangeWithScores(key, start, end);
    }

    @Override
    public Set<Tuple> zrevrangeWithScores(String key, long start, long end) {
        return jedisCluster.zrevrangeWithScores(key, start, end);
    }

    @Override
    public Long zcard(String key) {
        return jedisCluster.zcard(key);
    }

    @Override
    public Double zscore(String key, String member) {
        return jedisCluster.zscore(key, member);
    }

    @Override
    public List<String> sort(String key) {
        return jedisCluster.sort(key);
    }

    @Override
    public List<String> sort(String key, SortingParams sortingParameters) {
        return jedisCluster.sort(key, sortingParameters);

    }

    @Override
    public Long zcount(String key, double min, double max) {
        return jedisCluster.zcount(key, min, max);
    }

    @Override
    public Long zcount(String key, String min, String max) {
        return jedisCluster.zcount(key, min, max);
    }

    @Override
    public Set<String> zrangeByScore(String key, double min, double max) {
        return jedisCluster.zrangeByScore(key, min, max);
    }

    @Override
    public Set<String> zrangeByScore(String key, String min, String max) {
        return jedisCluster.zrangeByScore(key, min, max);
    }

    @Override
    public Set<String> zrevrangeByScore(String key, double max, double min) {
        return jedisCluster.zrevrangeByScore(key, max, min);
    }

    @Override
    public Set<String> zrangeByScore(String key, double min, double max, int offset, int count) {
        return jedisCluster.zrangeByScore(key, min, max, offset, count);
    }

    @Override
    public Set<String> zrevrangeByScore(String key, String max, String min) {
        return jedisCluster.zrevrangeByScore(key, max, min);
    }

    @Override
    public Set<String> zrangeByScore(String key, String min, String max, int offset, int count) {
        return jedisCluster.zrangeByScore(key, min, max, offset, count);
    }

    @Override
    public Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count) {
        return jedisCluster.zrevrangeByScore(key, max, min, offset, count);
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
        return jedisCluster.zrangeByScoreWithScores(key, min, max);
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min) {
        return jedisCluster.zrevrangeByScoreWithScores(key, max, min);
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max, int offset, int count) {
        return jedisCluster.zrangeByScoreWithScores(key, min, max, offset, count);
    }

    @Override
    public Set<String> zrevrangeByScore(String key, String max, String min, int offset, int count) {
        return jedisCluster.zrevrangeByScore(key, max, min, offset, count);
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max) {
        return jedisCluster.zrangeByScoreWithScores(key, min, max);

    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min) {
        return jedisCluster.zrevrangeByScoreWithScores(key, max, min);

    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max, int offset, int count) {
        return jedisCluster.zrangeByScoreWithScores(key, min, max, offset, count);
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count) {
        return jedisCluster.zrevrangeByScoreWithScores(key, max, min, offset, count);
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min, int offset, int count) {
        return jedisCluster.zrevrangeByScoreWithScores(key, max, min, offset, count);
    }

    @Override
    public Long zremrangeByRank(String key, long start, long end) {
        return jedisCluster.zremrangeByRank(key, start, end);
    }

    @Override
    public Long zremrangeByScore(String key, double start, double end) {
        return jedisCluster.zremrangeByScore(key, start, end);
    }

    @Override
    public Long zremrangeByScore(String key, String start, String end) {
        return jedisCluster.zremrangeByScore(key, start, end);
    }

    @Override
    public Long zlexcount(String key, String min, String max) {
        return jedisCluster.zlexcount(key, min, max);
    }

    @Override
    public Set<String> zrangeByLex(String key, String min, String max) {
        return jedisCluster.zrangeByLex(key, min, max);
    }

    @Override
    public Set<String> zrangeByLex(String key, String min, String max, int offset, int count) {
        return jedisCluster.zrangeByLex(key, min, max, offset, count);
    }

    @Override
    public Set<String> zrevrangeByLex(String key, String max, String min) {
        return jedisCluster.zrevrangeByLex(key, max, min);
    }

    @Override
    public Set<String> zrevrangeByLex(String key, String max, String min, int offset, int count) {
        return jedisCluster.zrevrangeByLex(key, max, min, offset, count);
    }

    @Override
    public Long zremrangeByLex(String key, String min, String max) {
        return jedisCluster.zremrangeByLex(key, min, max);
    }

    @Override
    public Long linsert(String key, LIST_POSITION where, String pivot, String value) {
        return jedisCluster.linsert(key, where, pivot, value);
    }

    @Override
    public Long lpushx(String key, String... string) {
        return jedisCluster.lpushx(key, string);
    }

    @Override
    public Long rpushx(String key, String... string) {
        return jedisCluster.rpushx(key, string);
    }

    @Override
    @Deprecated
    public List<String> blpop(String arg) {
        return jedisCluster.blpop(arg);
    }

    @Override
    public List<String> blpop(int timeout, String key) {
        return jedisCluster.blpop(timeout, key);
    }

    @Override
    @Deprecated
    public List<String> brpop(String arg) {
        return jedisCluster.blpop(arg);

    }

    @Override
    @Deprecated
    public List<String> brpop(int timeout, String key) {
        return jedisCluster.brpop(timeout, key);

    }

    @Override
    public String echo(String string) {
        return jedisCluster.echo(string);
    }

    @Override
    @Deprecated
    public Long move(String key, int dbIndex) {
        return jedisCluster.move(key, dbIndex);

    }

    @Override
    public Long bitcount(String key) {
        return jedisCluster.bitcount(key);
    }

    @Override
    public Long bitcount(String key, long start, long end) {
        return jedisCluster.bitcount(key, start, end);
    }

    @Override
    public Long bitpos(String key, boolean value) {
        return jedisCluster.bitpos(key, value);
    }

    @Override
    public Long bitpos(String key, boolean value, BitPosParams params) {
        return jedisCluster.bitpos(key, value, params);
    }

    @Override
    @Deprecated
    public ScanResult<Entry<String, String>> hscan(String key, int cursor) {
        return jedisCluster.hscan(key, cursor);
    }

    @Override
    @Deprecated
    public ScanResult<String> sscan(String key, int cursor) {
        return jedisCluster.sscan(key, cursor);
    }

    @Override
    @Deprecated
    public ScanResult<Tuple> zscan(String key, int cursor) {
        return jedisCluster.zscan(key, cursor);
    }

    @Override
    public ScanResult<Entry<String, String>> hscan(String key, String cursor) {
        return jedisCluster.hscan(key, cursor);
    }

    @Override
    public ScanResult<Entry<String, String>> hscan(String key, String cursor, ScanParams params) {
        return jedisCluster.hscan(key, cursor, params);
    }

    @Override
    public ScanResult<String> sscan(String key, String cursor) {
        return jedisCluster.sscan(key, cursor);
    }

    @Override
    public ScanResult<String> sscan(String key, String cursor, ScanParams params) {
        return jedisCluster.sscan(key, cursor, params);
    }

    @Override
    public ScanResult<Tuple> zscan(String key, String cursor) {
        return jedisCluster.zscan(key, cursor);

    }

    @Override
    public ScanResult<Tuple> zscan(String key, String cursor, ScanParams params) {
        return jedisCluster.zscan(key, cursor, params);
    }

    @Override
    public Long pfadd(String key, String... elements) {
        return jedisCluster.pfadd(key, elements);
    }

    @Override
    public long pfcount(String key) {
        return jedisCluster.pfcount(key);
    }

    @Override
    public Long geoadd(String key, Map<String, GeoCoordinate> memberCoordinateMap) {
        return jedisCluster.geoadd(key, memberCoordinateMap);
    }

    @Override
    public Double geodist(String key, String member1, String member2, GeoUnit unit) {
        return jedisCluster.geodist(key, member1, member2, unit);
    }

    @Override
    public List<String> geohash(String key, String... members) {
        return jedisCluster.geohash(key, members);
    }

    @Override
    public List<GeoRadiusResponse> georadius(String key, double longitude, double latitude, double radius,
                                             GeoUnit unit) {
        return jedisCluster.georadius(key, longitude, latitude, radius, unit);
    }

    @Override
    public List<GeoRadiusResponse> georadius(String key, double longitude, double latitude, double radius, GeoUnit unit,
                                             GeoRadiusParam param) {
        return jedisCluster.georadius(key, longitude, latitude, radius, unit, param);

    }

    @Override
    public List<GeoRadiusResponse> georadiusByMember(String key, String member, double radius, GeoUnit unit,
                                                     GeoRadiusParam param) {
        return jedisCluster.georadiusByMember(key, member, radius, unit, param);
    }

    @Override
    public List<Long> bitfield(String key, String... arguments) {
        return jedisCluster.bitfield(key, arguments);
    }

    @Override
    public Long exists(String... keys) {
        return jedisCluster.exists(keys);
    }

    @Override
    public Long del(String... keys) {
        return jedisCluster.del(keys);

    }

    @Override
    public List<String> blpop(int timeout, String... keys) {
        return jedisCluster.blpop(timeout, keys);
    }

    @Override
    public List<String> brpop(int timeout, String... keys) {
        return jedisCluster.brpop(timeout, keys);
    }

    @Override
    public List<String> mget(String... keys) {
        return jedisCluster.mget(keys);
    }

    @Override
    public String mset(String... keysvalues) {
        return jedisCluster.mset(keysvalues);
    }

    @Override
    public Long msetnx(String... keysvalues) {
        return jedisCluster.msetnx(keysvalues);
    }

    @Override
    public String rename(String oldkey, String newkey) {
        return jedisCluster.rename(oldkey, newkey);
    }

    @Override
    public Long renamenx(String oldkey, String newkey) {
        return jedisCluster.renamenx(oldkey, newkey);
    }

    @Override
    public String rpoplpush(String srckey, String dstkey) {
        return jedisCluster.rpoplpush(srckey, dstkey);
    }

    @Override
    public Set<String> sdiff(String... keys) {
        return jedisCluster.sdiff(keys);
    }

    @Override
    public Long sdiffstore(String dstkey, String... keys) {
        return jedisCluster.sdiffstore(dstkey, keys);
    }

    @Override
    public Set<String> sinter(String... keys) {
        return jedisCluster.sinter(keys);
    }

    @Override
    public Long sinterstore(String dstkey, String... keys) {
        return jedisCluster.sinterstore(dstkey, keys);
    }

    @Override
    public Long smove(String srckey, String dstkey, String member) {
        return jedisCluster.smove(srckey, dstkey, member);
    }

    @Override
    public Long sort(String key, SortingParams sortingParameters, String dstkey) {
        return jedisCluster.sort(key, sortingParameters, dstkey);
    }

    @Override
    public Long sort(String key, String dstkey) {
        return jedisCluster.sort(key, dstkey);
    }

    @Override
    public Set<String> sunion(String... keys) {
        return jedisCluster.sunion(keys);
    }

    @Override
    public Long sunionstore(String dstkey, String... keys) {
        return jedisCluster.sunionstore(dstkey, keys);
    }

    @Override
    public Long zinterstore(String dstkey, String... sets) {
        return jedisCluster.zinterstore(dstkey, sets);
    }

    @Override
    public Long zinterstore(String dstkey, ZParams params, String... sets) {
        return jedisCluster.zinterstore(dstkey, params, sets);
    }

    @Override
    public Long zunionstore(String dstkey, String... sets) {
        return jedisCluster.zunionstore(dstkey, sets);

    }

    @Override
    public Long zunionstore(String dstkey, ZParams params, String... sets) {
        return jedisCluster.zunionstore(dstkey, params, sets);
    }

    @Override
    public String brpoplpush(String source, String destination, int timeout) {
        return jedisCluster.brpoplpush(source, destination, timeout);
    }

    @Override
    public Long publish(String channel, String message) {
        return jedisCluster.publish(channel, message);
    }

    @Override
    public void subscribe(JedisPubSub jedisPubSub, String... channels) {
        jedisCluster.subscribe(jedisPubSub, channels);
    }

    @Override
    public void psubscribe(JedisPubSub jedisPubSub, String... patterns) {
        jedisCluster.psubscribe(jedisPubSub, patterns);
    }

    @Override
    public Long bitop(BitOP op, String destKey, String... srcKeys) {
        return jedisCluster.bitop(op, destKey, srcKeys);
    }

    @Override
    public String pfmerge(String destkey, String... sourcekeys) {
        return jedisCluster.pfmerge(destkey, sourcekeys);
    }

    @Override
    public long pfcount(String... keys) {
        return jedisCluster.pfcount(keys);
    }

    @Override
    public ScanResult<String> scan(String cursor, ScanParams params) {
        return jedisCluster.scan(cursor, params);
    }

    @Override
    public List<String> blpop(String... args) {
        return jedisCluster.blpop(1000, args);
    }

    @Override
    public List<String> brpop(String... args) {
        return jedisCluster.brpop(1000, args);
    }

    @Override
    @Deprecated
    public String watch(String... keys) {
        return null;
    }

    @Override
    @Deprecated
    public String unwatch() {
        return null;
    }

    @Override
    @Deprecated
    public String randomKey() {
        return null;
    }

    @Override
    @Deprecated
    public ScanResult<String> scan(int cursor) {
        return null;
    }

    @Override
    @Deprecated
    public ScanResult<String> scan(String cursor) {
        return null;
    }


}
