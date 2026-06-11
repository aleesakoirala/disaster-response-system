package com.university.disasterresponsesystem.dao;

import com.university.disasterresponsesystem.common.model.DepartmentType;
import com.university.disasterresponsesystem.common.model.Resource;
import com.university.disasterresponsesystem.common.model.ResourceStatus;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for the resources table.
 * Supports Feature 2: Resource Allocation Tracker.
 *
 * @author Joyee Chakraborty - 12286715
 */
public class ResourceDao {

    private Connection conn() throws SQLException {
        return Database.getInstance().getConnection();
    }

    /**
     * Returns all resources.
     *
     * @return list of Resource objects
     */
    public List<Resource> findAll() {
        String sql = "SELECT * FROM resources ORDER BY id";
        List<Resource> list = new ArrayList<>();
        try (Statement st = conn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("[ResourceDao] findAll error: " + e.getMessage());
        }
        return list;
    }

    /**
     * Finds a resource by its ID. Returns null if not found.
     *
     * @param resourceId the ID to look up
     * @return Resource or null
     */
    public Resource findById(Long resourceId) {
        String sql = "SELECT * FROM resources WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setLong(1, resourceId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[ResourceDao] findById error: " + e.getMessage());
        }
        return null;
    }

    /**
     * Inserts a new resource and sets the generated ID.
     *
     * @param resource Resource to insert
     * @return Resource with id populated
     */
    public Resource insert(Resource resource) {
        String sql = "INSERT INTO resources (name, department, status, assigned_incident_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, resource.getName());
            ps.setString(2, resource.getDepartment().name());
            ps.setString(3, resource.getStatus().name());
            if (resource.getAssignedIncidentId() != null) {
                ps.setLong(4, resource.getAssignedIncidentId());
            } else {
                ps.setNull(4, Types.BIGINT);
            }
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    resource.setId(keys.getLong(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("[ResourceDao] insert error: " + e.getMessage());
        }
        return resource;
    }

    /**
     * Updates an existing resource's status and assigned incident.
     *
     * @param r Resource to update (must have a valid id)
     */
    public void update(Resource r) {
        String sql = "UPDATE resources SET name = ?, department = ?, status = ?, assigned_incident_id = ? WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, r.getName());
            ps.setString(2, r.getDepartment().name());
            ps.setString(3, r.getStatus().name());
            if (r.getAssignedIncidentId() != null) {
                ps.setLong(4, r.getAssignedIncidentId());
            } else {
                ps.setNull(4, Types.BIGINT);
            }
            ps.setLong(5, r.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[ResourceDao] update error: " + e.getMessage());
        }
    }

    private Resource mapRow(ResultSet rs) throws SQLException {
        Resource r = new Resource();
        r.setId(rs.getLong("id"));
        r.setName(rs.getString("name"));
        r.setDepartment(DepartmentType.valueOf(rs.getString("department")));
        r.setStatus(ResourceStatus.valueOf(rs.getString("status")));
        long inc = rs.getLong("assigned_incident_id");
        r.setAssignedIncidentId(rs.wasNull() ? null : inc);
        return r;
    }
}