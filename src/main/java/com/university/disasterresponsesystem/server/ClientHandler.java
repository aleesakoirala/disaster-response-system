/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.university.disasterresponsesystem.server;

import com.university.disasterresponsesystem.common.protocol.Request;
import com.university.disasterresponsesystem.common.protocol.Response;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Handles one client connection on its own thread: read Request, reply
 * Response.
 *
 * @author alisha -12268551
 */
public class ClientHandler implements Runnable {

    private final Socket socket;
    private final RequestDispatcher dispatcher;

    public ClientHandler(Socket socket, RequestDispatcher dispatcher) {
        this.socket = socket;
        this.dispatcher = dispatcher;
    }

    @Override
    public void run() {
        String thread = Thread.currentThread().getName();
        try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
            out.flush(); // send stream header first (avoids client/server deadlock)
            try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

                Request request = (Request) in.readObject();
                System.out.println("[" + thread + "] handling " + request.getType()
                        + " (user=" + request.getUsername() + ")");

                Response response;
                try {
                    response = dispatcher.handle(request);
                } catch (Exception ex) {
                    response = Response.fail("Server error: " + ex.getMessage());
                }
                out.writeObject(response);
                out.flush();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[" + thread + "] connection error: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {
            }
        }
    }
}
