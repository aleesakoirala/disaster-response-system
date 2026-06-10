/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.university.disasterresponsesystem.client;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Shobiga Jeyasekar - 12269476
 */
public class ClientApp extends Application{
@Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/com/university/disasterresponsesystem/view/login.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("DRS-Enhanced — Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) { launch(args); }
}
