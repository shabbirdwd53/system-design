package com.dailycodebuffer.system_design.LoadBalancer;

import com.dailycodebuffer.system_design.LoadBalancer.model.Request;
import com.dailycodebuffer.system_design.LoadBalancer.model.Server;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Round Robin Load Balancer Implementation
 * Routes requests to servers in a sequential circular manner.
 */
public class RoundRobinLoadBalancer extends AbstractLoadBalancer {
    private AtomicInteger position = new AtomicInteger(0);

    @Override
    public Server getServer(Request request) {
        List<Server> healthyServers = getHealthyServers();

        if (healthyServers.isEmpty()) {
            return null; // No servers available
        }

        // Get position and increment atomically
        int pos = position.getAndIncrement() % healthyServers.size();
        return healthyServers.get(pos);
    }
}