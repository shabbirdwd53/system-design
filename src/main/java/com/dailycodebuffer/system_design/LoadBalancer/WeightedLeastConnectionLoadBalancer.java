package com.dailycodebuffer.system_design.LoadBalancer;

import com.dailycodebuffer.system_design.LoadBalancer.model.Request;
import com.dailycodebuffer.system_design.LoadBalancer.model.Server;

import java.util.List;

/**
 * Weighted Least Connection Load Balancer Implementation
 * Considers both server capacity (weight) and current connections.
 */
class WeightedLeastConnectionLoadBalancer extends AbstractLoadBalancer {

    @Override
    public Server getServer(Request request) {
        List<Server> healthyServers = getHealthyServers();

        if (healthyServers.isEmpty()) {
            return null; // No servers available
        }

        // Find server with the best ratio (weight/active_connections)
        Server selectedServer = null;
        double lowestRatio = Double.MAX_VALUE;

        for (Server server : healthyServers) {
            // For new servers with 0 connections, use a small value to avoid division by zero
            int connections = Math.max(server.getActiveConnections(), 1);
            double ratio = (double) connections / server.getWeight();

            if (ratio < lowestRatio) {
                lowestRatio = ratio;
                selectedServer = server;
            }
        }

        // Increment active connections when selecting a server
        selectedServer.incrementConnections();
        return selectedServer;
    }
}
