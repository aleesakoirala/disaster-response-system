package com.university.disasterresponsesystem.dao;

import com.university.disasterresponsesystem.common.model.Alert;
import com.university.disasterresponsesystem.common.model.AlertSeverity;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for the alerts table.
 * Supports Feature 1: Real-Time Emergency Alerts.
 *
 * @author Joyee Chakraborty - 12286715
 */
public class AlertDao {

    private Connection conn() throws SQLException {
        return Database.getInstance().getConnection();
    }

    /**
     * Inserts a new alert and sets the generated ID.
     *
     * @param a Alert to insert
     * @return Alert with id populated
     */
    public Alert insert(Alert a) {
        String sql = "INSERT INTO alerts (incident_id, message, severity, issued_by, issued_at, active) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, a.getIncidentId());
            ps.setString(2, a.getMessage());
            ps.setString(3, a.getSeverity().name());
            ps.setString(4, a.getIssuedBy());
            ps.setTimestamp(5, Timestamp.valueOf(a.getIssuedAt()));
            ps.setBoolean(6, a.isActive());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    a.setId(keys.getLong(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("[AlertDao] insert error: " + e.getMessage());
        }
        return a;
    }

    /**
     * Returns all alerts, optionally filtered to only active ones.
     *
     * @param activeOnly if true, returns only alerts where active = 1
     * @return list of Alert objects
     */
    public List<Alert> findAll(boolean activeOnly) {
        String sql = activeOnly
                ? "SELECT * FROM alerts WHERE active = 1 ORDER BY issued_at DESC"
                : "SELECT * FROM alerts ORDER BY issued_at DESC";
        List<Alert> list = new ArrayList<>();
        try (Statement st = conn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("[AlertDao] findAll error: " + e.getMessage());
        }
        return list;
    }

    /**
     * Activates or deactivates an alert by its ID.
     *
     * @param alertId ID of the alert to update
     * @param active  true to activate, false to deactivate
     */
    public void setActive(Long alertId, boolean active) {
        String sql = "UPDATE alerts SET active = ? WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setBoolean(1, active);
            ps.setLong(2, alertId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[AlertDao] setActive error: " + e.getMessage());
        }
    }

    private Alert mapRow(ResultSet rs) throws SQLException {
        Alert a = new Alert();
        a.setId(rs.getLong("id"));
        a.setIncidentId(rs.getLong("incident_id"));
        a.setMessage(rs.getString("message"));
        a.setSeverity(AlertSeverity.valueOf(rs.getString("severity")));
        a.setIssuedBy(rs.getString("issued_by"));
        a.setIssuedAt(rs.getTimestamp("issued_at").toLocalDateTime());
        a.setActive(rs.getBoolean("active"));
        return a;
    }
}