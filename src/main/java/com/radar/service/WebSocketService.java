package com.radar.service;

import com.radar.model.Radar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void broadcastRadarUpdate(Radar radar) {
        messagingTemplate.convertAndSend("/topic/radar/updates", radar);
    }

    public void broadcastRadarDelete(Long radarId) {
        messagingTemplate.convertAndSend("/topic/radar/deletions", radarId);
    }
}