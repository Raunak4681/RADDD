package com.radar.repository;

import com.radar.model.Radar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.locationtech.jts.geom.Geometry;
import java.util.List;

public interface RadarRepository extends JpaRepository<Radar, Long> {
    @Query(value = "SELECT r FROM Radar r WHERE ST_DWithin(r.coverage, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326), :distance)")
    List<Radar> findRadarsInRange(
        @Param("longitude") double longitude,
        @Param("latitude") double latitude,
        @Param("distance") double distance
    );

    @Query(value = "SELECT r FROM Radar r WHERE ST_Intersects(r.coverage, :area)")
    List<Radar> findRadarsIntersectingArea(@Param("area") Geometry area);
}