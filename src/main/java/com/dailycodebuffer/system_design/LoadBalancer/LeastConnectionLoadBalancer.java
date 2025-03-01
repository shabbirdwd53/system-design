package com.dailycodebuffer.system_design.LoadBalancer;

import com.dailycodebuffer.system_design.LoadBalancer.model.Request;
import com.dailycodebuffer.system_design.LoadBalancer.model.Server;

import java.util.List;

/**
 * Least Connection Load Balancer Implementation
 * Routes traffic to the server with the fewest active connections.
 */
public class LeastConnectionLoadBalancer extends AbstractLoadBalancer {

    @Override
    public Server getServer(Request request) {
        List<Server> healthyServers = getHealthyServers();

        if (healthyServers.isEmpty()) {
            return null; // No servers available
        }

        // Find server with the least active connections
        Server leastConnectedServer = healthyServers.get(0);
        int minConnections = leastConnectedServer.getActiveConnections();

        for (int i = 1; i < healthyServers.size(); i++) {
            Server server = healthyServers.get(i);
            int connections = server.getActiveConnections();

            if (connections < minConnections) {
                minConnections = connections;
                leastConnectedServer = server;
            }
        }

        // Increment active connections when selecting a server
        leastConnectedServer.incrementConnections();
        return leastConnectedServer;
    }
}