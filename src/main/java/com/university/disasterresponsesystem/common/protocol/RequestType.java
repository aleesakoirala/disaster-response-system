/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.university.disasterresponsesystem.common.protocol;

/**
 * Every action the client can ask the server to perform. This is the agreed
 * contract: Client sends one of these, Server has a matching branch in
 * RequestDispatcher.
 *
 * @author alisha -12268551
 *
 */
public enum RequestType {
    // Auth (login feature / 2.5)
    LOGIN,
    // Disaster reporting & assessment (from Assignment 2)
    SUBMIT_REPORT,
    GET_REPORTS,
    ASSESS_REPORT,
    GET_INCIDENTS,
    ASSIGN_DEPARTMENTS,
    RECORD_DAMAGE,
    GET_CHECKLIST,
    RECOMMEND_RESOURCES,
    INCIDENT_SUMMARY,
    // Resource Management feature
    ADD_RESOURCE,
    GET_RESOURCES,
    ALLOCATE_RESOURCE,
    // Alert Management feature
    CREATE_ALERT,
    GET_ALERTS,
    DEACTIVATE_ALERT,
    // Audit (2.5 non-repudiation)
    GET_AUDIT_LOG
}
