/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.university.disasterresponsesystem.common.model;

/**
 * Categories of disaster a citizen/operator can report.
 *
 * @author alisha -12268551
 */
public enum DisasterType {
    FIRE("Fire"), FLOOD("Flood"), EARTHQUAKE("Earthquake"),
    HURRICANE("Hurricane"), STORM("Storm"), LANDSLIDE("Landslide"), OTHER("Other");

    private final String displayName;

    DisasterType(String displayName) {
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
