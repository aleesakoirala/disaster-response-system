/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.university.disasterresponsesystem.client.net;

import com.university.disasterresponsesystem.common.model.User;
import com.university.disasterresponsesystem.common.protocol.Request;
import com.university.disasterresponsesystem.common.protocol.Response;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author Shobiga Jeyasekar - 12269476
 */
/**
 * Client side of the socket protocol. Opens a fresh connection per request,
 * sends a Request, and reads back a Response. Also holds the logged-in session.
 */
public class ServerConnection {

    private static final String HOST = "localhost";
    private static final int PORT = 6000;

    private static User currentUser;

    public static Response send(Request request) {
        try (Socket socket = new Socket(HOST, PORT)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            out.writeObject(request);
            out.flush();
            return (Response) in.readObject();
        } catch (Exception e) {
            return Response.fail("Cannot reach server: " + e.getMessage());
        }
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static String currentUsername() {
        return currentUser == null ? null : currentUser.getUsername();
    }
}
