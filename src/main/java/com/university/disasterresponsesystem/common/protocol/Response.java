/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.university.disasterresponsesystem.common.protocol;

import java.io.Serializable;

/**
 * A message from server back to client. `data` carries the payload, which is
 * any Serializable object (a User, a List of Incidents, a String summary, ...).
 *
 * @author alisha -12268551
 */
public class Response implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean success;
    private String message;
    private Object data;

    public Response() {
    }

    public Response(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static Response ok(Object data) {
        return new Response(true, "OK", data);
    }

    public static Response ok(String message, Object data) {
        return new Response(true, message, data);
    }

    public static Response fail(String message) {
        return new Response(false, message, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Response{success=" + success + ", message=" + message + "}";
    }
}
