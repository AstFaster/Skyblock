package fr.astfaster.skyblock.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisConnection {

    private final JedisPool jedisPool;

    public RedisConnection(String host, int port, String password) {
        final JedisPoolConfig config = new JedisPoolConfig();
        config.setJmxEnabled(false);
        config.setMaxTotal(-1);

        this.jedisPool = new JedisPool(config, host, port, 300, password);
    }

    public Jedis getJedis() {
        return this.jedisPool.getResource();
    }

    public JedisPool getJedisPool() {
        return this.jedisPool;
    }

}
