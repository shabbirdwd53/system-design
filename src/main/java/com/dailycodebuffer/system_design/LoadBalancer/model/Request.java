package com.dailycodebuffer.system_design.LoadBalancer.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a client request to be processed
 */
public class Request {
    private String clientIP;
    private String url;
    private Map<String, String> headers;
    private String sessionId;

    public Request(String clientIP, String url) {
        this.clientIP = clientIP;
        this.url = url;
        this.headers = new HashMap<>();
    }

    public String getClientIP() {
        return clientIP;
    }

    public String getUrl() {
        return url;
    }

    public void addHeader(String name, String value) {
        headers.put(name, value);
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}