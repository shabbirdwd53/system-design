package com.dailycodebuffer.system_design.LoadBalancer;

import com.dailycodebuffer.system_design.LoadBalancer.model.Request;
import com.dailycodebuffer.system_design.LoadBalancer.model.Server;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * IP Hash Load Balancer Implementation
 * Routes requests based on client IP address to ensure session persistence.
 */
public class IPHashLoadBalancer extends AbstractLoadBalancer {

    @Override
    public Server getServer(Request request) {
        List<Server> healthyServers = getHealthyServers();

        if (healthyServers.isEmpty()) {
            return null; // No servers available
        }

        String clientIP = request.getClientIP();
        if (clientIP == null || clientIP.isEmpty()) {
            // Fallback to round robin if no IP is available
            int index = Math.abs(request.hashCode()) % healthyServers.size();
            return healthyServers.get(index);
        }

        // Generate consistent hash from IP
        int hash = Math.abs(clientIP.hashCode());
        int index = hash % healthyServers.size();

        return healthyServers.get(index);
    }

    /**
     * Alternative implementation with MD5 hash for better distribution
     */
    private int getConsistentHash(String clientIP, int buckets) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(clientIP.getBytes());

            // Use the first 4 bytes to create an int
            return Math.abs(((digest[0] & 0xFF) << 24) |
                    ((digest[1] & 0xFF) << 16) |
                    ((digest[2] & 0xFF) << 8) |
                    (digest[3] & 0xFF)) % buckets;
        } catch (NoSuchAlgorithmException e) {
            // Fallback to simple hash
            return Math.abs(clientIP.hashCode()) % buckets;
        }
    }
}
