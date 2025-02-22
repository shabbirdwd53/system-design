package com.dailycodebuffer.system_design.ConsistentHashing.RedisPubSub;

import redis.clients.jedis.JedisPubSub;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

class ConsistentHashing {
    private static final Logger logger = Logger.getLogger(ConsistentHashing.class.getName());
    private final TreeMap<Long, String> ring = new TreeMap<>();
    private final int virtualNodes;
    private final MessageDigest sha256;
    private static final String CHANNEL_NAME = "hash_ring_updates";

    public ConsistentHashing(int virtualNodes) {
        this.virtualNodes = virtualNodes;
        try {
            this.sha256 = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 Algorithm not found", e);
        }
        subscribeToUpdates();
    }

    private long hashSHA256(String key) {
        sha256.update(key.getBytes(StandardCharsets.UTF_8));
        byte[] digest = sha256.digest();
        return ((long) (digest[3] & 0xFF) << 24) | ((long) (digest[2] & 0xFF) << 16) |
                ((long) (digest[1] & 0xFF) << 8) | (digest[0] & 0xFF);
    }

    public void addNode(String node) {
        for (int i = 0; i < virtualNodes; i++) {
            long hash = hashSHA256(node + "#" + i);
            ring.put(hash, node);
        }
        logger.info("Node added: " + node);
        RedisUtil.publish(CHANNEL_NAME, "ADD:" + node); // Broadcast change
    }

    public void removeNode(String node) {
        for (int i = 0; i < virtualNodes; i++) {
            long hash = hashSHA256(node + "#" + i);
            ring.remove(hash);
        }
        logger.info("Node removed: " + node);
        RedisUtil.publish(CHANNEL_NAME, "REMOVE:" + node); // Broadcast change
    }

    public String getNode(String key) {
        if (ring.isEmpty()) {
            logger.warning("Ring is empty, cannot assign key: " + key);
            return null;
        }
        long hash = hashSHA256(key);
        Map.Entry<Long, String> entry = ring.ceilingEntry(hash);
        return (entry == null) ? ring.firstEntry().getValue() : entry.getValue();
    }

    private void subscribeToUpdates() {
        new Thread(() -> RedisUtil.subscribe(CHANNEL_NAME, new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                if (channel.equals(CHANNEL_NAME)) {
                    if (message.startsWith("ADD:")) {
                        String node = message.substring(4);
                        if (!ring.containsValue(node)) {
                            addNode(node);
                            logger.info("Synchronized node addition: " + node);
                        }
                    } else if (message.startsWith("REMOVE:")) {
                        String node = message.substring(7);
                        removeNode(node);
                        logger.info("Synchronized node removal: " + node);
                    }
                }
            }
        })).start();
    }
}
