package com.dailycodebuffer.system_design.ConsistentHashing.RedisPubSub;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class RedisUtil {
    private static final String REDIS_HOST = "localhost";

    public static void publish(String channel, String message) {
        try (Jedis jedis = new Jedis(REDIS_HOST)) {
            jedis.publish(channel, message);
        }
    }

    public static void subscribe(String channel, JedisPubSub subscriber) {
        try (Jedis jedis = new Jedis(REDIS_HOST)) {
            jedis.subscribe(subscriber, channel);
        }
    }
}
