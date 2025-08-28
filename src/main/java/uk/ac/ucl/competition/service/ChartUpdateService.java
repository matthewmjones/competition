package uk.ac.ucl.competition.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class ChartUpdateService {
    
    private static final Logger logger = LoggerFactory.getLogger(ChartUpdateService.class);
    
    // Thread-safe list to store all active SSE connections
    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    
    public SseEmitter addEmitter() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        
        emitter.onCompletion(() -> {
            logger.debug("SSE connection completed");
            emitters.remove(emitter);
        });
        
        emitter.onTimeout(() -> {
            logger.debug("SSE connection timed out");
            emitters.remove(emitter);
        });
        
        emitter.onError((ex) -> {
            logger.debug("SSE connection error: {}", ex.getMessage());
            emitters.remove(emitter);
        });
        
        emitters.add(emitter);
        logger.info("New SSE connection added. Total connections: {}", emitters.size());
        
        return emitter;
    }
    
    public void notifyChartsUpdate() {
        logger.info("Notifying {} clients about chart updates", emitters.size());
        
        emitters.removeIf(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                    .name("chart-update")
                    .data("refresh"));
                return false;
            } catch (IOException e) {
                logger.debug("Failed to send SSE event, removing emitter: {}", e.getMessage());
                return true;
            }
        });
    }
}