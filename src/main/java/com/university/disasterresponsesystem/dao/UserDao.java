package com.university.disasterresponsesystem.dao;

import com.university.disasterresponsesystem.common.model.User;
import com.university.disasterresponsesystem.common.model.UserRole;
import java.sql.*;

/**
 * Data Access Object for the users table.
 *
 * @author Joyee Chakraborty - 12286715
 */
public class UserDao {

    private Connection conn() throws SQLException {
        return Database.getInstance().getConnection();
    }

    /**
     * Finds a user by username. Returns null if not found.
     *
     * @param username the username to look up
     * @return User object or null
     */
    public User findByUsername(String username) {
        String sql = "SELECT id, username, password_hash, salt, full_name, role FROM users WHERE username = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[UserDao] findByUsername error: " + e.getMessage());
        }
        return null;
    }

    /**
     * Returns the total number of users in the database.
     *
     * @return row count
     */
    public int count() {
        String sql = "SELECT COUNT(*) FROM users";
        try (Statement st = conn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("[UserDao] count error: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Inserts a new user and sets the generated ID on the returned object.
     *
     * @param user User to insert (id must be null)
     * @return User with id populated
     */
    public User insert(User user) {
        String sql = "INSERT INTO users (username, password_hash, salt, full_name, role) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getSalt());
            ps.setString(4, user.getFullName());
            ps.setString(5, user.getRole().name());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    user.setId(keys.getLong(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("[UserDao] insert error: " + e.getMessage());
        }
        return user;
    }

    private User mapRow(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getLong("id"));
        u.setUsername(rs.getString("username"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setSalt(rs.getString("salt"));
        u.setFullName(rs.getString("full_name"));
        u.setRole(UserRole.valueOf(rs.getString("role")));
        return u;
    }
}