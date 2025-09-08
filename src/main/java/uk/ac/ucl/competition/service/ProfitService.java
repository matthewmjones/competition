package uk.ac.ucl.competition.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ucl.competition.entity.Company;
import uk.ac.ucl.competition.entity.Profit;
import uk.ac.ucl.competition.entity.Price;
import uk.ac.ucl.competition.entity.Demand;
import uk.ac.ucl.competition.repository.ProfitRepository;
import uk.ac.ucl.competition.repository.PriceRepository;
import uk.ac.ucl.competition.repository.DemandRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProfitService {

    private static final Logger logger = LoggerFactory.getLogger(ProfitService.class);

    private final ProfitRepository profitRepository;
    private final PriceRepository priceRepository;
    private final DemandRepository demandRepository;
    private final ChartUpdateService chartUpdateService;
    
    public ProfitService(ProfitRepository profitRepository, 
                        PriceRepository priceRepository,
                        DemandRepository demandRepository,
                        ChartUpdateService chartUpdateService) {
        this.profitRepository = profitRepository;
        this.priceRepository = priceRepository;
        this.demandRepository = demandRepository;
        this.chartUpdateService = chartUpdateService;
    }

    public BigDecimal calculateProfit(BigDecimal price, BigDecimal demand) {
        // Formula: profit = (price - 2) * demand
        BigDecimal profit = price.subtract(BigDecimal.valueOf(2)).multiply(demand);
        
        return profit.setScale(2, RoundingMode.HALF_UP);
    }

    public void recalculateAllProfitsAfterPriceChange(LocalTime time) {
        logger.info("Recalculating profits for ALL companies at time: {}", time);
        
        List<Price> allPrices = priceRepository.findAll();
        Set<Company> allCompanies = allPrices.stream()
            .map(Price::getCompany)
            .collect(Collectors.toSet());
            
        logger.info("Found {} companies to recalculate profits for at time: {}", allCompanies.size(), time);
        
        List<Profit> existingProfitsAtTime = profitRepository.findByTime(time);
        logger.info("Deleting {} existing profit entries at time: {}", existingProfitsAtTime.size(), time);
        profitRepository.deleteAll(existingProfitsAtTime);
        
        for (Company company : allCompanies) {
            List<Price> companyPrices = priceRepository.findByCompanyIdOrderByTimeDesc(company.getId());
            
            if (!companyPrices.isEmpty()) {
                Price latestPrice = companyPrices.get(0);
                BigDecimal currentPrice = latestPrice.getPrice();
                
                List<Demand> companyDemands = demandRepository.findByCompanyIdOrderByTimeDesc(company.getId());
                
                if (!companyDemands.isEmpty()) {
                    Demand latestDemand = companyDemands.get(0);
                    BigDecimal currentDemand = latestDemand.getDemand();
                    
                    BigDecimal profit = calculateProfit(currentPrice, currentDemand);
                    Profit profitEntity = new Profit(company, time, profit);
                    profitRepository.save(profitEntity);
                    
                    logger.info("Updated profit for {} at time {}: \u00a4{} (using price: \u00a4{}, demand: {} units)", 
                        company.getName(), time, profit, currentPrice, currentDemand);
                } else {
                    logger.warn("No demand data found for company: {}", company.getName());
                }
            } else {
                logger.warn("No price data found for company: {}", company.getName());
            }
        }
        
        logger.info("Completed profit recalculation for ALL companies at time: {}", time);
        
        chartUpdateService.notifyChartsUpdate();
    }

    public List<Profit> getProfitsByCompany(Company company) {
        return profitRepository.findByCompanyOrderByTimeAsc(company);
    }

    public List<Profit> getProfitsByTime(LocalTime time) {
        return profitRepository.findByTimeWithCompany(time);
    }
}