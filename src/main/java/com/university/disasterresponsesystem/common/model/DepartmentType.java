/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.university.disasterresponsesystem.common.model;

/**
 * Departments that can be coordinated during a response.
 *
 * @author alisha -12268551
 */
public enum DepartmentType {
    FIRE_AND_EMERGENCY("Fire and Emergency"), HOSPITAL("Hospital"), POLICE("Police"),
    ELECTRICITY("Electricity"), TRANSPORT("Transport"), WASTE_MANAGEMENT("Waste Management"),
    WATER_SUPPLY("Water Supply"), SHELTER_SERVICES("Shelter Services");

    private final String displayName;

    DepartmentType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
