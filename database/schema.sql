-- =============================================================
-- Disaster Response System (DRS-Enhanced) - Database Schema
-- Author: Joyee Chakraborty - 12286715
-- =============================================================

CREATE DATABASE IF NOT EXISTS drs_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE drs_db;

-- -------------------------------------------------------------
-- users
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS users (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    username      VARCHAR(50)  NOT NULL UNIQUE,
    password_hash VARCHAR(128) NOT NULL,
    salt          VARCHAR(64)  NOT NULL,
    full_name     VARCHAR(100) NOT NULL,
    role          ENUM('ADMIN','COORDINATOR','OPERATOR','VIEWER') NOT NULL DEFAULT 'OPERATOR',
    created_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- -------------------------------------------------------------
-- disaster_reports
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS disaster_reports (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    reporter_name    VARCHAR(100) NOT NULL,
    disaster_type    ENUM('FIRE','FLOOD','EARTHQUAKE','HURRICANE','STORM','LANDSLIDE','OTHER') NOT NULL,
    location         VARCHAR(200) NOT NULL,
    description      TEXT,
    people_affected  INT          NOT NULL DEFAULT 0,
    reported_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- -------------------------------------------------------------
-- incidents
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS incidents (
    id                   BIGINT AUTO_INCREMENT PRIMARY KEY,
    report_id            BIGINT NOT NULL,
    severity_score       INT    NOT NULL DEFAULT 0,
    priority_level       ENUM('LOW','MEDIUM','HIGH','CRITICAL') NOT NULL DEFAULT 'LOW',
    status               ENUM('REPORTED','ASSESSED','DISPATCHED','IN_PROGRESS','RESOLVED') NOT NULL DEFAULT 'REPORTED',
    assigned_departments TEXT,
    warning_message      TEXT,
    damage_summary       TEXT,
    created_at           DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at           DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_incident_report FOREIGN KEY (report_id) REFERENCES disaster_reports(id)
);

-- -------------------------------------------------------------
-- alerts  (Feature 1: Real-Time Emergency Alerts)
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS alerts (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    incident_id BIGINT       NOT NULL,
    message     TEXT         NOT NULL,
    severity    ENUM('INFO','ADVISORY','WATCH','WARNING','EMERGENCY') NOT NULL DEFAULT 'INFO',
    issued_by   VARCHAR(50)  NOT NULL,
    issued_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    active      TINYINT(1)   NOT NULL DEFAULT 1,
    CONSTRAINT fk_alert_incident FOREIGN KEY (incident_id) REFERENCES incidents(id)
);

-- -------------------------------------------------------------
-- resources  (Feature 2: Resource Allocation Tracker)
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS resources (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    name                VARCHAR(100) NOT NULL,
    department          ENUM('FIRE_AND_EMERGENCY','HOSPITAL','POLICE','ELECTRICITY',
                             'TRANSPORT','WASTE_MANAGEMENT','WATER_SUPPLY','SHELTER_SERVICES') NOT NULL,
    status              ENUM('AVAILABLE','DISPATCHED','OUT_OF_SERVICE') NOT NULL DEFAULT 'AVAILABLE',
    assigned_incident_id BIGINT DEFAULT NULL,
    CONSTRAINT fk_resource_incident FOREIGN KEY (assigned_incident_id) REFERENCES incidents(id)
);

-- -------------------------------------------------------------
-- audit_log  (privacy & security)
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS audit_log (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50)  NOT NULL,
    action      VARCHAR(100) NOT NULL,
    target      VARCHAR(100),
    performed_at DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP
);
