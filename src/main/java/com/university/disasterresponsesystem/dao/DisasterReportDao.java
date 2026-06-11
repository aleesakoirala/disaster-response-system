package com.university.disasterresponsesystem.dao;

import com.university.disasterresponsesystem.common.model.DisasterReport;
import com.university.disasterresponsesystem.common.model.DisasterType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for the disaster_reports table.
 *
 * @author Joyee Chakraborty - 12286715
 */
public class DisasterReportDao {

    private Connection conn() throws SQLException {
        return Database.getInstance().getConnection();
    }

    public DisasterReport insert(DisasterReport r) {
        String sql = "INSERT INTO disaster_reports "
                   + "(reporter_name, disaster_type, location, description, people_affected, reported_at) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, r.getReporterName());
            ps.setString(2, r.getDisasterType().name());
            ps.setString(3, r.getLocation());
            ps.setString(4, r.getDescription());
            ps.setInt(5, r.getPeopleAffected());
            ps.setTimestamp(6, Timestamp.valueOf(r.getReportedAt()));
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    r.setId(keys.getLong(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("[DisasterReportDao] insert error: " + e.getMessage());
        }
        return r;
    }

    public List<DisasterReport> findAll() {
        String sql = "SELECT * FROM disaster_reports ORDER BY reported_at DESC";
        List<DisasterReport> list = new ArrayList<>();
        try (Statement st = conn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("[DisasterReportDao] findAll error: " + e.getMessage());
        }
        return list;
    }

    public DisasterReport findById(Long reportId) {
        String sql = "SELECT * FROM disaster_reports WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setLong(1, reportId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[DisasterReportDao] findById error: " + e.getMessage());
        }
        return null;
    }

    private DisasterReport mapRow(ResultSet rs) throws SQLException {
        DisasterReport d = new DisasterReport();
        d.setId(rs.getLong("id"));
        d.setReporterName(rs.getString("reporter_name"));
        d.setDisasterType(DisasterType.valueOf(rs.getString("disaster_type")));
        d.setLocation(rs.getString("location"));
        d.setDescription(rs.getString("description"));
        d.setPeopleAffected(rs.getInt("people_affected"));
        d.setReportedAt(rs.getTimestamp("reported_at").toLocalDateTime());
        return d;
    }
}