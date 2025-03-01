package com.dailycodebuffer.system_design.LoadBalancer;

import com.dailycodebuffer.system_design.LoadBalancer.model.Server;
import com.dailycodebuffer.system_design.LoadBalancer.model.ServerStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract base class that implements common functionality
 * for all load balancer implementations.
 */
abstract class AbstractLoadBalancer implements LoadBalancer {
    protected List<Server> servers;
    protected Map<String, Server> serverMap;

    public AbstractLoadBalancer() {
        servers = new ArrayList<>();
        serverMap = new HashMap<>();
    }

    @Override
    public void addServer(Server server) {
        servers.add(server);
        serverMap.put(server.getId(), server);
    }

    @Override
    public void removeServer(Server server) {
        servers.remove(server);
        serverMap.remove(server.getId());
    }

    @Override
    public void updateServerStatus() {
        for (Server server : servers) {
            boolean isUp = server.healthCheck();
            server.setStatus(isUp ? ServerStatus.UP : ServerStatus.DOWN);
        }
    }

    /**
     * Returns a list of healthy servers
     * @return List of servers that are up and able to handle requests
     */
    protected List<Server> getHealthyServers() {
        List<Server> healthyServers = new ArrayList<>();
        for (Server server : servers) {
            if (server.getStatus() == ServerStatus.UP) {
                healthyServers.add(server);
            }
        }
        return healthyServers;
    }
}
