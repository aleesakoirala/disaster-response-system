/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.university.disasterresponsesystem.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Multi-threaded socket server. Accepts client connections and hands each one
 * to a worker thread from a pool, so many clients are served concurrently.
 *
 * @author alisha -12268551
 */
public class DrsServer {

    public static final int DEFAULT_PORT = 5000;

    private final int port;
    private final ExecutorService threadPool;
    private final RequestDispatcher dispatcher;
    private volatile boolean running;

    public DrsServer(int port) {
        this.port = port;
        this.threadPool = Executors.newFixedThreadPool(20); // up to 20 concurrent clients
        this.dispatcher = new RequestDispatcher();
    }

    public void start() {
        running = true;
        System.out.println("DRS multi-threaded server starting on port " + port + " ...");
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening. Waiting for clients.");
            while (running) {
                Socket clientSocket = serverSocket.accept();             // waits for a client
                System.out.println("Client connected from " + clientSocket.getInetAddress());
                threadPool.submit(new ClientHandler(clientSocket, dispatcher)); // own thread
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        } finally {
            threadPool.shutdown();
        }
    }

    public void stop() {
        running = false;
    }
}
