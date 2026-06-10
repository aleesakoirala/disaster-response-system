/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.university.disasterresponsesystem.server;

/**
 * Entry point for the multi-threaded DRS server.
 *
 * @author alisha -12268551
 */
public class ServerApp {

    public static void main(String[] args) {
        int port = DrsServer.DEFAULT_PORT;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignored) {
            }
        }
        new DrsServer(port).start();
    }
}
