/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.university.disasterresponsesystem.common.model;

import java.io.Serializable;

/**
 * A response resource (rescue team, ambulance, fire unit, etc.). Resource
 * Management feature: tracked and dispatched to incidents.
 *
 * @author alisha -12268551
 */
public class Resource implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private DepartmentType department;
    private ResourceStatus status;
    private Long assignedIncidentId;   // null when not dispatched

    public Resource() {
    }

    public Resource(String name, DepartmentType department) {
        this.name = name;
        this.department = department;
        this.status = ResourceStatus.AVAILABLE;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DepartmentType getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentType department) {
        this.department = department;
    }

    public ResourceStatus getStatus() {
        return status;
    }

    public void setStatus(ResourceStatus status) {
        this.status = status;
    }

    public Long getAssignedIncidentId() {
        return assignedIncidentId;
    }

    public void setAssignedIncidentId(Long assignedIncidentId) {
        this.assignedIncidentId = assignedIncidentId;
    }

    @Override
    public String toString() {
        String where = (assignedIncidentId == null) ? "-" : ("INC-" + assignedIncidentId);
        return name + " | " + department + " | " + status + " | incident: " + where;
    }
}
