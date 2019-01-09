package com.radarwin.framework.cache;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.*;

/**
 * Created by josh on 15/7/12.
 */
@Deprecated
class RedisCacheWithoutCluster extends RedisCache {

    @Override
    public boolean existsCache(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.exists(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            pool.returnResourceObject(jedis);
        }
    }

    @Override
    public boolean existsHashCache(String key, String field) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hexists(key, field);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            pool.returnResourceObject(jedis);
        }
    }

    @Override
    public void addCache(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.set(key, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            pool.returnResourceObject(jedis);
        }
    }

    @Override
    public void addCache(String key, String field, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.hset(key, field, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            pool.returnResourceObject(jedis);
        }
    }

    @Override
    public void addCacheWithExpire(String key, String value, int seconds) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.setex(key, seconds, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            pool.returnResourceObject(jedis);
        }
    }

    @Override
    public void addCache(String key, Map<String, Object> map) {
        if (map != null && map.size() > 0) {
            Jedis jedis = null;
            try {
                jedis = pool.getResource();
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
                jedis.hmset(key, m);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                pool.returnResourceObject(jedis);
            }
        }
    }

    @Override
    public String getCache(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.get(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            pool.returnResourceObject(jedis);
        }
    }

    @Override
    public String getCache(String key, String field) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hget(key, field);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            pool.returnResourceObject(jedis);
        }
    }

    @Override
    public List<String> getCache(String key, String... fields) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hmget(key, fields);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            pool.returnResourceObject(jedis);
        }
    }

    @Override
    public Map<String, String> getHashAllFieldCache(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hgetAll(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            pool.returnResourceObject(jedis);
        }
    }

    @Override
    public Set<String> getAllKeys() {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.keys("*");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            pool.returnResourceObject(jedis);
        }
    }

    @Override
    public Long increaseBy(String key, long offset) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.incrBy(key, offset);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            pool.returnResourceObject(jedis);
        }
    }

    @Override
    public Double increaseBy(String key, double offset) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.incrByFloat(key, offset);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            pool.returnResourceObject(jedis);
        }
    }

    @Override
    public Long hincreaseBy(String key, String field, long offset) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hincrBy(key, field, offset);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            pool.returnResourceObject(jedis);
        }
    }

    @Override
    public Double hincreaseBy(String key, String field, double offset) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hincrByFloat(key, field, offset);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            pool.returnResourceObject(jedis);
        }
    }

    @Override
    public Long decreaseBy(String key, long offset) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.decrBy(key, offset);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            pool.returnResourceObject(jedis);
        }
    }

    @Override
    public Long increase(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.incr(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            pool.returnResourceObject(jedis);
        }
    }

    @Override
    public Long decrease(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.decr(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            pool.returnResourceObject(jedis);
        }
    }

    @Override
    public void expireKey(String key, int seconds) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.expire(key, seconds);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            pool.returnResourceObject(jedis);
        }
    }

    @Override
    public void delCache(String... keys) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.del(keys);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            pool.returnResourceObject(jedis);
        }
    }

    @Override
    public void delHashCache(String key, String... fields) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.hdel(key, fields);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            pool.returnResourceObject(jedis);
        }
    }

    @Override
    public void publish(String channel, String message) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.publish(channel, message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            pool.returnResourceObject(jedis);
        }
    }

    @Override
    public void publish(byte[] channel, byte[] message) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.publish(channel, message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            pool.returnResourceObject(jedis);
        }
    }

    @Override
    public void subscribe(JedisPubSub jedisPubSub, String channel) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.subscribe(jedisPubSub, channel);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            pool.returnResourceObject(jedis);
        }
    }


    @Override
    public void lPush(String key, String... value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.lpush(key, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            pool.returnResourceObject(jedis);
        }
    }

    @Override
    public void rPush(String key, String... value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.rpush(key, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            pool.returnResourceObject(jedis);
        }
    }


    @Override
    public List<String> lRange(String key, long start, long end) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lrange(key, start, end);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            pool.returnResourceObject(jedis);
        }
    }


    @Override
    public void lRem(String key, long count, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.lrem(key, count, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            pool.returnResourceObject(jedis);
        }
    }


    @Override
    public String lPop(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpop(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            pool.returnResourceObject(jedis);
        }
    }


    @Override
    public String rPop(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.rpop(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            pool.returnResourceObject(jedis);
        }
    }

    /**
     * 关闭缓存客户端
     */
    @Override
    public void closeCacheClient() {
        pool.close();
    }
}
