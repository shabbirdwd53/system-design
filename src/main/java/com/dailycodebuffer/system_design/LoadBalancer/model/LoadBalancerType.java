package com.dailycodebuffer.system_design.LoadBalancer.model;

/**
 * Enumeration of supported load balancer types
 */
public enum LoadBalancerType {
    ROUND_ROBIN,
    WEIGHTED_ROUND_ROBIN,
    IP_HASH,
    LEAST_CONNECTION,
    WEIGHTED_LEAST_CONNECTION,
    LEAST_RESPONSE_TIME,
    RESOURCE_BASED;
}
