package com.dailycodebuffer.system_design.LoadBalancer;

import com.dailycodebuffer.system_design.LoadBalancer.model.Request;
import com.dailycodebuffer.system_design.LoadBalancer.model.Server;

/**
 * LoadBalancer interface that defines the core functionality
 * for all load balancing implementations.
 */
public interface LoadBalancer {
    /**
     * Selects a server to handle the current request.
     * @param request The incoming request to be processed
     * @return The selected server instance
     */
    Server getServer(Request request);

    /**
     * Adds a new server to the load balancer
     * @param server The server to be added
     */
    void addServer(Server server);

    /**
     * Removes a server from the load balancer
     * @param server The server to be removed
     */
    void removeServer(Server server);

    /**
     * Updates the status of all servers
     */
    void updateServerStatus();
}
