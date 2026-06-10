/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.university.disasterresponsesystem.server.service;

import com.university.disasterresponsesystem.common.model.User;
import com.university.disasterresponsesystem.common.model.UserRole;
import com.university.disasterresponsesystem.dao.UserDao;

/**
 * Authentication + user creation
 *
 * @author alisha -12268551
 */
public class AuthService {

    private final UserDao userDao = new UserDao();

    public User createUser(String username, String password, String fullName, UserRole role) {
        String salt = PasswordUtil.generateSalt();
        String hash = PasswordUtil.hash(password, salt);
        return userDao.insert(new User(username, hash, salt, fullName, role));
    }

    /**
     * Returns the user on success, or null on bad credentials.
     *
     * @param username
     * @param password
     * @return
     */
    public User login(String username, String password) {
        User u = userDao.findByUsername(username);
        if (u == null) {
            return null;
        }
        return PasswordUtil.verify(password, u.getSalt(), u.getPasswordHash()) ? u : null;
    }

    public UserRole roleOf(String username) {
        User u = userDao.findByUsername(username);
        return (u == null) ? null : u.getRole();
    }

    /**
     * Creates a default admin (admin/admin123) if no users exist yet.
     */
    public void seedDefaultAdmin() {
        if (userDao.count() == 0) {
            createUser("admin", "admin123", "System Administrator", UserRole.ADMIN);
            System.out.println("Seeded default admin user: admin / admin123");
        }
    }
}
