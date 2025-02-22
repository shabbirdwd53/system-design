package com.dailycodebuffer.system_design.ConsistentHashing;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ConsistentHashingDemo {
    private static final Logger logger = Logger.getLogger(ConsistentHashingDemo.class.getName());

    public static void main(String[] args) {
        ConsistentHashing ch = new ConsistentHashing(100);

        // Add nodes to the system
        ch.addNode("Server-1");
        ch.addNode("Server-2");
        ch.addNode("Server-3");
        ch.addNode("Server-4");
        ch.addNode("Server-5");

        ch.printNodes();

        // Simulating 1 Million Requests
        Map<String, Integer> distribution = new HashMap<>();
        int requestCount = 1_000_000;

        for (int i = 0; i < requestCount; i++) {
            String key = "request-" + i;
            String assignedNode = ch.getNode(key);
            if (assignedNode == null) {
                logger.warning("Failed to assign key: " + key);
                continue;
            }
            distribution.put(assignedNode, distribution.getOrDefault(assignedNode, 0) + 1);
        }

        // Print distribution
        System.out.println("Request Distribution:");
        for (Map.Entry<String, Integer> entry : distribution.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}
