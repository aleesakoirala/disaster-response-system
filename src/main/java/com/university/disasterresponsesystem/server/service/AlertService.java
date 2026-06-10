/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.university.disasterresponsesystem.server.service;

import com.university.disasterresponsesystem.common.model.Alert;
import com.university.disasterresponsesystem.common.model.AlertSeverity;
import com.university.disasterresponsesystem.dao.AlertDao;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Alert Management feature: raise, list and clear public alerts.
 *
 * @author alisha -12268551
 */
public class AlertService {

    private final AlertDao alertDao = new AlertDao();

    public Alert createAlert(Long incidentId, String message, AlertSeverity severity, String issuedBy) {
        Alert a = new Alert(incidentId, message, severity, issuedBy, LocalDateTime.now());
        return alertDao.insert(a);
    }

    public List<Alert> getAlerts(boolean activeOnly) {
        return alertDao.findAll(activeOnly);
    }

    public void deactivate(Long alertId) {
        alertDao.setActive(alertId, false);
    }
}
