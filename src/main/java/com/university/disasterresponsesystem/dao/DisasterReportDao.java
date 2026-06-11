package com.university.disasterresponsesystem.dao;

import com.university.disasterresponsesystem.common.model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Data Access Object for the incidents table.
 *
 * @author Joyee Chakraborty - 12286715
 */
public class IncidentDao {

    private final DisasterReportDao reportDao = new DisasterReportDao();

    private Connection conn() throws SQLException {
        return Database.getInstance().getConnection();
    }

    /**
     * Returns all incidents with their linked DisasterReport populated.
     *
     * @return list of Incident
     */
    public List<Incident> findAll() {
        String sql = "SELECT * FROM incidents ORDER BY id DESC";
        List<Incident> list = new ArrayList<>();
        try (Statement st = conn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("[IncidentDao] findAll error: " + e.getMessage());
        }
        return list;
    }

    /**
     * Finds an incident by its ID. Returns null if not found.
     *
     * @param incidentId the ID to look up
     * @return Incident or null
     */
    public Incident findById(Long incidentId) {
        String sql = "SELECT * FROM incidents WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setLong(1, incidentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[IncidentDao] findById error: " + e.getMessage());
        }
        return null;
    }

    /**
     * Finds the incident linked to a specific disaster report ID.
     *
     * @param reportId the source report ID
     * @return Incident or null
     */
    public Object findByReportId(Long reportId) {
        String sql = "SELECT * FROM incidents WHERE report_id = ? LIMIT 1";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setLong(1, reportId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[IncidentDao] findByReportId error: " + e.getMessage());
        }
        return null;
    }

    /**
     * Inserts a new incident and sets the generated ID.
     *
     * @param incident Incident to insert
     * @return Incident with id populated
     */
    public Incident insert(Incident incident) {
        String sql = "INSERT INTO incidents "
                   + "(report_id, severity_score, priority_level, status, assigned_departments, warning_message, damage_summary) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, incident.getSourceReport().getId());
            ps.setInt(2, incident.getSeverityScore());
            ps.setString(3, incident.getPriorityLevel().name());
            ps.setString(4, incident.getStatus().name());
            ps.setString(5, departmentsToString(incident.getAssignedDepartments()));
            ps.setString(6, incident.getWarningMessage());
            ps.setString(7, incident.getDamageSummary());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    incident.setId(keys.getLong(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("[IncidentDao] insert error: " + e.getMessage());
        }
        return incident;
    }

    /**
     * Updates an existing incident record.
     *
     * @param i Incident to update (must have valid id)
     */
    public void update(Incident i) {
        String sql = "UPDATE incidents SET severity_score=?, priority_level=?, status=?, "
                   + "assigned_departments=?, warning_message=?, damage_summary=? WHERE id=?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, i.getSeverityScore());
            ps.setString(2, i.getPriorityLevel().name());
            ps.setString(3, i.getStatus().name());
            ps.setString(4, departmentsToString(i.getAssignedDepartments()));
            ps.setString(5, i.getWarningMessage());
            ps.setString(6, i.getDamageSummary());
            ps.setLong(7, i.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[IncidentDao] update error: " + e.getMessage());
        }
    }

    private Incident mapRow(ResultSet rs) throws SQLException {
        Incident i = new Incident();
        i.setId(rs.getLong("id"));
        long reportId = rs.getLong("report_id");
        i.setSourceReport(reportDao.findById(reportId));
        i.setSeverityScore(rs.getInt("severity_score"));
        i.setPriorityLevel(PriorityLevel.valueOf(rs.getString("priority_level")));
        i.setStatus(IncidentStatus.valueOf(rs.getString("status")));
        i.setAssignedDepartments(stringToDepartments(rs.getString("assigned_departments")));
        i.setWarningMessage(rs.getString("warning_message"));
        i.setDamageSummary(rs.getString("damage_summary"));
        return i;
    }

    private String departmentsToString(List<DepartmentType> depts) {
        if (depts == null || depts.isEmpty()) return "";
        return depts.stream().map(Enum::name).collect(Collectors.joining(","));
    }

    private List<DepartmentType> stringToDepartments(String s) {
        List<DepartmentType> result = new ArrayList<>();
        if (s == null || s.isBlank()) return result;
        for (String name : s.split(",")) {
            try {
                result.add(DepartmentType.valueOf(name.trim()));
            } catch (IllegalArgumentException ignored) { }
        }
        return result;
    }
}