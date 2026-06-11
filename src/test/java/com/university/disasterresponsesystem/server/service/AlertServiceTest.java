package com.university.disasterresponsesystem.server.service;

import com.university.disasterresponsesystem.common.model.Alert;
import com.university.disasterresponsesystem.common.model.AlertSeverity;
import com.university.disasterresponsesystem.dao.AlertDao;
import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Feature 1: Real-Time Emergency Alerts.
 *
 * @author Joyee Chakraborty - 12286715
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AlertServiceTest {

    private static class InMemoryAlertDao extends AlertDao {

        private final java.util.List<Alert> store = new java.util.ArrayList<>();
        private long idSeq = 1;

        @Override
        public Alert insert(Alert a) {
            a.setId(idSeq++);
            store.add(a);
            return a;
        }

        @Override
        public java.util.List<Alert> findAll(boolean activeOnly) {
            if (!activeOnly) return new java.util.ArrayList<>(store);
            java.util.List<Alert> active = new java.util.ArrayList<>();
            for (Alert a : store) {
                if (a.isActive()) active.add(a);
            }
            return active;
        }

        @Override
        public void setActive(Long alertId, boolean active) {
            for (Alert a : store) {
                if (a.getId().equals(alertId)) {
                    a.setActive(active);
                    return;
                }
            }
        }
    }

    private AlertService service;

    @BeforeEach
    void setUp() {
        service = new AlertService(new InMemoryAlertDao());
    }

    @Test
    @Order(1)
    @DisplayName("createAlert - returns alert with generated ID")
    void testCreateAlertReturnsId() {
        Alert a = service.createAlert(1L, "Test message", AlertSeverity.WARNING, "operator1");
        assertNotNull(a, "Alert should not be null");
        assertNotNull(a.getId(), "Generated ID should not be null");
        assertEquals("Test message", a.getMessage());
        assertEquals(AlertSeverity.WARNING, a.getSeverity());
        assertTrue(a.isActive(), "New alert should be active by default");
    }

    @Test
    @Order(2)
    @DisplayName("createAlert - issuedAt timestamp is set automatically")
    void testCreateAlertTimestamp() {
        Alert a = service.createAlert(1L, "Flood warning", AlertSeverity.EMERGENCY, "admin");
        assertNotNull(a.getIssuedAt(), "issuedAt should be set");
    }

    @Test
    @Order(3)
    @DisplayName("getAlerts(activeOnly=true) - returns only active alerts")
    void testGetActiveAlerts() {
        service.createAlert(1L, "Active alert", AlertSeverity.WARNING, "admin");
        Alert second = service.createAlert(2L, "Will be cleared", AlertSeverity.INFO, "admin");
        service.deactivate(second.getId());

        List<Alert> active = service.getAlerts(true);
        assertEquals(1, active.size(), "Only one alert should be active");
        assertTrue(active.get(0).isActive());
    }

    @Test
    @Order(4)
    @DisplayName("getAlerts(activeOnly=false) - returns all alerts including cleared")
    void testGetAllAlerts() {
        service.createAlert(1L, "Alert A", AlertSeverity.ADVISORY, "operator1");
        service.createAlert(2L, "Alert B", AlertSeverity.WATCH, "operator1");
        List<Alert> all = service.getAlerts(false);
        assertEquals(2, all.size(), "Should return all two alerts");
    }

    @Test
    @Order(5)
    @DisplayName("deactivate - sets alert active flag to false")
    void testDeactivate() {
        Alert a = service.createAlert(1L, "To be cleared", AlertSeverity.WARNING, "admin");
        service.deactivate(a.getId());
        List<Alert> active = service.getAlerts(true);
        assertTrue(active.isEmpty(), "No active alerts should remain after deactivation");
    }

    @Test
    @Order(6)
    @DisplayName("createAlert - EMERGENCY severity is stored correctly")
    void testEmergencySeverityStored() {
        Alert a = service.createAlert(1L, "Critical event", AlertSeverity.EMERGENCY, "coordinator1");
        assertEquals(AlertSeverity.EMERGENCY, a.getSeverity());
    }

    @Test
    @Order(7)
    @DisplayName("createAlert - issuedBy field is persisted")
    void testIssuedByField() {
        Alert a = service.createAlert(3L, "Resource alert", AlertSeverity.INFO, "coordinator1");
        assertEquals("coordinator1", a.getIssuedBy());
    }

    @Test
    @Order(8)
    @DisplayName("createAlert with null message - insert still called without exception")
    void testCreateAlertNullMessage() {
        assertDoesNotThrow(() ->
            service.createAlert(1L, null, AlertSeverity.INFO, "admin"),
            "createAlert should not throw even with null message"
        );
    }
}