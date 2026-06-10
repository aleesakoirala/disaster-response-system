/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.university.disasterresponsesystem.server.service;

import com.university.disasterresponsesystem.common.model.DisasterReport;
import com.university.disasterresponsesystem.common.model.PriorityLevel;

/**
 * Pure assessment rules (no database) so they are easy to unit-test.
 *
 * @author alisha -12268551
 */
public class AssessmentLogic {

    /**
     * Maps a 1-10 severity score to a priority level.
     */
    public PriorityLevel determinePriority(int severityScore) {
        if (severityScore >= 9) {
            return PriorityLevel.CRITICAL;
        }
        if (severityScore >= 7) {
            return PriorityLevel.HIGH;
        }
        if (severityScore >= 4) {
            return PriorityLevel.MEDIUM;
        }
        return PriorityLevel.LOW;
    }

    /**
     * Suggests a severity score (1-10) from the report's scale.
     */
    public int suggestSeverity(DisasterReport report) {
        int people = report.getPeopleAffected();
        int base;
        if (people >= 1000) {
            base = 10;
        } else if (people >= 500) {
            base = 8;
        } else if (people >= 100) {
            base = 6;
        } else if (people >= 20) {
            base = 4;
        } else {
            base = 2;
        }
        return Math.max(1, Math.min(10, base));
    }
}
