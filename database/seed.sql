-- =============================================================
-- DRS-Enhanced - Seed Data
-- Author: Joyee Chakraborty - 12286715
-- Run schema.sql first.
-- =============================================================

USE drs_db;

-- Sample users
INSERT IGNORE INTO users (username, password_hash, salt, full_name, role) VALUES
('admin',
 'a6b3f1c2d4e5f6a7b8c9d0e1f2a3b4c5d6e7f8a9b0c1d2e3f4a5b6c7d8e9f0a1',
 'drs_salt_admin',
 'System Administrator',
 'ADMIN'),
('coordinator1',
 'b7c4d2e3f5a6b7c8d9e0f1a2b3c4d5e6f7a8b9c0d1e2f3a4b5c6d7e8f9a0b1c2',
 'drs_salt_coord1',
 'Jane Coordinator',
 'COORDINATOR'),
('operator1',
 'c8d5e3f4a6b7c8d9e0f1a2b3c4d5e6f7a8b9c0d1e2f3a4b5c6d7e8f9a0b1c2d3',
 'drs_salt_op1',
 'John Operator',
 'OPERATOR');

-- Sample disaster reports
INSERT IGNORE INTO disaster_reports (reporter_name, disaster_type, location, description, people_affected, reported_at) VALUES
('Alice Brown', 'FIRE',       'Brisbane CBD',  'Building fire on Queen Street',  50,  '2025-06-01 08:30:00'),
('Bob Smith',   'FLOOD',      'Ipswich, QLD',  'Flooding in residential area',   200, '2025-06-02 14:00:00'),
('Carol Davis', 'EARTHQUAKE', 'Gold Coast',    'Magnitude 4.2 tremor reported',  30,  '2025-06-03 09:15:00');

-- Sample incidents
INSERT IGNORE INTO incidents (report_id, severity_score, priority_level, status, assigned_departments, warning_message, damage_summary) VALUES
(1, 75, 'HIGH',     'IN_PROGRESS', 'FIRE_AND_EMERGENCY,HOSPITAL', 'Evacuate Queen Street', 'Partial structural damage'),
(2, 90, 'CRITICAL', 'DISPATCHED',  'WATER_SUPPLY,TRANSPORT',      'Flood warning issued',  'Roads submerged'),
(3, 40, 'MEDIUM',   'ASSESSED',    'HOSPITAL,ELECTRICITY',        'Aftershocks possible',  'Minor damage reported');

-- Sample alerts (Feature 1: Real-Time Emergency Alerts)
INSERT IGNORE INTO alerts (incident_id, message, severity, issued_by, issued_at, active) VALUES
(1, 'EMERGENCY: Evacuate Queen Street immediately. Fire crews on scene.', 'EMERGENCY', 'coordinator1', '2025-06-01 08:45:00', 1),
(2, 'WARNING: Ipswich flood levels rising. Avoid low-lying areas.',       'WARNING',   'coordinator1', '2025-06-02 14:30:00', 1),
(3, 'ADVISORY: Minor earthquake detected at Gold Coast. Stay calm.',      'ADVISORY',  'admin',        '2025-06-03 09:20:00', 1);

-- Sample resources (Feature 2: Resource Allocation Tracker)
INSERT IGNORE INTO resources (name, department, status, assigned_incident_id) VALUES
('Fire Truck Alpha',     'FIRE_AND_EMERGENCY', 'DISPATCHED',    1),
('Ambulance Unit 3',     'HOSPITAL',           'DISPATCHED',    1),
('Rescue Boat B1',       'FIRE_AND_EMERGENCY', 'DISPATCHED',    2),
('Police Unit 7',        'POLICE',             'AVAILABLE',     NULL),
('Water Pump Unit 2',    'WATER_SUPPLY',       'DISPATCHED',    2),
('Helicopter Med-1',     'HOSPITAL',           'AVAILABLE',     NULL),
('Power Repair Team',    'ELECTRICITY',        'DISPATCHED',    3),
('Evacuation Bus Fleet', 'TRANSPORT',          'AVAILABLE',     NULL);

-- Sample audit log
INSERT IGNORE INTO audit_log (username, action, target, performed_at) VALUES
('admin',        'LOGIN',            'system',       '2025-06-01 08:00:00'),
('coordinator1', 'CREATE_ALERT',     'alerts/1',     '2025-06-01 08:45:00'),
('coordinator1', 'ALLOCATE_RESOURCE','resources/1',  '2025-06-01 08:50:00');
