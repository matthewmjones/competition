package uk.ac.ucl.competition.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import uk.ac.ucl.competition.service.ChartUpdateService;

@RestController
public class SSEController {
    
    private final ChartUpdateService chartUpdateService;
    
    @Autowired
    public SSEController(ChartUpdateService chartUpdateService) {
        this.chartUpdateService = chartUpdateService;
    }
    
    @GetMapping("/api/chart-updates")
    public SseEmitter streamChartUpdates() {
        return chartUpdateService.addEmitter();
    }
}