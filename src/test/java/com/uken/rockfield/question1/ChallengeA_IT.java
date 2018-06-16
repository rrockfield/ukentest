package com.uken.rockfield.question1;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import org.junit.*;

public class ChallengeA_IT {

    private static final ExecutorService executor = Executors.newCachedThreadPool();
    private static final int MAX_RECORDS = 100000;
    private static final String URL = "redis-12967.c14.us-east-1-2.ec2.cloud.redislabs.com";
    private static final int PORT = 12967;
    private static final String PASSWORD = "p9NkvKkuYL5mxNLbTElxvAeJhIQUUkAR";
    private static final String PREFIX = "value-";

    @Test
    @Ignore
    @Deprecated
    public void consecutiveInsertionTest() {
        RedisClient client = new RedisClient(URL, PORT, PASSWORD);
        client.clear();
        System.out.println("Starting consecutiveInsertionTest...");
        Instant start = Instant.now();
        Map<String, String> map = buildMap(0, MAX_RECORDS);
        client.consecutiveInsertion(map);
        Instant end = Instant.now();
        System.out.println("Consecutive method took: " + Duration.between(start, end).getSeconds() + " seconds");
        assertValues();
        client.clear();
    }

    @Test
    public void singleTransactionInsertionTest() {
        RedisClient client = new RedisClient(URL, PORT, PASSWORD);
        client.clear();
        System.out.println("Starting singleTransactionInsertionTest...");
        Instant start = Instant.now();
        Map<String, String> map = buildMap(0, MAX_RECORDS);
        client.transactionInsertion(map);
        Instant end = Instant.now();
        System.out.println("Transaction method took: " + Duration.between(start, end).getSeconds() + " seconds");
        assertValues();
        client.clear();
    }

    @Test
    public void singlePipelineInsertionTest() throws Exception {
        RedisClient client = new RedisClient(URL, PORT, PASSWORD);
        client.clear();
        System.out.println("Starting singlePipelineInsertionTest...");
        Instant start = Instant.now();
        Map<String, String> map = buildMap(0, MAX_RECORDS);
        client.pipelineInsertion(map);
        Instant end = Instant.now();
        System.out.println("Single Pipeline method took: " + Duration.between(start, end).getSeconds() + " seconds");
        assertValues();
        client.clear();
    }

    @Test
    @Ignore
    public void parallelPipelineInsertionTest() throws Exception {
        RedisClient client = new RedisClient(URL, PORT, PASSWORD);
        client.clear();
        System.out.println("Starting parallelPipelineInsertionTest...");
        Instant start = Instant.now();
        int n = MAX_RECORDS / 10;
        List<Future> threadList = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            Map<String, String> map = buildMap((i * n), ((i + 1) * n));
            RedisAsynchronousPipeline asynchJob = new RedisAsynchronousPipeline(client, map);
            Future thread = executor.submit(asynchJob);
            threadList.add(thread);
        }
        for (Future response : threadList) {
            response.get();
        }
        Instant end = Instant.now();
        System.out.println("Parallel Pipeline method took: " + Duration.between(start, end).getSeconds() + " seconds");
        assertValues();
        client.clear();
    }

    private Map<String, String> buildMap(int start, int end) {
        Map<String, String> map = new HashMap<>();
        for (int i = start; i < end; i++) {
            map.put(PREFIX + i, Integer.toString(i));
        }
        return map;
    }
    
    private void assertValues() {
        RedisClient client = new RedisClient(URL, PORT, PASSWORD);
        Assert.assertTrue(client.get(PREFIX + 0).equals("0"));
        Assert.assertTrue(client.get(PREFIX + 99).equals("99"));
        Assert.assertTrue(client.get(PREFIX + 999).equals("999"));
        Assert.assertTrue(client.get(PREFIX + 9999).equals("9999"));
        Assert.assertTrue(client.get(PREFIX + 99999).equals("99999"));
    }
}
