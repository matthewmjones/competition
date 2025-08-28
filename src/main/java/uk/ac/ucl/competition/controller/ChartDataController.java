package uk.ac.ucl.competition.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ucl.competition.service.CompanyService;
import uk.ac.ucl.competition.entity.Company;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ChartDataController {
    
    private final CompanyService companyService;
    
    @Autowired
    public ChartDataController(CompanyService companyService) {
        this.companyService = companyService;
    }
    
    @GetMapping("/api/chart-data/all")
    public Map<String, Object> getAllChartData() {
        List<Company> companies = companyService.getAllCompanies();
        List<Map<String, Object>> companiesData = companies.stream()
            .map(company -> {
                Map<String, Object> companyData = new HashMap<>();
                companyData.put("id", company.getId());
                companyData.put("name", company.getName());
                
                // Get prices
                List<Map<String, Object>> pricesData = companyService.getPricesForCompany(company.getId())
                    .stream()
                    .map(price -> {
                        Map<String, Object> priceData = new HashMap<>();
                        priceData.put("time", price.getTime().toString());
                        priceData.put("price", price.getPrice());
                        return priceData;
                    })
                    .collect(Collectors.toList());
                
                // Get demands
                List<Map<String, Object>> demandsData = companyService.getDemandsForCompany(company.getId())
                    .stream()
                    .map(demand -> {
                        Map<String, Object> demandData = new HashMap<>();
                        demandData.put("time", demand.getTime().toString());
                        demandData.put("demand", demand.getDemand());
                        return demandData;
                    })
                    .collect(Collectors.toList());
                
                // Get profits
                List<Map<String, Object>> profitsData = companyService.getProfitsForCompany(company.getId())
                    .stream()
                    .map(profit -> {
                        Map<String, Object> profitData = new HashMap<>();
                        profitData.put("time", profit.getTime().toString());
                        profitData.put("profit", profit.getProfit());
                        return profitData;
                    })
                    .collect(Collectors.toList());
                
                companyData.put("prices", pricesData);
                companyData.put("demands", demandsData);
                companyData.put("profits", profitsData);
                return companyData;
            })
            .collect(Collectors.toList());
        
        Map<String, Object> response = new HashMap<>();
        response.put("companies", companiesData);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
    
    @GetMapping("/api/chart-data/company/{companyName}")
    public Map<String, Object> getCompanyChartData(@PathVariable String companyName) {
        // Find company by name (case-insensitive)
        List<Company> allCompanies = companyService.getAllCompanies();
        Company company = allCompanies.stream()
            .filter(c -> c.getName().equalsIgnoreCase(companyName))
            .findFirst()
            .orElse(null);
        
        if (company == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Company not found: " + companyName);
            return error;
        }
        
        Map<String, Object> companyData = new HashMap<>();
        companyData.put("id", company.getId());
        companyData.put("name", company.getName());
        
        // Get prices
        List<Map<String, Object>> pricesData = companyService.getPricesForCompany(company.getId())
            .stream()
            .map(price -> {
                Map<String, Object> priceData = new HashMap<>();
                priceData.put("time", price.getTime().toString());
                priceData.put("price", price.getPrice());
                return priceData;
            })
            .collect(Collectors.toList());
        
        // Get demands
        List<Map<String, Object>> demandsData = companyService.getDemandsForCompany(company.getId())
            .stream()
            .map(demand -> {
                Map<String, Object> demandData = new HashMap<>();
                demandData.put("time", demand.getTime().toString());
                demandData.put("demand", demand.getDemand());
                return demandData;
            })
            .collect(Collectors.toList());
        
        // Get profits
        List<Map<String, Object>> profitsData = companyService.getProfitsForCompany(company.getId())
            .stream()
            .map(profit -> {
                Map<String, Object> profitData = new HashMap<>();
                profitData.put("time", profit.getTime().toString());
                profitData.put("profit", profit.getProfit());
                return profitData;
            })
            .collect(Collectors.toList());
        
        // Get all demands for scaling
        List<Map<String, Object>> allDemandsData = allCompanies.stream()
            .flatMap(comp -> companyService.getDemandsForCompany(comp.getId()).stream())
            .map(demand -> {
                Map<String, Object> demandData = new HashMap<>();
                demandData.put("demand", demand.getDemand());
                return demandData;
            })
            .collect(Collectors.toList());
        
        companyData.put("prices", pricesData);
        companyData.put("demands", demandsData);
        companyData.put("profits", profitsData);
        companyData.put("allDemands", allDemandsData);
        companyData.put("timestamp", System.currentTimeMillis());
        
        return companyData;
    }
}