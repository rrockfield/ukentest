package com.uken.rockfield.question1;

import java.util.Map;
import java.util.concurrent.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RedisBuffer {

    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final RedisClient client = new RedisClient("redis-12967.c14.us-east-1-2.ec2.cloud.redislabs.com", 12967, "p9NkvKkuYL5mxNLbTElxvAeJhIQUUkAR");
    private Map<String, String> setters = new ConcurrentHashMap<>();
    private static Logger logger = LoggerFactory.getLogger(RedisBuffer.class);

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        RedisAsynchronousPipeline asynchJob = new RedisAsynchronousPipeline(client, setters);
        setters = new ConcurrentHashMap<>();
        executor.submit(asynchJob);
        logger.debug("FLUSH!");
    }

    String getValue(String key) {
        String value = setters.get(key);
        if (value == null) {
            return client.get(key);
        }
        return value;
    }

    public void setValue(String key, String value) {
        logger.debug("Buffer Set (" + key + "," + value + ")");
        setters.put(key, value);
    }

    void clear() {
        client.clear();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Buffer=");
        setters.entrySet().forEach((entry) -> {
            sb.append("(").append(entry.getKey()).append(",").append(entry.getValue()).append(")");
        });
        return sb.toString();
    }
}
