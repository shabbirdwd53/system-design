package com.dailycodebuffer.system_design.LoadBalancer;

import com.dailycodebuffer.system_design.LoadBalancer.model.Request;
import com.dailycodebuffer.system_design.LoadBalancer.model.Server;
import com.dailycodebuffer.system_design.LoadBalancer.model.ServerStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Weighted Round Robin Load Balancer Implementation
 * Distributes requests based on predefined server weights.
 */
public class WeightedRoundRobinLoadBalancer extends AbstractLoadBalancer {
    private AtomicInteger currentIndex = new AtomicInteger(0);
    private List<ServerWithWeight> weightedServers = new ArrayList<>();
    private int totalWeight = 0;
    private int currentWeight = 0;
    private int gcd = 0; // Greatest common divisor of all weights

    static class ServerWithWeight {
        Server server;
        int weight;
        int effectiveWeight;
        int currentWeight;

        ServerWithWeight(Server server, int weight) {
            this.server = server;
            this.weight = weight;
            this.effectiveWeight = weight;
            this.currentWeight = 0;
        }
    }

    @Override
    public void addServer(Server server) {
        super.addServer(server);
        int weight = server.getWeight();
        weightedServers.add(new ServerWithWeight(server, weight));
        totalWeight += weight;

        // Recalculate the GCD
        if (weightedServers.size() == 1) {
            gcd = weight;
        } else {
            gcd = calculateGCD(gcd, weight);
        }
    }

    @Override
    public void removeServer(Server server) {
        super.removeServer(server);

        // Remove from weighted servers list
        ServerWithWeight toRemove = null;
        for (ServerWithWeight sw : weightedServers) {
            if (sw.server.equals(server)) {
                toRemove = sw;
                break;
            }
        }

        if (toRemove != null) {
            totalWeight -= toRemove.weight;
            weightedServers.remove(toRemove);

            // Recalculate GCD
            recalculateGCD();
        }
    }

    @Override
    public Server getServer(Request request) {
        List<ServerWithWeight> healthyWeightedServers = new ArrayList<>();
        for (ServerWithWeight sw : weightedServers) {
            if (sw.server.getStatus() == ServerStatus.UP) {
                healthyWeightedServers.add(sw);
            }
        }

        if (healthyWeightedServers.isEmpty()) {
            return null; // No servers available
        }

        // Implementation of weighted round-robin algorithm (Nginx style)
        ServerWithWeight selected = null;

        while (selected == null) {
            for (int i = 0; i < healthyWeightedServers.size(); i++) {
                ServerWithWeight sw = healthyWeightedServers.get(i);

                // Add effective weight to current weight
                sw.currentWeight += sw.effectiveWeight;

                // If this is the first iteration or the current weight is higher than selected, select this server
                if (selected == null || sw.currentWeight > selected.currentWeight) {
                    selected = sw;
                }
            }

            if (selected != null) {
                // Decrease the current weight by total weight
                selected.currentWeight -= totalWeight;
                break;
            }
        }

        return selected.server;
    }

    private int calculateGCD(int a, int b) {
        if (b == 0) {
            return a;
        }
        return calculateGCD(b, a % b);
    }

    private void recalculateGCD() {
        if (weightedServers.isEmpty()) {
            gcd = 0;
            return;
        }

        int result = weightedServers.get(0).weight;
        for (int i = 1; i < weightedServers.size(); i++) {
            result = calculateGCD(result, weightedServers.get(i).weight);
        }
        gcd = result;
    }
}

