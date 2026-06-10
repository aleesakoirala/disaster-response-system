/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.university.disasterresponsesystem.client.controller;

import com.university.disasterresponsesystem.client.net.ServerConnection;
import com.university.disasterresponsesystem.common.model.User;
import com.university.disasterresponsesystem.common.protocol.Request;
import com.university.disasterresponsesystem.common.protocol.RequestType;
import com.university.disasterresponsesystem.common.protocol.Response;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author Shobiga Jeyasekar - 12269476
 */
public class LoginController {
@FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    @FXML
    private void handleLogin() {
        Request req = new Request(RequestType.LOGIN, null)
                .put("username", usernameField.getText())
                .put("password", passwordField.getText());
        Response resp = ServerConnection.send(req);
        if (!resp.isSuccess()) {
            messageLabel.setText(resp.getMessage());
            return;
        }
        ServerConnection.setCurrentUser((User) resp.getData());
        openDashboard();
    }

    private void openDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/university/disasterresponsesystem/view/dashboard.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setTitle("DRS-Enhanced — Dashboard (" + ServerConnection.getCurrentUser().getRole() + ")");
            stage.setScene(scene);
        } catch (Exception e) {
            messageLabel.setText("Cannot open dashboard: " + e.getMessage());
        }
    }
}
