package com.plugin.javawidget.redis;

import redis.clients.jedis.*;
import redis.clients.jedis.params.geo.GeoRadiusParam;
import redis.clients.jedis.params.sortedset.ZAddParams;
import redis.clients.jedis.params.sortedset.ZIncrByParams;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Redis 统一使用，使用人忽略集群还是单个机器
 * <p/>
 * 在配置中完成切换
 *
 * @author LK
 * @version 1.0
 * @date 2017-12-22 20:26
 */
public interface RedisClient {

    String set(String key, String value);

    /**
     * 将字符串值 value 关联到 key
     * @param key
     * @param value
     * @param nxxx NX(不存在操作) XX(存在操作)
     * @param expx EX(秒) PX(毫秒)
     * @param time 过期时间 秒
     * @return
     */
    String set(String key, String value, String nxxx, String expx, long time);

    /**
     * 获取key
     * @param key 名称
     * @return 无返回null 有返回值
     */
    String get(String key);

    /**
     * 获取JSON并且转换为对象
     * @param key
     * @param classs
     * @return
     */
    <E> E get(String key,Class classs);


    Long del(String key);
    /**
     * 分布式安全锁
     * @param key
     * @param
     * @return
     */
    Long del(String key,List<String> keys, List<String> args);

    Long hset(String key, String field, String value);


    String hget(String key, String field);


    Long hdel(String key, String... fields);


    Long setnx(String key, String value);


    Long hsetnx(String key, String field, String value);


    /**
     * 在 Redis 2.4 版本中，过期时间的延迟在 1 秒钟之内 —— 也即是，就算 key 已经过期，但它还是可能在过期之后一秒钟之内被访问到，而在新的 Redis 2.6 版本中，延迟被降低到 1 毫秒之内
     * @param key
     * @param seconds
     * @return 设置成功返回 1 。当 key 不存在或者不能为 key 设置生存时间时(比如在低于 2.1.3 版本的 Redis 中你尝试更新 key 的生存时间)，返回 0 。

     */
    Long expire(String key, int seconds);


    Long incr(String key);


    Long decr(String key);


    Boolean exists(String key);


    Long persist(String key);


    Long ttl(String key);


    Long lpush(String key, String... strings);


    String rpop(String key);


    Long sadd(String key, String... strings);


    Set<String> smembers(String key);


    List<String> lrange(String key, long start, long end);


    Map<String, String> hgetAll(String key);


    Long geoadd(String key, double longitude, double latitude, String member);


    List<GeoCoordinate> geopos(String key, String... members);


    Double geodist(String key, String member1, String member2);


    List<GeoRadiusResponse> georadiusByMember(String key, String member, double radius, GeoUnit unit);


    Set<String> keys(String pattern);

    Long rpush(String key, String... strings);

    String set(String key, String value, String nxxx);


    String type(String key);


    Long pexpire(String key, long milliseconds);


    Long expireAt(String key, long unixTime);

    Long pexpireAt(String key, long millisecondsTimestamp);


    Long pttl(String key);


    Boolean setbit(String key, long offset, boolean value);


    Boolean setbit(String key, long offset, String value);


    Boolean getbit(String key, long offset);


    Long setrange(String key, long offset, String value);


    String getrange(String key, long startOffset, long endOffset);


    String getSet(String key, String value);

    /**
     *  将值 value 关联到 key ，并将 key 的过期时间设为 seconds (以秒为单位)
     * @param key
     * @param seconds (以秒为单位)
     * @param value
     * @return
     */
    String setex(String key, int seconds, String value);

    /**
     *  将值 value 关联到 key ，并将 key 的过期时间设为 seconds (以秒为单位)
     * @param key
     * @param seconds (以秒为单位)
     * @param value
     * @return
     */
    String setexObject(String key, int seconds, Object value);


    String psetex(String key, long milliseconds, String value);


    Long decrBy(String key, long integer);


    Long incrBy(String key, long integer);


    Double incrByFloat(String key, double value);


    Long append(String key, String value);


    String substr(String key, int start, int end);


    String hmset(String key, Map<String, String> hash);


    List<String> hmget(String key, String... fields);


    Long hincrBy(String key, String field, long value);


    Double hincrByFloat(String key, String field, double value);


    Boolean hexists(String key, String field);


    Long hlen(String key);


    Set<String> hkeys(String key);


    List<String> hvals(String key);


    Long llen(String key);


    String ltrim(String key, long start, long end);


    String lindex(String key, long index);


    String lset(String key, long index, String value);


    Long lrem(String key, long count, String value);


    String lpop(String key);


    Long srem(String key, String... member);


    String spop(String key);


    Set<String> spop(String key, long count);


    Long scard(String key);


    Boolean sismember(String key, String member);


    String srandmember(String key);


    List<String> srandmember(String key, int count);


    Long strlen(String key);


    Long zadd(String key, double score, String member);


    Long zadd(String key, double score, String member, ZAddParams params);


    Long zadd(String key, Map<String, Double> scoreMembers);


    Long zadd(String key, Map<String, Double> scoreMembers, ZAddParams params);


    Long zrem(String key, String... member);


    Double zincrby(String key, double score, String member);


    Double zincrby(String key, double score, String member, ZIncrByParams params);


    Long zrank(String key, String member);


    Long zrevrank(String key, String member);


    Set<String> zrevrange(String key, long start, long end);


    Set<Tuple> zrangeWithScores(String key, long start, long end);


    Set<Tuple> zrevrangeWithScores(String key, long start, long end);

    Long zcard(String key);


    Double zscore(String key, String member);


    List<String> sort(String key);


    List<String> sort(String key, SortingParams sortingParameters);


    Long zcount(String key, double min, double max);


    Long zcount(String key, String min, String max);


    Set<String> zrangeByScore(String key, double min, double max);


    Set<String> zrangeByScore(String key, String min, String max);


    Set<String> zrevrangeByScore(String key, double max, double min);


    Set<String> zrangeByScore(String key, double min, double max, int offset, int count);


    Set<String> zrevrangeByScore(String key, String max, String min);


    Set<String> zrangeByScore(String key, String min, String max, int offset, int count);


    Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count);


    Set<Tuple> zrangeByScoreWithScores(String key, double min, double max);


    Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min);


    Set<Tuple> zrangeByScoreWithScores(String key, double min, double max, int offset, int count);


    Set<String> zrevrangeByScore(String key, String max, String min, int offset, int count);


    Set<Tuple> zrangeByScoreWithScores(String key, String min, String max);


    Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min);


    Set<Tuple> zrangeByScoreWithScores(String key, String min, String max, int offset, int count);


    Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count);


    Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min, int offset, int count);


    Long zremrangeByRank(String key, long start, long end);


    Long zremrangeByScore(String key, double start, double end);


    Long zremrangeByScore(String key, String start, String end);


    Long zlexcount(String key, String min, String max);


    Set<String> zrangeByLex(String key, String min, String max);


    Set<String> zrangeByLex(String key, String min, String max, int offset, int count);


    Set<String> zrevrangeByLex(String key, String max, String min);


    Set<String> zrevrangeByLex(String key, String max, String min, int offset, int count);


    Long zremrangeByLex(String key, String min, String max);


    Long linsert(String key, BinaryClient.LIST_POSITION where, String pivot, String value);

    Long lpushx(String key, String... string);


    Long rpushx(String key, String... string);


    @Deprecated
    List<String> blpop(String arg);


    List<String> blpop(int timeout, String key);


    @Deprecated
    List<String> brpop(String arg);


    List<String> brpop(int timeout, String key);


    String echo(String string);

    Long move(String key, int dbIndex);


    Long bitcount(String key);


    Long bitcount(String key, long start, long end);


    Long bitpos(String key, boolean value);


    Long bitpos(String key, boolean value, BitPosParams params);


    @Deprecated
    ScanResult<Map.Entry<String, String>> hscan(String key, int cursor);


    @Deprecated
    ScanResult<String> sscan(String key, int cursor);


    @Deprecated
    ScanResult<Tuple> zscan(String key, int cursor);


    ScanResult<Map.Entry<String, String>> hscan(String key, String cursor);


    ScanResult<Map.Entry<String, String>> hscan(String key, String cursor, ScanParams params);

    ScanResult<String> sscan(String key, String cursor);


    ScanResult<String> sscan(String key, String cursor, ScanParams params);

    ScanResult<Tuple> zscan(String key, String cursor);


    ScanResult<Tuple> zscan(String key, String cursor, ScanParams params);


    Long pfadd(String key, String... elements);


    long pfcount(String key);


    Long geoadd(String key, Map<String, GeoCoordinate> memberCoordinateMap);


    Double geodist(String key, String member1, String member2, GeoUnit unit);


    List<String> geohash(String key, String... members);


    List<GeoRadiusResponse> georadius(String key, double longitude, double latitude, double radius,
                                      GeoUnit unit);


    List<GeoRadiusResponse> georadius(String key, double longitude, double latitude, double radius, GeoUnit unit,
                                      GeoRadiusParam param);


    List<GeoRadiusResponse> georadiusByMember(String key, String member, double radius, GeoUnit unit,
                                              GeoRadiusParam param);


    List<Long> bitfield(String key, String... arguments);


    Long del(String... keys);


    Long exists(String... keys);


    List<String> blpop(int timeout, String... keys);


    List<String> brpop(int timeout, String... keys);


    List<String> blpop(String... args);


    List<String> brpop(String... args);


    List<String> mget(String... keys);


    String mset(String... keysvalues);


    Long msetnx(String... keysvalues);


    String rename(String oldkey, String newkey);


    Long renamenx(String oldkey, String newkey);


    String rpoplpush(String srckey, String dstkey);


    Set<String> sdiff(String... keys);


    Long sdiffstore(String dstkey, String... keys);

    Set<String> sinter(String... keys);

    Long sinterstore(String dstkey, String... keys);


    Long smove(String srckey, String dstkey, String member);


    Long sort(String key, SortingParams sortingParameters, String dstkey);

    Long sort(String key, String dstkey);


    Set<String> sunion(String... keys);


    Long sunionstore(String dstkey, String... keys);


    String watch(String... keys);


    String unwatch();


    Long zinterstore(String dstkey, String... sets);


    Long zinterstore(String dstkey, ZParams params, String... sets);


    Long zunionstore(String dstkey, String... sets);


    Long zunionstore(String dstkey, ZParams params, String... sets);


    String brpoplpush(String source, String destination, int timeout);


    Long publish(String channel, String message);


    void subscribe(JedisPubSub jedisPubSub, String... channels);


    void psubscribe(JedisPubSub jedisPubSub, String... patterns);


    String randomKey();


    Long bitop(BitOP op, String destKey, String... srcKeys);


    @Deprecated
    ScanResult<String> scan(int cursor);


    ScanResult<String> scan(String cursor);


    ScanResult<String> scan(String cursor, ScanParams params);


    String pfmerge(String destkey, String... sourcekeys);

    long pfcount(String... keys);


    Set<String> zrange(String key, long start, long end);
}