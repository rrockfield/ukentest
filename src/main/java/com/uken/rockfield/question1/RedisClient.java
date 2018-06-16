package com.uken.rockfield.question1;

import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;

public class RedisClient {

    private JedisPool pool;
    private String password;
    private static Logger logger = LoggerFactory.getLogger(RedisClient.class);

    public RedisClient(String url, int port, String password) {
        this(url, port);
        this.password = password;
    }

    public RedisClient(String url, int port) {
        pool = new JedisPool(url, port);
    }

    public Jedis getJedisInstance() {
        Jedis jedis = pool.getResource();
        if (password != null) {
            jedis.auth(password);
        }
        return jedis;
    }

    public String get(String key) {
        String value;
        try (Jedis jedis = getJedisInstance()) {
            jedis.connect();
            value = jedis.get(key);
            jedis.disconnect();
        }
        return value;
    }

    public void clear() {
        Jedis jedis = getJedisInstance();
        jedis.connect();
        jedis.flushAll();
        logger.debug("CLEAR!");
        jedis.disconnect();
    }

    @Deprecated
    public void consecutiveInsertion(Map<String, String> setters) {
        try (Jedis jedis = getJedisInstance()) {
            jedis.connect();
            setters.entrySet().forEach(entry -> {
                String response = jedis.set(entry.getKey(), entry.getValue());
                if (!response.equals("OK")) {
                    throw new RuntimeException(response);
                }
            });
            jedis.disconnect();
        }
    }

    public void transactionInsertion(Map<String, String> setters) {
        try (Jedis jedis = getJedisInstance()) {
            Transaction transaction = jedis.multi();
            setters.entrySet().forEach(entry -> {
                transaction.set(entry.getKey(), entry.getValue());
            });
            transaction.exec();
            jedis.resetState();
        }
    }

    public void pipelineInsertion(Map<String, String> setters) {
        try (Jedis jedis = getJedisInstance()) {
            Pipeline pipeline = jedis.pipelined();
            setters.entrySet().forEach(entry -> {
                pipeline.set(entry.getKey(), entry.getValue());
            });
            pipeline.sync();
            jedis.disconnect();
        }
    }
}
