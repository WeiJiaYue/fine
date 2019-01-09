package com.yunbao.framework.cache.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.util.*;

/**
 * Created by josh on 15/7/12.
 */
public class RedisCacheWithCluster extends RedisCache {

    private final JedisCluster jedisCluster;





    public JedisCluster getJedisCluster() {
        return jedisCluster;
    }

    public RedisCacheWithCluster(JedisCluster jedisCluster) {
        this.jedisCluster=jedisCluster;
    }

    /**
     * 判断缓存是否存在
     *
     * @param key
     * @return
     */
    @Override
    public boolean existsCache(String key) {
        return jedisCluster.exists(key);
    }

    @Override
    public boolean existsHashCache(String key, String field) {
        return jedisCluster.hexists(key, field);
    }

    /**
     * 添加 string 类型的缓存
     *
     * @param key
     * @param value
     */
    @Override
    public void addCache(String key, String value) {
        jedisCluster.set(key, value);
    }

    /**
     * 添加field的缓存
     *
     * @param key
     * @param field
     * @param value
     */
    @Override
    public void addCache(String key, String field, String value) {
        jedisCluster.hset(key, field, value);
    }

    /**
     * 添加缓存并设置有效期
     *
     * @param key
     * @param value
     * @param seconds 单位 (秒)
     */
    @Override
    public void addCacheWithExpire(String key, String value, int seconds) {
        jedisCluster.setex(key, seconds, value);
    }

    /**
     * 添加缓存 hashMap类型
     *
     * @param key
     * @param map
     */
    @Override
    public void addCache(String key, Map<String, Object> map) {
        if (map != null && map.size() > 0) {
            Map<String, String> m = new HashMap<>();
            Set<String> sets = map.keySet();
            Iterator<String> it = sets.iterator();
            while (it.hasNext()) {
                String k = it.next();
                Object o = map.get(k);

                if (o != null) {
                    if (o instanceof String) {
                        m.put(k, (String) o);
                    } else if (!(o instanceof Collection)) {
                        m.put(k, String.valueOf(o));
                    } else {
                        m.put(k, null);
                    }
                } else {
                    m.put(k, null);
                }
            }
            jedisCluster.hmset(key, m);
        }
    }

    /**
     * 获取 String 型的key
     *
     * @param key
     * @return
     */
    @Override
    public String getCache(String key) {
        return jedisCluster.get(key);
    }

    /**
     * 根据key名，获取field的值
     *
     * @param key
     * @param field
     * @return
     */
    @Override
    public String getCache(String key, String field) {
        return jedisCluster.hget(key, field);
    }

    /**
     * 根据key名，获取多个field的值
     *
     * @param key
     * @param fields
     * @return
     */
    @Override
    public List<String> getCache(String key, String... fields) {
        return jedisCluster.hmget(key, fields);
    }

    /**
     * 针对hash类型，获取hash类型的所有key的filed和value
     *
     * @param key
     * @return
     */
    @Override
    public Map<String, String> getHashAllFieldCache(String key) {
        return jedisCluster.hgetAll(key);
    }

    /**
     * 获取所有的key
     *
     * @return
     */
    @Override
    public Set<String> getAllKeys() {
        Set<String> keysSet = new HashSet<>();
        Collection<JedisPool> poolCollection = jedisCluster.getClusterNodes().values();
        Iterator<JedisPool> iterator = poolCollection.iterator();
        while (iterator.hasNext()) {
            JedisPool jedisPool = iterator.next();
            Jedis jedis = null;
            try {
                jedis = jedisPool.getResource();
                keysSet.addAll(jedis.keys("*"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                jedisPool.returnResourceObject(jedis);
            }
        }
        return keysSet;
    }

    /**
     * 相加key的值
     *
     * @param key
     * @param offset
     * @return 相加后的值
     */
    @Override
    public Long increaseBy(String key, long offset) {
        return jedisCluster.incrBy(key, offset);
    }

    @Override
    public Double increaseBy(String key, double offset) {
        return jedisCluster.incrByFloat(key, offset);
    }

    @Override
    public Long hincreaseBy(String key, String field, long offset) {
        return jedisCluster.hincrBy(key, field, offset);
    }

    @Override
    public Double hincreaseBy(String key, String field, double offset) {
        return jedisCluster.hincrByFloat(key, field, offset);
    }

    /**
     * 相减key的值
     *
     * @param key
     * @param offset
     * @return 相减后的值
     */
    @Override
    public Long decreaseBy(String key, long offset) {
        return jedisCluster.decrBy(key, offset);
    }

    /**
     * 递增key的值，用于计数，自动加1
     *
     * @param key
     * @return 递增后的值
     */
    @Override
    public Long increase(String key) {
        return jedisCluster.incr(key);
    }

    /**
     * 递减key的值，用于计数，自动减1
     *
     * @param key
     * @return 递减后的值
     */
    @Override
    public Long decrease(String key) {
        return jedisCluster.decr(key);
    }

    /**
     * 指定key的失效时间
     *
     * @param key
     * @param seconds
     */
    @Override
    public void expireKey(String key, int seconds) {
        jedisCluster.expire(key, seconds);
    }

    /**
     * 清除缓存
     *
     * @param keys
     */
    @Override
    public void delCache(String... keys) {
        if (keys != null && keys.length > 0) {
            for (String k : keys) {
                jedisCluster.del(k);
            }
        }
    }

    /**
     * 清除缓存
     *
     * @param key
     * @param fields hash的field,支持一次删除多个field的值
     */
    @Override
    public void delHashCache(String key, String... fields) {
        jedisCluster.hdel(key, fields);
    }

    @Override
    public void publish(String channel, String message) {
        Collection<JedisPool> poolCollection = jedisCluster.getClusterNodes().values();
        Iterator<JedisPool> iterator = poolCollection.iterator();
        if (iterator.hasNext()) {
            JedisPool jedisPool = iterator.next();
            Jedis jedis = null;
            try {
                jedis = jedisPool.getResource();
                jedis.publish(channel, message);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                jedisPool.returnResourceObject(jedis);
            }
        }
    }

    @Override
    public void publish(byte[] channel, byte[] message) {
        Collection<JedisPool> poolCollection = jedisCluster.getClusterNodes().values();
        Iterator<JedisPool> iterator = poolCollection.iterator();
        if (iterator.hasNext()) {
            JedisPool jedisPool = iterator.next();
            Jedis jedis = null;
            try {
                jedis = jedisPool.getResource();
                jedis.publish(channel, message);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                jedisPool.returnResourceObject(jedis);
            }
        }
    }

    /**
     * 订阅数据
     *
     * @param jedisPubSub
     * @param channel
     */
    public void subscribe(JedisPubSub jedisPubSub, String channel) {
        Collection<JedisPool> poolCollection = jedisCluster.getClusterNodes().values();
        Iterator<JedisPool> iterator = poolCollection.iterator();
        while (iterator.hasNext()) {
            JedisPool jedisPool = iterator.next();
            Jedis jedis = null;
            try {
                jedis = jedisPool.getResource();
                jedis.subscribe(jedisPubSub, channel);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                jedisPool.returnResourceObject(jedis);
            }
        }
    }

    @Override
    public void lPush(String key, String... value) {
        Collection<JedisPool> poolCollection = jedisCluster.getClusterNodes().values();
        Iterator<JedisPool> iterator = poolCollection.iterator();
        while (iterator.hasNext()) {
            JedisPool jedisPool = iterator.next();
            Jedis jedis = null;
            try {
                jedis = jedisPool.getResource();
                jedis.lpush(key, value);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                jedisPool.returnResourceObject(jedis);
            }
        }
    }

    @Override
    public void rPush(String key, String... value) {
        Collection<JedisPool> poolCollection = jedisCluster.getClusterNodes().values();
        Iterator<JedisPool> iterator = poolCollection.iterator();
        while (iterator.hasNext()) {
            JedisPool jedisPool = iterator.next();
            Jedis jedis = null;
            try {
                jedis = jedisPool.getResource();
                jedis.rpush(key, value);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                jedisPool.returnResourceObject(jedis);
            }
        }
    }

    @Override
    public List<String> lRange(String key, long start, long end) {
        List<String> allResults = new ArrayList<>();
        Collection<JedisPool> poolCollection = jedisCluster.getClusterNodes().values();
        Iterator<JedisPool> iterator = poolCollection.iterator();
        while (iterator.hasNext()) {
            JedisPool jedisPool = iterator.next();
            Jedis jedis = null;
            try {
                jedis = jedisPool.getResource();
                allResults.addAll(jedis.lrange(key, start, end));
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                jedisPool.returnResourceObject(jedis);
            }
        }
        return allResults;
    }

    @Override
    public void lRem(String key, long count, String value) {
        Collection<JedisPool> poolCollection = jedisCluster.getClusterNodes().values();
        Iterator<JedisPool> iterator = poolCollection.iterator();
        while (iterator.hasNext()) {
            JedisPool jedisPool = iterator.next();
            Jedis jedis = null;
            try {
                jedis = jedisPool.getResource();
                jedis.lrem(key, count, value);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                jedisPool.returnResourceObject(jedis);
            }
        }
    }


    @Override
    public String lPop(String key) {
        return jedisCluster.lpop(key);
    }

    @Override
    public String rPop(String key) {
        return jedisCluster.rpop(key);
    }

    /**
     * 关闭缓存客户端
     */
    @Override
    public void closeCacheClient() {
        try {
            jedisCluster.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
