/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.university.disasterresponsesystem.common.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A public alert/warning raised for an incident.
 *
 * @author alisha -12268551
 */
public class Alert implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long incidentId;
    private String message;
    private AlertSeverity severity;
    private String issuedBy;          // username (audit / non-repudiation)
    private LocalDateTime issuedAt;   // timestamp
    private boolean active = true;

    public Alert() {
    }

    public Alert(Long incidentId, String message, AlertSeverity severity,
            String issuedBy, LocalDateTime issuedAt) {
        this.incidentId = incidentId;
        this.message = message;
        this.severity = severity;
        this.issuedBy = issuedBy;
        this.issuedAt = issuedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIncidentId() {
        return incidentId;
    }

    public void setIncidentId(Long incidentId) {
        this.incidentId = incidentId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AlertSeverity getSeverity() {
        return severity;
    }

    public void setSeverity(AlertSeverity severity) {
        this.severity = severity;
    }

    public String getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(LocalDateTime issuedAt) {
        this.issuedAt = issuedAt;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "[" + severity + "] " + message + (active ? " (active)" : " (cleared)");
    }
}
