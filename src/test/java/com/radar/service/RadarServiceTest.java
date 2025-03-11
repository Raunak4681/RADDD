package com.radar.service;

import com.radar.model.Radar;
import com.radar.repository.RadarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RadarServiceTest {

    @Mock
    private RadarRepository radarRepository;

    @Mock
    private WebSocketService webSocketService;

    @InjectMocks
    private RadarService radarService;

    private Radar testRadar;
    private Geometry testGeometry;

    @BeforeEach
    void setUp() throws ParseException {
        MockitoAnnotations.openMocks(this);

        // Create test radar data
        testRadar = new Radar();
        testRadar.setId(1L);
        testRadar.setName("Test Radar");
        testRadar.setLatitude(40.0);
        testRadar.setLongitude(-74.0);
        testRadar.setRange(100.0);

        // Create test geometry
        WKTReader reader = new WKTReader();
        testGeometry = reader.read("POINT(-74 40)");
        testRadar.setCoverage(testGeometry);
    }

    @Test
    void getRadarById_ShouldReturnRadar_WhenRadarExists() {
        // Arrange
        when(radarRepository.findById(1L)).thenReturn(Optional.of(testRadar));

        // Act
        Optional<Radar> result = radarService.getRadarById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testRadar.getId(), result.get().getId());
        assertEquals(testRadar.getName(), result.get().getName());
        verify(radarRepository).findById(1L);
    }

    @Test
    void getRadarById_ShouldReturnEmpty_WhenRadarDoesNotExist() {
        // Arrange
        when(radarRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Radar> result = radarService.getRadarById(999L);

        // Assert
        assertFalse(result.isPresent());
        verify(radarRepository).findById(999L);
    }

    @Test
    void createRadar_ShouldSaveAndBroadcast() {
        // Arrange
        when(radarRepository.save(any(Radar.class))).thenReturn(testRadar);

        // Act
        Radar result = radarService.createRadar(testRadar);

        // Assert
        assertNotNull(result);
        assertEquals(testRadar.getName(), result.getName());
        verify(radarRepository).save(testRadar);
        verify(webSocketService).broadcastRadarUpdate(testRadar);
    }
}