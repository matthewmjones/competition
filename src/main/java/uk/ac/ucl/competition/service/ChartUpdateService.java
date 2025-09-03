package uk.ac.ucl.competition.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ChartUpdateService {
    
    private static final Logger logger = LoggerFactory.getLogger(ChartUpdateService.class);
    
    private final SimpMessagingTemplate messagingTemplate;
    
    @Autowired
    public ChartUpdateService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
    
    public void notifyChartsUpdate() {
        logger.info("Broadcasting chart update notification via WebSocket");
        
        // Send message to all subscribers of /topic/chart-updates
        messagingTemplate.convertAndSend("/topic/chart-updates", "refresh");
        
        logger.info("Chart update notification sent successfully");
    }
}