package uk.ac.ucl.competition.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.ac.ucl.competition.entity.Company;
import uk.ac.ucl.competition.service.CompanyService;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class WebController {
    
    private final CompanyService companyService;
    
    @Autowired
    public WebController(CompanyService companyService) {
        this.companyService = companyService;
    }
    
    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<Company> companies = companyService.getAllCompanies();
        // Create a simplified structure to avoid circular references
        List<Map<String, Object>> companiesData = companies.stream()
            .map(company -> {
                Map<String, Object> companyData = new HashMap<>();
                companyData.put("id", company.getId());
                companyData.put("name", company.getName());
                companyData.put("email", company.getEmail());
                companyData.put("phone", company.getPhone());
                
                // Get prices without company reference to avoid circular dependency
                List<Map<String, Object>> pricesData = companyService.getPricesForCompany(company.getId())
                    .stream()
                    .map(price -> {
                        Map<String, Object> priceData = new HashMap<>();
                        priceData.put("id", price.getId());
                        priceData.put("time", price.getTime().toString());
                        priceData.put("price", price.getPrice());
                        return priceData;
                    })
                    .collect(Collectors.toList());
                
                // Get demands without company reference
                List<Map<String, Object>> demandsData = companyService.getDemandsForCompany(company.getId())
                    .stream()
                    .map(demand -> {
                        Map<String, Object> demandData = new HashMap<>();
                        demandData.put("id", demand.getId());
                        demandData.put("time", demand.getTime().toString());
                        demandData.put("demand", demand.getDemand());
                        return demandData;
                    })
                    .collect(Collectors.toList());
                
                // Get profits without company reference
                List<Map<String, Object>> profitsData = companyService.getProfitsForCompany(company.getId())
                    .stream()
                    .map(profit -> {
                        Map<String, Object> profitData = new HashMap<>();
                        profitData.put("id", profit.getId());
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
        
        model.addAttribute("companies", companiesData);
        return "dashboard";
    }
    
    @GetMapping("/company_input")
    public String companyInput() {
        return "company_input";
    }
    
    @PostMapping("/company_input")
    public String addCompany(@RequestParam String name, 
                           @RequestParam(required = false) String email, 
                           @RequestParam(required = false) String phone, 
                           RedirectAttributes redirectAttributes) {
        try {
            if (companyService.existsByName(name)) {
                redirectAttributes.addFlashAttribute("error", "Company with name '" + name + "' already exists!");
                return "redirect:/company_input";
            }
            
            // Convert empty strings to null for optional fields
            String normalizedEmail = (email != null && email.trim().isEmpty()) ? null : email;
            String normalizedPhone = (phone != null && phone.trim().isEmpty()) ? null : phone;
            
            if (normalizedEmail != null && companyService.existsByEmail(normalizedEmail)) {
                redirectAttributes.addFlashAttribute("error", "Company with email '" + normalizedEmail + "' already exists!");
                return "redirect:/company_input";
            }
            
            if (normalizedPhone != null && companyService.existsByPhone(normalizedPhone)) {
                redirectAttributes.addFlashAttribute("error", "Company with phone '" + normalizedPhone + "' already exists!");
                return "redirect:/company_input";
            }
            
            companyService.createCompany(name, normalizedEmail, normalizedPhone);
            redirectAttributes.addFlashAttribute("success", "Company '" + name + "' added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error adding company: " + e.getMessage());
        }
        return "redirect:/company_input";
    }
    
    @GetMapping("/price_input")
    public String priceInput(Model model) {
        model.addAttribute("companies", companyService.getAllCompanies());
        return "price_input";
    }
    
    @PostMapping("/price_input")
    public String addPrice(@RequestParam Integer companyId,
                          @RequestParam BigDecimal price,
                          RedirectAttributes redirectAttributes) {
        try {
            // Validate price range
            if (price.compareTo(new BigDecimal("1.00")) < 0 || price.compareTo(new BigDecimal("20.00")) > 0) {
                redirectAttributes.addFlashAttribute("error", "Price must be between \u00a41.00 and \u00a420.00. You entered: \u00a4" + price);
                return "redirect:/price_input";
            }
            
            LocalTime currentTime = LocalTime.now();
            companyService.addPriceToCompany(companyId, currentTime, price);
            redirectAttributes.addFlashAttribute("success", "Price data added successfully at " + currentTime.toString() + "!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error adding price: " + e.getMessage());
        }
        return "redirect:/price_input";
    }
    
    @GetMapping("/dashboard/company")
    public String companySelection(Model model) {
        List<Company> companies = companyService.getAllCompanies();
        model.addAttribute("companies", companies);
        return "company_selection";
    }
    
    @GetMapping("/dashboard/company/{companyName}")
    public String companyDashboard(@PathVariable String companyName, Model model) {
        // Find company by name (case-insensitive)
        Optional<Company> companyOpt = companyService.getCompanyByName(companyName);
        
        if (companyOpt.isEmpty()) {
            // Try case-insensitive search
            List<Company> allCompanies = companyService.getAllCompanies();
            companyOpt = allCompanies.stream()
                .filter(c -> c.getName().equalsIgnoreCase(companyName))
                .findFirst();
        }
        
        if (companyOpt.isEmpty()) {
            model.addAttribute("error", "Company '" + companyName + "' not found");
            model.addAttribute("availableCompanies", companyService.getAllCompanies());
            return "company_not_found";
        }
        
        Company company = companyOpt.get();
        
        // Create clean company data without circular references
        Map<String, Object> companyData = new HashMap<>();
        companyData.put("id", company.getId());
        companyData.put("name", company.getName());
        companyData.put("email", company.getEmail());
        companyData.put("phone", company.getPhone());
        
        // Get prices without company reference
        List<Map<String, Object>> pricesData = companyService.getPricesForCompany(company.getId())
            .stream()
            .map(price -> {
                Map<String, Object> priceData = new HashMap<>();
                priceData.put("id", price.getId());
                priceData.put("time", price.getTime().toString());
                priceData.put("price", price.getPrice());
                return priceData;
            })
            .collect(Collectors.toList());
        
        // Get demands without company reference
        List<Map<String, Object>> demandsData = companyService.getDemandsForCompany(company.getId())
            .stream()
            .map(demand -> {
                Map<String, Object> demandData = new HashMap<>();
                demandData.put("id", demand.getId());
                demandData.put("time", demand.getTime().toString());
                demandData.put("demand", demand.getDemand());
                return demandData;
            })
            .collect(Collectors.toList());
        
        // Get profits without company reference
        List<Map<String, Object>> profitsData = companyService.getProfitsForCompany(company.getId())
            .stream()
            .map(profit -> {
                Map<String, Object> profitData = new HashMap<>();
                profitData.put("id", profit.getId());
                profitData.put("time", profit.getTime().toString());
                profitData.put("profit", profit.getProfit());
                return profitData;
            })
            .collect(Collectors.toList());
        
        companyData.put("prices", pricesData);
        companyData.put("demands", demandsData);
        companyData.put("profits", profitsData);
        
        // Get all companies' demand data for consistent scaling
        List<Company> allCompanies = companyService.getAllCompanies();
        List<Map<String, Object>> allDemandsData = allCompanies.stream()
            .flatMap(comp -> companyService.getDemandsForCompany(comp.getId()).stream())
            .map(demand -> {
                Map<String, Object> demandData = new HashMap<>();
                demandData.put("demand", demand.getDemand());
                return demandData;
            })
            .collect(Collectors.toList());
        
        model.addAttribute("company", companyData);
        model.addAttribute("allDemands", allDemandsData);
        return "company_dashboard";
    }
}