package uk.ac.ucl.competition.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class WebSocketController {
    
    private static final Logger logger = LoggerFactory.getLogger(WebSocketController.class);
    
    @MessageMapping("/chart-ping")
    @SendTo("/topic/chart-updates")
    public String handleChartPing(String message) {
        logger.info("Received WebSocket ping: {}", message);
        return "pong";
    }
}