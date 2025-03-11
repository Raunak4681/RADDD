package com.radar.service;

import com.radar.model.Radar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.mockito.Mockito.verify;

class WebSocketServiceTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private WebSocketService webSocketService;

    private Radar testRadar;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testRadar = new Radar();
        testRadar.setId(1L);
        testRadar.setName("Test Radar");
        testRadar.setLatitude(40.0);
        testRadar.setLongitude(-74.0);
        testRadar.setRange(100.0);
    }

    @Test
    void broadcastRadarUpdate_ShouldSendMessage() {
        // Act
        webSocketService.broadcastRadarUpdate(testRadar);

        // Assert
        verify(messagingTemplate).convertAndSend("/topic/radar/updates", testRadar);
    }

    @Test
    void broadcastRadarDelete_ShouldSendMessage() {
        // Arrange
        Long radarId = 1L;

        // Act
        webSocketService.broadcastRadarDelete(radarId);

        // Assert
        verify(messagingTemplate).convertAndSend("/topic/radar/deletions", radarId);
    }
}