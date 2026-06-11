package com.university.disasterresponsesystem.dao;

import java.sql.*;

/**
 * Data Access Object for the audit_log table. Records every significant user
 * action for non-repudiation and privacy/security compliance (Assignment spec
 * section 2.5).
 *
 * @author Joyee Chakraborty - 12286715
 */
public class AuditDao {

    private Connection conn() throws SQLException {
        return Database.getInstance().getConnection();
    }

    /**
     * Logs a user action.
     *
     * @param username the actor's username
     * @param action a short action code, e.g. "CREATE_ALERT"
     * @param target the affected resource, e.g. "alerts/5" (may be null)
     */
    public void log(String username, String action, String target) {
        String sql = "INSERT INTO audit_log (username, action, target) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, action);
            ps.setString(3, target);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[AuditDao] log error: " + e.getMessage());
        }
    }

    public java.util.List<String> findRecent(int limit) {
        java.util.List<String> list = new java.util.ArrayList<>();
        String sql = "SELECT * FROM audit_log ORDER BY id DESC LIMIT ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(rs.getString("username") + " | "
                            + rs.getString("action") + " | "
                            + rs.getString("target"));
                }
            }
        } catch (SQLException e) {
            System.err.println("[AuditDao] findRecent error: " + e.getMessage());
        }
        return list;
    }

    @Override
    public String toString() {
        return "AuditDao{}";
    }
}
