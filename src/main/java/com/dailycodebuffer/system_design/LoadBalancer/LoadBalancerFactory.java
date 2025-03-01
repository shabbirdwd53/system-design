package com.dailycodebuffer.system_design.LoadBalancer;

import com.dailycodebuffer.system_design.LoadBalancer.model.LoadBalancerType;

/**
 * A load balancer factory to instantiate specific implementations
 */
public class LoadBalancerFactory {
    public static LoadBalancer createLoadBalancer(LoadBalancerType type) {
        switch (type) {
            case ROUND_ROBIN:
                return new RoundRobinLoadBalancer();
            case WEIGHTED_ROUND_ROBIN:
                return new WeightedRoundRobinLoadBalancer();
            case IP_HASH:
                return new IPHashLoadBalancer();
            case LEAST_CONNECTION:
                return new LeastConnectionLoadBalancer();
            case WEIGHTED_LEAST_CONNECTION:
                return new WeightedLeastConnectionLoadBalancer();
            case LEAST_RESPONSE_TIME:
                return new LeastResponseTimeLoadBalancer();
            case RESOURCE_BASED:
                return new ResourceBasedLoadBalancer();
            default:
                return new RoundRobinLoadBalancer(); // Default
        }
    }
}
