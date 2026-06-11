package com.university.disasterresponsesystem.server;

import com.university.disasterresponsesystem.common.model.*;
import com.university.disasterresponsesystem.common.protocol.Request;
import com.university.disasterresponsesystem.common.protocol.Response;
import com.university.disasterresponsesystem.dao.AuditDao;
import com.university.disasterresponsesystem.server.service.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Routes each request to the right service method. Enforces role-based access
 * and writes audit records.
 *
 * @author alisha - 12268551
 */
public class RequestDispatcher {

    private final AuthService auth = new AuthService();
    private final DisasterResponseService drs = new DisasterResponseService();
    private final ResourceService resources = new ResourceService();
    private final AlertService alerts = new AlertService();
    private final AuditDao audit = new AuditDao();

    public Response handle(Request req) {
        if (req == null || req.getType() == null) {
            return Response.fail("Empty request");
        }
        try {
            return switch (req.getType()) {
                case LOGIN ->
                    login(req);
                case SUBMIT_REPORT ->
                    submitReport(req);
                case GET_REPORTS ->
                    Response.ok(new ArrayList<>(drs.getReports()));
                case ASSESS_REPORT ->
                    assess(req);
                case GET_INCIDENTS ->
                    Response.ok(new ArrayList<>(drs.getIncidents()));
                case ASSIGN_DEPARTMENTS ->
                    assignDepartments(req);
                case RECORD_DAMAGE ->
                    recordDamage(req);
                case GET_CHECKLIST ->
                    Response.ok(drs.getChecklist(
                    DisasterType.valueOf(req.getString("disasterType"))));
                case RECOMMEND_RESOURCES ->
                    Response.ok(drs.recommendResources(req.getLong("incidentId")));
                case INCIDENT_SUMMARY ->
                    Response.ok(drs.incidentSummary(req.getLong("incidentId")));
                case ADD_RESOURCE ->
                    addResource(req);
                case GET_RESOURCES ->
                    Response.ok(new ArrayList<>(resources.getResources()));
                case ALLOCATE_RESOURCE ->
                    allocate(req);
                case CREATE_ALERT ->
                    createAlert(req);
                case GET_ALERTS ->
                    Response.ok(new ArrayList<>(alerts.getAlerts(false)));
                case DEACTIVATE_ALERT ->
                    deactivateAlert(req);
                case GET_AUDIT_LOG ->
                    Response.ok(new ArrayList<>(audit.findRecent(100)));
            };
        } catch (IllegalArgumentException | IllegalStateException e) {
            return Response.fail(e.getMessage());
        } catch (Exception e) {
            return Response.fail("Unexpected error: " + e.getMessage());
        }
    }

    // ── LOGIN ──────────────────────────────────────────────────────────────
    private Response login(Request req) {
        User u = auth.login(req.getString("username"), req.getString("password"));
        if (u == null) {
            return Response.fail("Invalid username or password");
        }
        u.setPasswordHash(null);
        u.setSalt(null);
        audit.log(u.getUsername(), "LOGIN", "successful login");
        return Response.ok("Welcome " + u.getFullName(), u);
    }

    // ── REPORTS ────────────────────────────────────────────────────────────
    private Response submitReport(Request req) {
        if (!hasRole(req.getUsername(), UserRole.OPERATOR)) {
            return denied();
        }
        DisasterReport r = drs.submitReport(
                req.getString("reporterName"),
                DisasterType.valueOf(req.getString("disasterType")),
                req.getString("location"),
                req.getString("description"),
                req.getInt("peopleAffected"));
        audit.log(req.getUsername(), "SUBMIT_REPORT", "report " + r.getId());
        return Response.ok("Report submitted", r);
    }

    private Response assess(Request req) {
        if (!hasRole(req.getUsername(), UserRole.COORDINATOR)) {
            return denied();
        }
        Incident i = drs.assessReport(req.getLong("reportId"), req.getInt("severityScore"));
        audit.log(req.getUsername(), "ASSESS_REPORT", "incident " + i.getId());
        return Response.ok("Report assessed", i);
    }

    @SuppressWarnings("unchecked")
    private Response assignDepartments(Request req) {
        if (!hasRole(req.getUsername(), UserRole.COORDINATOR)) {
            return denied();
        }
        List<String> names = (List<String>) req.get("departments");
        List<DepartmentType> depts = new ArrayList<>();
        if (names != null) {
            for (String n : names) {
                depts.add(DepartmentType.valueOf(n));
            }
        }
        drs.assignDepartments(req.getLong("incidentId"), depts);
        audit.log(req.getUsername(), "ASSIGN_DEPARTMENTS", "incident " + req.getLong("incidentId"));
        return Response.ok("Departments assigned", null);
    }

    private Response recordDamage(Request req) {
        if (!hasRole(req.getUsername(), UserRole.COORDINATOR)) {
            return denied();
        }
        drs.recordDamage(req.getLong("incidentId"), req.getString("damageSummary"));
        audit.log(req.getUsername(), "RECORD_DAMAGE", "incident " + req.getLong("incidentId"));
        return Response.ok("Damage recorded", null);
    }

    // ── RESOURCES ──────────────────────────────────────────────────────────
    private Response addResource(Request req) {
        if (!hasRole(req.getUsername(), UserRole.COORDINATOR)) {
            return denied();
        }
        Resource r = resources.addResource(req.getString("name"),
                DepartmentType.valueOf(req.getString("department")));
        audit.log(req.getUsername(), "ADD_RESOURCE", "resource " + r.getId());
        return Response.ok("Resource added", r);
    }

    private Response allocate(Request req) {
        if (!hasRole(req.getUsername(), UserRole.COORDINATOR)) {
            return denied();
        }
        Resource r = resources.allocate(req.getLong("resourceId"), req.getLong("incidentId"));
        audit.log(req.getUsername(), "ALLOCATE_RESOURCE",
                "resource " + r.getId() + " -> incident " + req.getLong("incidentId"));
        return Response.ok("Resource dispatched", r);
    }

    // ── ALERTS ─────────────────────────────────────────────────────────────
    private Response createAlert(Request req) {
        if (!hasRole(req.getUsername(), UserRole.COORDINATOR)) {
            return denied();
        }
        Alert a = alerts.createAlert(req.getLong("incidentId"), req.getString("message"),
                AlertSeverity.valueOf(req.getString("severity")), req.getUsername());
        audit.log(req.getUsername(), "CREATE_ALERT", "alert " + a.getId());
        return Response.ok("Alert created", a);
    }

    private Response deactivateAlert(Request req) {
        if (!hasRole(req.getUsername(), UserRole.COORDINATOR)) {
            return denied();
        }
        alerts.deactivate(req.getLong("alertId"));
        audit.log(req.getUsername(), "DEACTIVATE_ALERT", "alert " + req.getLong("alertId"));
        return Response.ok("Alert cleared", null);
    }

    // ── ROLE CHECK ─────────────────────────────────────────────────────────
    private boolean hasRole(String username, UserRole minimum) {
        UserRole role = auth.roleOf(username);
        if (role == null) {
            return false;
        }
        return rank(role) >= rank(minimum);
    }

    private int rank(UserRole r) {
        return switch (r) {
            case ADMIN ->
                3;
            case COORDINATOR ->
                2;
            case OPERATOR ->
                1;
            case VIEWER ->
                0;
        };
    }

    private Response denied() {
        return Response.fail("Access denied: insufficient role");
    }
}
