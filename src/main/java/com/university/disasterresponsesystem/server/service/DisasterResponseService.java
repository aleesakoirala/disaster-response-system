/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.university.disasterresponsesystem.server.service;

import com.university.disasterresponsesystem.common.model.DepartmentType;
import com.university.disasterresponsesystem.common.model.DisasterReport;
import com.university.disasterresponsesystem.common.model.DisasterType;
import com.university.disasterresponsesystem.common.model.Incident;
import com.university.disasterresponsesystem.common.model.IncidentStatus;
import com.university.disasterresponsesystem.common.model.PriorityLevel;
import com.university.disasterresponsesystem.common.model.Resource;
import com.university.disasterresponsesystem.common.model.ResourceStatus;
import com.university.disasterresponsesystem.dao.DisasterReportDao;
import com.university.disasterresponsesystem.dao.IncidentDao;
import com.university.disasterresponsesystem.dao.ResourceDao;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Core disaster-response workflow
 *
 * @author alisha -12268551
 */
public class DisasterResponseService {

    private final DisasterReportDao reportDao = new DisasterReportDao();
    private final IncidentDao incidentDao = new IncidentDao();
    private final ResourceDao resourceDao = new ResourceDao();
    private final AssessmentLogic assessment = new AssessmentLogic();
    private final CoordinationLogic coordination = new CoordinationLogic();
    private final PreparednessLogic preparedness = new PreparednessLogic();

    public DisasterReport submitReport(
            String reporter,
            DisasterType type,
            String location,
            String description,
            int peopleAffected) {
        DisasterReport r = new DisasterReport(reporter, type, location, description,
                peopleAffected, LocalDateTime.now());
        return reportDao.insert(r);
    }

    public List<DisasterReport> getReports() {
        return reportDao.findAll();
    }

    public List<Incident> getIncidents() {
        return incidentDao.findAll();
    }

    public Incident assessReport(Long reportId, int severityScore) {
        DisasterReport report = reportDao.findById(reportId);
        if (report == null) {
            throw new IllegalArgumentException("Report not found: " + reportId);
        }
        if (incidentDao.findByReportId(reportId) != null) {
            throw new IllegalStateException("Report already assessed");
        }
        PriorityLevel priority = assessment.determinePriority(severityScore);
        Incident incident = new Incident(report, severityScore, priority, IncidentStatus.ASSESSED);
        // auto-recommend departments
        incident.setAssignedDepartments(coordination.recommendDepartments(report.getDisasterType()));
        return incidentDao.insert(incident);
    }

    public void assignDepartments(Long incidentId, List<DepartmentType> departments) {
        Incident i = require(incidentId);
        i.setAssignedDepartments(departments);
        i.setStatus(IncidentStatus.DISPATCHED);
        incidentDao.update(i);
    }

    public void recordDamage(Long incidentId, String damageSummary) {
        Incident i = require(incidentId);
        i.setDamageSummary(damageSummary);
        incidentDao.update(i);
    }

    public String getChecklist(DisasterType type) {
        return preparedness.getChecklist(type);
    }

    public String recommendResources(Long incidentId) {
        Incident i = require(incidentId);
        List<DepartmentType> depts = coordination.recommendDepartments(i.getSourceReport().getDisasterType());
        List<Resource> all = resourceDao.findAll();
        StringBuilder sb = new StringBuilder("Recommended departments and available resources:\n");
        for (DepartmentType d : depts) {
            long available = all.stream()
                    .filter(r -> r.getDepartment() == d && r.getStatus() == ResourceStatus.AVAILABLE)
                    .count();
            sb.append(" - ").append(d).append(": ").append(available).append(" available\n");
        }
        return sb.toString();
    }

    public String incidentSummary(Long incidentId) {
        Incident i = require(incidentId);
        DisasterReport r = i.getSourceReport();
        return "Incident INC-" + i.getId() + "\n"
                + "Type: " + r.getDisasterType() + " at " + r.getLocation() + "\n"
                + "Priority: " + i.getPriorityLevel() + " | Status: " + i.getStatus() + "\n"
                + "People affected: " + r.getPeopleAffected() + "\n"
                + "Departments: " + i.getAssignedDepartments() + "\n"
                + "Damage: " + (i.getDamageSummary().isBlank() ? "(none recorded)" : i.getDamageSummary());
    }

    private Incident require(Long incidentId) {
        Incident i = incidentDao.findById(incidentId);
        if (i == null) {
            throw new IllegalArgumentException("Incident not found: " + incidentId);
        }
        return i;
    }
}
