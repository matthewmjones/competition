package uk.ac.ucl.competition.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uk.ac.ucl.competition.service.AdminService;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    private final AdminService adminService;
    
    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }
    
    @GetMapping
    public String adminPage(Model model) {
        model.addAttribute("stats", adminService.getDataStats());
        return "admin";
    }
    
    @PostMapping("/reset/all")
    public ResponseEntity<Map<String, Object>> resetAll(@RequestParam(required = false) String confirm) {
        Map<String, Object> response = new HashMap<>();
        
        if (!"DELETE_ALL_DATA".equals(confirm)) {
            response.put("success", false);
            response.put("error", "Confirmation text required");
            return ResponseEntity.badRequest().body(response);
        }
        
        try {
            Map<String, Integer> deletedCounts = adminService.resetAllData();
            response.put("success", true);
            response.put("message", "All data deleted successfully");
            response.put("deleted", deletedCounts);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Failed to reset data: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    @PostMapping("/reset/data-only")
    public ResponseEntity<Map<String, Object>> resetDataOnly(@RequestParam(required = false) String confirm) {
        Map<String, Object> response = new HashMap<>();
        
        if (!"DELETE_DATA_KEEP_COMPANIES".equals(confirm)) {
            response.put("success", false);
            response.put("error", "Confirmation text required");
            return ResponseEntity.badRequest().body(response);
        }
        
        try {
            Map<String, Integer> deletedCounts = adminService.resetDataOnly();
            response.put("success", true);
            response.put("message", "Price, demand, and profit data deleted successfully. Companies preserved.");
            response.put("deleted", deletedCounts);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Failed to reset data: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}