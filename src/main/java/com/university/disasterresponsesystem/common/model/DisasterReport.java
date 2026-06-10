/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.university.disasterresponsesystem.common.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A disaster reported by an operator/citizen
 *
 * @author alisha -12268551
 */
public class DisasterReport implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String reporterName;
    private DisasterType disasterType;
    private String location;
    private String description;
    private int peopleAffected;
    private LocalDateTime reportedAt;   // timestamp (Assignment 2.5)

    public DisasterReport() {
    }

    public DisasterReport(String reporterName, DisasterType disasterType, String location,
            String description, int peopleAffected, LocalDateTime reportedAt) {
        this.reporterName = reporterName;
        this.disasterType = disasterType;
        this.location = location;
        this.description = description;
        this.peopleAffected = peopleAffected;
        this.reportedAt = reportedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public DisasterType getDisasterType() {
        return disasterType;
    }

    public void setDisasterType(DisasterType disasterType) {
        this.disasterType = disasterType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPeopleAffected() {
        return peopleAffected;
    }

    public void setPeopleAffected(int peopleAffected) {
        this.peopleAffected = peopleAffected;
    }

    public LocalDateTime getReportedAt() {
        return reportedAt;
    }

    public void setReportedAt(LocalDateTime reportedAt) {
        this.reportedAt = reportedAt;
    }

    @Override
    public String toString() {
        return "REP-" + id + " | " + disasterType + " | " + location
                + " | affected: " + peopleAffected;
    }
}
