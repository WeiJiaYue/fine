package com.yunbao.framework.cache.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by lewis on 2018/8/10.
 */

/**
 * 使用从config-server获取redis配置文件
 */
@ConfigurationProperties(prefix = "redis")
public class RedisProperties {


    /**
     * redis服务地址，单机模式eg:192.168.1.88:5672， 集群模式下用逗号分隔eg:192.168.1.88:5672,192.168.1.88:5673
     */
    private String hosts;

    /**
     * 是否启用集群模式
     */
    private boolean enableCluster;

    /**
     * redis密码
     */
    private String password;

    /**
     * 最大连接
     */
    private int maxTotal=1000;

    /**
     * 最大空闲
     */
    private int maxIdle=10;

    /**
     * 最大等待单位秒
     */
    private int maxWaitMillis=3000;

    private int maxRedirection=1000;


    public String getHosts() {
        return hosts;
    }

    public void setHosts(String hosts) {
        this.hosts = hosts;
    }

    public boolean isEnableCluster() {
        return enableCluster;
    }

    public void setEnableCluster(boolean enableCluster) {
        this.enableCluster = enableCluster;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMaxWaitMillis() {
        return maxWaitMillis;
    }

    public void setMaxWaitMillis(int maxWaitMillis) {
        this.maxWaitMillis = maxWaitMillis;
    }

    public int getMaxRedirection() {
        return maxRedirection;
    }

    public void setMaxRedirection(int maxRedirection) {
        this.maxRedirection = maxRedirection;
    }
}
