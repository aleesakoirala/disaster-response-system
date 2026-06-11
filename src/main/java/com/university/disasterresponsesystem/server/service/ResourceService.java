package com.university.disasterresponsesystem.server.service;

import com.university.disasterresponsesystem.common.model.DepartmentType;
import com.university.disasterresponsesystem.common.model.Resource;
import com.university.disasterresponsesystem.common.model.ResourceStatus;
import com.university.disasterresponsesystem.dao.IncidentDao;
import com.university.disasterresponsesystem.dao.ResourceDao;
import java.util.List;

/**
 * Resource Management feature: register, list and dispatch resources.
 * Feature 2 - Resource Allocation Tracker.
 *
 * @author alisha -12268551 (original stub)
 * @author Joyee Chakraborty - 12286715 (implementation)
 */
public class ResourceService {

    private final ResourceDao resourceDao;
    private final IncidentDao incidentDao;

    /** Default constructor - uses live DAOs backed by MySQL. */
    public ResourceService() {
        this.resourceDao = new ResourceDao();
        this.incidentDao = new IncidentDao();
    }

    /** Testable constructor - accepts any DAO implementations. */
    public ResourceService(ResourceDao resourceDao, IncidentDao incidentDao) {
        this.resourceDao = resourceDao;
        this.incidentDao = incidentDao;
    }

    /**
     * Registers a new resource in the system.
     *
     * @param name       the resource name
     * @param department the department it belongs to
     * @return the saved Resource with generated ID
     */
    public Resource addResource(String name, DepartmentType department) {
        return resourceDao.insert(new Resource(name, department));
    }

    /**
     * Returns all resources in the system.
     *
     * @return list of Resource objects
     */
    public List<Resource> getResources() {
        return resourceDao.findAll();
    }

    /**
     * Dispatches an available resource to an incident.
     *
     * @param resourceId the resource to dispatch
     * @param incidentId the target incident
     * @return the updated Resource
     * @throws IllegalArgumentException if resource or incident not found
     * @throws IllegalStateException    if resource is not AVAILABLE
     */
    public Resource allocate(Long resourceId, Long incidentId) {
        Resource r = resourceDao.findById(resourceId);
        if (r == null) {
            throw new IllegalArgumentException("Resource not found: " + resourceId);
        }
        if (r.getStatus() != ResourceStatus.AVAILABLE) {
            throw new IllegalStateException("Resource is not available: " + resourceId);
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