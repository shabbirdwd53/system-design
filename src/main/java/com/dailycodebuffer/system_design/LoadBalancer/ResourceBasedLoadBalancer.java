package com.dailycodebuffer.system_design.LoadBalancer;

import com.dailycodebuffer.system_design.LoadBalancer.model.Request;
import com.dailycodebuffer.system_design.LoadBalancer.model.Server;

import java.util.List;

/**
 * Resource-Based (Adaptive) Load Balancer Implementation
 * Makes decisions based on real-time monitoring of server resources.
 */
public class ResourceBasedLoadBalancer extends AbstractLoadBalancer {

    @Override
    public Server getServer(Request request) {
        List<Server> healthyServers = getHealthyServers();

        if (healthyServers.isEmpty()) {
            return null; // No servers available
        }

        // Find server with the lowest resource utilization score
        Server leastLoadedServer = healthyServers.get(0);
        double minLoadScore = calculateLoadScore(leastLoadedServer);

        for (int i = 1; i < healthyServers.size(); i++) {
            Server server = healthyServers.get(i);
            double loadScore = calculateLoadScore(server);

            if (loadScore < minLoadScore) {
                minLoadScore = loadScore;
                leastLoadedServer = server;
            }
        }

        return leastLoadedServer;
    }

    /**
     * Calculate a composite score based on various resource metrics
     * Lower score means more available resources
     */
    private double calculateLoadScore(Server server) {
        // Weights for different metrics (should be tuned based on application characteristics)
        double cpuWeight = 0.5;
        double memoryWeight = 0.3;
        double connectionsWeight = 0.2;

        // Normalize all values to 0-1 range
        double cpuScore = server.getCpuUtilization() / 100.0;
        double memoryScore = server.getMemoryUtilization() / 100.0;
        double connectionsScore = Math.min(1.0, server.getActiveConnections() / 1000.0); // Assuming 1000 connections is the max

        // Calculate weighted score
        return (cpuScore * cpuWeight) + (memoryScore * memoryWeight) + (connectionsScore * connectionsWeight);
    }
}
