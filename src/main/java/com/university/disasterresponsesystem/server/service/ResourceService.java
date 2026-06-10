/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.university.disasterresponsesystem.server.service;

import com.university.disasterresponsesystem.common.model.DepartmentType;
import com.university.disasterresponsesystem.common.model.Resource;
import com.university.disasterresponsesystem.common.model.ResourceStatus;
import com.university.disasterresponsesystem.dao.IncidentDao;
import com.university.disasterresponsesystem.dao.ResourceDao;
import java.util.List;

/**
 * Resource Management feature: register, list and dispatch resources.
 *
 * @author alisha -12268551
 */
public class ResourceService {

    private final ResourceDao resourceDao = new ResourceDao();
    private final IncidentDao incidentDao = new IncidentDao();

    public Resource addResource(String name, DepartmentType department) {
        return resourceDao.insert(new Resource(name, department));
    }

    public List<Resource> getResources() {
        return resourceDao.findAll();
    }

    /**
     * Dispatches an available resource to an incident.
     *
     * @param resourceId
     * @param incidentId
     * @return
     */
    public Resource allocate(Long resourceId, Long incidentId) {
        Resource r = resourceDao.findById(resourceId);
        if (r == null) {
            throw new IllegalArgumentException("Resource not found: " + resourceId);
        }
        if (r.getStatus() != ResourceStatus.AVAILABLE) {
            throw new IllegalStateException("Resource is not available");
        }
        if (incidentDao.findById(incidentId) == null) {
            throw new IllegalArgumentException("Incident not found: " + incidentId);
        }
        r.setStatus(ResourceStatus.DISPATCHED);
        r.setAssignedIncidentId(incidentId);
        resourceDao.update(r);
        return r;
    }
}
