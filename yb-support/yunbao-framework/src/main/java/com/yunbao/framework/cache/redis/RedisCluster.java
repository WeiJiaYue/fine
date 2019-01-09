package com.yunbao.framework.cache.redis;

import redis.clients.jedis.JedisCluster;

/**
 * Created by lewis on 2018/8/16.
 */
public interface RedisCluster {

    JedisCluster getJedisCluster(RedisProperties redisProperties);
}
