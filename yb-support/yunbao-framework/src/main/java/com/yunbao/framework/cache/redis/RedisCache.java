package com.yunbao.framework.cache.redis;

import redis.clients.jedis.JedisPubSub;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by josh on 15/7/7.
 */
public abstract class RedisCache {
    protected boolean isCluster;

    protected boolean isCluster() {
        return isCluster;
    }

    protected void setCluster(boolean cluster) {
        isCluster = cluster;
    }

    /**
     * 判断缓存是否存在
     *
     * @param key
     * @return
     */
    public abstract boolean existsCache(String key);

    /**
     * 判断hash缓存是否存在
     *
     * @param key
     * @param field
     * @return
     */
    public abstract boolean existsHashCache(String key, String field);

    /**
     * 添加 string 类型的缓存
     *
     * @param key
     * @param value
     */
    public abstract void addCache(String key, String value);

    /**
     * 添加field的缓存
     *
     * @param key
     * @param field
     * @param value
     */
    public abstract void addCache(String key, String field, String value);

    /**
     * 添加缓存并设置有效期
     *
     * @param key
     * @param value
     * @param seconds 单位 (秒)
     */
    public abstract void addCacheWithExpire(String key, String value, int seconds);

    /**
     * 添加缓存 hashMap类型
     *
     * @param key
     * @param map
     */
    public abstract void addCache(String key, Map<String, Object> map);

    /**
     * 获取 String 型的key
     *
     * @param key
     * @return
     */
    public abstract String getCache(String key);

    /**
     * 根据key名，获取field的值
     *
     * @param key
     * @param field
     * @return
     */
    public abstract String getCache(String key, String field);

    /**
     * 根据key名，获取多个field的值
     *
     * @param key
     * @param fields
     * @return
     */
    public abstract List<String> getCache(String key, String... fields);

    /**
     * 针对hash类型，获取hash类型的所有key的filed和value
     *
     * @param key
     * @return
     */
    public abstract Map<String, String> getHashAllFieldCache(String key);

    /**
     * 获取所有的key
     *
     * @return
     */
    public abstract Set<String> getAllKeys();

    /**
     * 相加key的值
     *
     * @param key
     * @param offset
     * @return 相加后的值
     */
    public abstract Long increaseBy(String key, long offset);

    /**
     * @param key
     * @param offset
     * @return
     */
    public abstract Double increaseBy(String key, double offset);

    /**
     * 通过key相加field的值
     *
     * @param key
     * @param field
     * @param offset
     * @return
     */
    public abstract Long hincreaseBy(String key, String field, long offset);

    /**
     * 通过key相加field的值
     *
     * @param key
     * @param field
     * @param offset
     * @return
     */
    public abstract Double hincreaseBy(String key, String field, double offset);

    /**
     * 相减key的值
     *
     * @param key
     * @param offset
     * @return 相减后的值
     */
    public abstract Long decreaseBy(String key, long offset);

    /**
     * 递增key的值，用于计数，自动加1
     *
     * @param key
     * @return 递增后的值
     */
    public abstract Long increase(String key);

    /**
     * 递减key的值，用于计数，自动减1
     *
     * @param key
     * @return 递减后的值
     */
    public abstract Long decrease(String key);

    /**
     * 指定key的失效时间
     *
     * @param key
     * @param seconds
     */
    public abstract void expireKey(String key, int seconds);

    /**
     * 清除缓存
     *
     * @param keys
     */
    public abstract void delCache(String... keys);

    /**
     * 清除缓存
     *
     * @param key
     * @param fields hash的field,支持一次删除多个field的值
     */
    public abstract void delHashCache(String key, String... fields);

    /**
     * 推送消息
     *
     * @param channel
     * @param message
     */
    public abstract void publish(String channel, String message);

    /**
     * 推送消息
     *
     * @param channel
     * @param message
     */
    public abstract void publish(byte[] channel, byte[] message);

    /**
     * 订阅频道数据
     *
     * @param jedisPubSub
     * @param channel
     */
    public abstract void subscribe(JedisPubSub jedisPubSub, String channel);

    /**
     * 关闭缓存客户端
     */
    public abstract void closeCacheClient();

    public abstract void lPush(String key, String... value);


    public abstract void rPush(String key, String... value);


    public abstract List<String> lRange(String key, long start, long end);


    public abstract void lRem(String key, long count, String value);

    public abstract String lPop(String key);


    public abstract String rPop(String key);


}

