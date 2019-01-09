package com.radarwin.framework.cache;

import redis.clients.jedis.JedisPubSub;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by josh on 16/6/2.
 */
public class RedisEmptyCache extends RedisCache {


    @Override
    public boolean existsCache(String key) {
        return false;
    }

    @Override
    public boolean existsHashCache(String key, String field) {
        return false;
    }

    @Override
    public void addCache(String key, String value) {

    }

    @Override
    public void addCache(String key, String field, String value) {

    }

    @Override
    public void addCacheWithExpire(String key, String value, int seconds) {

    }

    @Override
    public void addCache(String key, Map<String, Object> map) {

    }

    @Override
    public String getCache(String key) {
        return null;
    }

    @Override
    public String getCache(String key, String field) {
        return null;
    }

    @Override
    public List<String> getCache(String key, String... fields) {
        return null;
    }

    @Override
    public Map<String, String> getHashAllFieldCache(String key) {
        return null;
    }

    @Override
    public Set<String> getAllKeys() {
        return null;
    }

    @Override
    public Long increaseBy(String key, long offset) {
        return null;
    }

    @Override
    public Double increaseBy(String key, double offset) {
        return null;
    }

    @Override
    public Long hincreaseBy(String key, String field, long offset) {
        return null;
    }

    @Override
    public Double hincreaseBy(String key, String field, double offset) {
        return null;
    }

    @Override
    public Long decreaseBy(String key, long offset) {
        return null;
    }

    @Override
    public Long increase(String key) {
        return null;
    }

    @Override
    public Long decrease(String key) {
        return null;
    }

    @Override
    public void expireKey(String key, int seconds) {

    }

    @Override
    public void delCache(String... keys) {

    }

    @Override
    public void delHashCache(String key, String... fields) {

    }

    @Override
    public void publish(String channel, String message) {

    }

    @Override
    public void publish(byte[] channel, byte[] message) {

    }

    @Override
    public void subscribe(JedisPubSub jedisPubSub, String channel) {

    }

    @Override
    public void closeCacheClient() {
    }


    @Override
    public void lPush(String key, String... value) {

    }

    @Override
    public void rPush(String key, String... value) {

    }


    @Override
    public String lPop(String key) {
        return null;
    }

    @Override
    public String rPop(String key) {
        return null;
    }

    @Override
    public List<String> lRange(String key, long start, long end) {
        return null;
    }

    @Override
    public void lRem(String key, long count, String value) {

    }
}
