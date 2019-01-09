package com.yunbao.framework.cache.redis;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import com.yunbao.framework.util.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by lewis on 2018/8/10.
 */

@Configuration
@EnableConfigurationProperties({RedisProperties.class})
public class RedisAutoConfiguration {

    private final RedisProperties redisProperties;


    public RedisAutoConfiguration(RedisProperties redisProperties) {
        this.redisProperties = redisProperties;
    }


    private JedisCluster initJedisCluster() {
        JedisCluster jedisCluster = null;
        String[] hostArray = redisProperties.getHosts().split(StringUtil.COMMA);
        Set<HostAndPort> jedisClusterNodes = new HashSet<>();
        JedisPoolConfig config = new JedisPoolConfig();
        // 设置jedis的最大实例数
        config.setMaxTotal(redisProperties.getMaxTotal());
        // 设置最大空闲数
        config.setMaxIdle(redisProperties.getMaxIdle());
        // 设置获取jedis实例超时时间
        config.setMaxWaitMillis(redisProperties.getMaxWaitMillis());
        //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
        config.setTestOnBorrow(true);
        for (String hostAndPort : hostArray) {
            String[] hp = hostAndPort.split(StringUtil.COLON);
            String host = hp[0];
            int port = Integer.valueOf(hp[1]);
            jedisClusterNodes.add(new HostAndPort(host, port));
            jedisCluster = new JedisCluster(jedisClusterNodes,
                    redisProperties.getMaxWaitMillis(),
                    redisProperties.getMaxRedirection(),
                    config);
        }
        return jedisCluster;
    }

    private JedisPool initJedisStandalone() {
        String[] hostArray = redisProperties.getHosts().split(StringUtil.COMMA);
        String[] hp = hostArray[0].split(StringUtil.COLON);
        JedisPoolConfig config = new JedisPoolConfig();
        // 设置jedis的最大实例数
        config.setMaxTotal(redisProperties.getMaxTotal());
        // 设置最大空闲数
        config.setMaxIdle(redisProperties.getMaxIdle());
        // 设置获取jedis实例超时时间
        config.setMaxWaitMillis(redisProperties.getMaxWaitMillis());
        //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
        config.setTestOnBorrow(true);
        // 实例化连接池
        if (StringUtil.isNotBlank(redisProperties.getPassword())) {
            return new JedisPool(config, hp[0], Integer.valueOf(hp[1]), 5000, redisProperties.getPassword());
        } else {
            return new JedisPool(config, hp[0], Integer.valueOf(hp[1]));
        }
    }

    @Bean
    public RedisCache redisCache() {
        RedisCache redisCache;
        // 是否支持启用集群
        if (redisProperties.isEnableCluster()) {
            redisCache = new RedisCacheWithCluster(initJedisCluster());
            redisCache.setCluster(true);
        } else {
            redisCache = new RedisCacheWithoutCluster(initJedisStandalone());
            redisCache.setCluster(false);
        }
        return redisCache;
    }


}