/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.university.disasterresponsesystem.common.protocol;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * A message from client to server. Carries the action type, the username of the
 * caller (for access control + audit), and a bag of named parameters.
 *
 * @author alisha -12268551
 */
public class Request implements Serializable {

    private static final long serialVersionUID = 1L;

    private RequestType type;
    private String username;                 // who is making the request
    private Map<String, Object> params = new HashMap<>();

    public Request() {
    }

    public Request(RequestType type, String username) {
        this.type = type;
        this.username = username;
    }

    public RequestType getType() {
        return type;
    }

    public void setType(RequestType type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    /**
     * Add a parameter
     */
    public Request put(String key, Object value) {
        params.put(key, value);
        return this;
    }

    public Object get(String key) {
        return params.get(key);
    }

    public String getString(String key) {
        Object v = params.get(key);
        return (v == null) ? null : v.toString();
    }

    public Long getLong(String key) {
        Object v = params.get(key);
        if (v == null) {
            return null;
        }
        if (v instanceof Long l) {
            return l;
        }
        if (v instanceof Number n) {
            return n.longValue();
        }
        return Long.valueOf(v.toString());
    }

    public int getInt(String key) {
        Object v = params.get(key);
        if (v instanceof Number n) {
            return n.intValue();
        }
        return (v == null) ? 0 : Integer.parseInt(v.toString());
    }

    @Override
    public String toString() {
        return "Request{" + type + ", user=" + username + ", params=" + params + "}";
    }
}
