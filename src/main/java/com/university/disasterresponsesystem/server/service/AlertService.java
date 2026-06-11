package com.university.disasterresponsesystem.server.service;

import com.university.disasterresponsesystem.common.model.Alert;
import com.university.disasterresponsesystem.common.model.AlertSeverity;
import com.university.disasterresponsesystem.dao.AlertDao;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Alert Management feature: raise, list and clear public alerts.
 * Feature 1 - Real-Time Emergency Alerts.
 *
 * @author alisha -12268551 (original stub)
 * @author Joyee Chakraborty - 12286715 (implementation)
 */
public class AlertService {

    private final AlertDao alertDao;

    /** Default constructor - uses live DAO backed by MySQL. */
    public AlertService() {
        this.alertDao = new AlertDao();
    }

    /** Testable constructor - accepts any AlertDao implementation. */
    public AlertService(AlertDao alertDao) {
        this.alertDao = alertDao;
    }

    /**
     * Creates and saves a new emergency alert.
     *
     * @param incidentId the incident this alert relates to
     * @param message    the alert message
     * @param severity   the severity level
     * @param issuedBy   username of the person issuing the alert
     * @return the saved Alert with generated ID
     */
    public Alert createAlert(Long incidentId, String message, AlertSeverity severity, String issuedBy) {
        Alert a = new Alert(incidentId, message, severity, issuedBy, LocalDateTime.now());
        return alertDao.insert(a);
    }

    /**
     * Returns all alerts, optionally filtered to active only.
     *
     * @param activeOnly if true, returns only active alerts
     * @return list of Alert objects
     */
    public List<Alert> getAlerts(boolean activeOnly) {
        return alertDao.findAll(activeOnly);
    }

    /**
     * Deactivates an alert by its ID.
     *
     * @param alertId the ID of the alert to deactivate
     */
    public void deactivate(Long alertId) {
        alertDao.setActive(alertId, false);
    }
}