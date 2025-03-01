package com.dailycodebuffer.system_design.LoadBalancer;

import com.dailycodebuffer.system_design.LoadBalancer.model.Request;
import com.dailycodebuffer.system_design.LoadBalancer.model.Server;

import java.util.List;

/**
 * Least Response Time Load Balancer Implementation
 * Routes to server with the lowest response time, indicating most available resources.
 */
class LeastResponseTimeLoadBalancer extends AbstractLoadBalancer {

    @Override
    public Server getServer(Request request) {
        List<Server> healthyServers = getHealthyServers();

        if (healthyServers.isEmpty()) {
            return null; // No servers available
        }

        // Find server with the lowest response time
        Server fastestServer = healthyServers.get(0);
        long minResponseTime = fastestServer.getAverageResponseTime();

        for (int i = 1; i < healthyServers.size(); i++) {
            Server server = healthyServers.get(i);
            long responseTime = server.getAverageResponseTime();

            if (responseTime < minResponseTime) {
                minResponseTime = responseTime;
                fastestServer = server;
            }
        }

        return fastestServer;
    }
}