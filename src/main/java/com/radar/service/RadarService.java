package com.radar.service;

import com.radar.model.Radar;
import com.radar.repository.RadarRepository;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.io.WKTWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RadarService {
    private final RadarRepository radarRepository;
    private final GeometryFactory geometryFactory;
    private final WKTWriter wktWriter;

    @Autowired
    public RadarService(RadarRepository radarRepository) {
        this.radarRepository = radarRepository;
        this.geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        this.wktWriter = new WKTWriter();
    }

    @Transactional(readOnly = true)
    public List<Radar> getAllRadars() {
        return radarRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Radar getRadarById(Long id) {
        return radarRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Radar not found with id: " + id));
    }

    @Transactional
    public Radar createRadar(Radar radar) {
        // Calculate the radar coverage considering elevation
        Geometry coverage = calculateRadarCoverage(radar.getLongitude(), radar.getLatitude(), radar.getRange());
        radar.setCoverage(coverage);
        return radarRepository.save(radar);
    }

    private Geometry calculateRadarCoverage(double longitude, double latitude, double range) {
        // Create a point representing the radar's location
        Point center = geometryFactory.createPoint(new Coordinate(longitude, latitude));
        
        // Convert range from kilometers to degrees (approximate)
        double rangeDegrees = range / 111.32;
        
        // Create a circular buffer around the radar location using PostGIS
        // ST_Buffer is more accurate for geographic coordinates than JTS buffer
        String sql = String.format(
            "SELECT ST_Buffer(ST_SetSRID(ST_MakePoint(%f, %f), 4326), %f)::geometry",
            longitude, latitude, rangeDegrees
        );
        
        // Execute the PostGIS query and get the resulting geometry
        // Note: This requires setting up a native query execution
        // TODO: Implement the following steps using PostGIS:
        // 1. Query elevation data within the buffer using PostGIS Raster functions
        // 2. Calculate line-of-sight visibility using ST_LineOfSight
        // 3. Create a more accurate coverage polygon considering terrain occlusion
        
        // For now, return a simple circular buffer
        // In production, replace this with actual PostGIS terrain analysis
        Geometry coverage = center.buffer(rangeDegrees);
        
        return coverage;
    }

    @Transactional
    public Radar updateRadar(Long id, Radar radarDetails) {
        Radar radar = getRadarById(id);
        radar.setName(radarDetails.getName());
        radar.setLatitude(radarDetails.getLatitude());
        radar.setLongitude(radarDetails.getLongitude());
        radar.setRange(radarDetails.getRange());
        radar.setCoverage(radarDetails.getCoverage());
        return radarRepository.save(radar);
    }

    @Transactional
    public void deleteRadar(Long id) {
        radarRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Radar> findRadarsInRange(double longitude, double latitude, double distance) {
        return radarRepository.findRadarsInRange(longitude, latitude, distance);
    }

    @Transactional(readOnly = true)
    public List<Radar> findRadarsIntersectingArea(Geometry area) {
        return radarRepository.findRadarsIntersectingArea(area);
    }

    public String convertGeometryToWKT(Geometry geometry) {
        return wktWriter.write(geometry);
    }
}