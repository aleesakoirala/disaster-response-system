/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.university.disasterresponsesystem.server.service;

import com.university.disasterresponsesystem.common.model.DepartmentType;
import com.university.disasterresponsesystem.common.model.DisasterType;
import java.util.List;

/**
 * Pure coordination rules: which departments respond to which disaster.
 *
 * @author alisha -12268551
 */
public class CoordinationLogic {

    public List<DepartmentType> recommendDepartments(DisasterType type) {
        return switch (type) {
            case FIRE ->
                List.of(DepartmentType.FIRE_AND_EMERGENCY, DepartmentType.HOSPITAL, DepartmentType.POLICE);
            case FLOOD ->
                List.of(DepartmentType.SHELTER_SERVICES, DepartmentType.WATER_SUPPLY,
                DepartmentType.TRANSPORT, DepartmentType.HOSPITAL);
            case EARTHQUAKE ->
                List.of(DepartmentType.FIRE_AND_EMERGENCY, DepartmentType.HOSPITAL,
                DepartmentType.SHELTER_SERVICES, DepartmentType.ELECTRICITY);
            case HURRICANE, STORM ->
                List.of(DepartmentType.SHELTER_SERVICES, DepartmentType.ELECTRICITY,
                DepartmentType.TRANSPORT, DepartmentType.WASTE_MANAGEMENT);
            case LANDSLIDE ->
                List.of(DepartmentType.FIRE_AND_EMERGENCY, DepartmentType.TRANSPORT, DepartmentType.HOSPITAL);
            case OTHER ->
                List.of(DepartmentType.POLICE, DepartmentType.HOSPITAL);
        };
    }
}
