/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.university.disasterresponsesystem.server.service;

import com.university.disasterresponsesystem.common.model.DisasterType;

/**
 * Pure preparedness checklists per disaster type.
 *
 * @author alisha -12268551
 */
public class PreparednessLogic {

    public String getChecklist(DisasterType type) {
        return switch (type) {
            case FIRE ->
                "Evacuate the area; shut off gas; deploy fire units; set up triage.";
            case FLOOD ->
                "Move people to higher ground; cut power to flooded zones; open shelters; issue water-safety notice.";
            case EARTHQUAKE ->
                "Search and rescue; inspect structures; restore power lines; open emergency shelters.";
            case HURRICANE ->
                "Board up buildings; pre-position supplies; open shelters; warn coastal residents.";
            case STORM ->
                "Secure loose objects; warn residents; ready power crews; monitor flooding.";
            case LANDSLIDE ->
                "Evacuate downslope areas; close affected roads; deploy rescue teams.";
            case OTHER ->
                "Assess the situation; notify relevant departments; keep the public informed.";
        };
    }
}
