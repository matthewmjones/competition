package uk.ac.ucl.competition.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ucl.competition.repository.CompanyRepository;
import uk.ac.ucl.competition.repository.DemandRepository;
import uk.ac.ucl.competition.repository.PriceRepository;
import uk.ac.ucl.competition.repository.ProfitRepository;

import java.util.HashMap;
import java.util.Map;

@Service
public class AdminService {
    
    private final CompanyRepository companyRepository;
    private final PriceRepository priceRepository;
    private final DemandRepository demandRepository;
    private final ProfitRepository profitRepository;
    private final ChartUpdateService chartUpdateService;
    
    @Autowired
    public AdminService(CompanyRepository companyRepository,
                       PriceRepository priceRepository,
                       DemandRepository demandRepository,
                       ProfitRepository profitRepository,
                       ChartUpdateService chartUpdateService) {
        this.companyRepository = companyRepository;
        this.priceRepository = priceRepository;
        this.demandRepository = demandRepository;
        this.profitRepository = profitRepository;
        this.chartUpdateService = chartUpdateService;
    }
    
    public Map<String, Integer> getDataStats() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("companies", (int) companyRepository.count());
        stats.put("prices", (int) priceRepository.count());
        stats.put("demands", (int) demandRepository.count());
        stats.put("profits", (int) profitRepository.count());
        return stats;
    }
    
    @Transactional
    public Map<String, Integer> resetAllData() {
        Map<String, Integer> deletedCounts = new HashMap<>();
        
        int profitsDeleted = (int) profitRepository.count();
        profitRepository.deleteAll();
        deletedCounts.put("profits", profitsDeleted);
        
        int demandsDeleted = (int) demandRepository.count();
        demandRepository.deleteAll();
        deletedCounts.put("demands", demandsDeleted);
        
        int pricesDeleted = (int) priceRepository.count();
        priceRepository.deleteAll();
        deletedCounts.put("prices", pricesDeleted);
        
        int companiesDeleted = (int) companyRepository.count();
        companyRepository.deleteAll();
        deletedCounts.put("companies", companiesDeleted);
        
        chartUpdateService.notifyChartsUpdate();
        
        return deletedCounts;
    }
    
    @Transactional
    public Map<String, Integer> resetDataOnly() {
        Map<String, Integer> deletedCounts = new HashMap<>();
        
        int profitsDeleted = (int) profitRepository.count();
        profitRepository.deleteAll();
        deletedCounts.put("profits", profitsDeleted);
        
        int demandsDeleted = (int) demandRepository.count();
        demandRepository.deleteAll();
        deletedCounts.put("demands", demandsDeleted);
        
        int pricesDeleted = (int) priceRepository.count();
        priceRepository.deleteAll();
        deletedCounts.put("prices", pricesDeleted);
        
        deletedCounts.put("companies", 0);
        
        chartUpdateService.notifyChartsUpdate();
        
        return deletedCounts;
    }
}