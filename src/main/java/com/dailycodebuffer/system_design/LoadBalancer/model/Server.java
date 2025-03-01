package com.dailycodebuffer.system_design.LoadBalancer.model;

import java.util.Objects;

/**
 * Represents a server in the load balancing system
 */
public class Server {
    private String id;
    private String host;
    private int port;
    private int weight;
    private ServerStatus status;
    private int activeConnections;
    private long averageResponseTime;
    private double cpuUtilization;
    private double memoryUtilization;
    private long currentBandwidth;

    public Server(String id, String host, int port) {
        this(id, host, port, 1); // Default weight of 1
    }

    public Server(String id, String host, int port, int weight) {
        this.id = id;
        this.host = host;
        this.port = port;
        this.weight = weight;
        this.status = ServerStatus.UNKNOWN;
        this.activeConnections = 0;
        this.averageResponseTime = 0;
        this.cpuUtilization = 0;
        this.memoryUtilization = 0;
        this.currentBandwidth = 0;
    }

    public String getId() {
        return id;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public ServerStatus getStatus() {
        return status;
    }

    public void setStatus(ServerStatus status) {
        this.status = status;
    }

    public int getActiveConnections() {
        return activeConnections;
    }

    public void setActiveConnections(int activeConnections) {
        this.activeConnections = activeConnections;
    }

    public void incrementConnections() {
        this.activeConnections++;
    }

    public void decrementConnections() {
        if (this.activeConnections > 0) {
            this.activeConnections--;
        }
    }

    public long getAverageResponseTime() {
        return averageResponseTime;
    }

    public void setAverageResponseTime(long averageResponseTime) {
        this.averageResponseTime = averageResponseTime;
    }

    public double getCpuUtilization() {
        return cpuUtilization;
    }

    public void setCpuUtilization(double cpuUtilization) {
        this.cpuUtilization = cpuUtilization;
    }

    public double getMemoryUtilization() {
        return memoryUtilization;
    }

    public void setMemoryUtilization(double memoryUtilization) {
        this.memoryUtilization = memoryUtilization;
    }

    public long getCurrentBandwidth() {
        return currentBandwidth;
    }

    public void setCurrentBandwidth(long currentBandwidth) {
        this.currentBandwidth = currentBandwidth;
    }

    /**
     * Performs a health check on the server
     * @return true if the server is healthy, false otherwise
     */
    public boolean healthCheck() {
        // In a real implementation, this would make an actual health check request
        // For demonstration, we'll just return true if CPU utilization is below 90%
        return cpuUtilization < 90.0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Server server = (Server) o;
        return Objects.equals(id, server.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Server{" +
                "id='" + id + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", status=" + status +
                '}';
    }
}