/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.university.disasterresponsesystem.client.controller;

import com.university.disasterresponsesystem.client.net.ServerConnection;
import com.university.disasterresponsesystem.common.model.DepartmentType;
import com.university.disasterresponsesystem.common.model.Resource;
import com.university.disasterresponsesystem.common.protocol.Request;
import com.university.disasterresponsesystem.common.protocol.RequestType;
import com.university.disasterresponsesystem.common.protocol.Response;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 *
 * @author Shobiga Jeyasekar - 12269476
 */
public class ResourceController {
@FXML private TextField nameField, resourceIdField, incidentIdField;
    @FXML private ChoiceBox<DepartmentType> deptChoice;
    @FXML private ListView<Resource> resourceList;
    @FXML private Label messageLabel;

    @FXML
    private void initialize() {
        deptChoice.setItems(FXCollections.observableArrayList(DepartmentType.values()));
        refresh();
    }

    @FXML
    private void handleAdd() {
        Request req = new Request(RequestType.ADD_RESOURCE, ServerConnection.currentUsername())
                .put("name", nameField.getText())
                .put("department", deptChoice.getValue().name());
        messageLabel.setText(ServerConnection.send(req).getMessage());
        refresh();
    }

    @FXML
    private void handleAllocate() {
        try {
            Request req = new Request(RequestType.ALLOCATE_RESOURCE, ServerConnection.currentUsername())
                    .put("resourceId", Long.parseLong(resourceIdField.getText().trim()))
                    .put("incidentId", Long.parseLong(incidentIdField.getText().trim()));
            messageLabel.setText(ServerConnection.send(req).getMessage());
            refresh();
        } catch (NumberFormatException e) {
            messageLabel.setText("Resource id and incident id must be numbers");
        }
    }

    @FXML
    @SuppressWarnings("unchecked")
    private void refresh() {
        Response r = ServerConnection.send(new Request(RequestType.GET_RESOURCES, ServerConnection.currentUsername()));
        if (r.isSuccess())
            resourceList.setItems(FXCollections.observableArrayList((List<Resource>) r.getData()));
    }
}
