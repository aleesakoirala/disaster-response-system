/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.university.disasterresponsesystem.server;

import com.university.disasterresponsesystem.common.protocol.Request;
import com.university.disasterresponsesystem.common.protocol.Response;

/**
 * Routes each request to the right business action. For now every case returns
 * a clear stub so the transport layer works end-to-end; we wire the real
 * services (and through them the DAO layer) into each case next.
 *
 * @author alisha -12268551
 */
public class RequestDispatcher {

    public Response handle(Request request) {
        if (request == null || request.getType() == null) {
            return Response.fail("Empty request");
        }
        switch (request.getType()) {
            case LOGIN, SUBMIT_REPORT, GET_REPORTS, ASSESS_REPORT, GET_INCIDENTS, ASSIGN_DEPARTMENTS, RECORD_DAMAGE, GET_CHECKLIST, RECOMMEND_RESOURCES, INCIDENT_SUMMARY, ADD_RESOURCE, GET_RESOURCES, ALLOCATE_RESOURCE, CREATE_ALERT, GET_ALERTS, DEACTIVATE_ALERT, GET_AUDIT_LOG -> {
                return Response.fail("Not implemented yet: " + request.getType());
            }
            default -> {
                return Response.fail("Unknown request type: " + request.getType());
            }
        }
    }
}
