package com.radar.model;

import javax.persistence.*;
import org.locationtech.jts.geom.Geometry;

@Entity
@Table(name = "radars")
public class Radar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double latitude;
    private double longitude;
    private double range; // in kilometers

    @Column(columnDefinition = "geometry")
    private Geometry coverage;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public Geometry getCoverage() {
        return coverage;
    }

    public void setCoverage(Geometry coverage) {
        this.coverage = coverage;
    }
}