/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.university.disasterresponsesystem.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * An assessed and coordinated incident derived from a DisasterReport. Holds the
 * source report object so the client can display it directly; the DAO populates
 * it on load. The id is assigned by MySQL.
 *
 * @author alisha -12268551
 */
public class Incident implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private DisasterReport sourceReport;
    private int severityScore;
    private PriorityLevel priorityLevel;
    private IncidentStatus status;
    private List<DepartmentType> assignedDepartments = new ArrayList<>();
    private String warningMessage = "";
    private String damageSummary = "";

    public Incident() {
    }

    public Incident(DisasterReport sourceReport, int severityScore,
            PriorityLevel priorityLevel, IncidentStatus status) {
        this.sourceReport = sourceReport;
        this.severityScore = severityScore;
        this.priorityLevel = priorityLevel;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DisasterReport getSourceReport() {
        return sourceReport;
    }

    public void setSourceReport(DisasterReport sourceReport) {
        this.sourceReport = sourceReport;
    }

    public int getSeverityScore() {
        return severityScore;
    }

    public void setSeverityScore(int severityScore) {
        this.severityScore = severityScore;
    }

    public PriorityLevel getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(PriorityLevel priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public IncidentStatus getStatus() {
        return status;
    }

    public void setStatus(IncidentStatus status) {
        this.status = status;
    }

    public List<DepartmentType> getAssignedDepartments() {
        return assignedDepartments;
    }

    public void setAssignedDepartments(List<DepartmentType> assignedDepartments) {
        this.assignedDepartments = (assignedDepartments == null) ? new ArrayList<>() : assignedDepartments;
    }

    public String getWarningMessage() {
        return warningMessage;
    }

    public void setWarningMessage(String warningMessage) {
        this.warningMessage = warningMessage;
    }

    public String getDamageSummary() {
        return damageSummary;
    }

    public void setDamageSummary(String damageSummary) {
        this.damageSummary = damageSummary;
    }

    public void addDepartment(DepartmentType d) {
        if (d != null && !assignedDepartments.contains(d)) {
            assignedDepartments.add(d);
        }
    }

    @Override
    public String toString() {
        String type = (sourceReport == null) ? "?" : String.valueOf(sourceReport.getDisasterType());
        return "INC-" + id + " | " + type + " | " + priorityLevel + " | " + status;
    }
}
