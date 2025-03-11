package com.radar.controller;

import com.radar.model.Radar;
import com.radar.service.RadarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/radar")
public class RadarController {
    private final RadarService radarService;
    private final WebSocketService webSocketService;

    @Autowired
    public RadarController(RadarService radarService, WebSocketService webSocketService) {
        this.radarService = radarService;
        this.webSocketService = webSocketService;
    }

    @GetMapping
    public List<Radar> getAllRadars() {
        return radarService.getAllRadars();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Radar> getRadarById(@PathVariable Long id) {
        Radar radar = radarService.getRadarById(id);
        return ResponseEntity.ok(radar);
    }

    @PostMapping
    public ResponseEntity<Radar> createRadar(@RequestBody Radar radar) {
        Radar createdRadar = radarService.createRadar(radar);
        webSocketService.broadcastRadarUpdate(createdRadar);
        return ResponseEntity.ok(createdRadar);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Radar> updateRadar(@PathVariable Long id, @RequestBody Radar radarDetails) {
        Radar updatedRadar = radarService.updateRadar(id, radarDetails);
        webSocketService.broadcastRadarUpdate(updatedRadar);
        return ResponseEntity.ok(updatedRadar);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRadar(@PathVariable Long id) {
        radarService.deleteRadar(id);
        webSocketService.broadcastRadarDelete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search/inRange")
    public List<Radar> findRadarsInRange(
            @RequestParam double longitude,
            @RequestParam double latitude,
            @RequestParam double distance) {
        return radarService.findRadarsInRange(longitude, latitude, distance);
    }
}