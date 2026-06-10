/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.university.disasterresponsesystem.client.controller;

import com.university.disasterresponsesystem.client.net.ServerConnection;
import com.university.disasterresponsesystem.common.model.Alert;
import com.university.disasterresponsesystem.common.model.AlertSeverity;
import com.university.disasterresponsesystem.common.protocol.Request;
import com.university.disasterresponsesystem.common.protocol.RequestType;
import com.university.disasterresponsesystem.common.protocol.Response;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 *
 * @author Shobiga Jeyasekar - 12269476
 */
public class AlertController {
@FXML private TextField incidentIdField, alertIdField;
    @FXML private TextArea messageArea;
    @FXML private ChoiceBox<AlertSeverity> severityChoice;
    @FXML private ListView<Alert> alertList;
    @FXML private Label messageLabel;

    @FXML
    private void initialize() {
        severityChoice.setItems(FXCollections.observableArrayList(AlertSeverity.values()));
        refresh();
    }

    @FXML
    private void handleCreate() {
        try {
            Long incidentId = incidentIdField.getText().isBlank()
                    ? null : Long.parseLong(incidentIdField.getText().trim());
            Request req = new Request(RequestType.CREATE_ALERT, ServerConnection.currentUsername())
                    .put("incidentId", incidentId)
                    .put("message", messageArea.getText())
                    .put("severity", severityChoice.getValue().name());
            messageLabel.setText(ServerConnection.send(req).getMessage());
            refresh();
        } catch (NumberFormatException e) {
            messageLabel.setText("Incident id must be a number (or leave blank)");
        }
    }

    @FXML
    private void handleDeactivate() {
        try {
            Request req = new Request(RequestType.DEACTIVATE_ALERT, ServerConnection.currentUsername())
                    .put("alertId", Long.parseLong(alertIdField.getText().trim()));
            messageLabel.setText(ServerConnection.send(req).getMessage());
            refresh();
        } catch (NumberFormatException e) {
            messageLabel.setText("Alert id must be a number");
        }
    }

    @FXML
    @SuppressWarnings("unchecked")
    private void refresh() {
        Response r = ServerConnection.send(new Request(RequestType.GET_ALERTS, ServerConnection.currentUsername()));
        if (r.isSuccess())
            alertList.setItems(FXCollections.observableArrayList((List<Alert>) r.getData()));
    }
}
