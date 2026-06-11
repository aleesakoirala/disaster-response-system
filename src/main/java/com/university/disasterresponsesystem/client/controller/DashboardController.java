/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.university.disasterresponsesystem.client.controller;

import com.university.disasterresponsesystem.client.net.ServerConnection;
import com.university.disasterresponsesystem.common.model.DisasterReport;
import com.university.disasterresponsesystem.common.model.DisasterType;
import com.university.disasterresponsesystem.common.model.Incident;
import com.university.disasterresponsesystem.common.protocol.Request;
import com.university.disasterresponsesystem.common.protocol.RequestType;
import com.university.disasterresponsesystem.common.protocol.Response;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author Shobiga Jeyasekar - 12269476
 */
public class DashboardController {

    @FXML
    private ChoiceBox<DisasterType> typeChoice;
    @FXML
    private TextField reporterField, locationField, peopleField, reportIdField, severityField;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private ListView<DisasterReport> reportsList;
    @FXML
    private ListView<Incident> incidentsList;
    @FXML
    private Label messageLabel;

    @FXML
    private void initialize() {
        typeChoice.setItems(FXCollections.observableArrayList(DisasterType.values()));

        reportsList.setCellFactory(lv -> new ListCell<DisasterReport>() {
            @Override
            protected void updateItem(DisasterReport item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.toString());
            }
        });

        incidentsList.setCellFactory(lv -> new ListCell<Incident>() {
            @Override
            protected void updateItem(Incident item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.toString());
            }
        });

        refresh();
    }

    @FXML
    private void handleSubmitReport() {
        try {
            Request req = new Request(RequestType.SUBMIT_REPORT, ServerConnection.currentUsername())
                    .put("reporterName", reporterField.getText())
                    .put("disasterType", typeChoice.getValue().name())
                    .put("location", locationField.getText())
                    .put("description", descriptionArea.getText())
                    .put("peopleAffected", Integer.parseInt(peopleField.getText().trim()));
            show(ServerConnection.send(req));
            refresh();
            reporterField.clear();
            locationField.clear();
            descriptionArea.clear();
            peopleField.clear();
            typeChoice.setValue(null);
        } catch (NumberFormatException e) {
            messageLabel.setText("People affected must be a number");
        }
    }

    @FXML
    private void handleAssess() {
        try {
            Request req = new Request(RequestType.ASSESS_REPORT, ServerConnection.currentUsername())
                    .put("reportId", Long.parseLong(reportIdField.getText().trim()))
                    .put("severityScore", Integer.parseInt(severityField.getText().trim()));
            show(ServerConnection.send(req));
            refresh();
        } catch (NumberFormatException e) {
            messageLabel.setText("Report id and severity must be numbers");
        }
    }

    @FXML
    @SuppressWarnings("unchecked")
    private void refresh() {
        Response reps = ServerConnection.send(new Request(RequestType.GET_REPORTS, ServerConnection.currentUsername()));
        if (reps.isSuccess()) {
            reportsList.setItems(FXCollections.observableArrayList((List<DisasterReport>) reps.getData()));
        }
        Response incs = ServerConnection.send(new Request(RequestType.GET_INCIDENTS, ServerConnection.currentUsername()));
        if (incs.isSuccess()) {
            incidentsList.setItems(FXCollections.observableArrayList((List<Incident>) incs.getData()));
        }
    }

    @FXML
    private void openResources() {
        openWindow("resources.fxml", "Resource Management");
    }

    @FXML
    private void openAlerts() {
        openWindow("alerts.fxml", "Alert Management");
    }

    private void openWindow(String fxml, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/university/disasterresponsesystem/view/" + fxml));
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (Exception e) {
            messageLabel.setText("Cannot open " + title + ": " + e.getMessage());
        }
    }

    private void show(Response r) {
        messageLabel.setText(r.getMessage());
    }

}
