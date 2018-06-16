package com.uken.rockfield.question1;

import java.util.Map;
import java.util.concurrent.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedisAsynchronousPipeline implements Callable {

    private final RedisClient client;
    private final Map<String, String> setters;
    private static Logger logger = LoggerFactory.getLogger(RedisAsynchronousPipeline.class);

    public RedisAsynchronousPipeline(RedisClient client, Map<String, String> setters) {
        this.client = client;
        this.setters = setters;
    }

    @Override
    public Object call() throws Exception {
        if (!setters.isEmpty()) {
            client.pipelineInsertion(setters);
        }
        return null;
    }
}
