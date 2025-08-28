package uk.ac.ucl.competition.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ucl.competition.entity.Company;
import uk.ac.ucl.competition.entity.Demand;
import uk.ac.ucl.competition.entity.Price;
import uk.ac.ucl.competition.repository.DemandRepository;
import uk.ac.ucl.competition.repository.PriceRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DemandService {

    private static final Logger logger = LoggerFactory.getLogger(DemandService.class);

    @Autowired
    private DemandRepository demandRepository;

    @Autowired
    private PriceRepository priceRepository;

    public BigDecimal calculateDemand(Company company, LocalTime time, BigDecimal ownPrice) {
        BigDecimal averageCompetitorPrice = calculateAverageCompetitorPriceUsingLatest(company, time);
        
        // Formula: demand = 100 - 5p + 2q
        // where p = own price, q = average competitor price
        BigDecimal demand = BigDecimal.valueOf(100)
                .subtract(BigDecimal.valueOf(5).multiply(ownPrice))
                .add(BigDecimal.valueOf(2).multiply(averageCompetitorPrice));
                
        // Ensure demand doesn't go negative
        return demand.max(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateAverageCompetitorPrice(Company company, LocalTime time) {
        List<Price> allPricesAtTime = priceRepository.findByTimeWithCompany(time);
        
        List<Price> competitorPrices = allPricesAtTime.stream()
                .filter(price -> !price.getCompany().getId().equals(company.getId()))
                .toList();
        
        if (competitorPrices.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal sum = competitorPrices.stream()
                .map(Price::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return sum.divide(BigDecimal.valueOf(competitorPrices.size()), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateAverageCompetitorPriceUsingLatest(Company company, LocalTime time) {
        List<Price> allPrices = priceRepository.findAll();
        Set<Company> allCompanies = allPrices.stream()
            .map(Price::getCompany)
            .collect(Collectors.toSet());
        
        List<BigDecimal> competitorPrices = new ArrayList<>();
        
        for (Company competitor : allCompanies) {
            if (!competitor.getId().equals(company.getId())) {
                List<Price> competitorPricesOrdered = priceRepository.findByCompanyIdOrderByTimeDesc(competitor.getId());
                if (!competitorPricesOrdered.isEmpty()) {
                    competitorPrices.add(competitorPricesOrdered.get(0).getPrice());
                }
            }
        }
        
        if (competitorPrices.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal sum = competitorPrices.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return sum.divide(BigDecimal.valueOf(competitorPrices.size()), 2, RoundingMode.HALF_UP);
    }

    public void calculateAndSaveDemandsForTime(LocalTime time) {
        List<Price> allPricesAtTime = priceRepository.findByTimeWithCompany(time);
        
        for (Price price : allPricesAtTime) {
            Company company = price.getCompany();
            BigDecimal demand = calculateDemand(company, time, price.getPrice());
            
            Demand demandEntity = new Demand(company, time, demand);
            demandRepository.save(demandEntity);
        }
    }

    public void recalculateDemandsForTime(LocalTime time) {
        logger.info("Recalculating demands for ALL companies at time: {}", time);
        
        List<Demand> existingDemands = demandRepository.findByTime(time);
        logger.info("Deleting {} existing demand entries at time: {}", existingDemands.size(), time);
        demandRepository.deleteAll(existingDemands);
        
        List<Price> allPricesAtTime = priceRepository.findByTimeWithCompany(time);
        logger.info("Recalculating demands for {} companies at time: {}", allPricesAtTime.size(), time);
        
        for (Price price : allPricesAtTime) {
            Company company = price.getCompany();
            BigDecimal demand = calculateDemand(company, time, price.getPrice());
            
            Demand demandEntity = new Demand(company, time, demand);
            demandRepository.save(demandEntity);
            
            logger.info("Updated demand for {}: {} units (price: ¤{}, avg competitor price: ¤{})", 
                company.getName(), demand, price.getPrice(), 
                calculateAverageCompetitorPrice(company, time));
        }
        
        logger.info("Completed demand recalculation for ALL companies at time: {}", time);
    }
    
    public void recalculateAllDemandsAfterPriceChange(LocalTime time) {
        logger.info("Recalculating demands for ALL companies at time: {}", time);
        
        List<Price> allPrices = priceRepository.findAll();
        Set<Company> allCompanies = allPrices.stream()
            .map(Price::getCompany)
            .collect(Collectors.toSet());
            
        logger.info("Found {} companies to recalculate demands for at time: {}", allCompanies.size(), time);
        
        List<Demand> existingDemandsAtTime = demandRepository.findByTime(time);
        logger.info("Deleting {} existing demand entries at time: {}", existingDemandsAtTime.size(), time);
        demandRepository.deleteAll(existingDemandsAtTime);
        
        for (Company company : allCompanies) {
            List<Price> companyPrices = priceRepository.findByCompanyIdOrderByTimeDesc(company.getId());
            
            if (!companyPrices.isEmpty()) {
                Price latestPrice = companyPrices.get(0);
                BigDecimal currentPrice = latestPrice.getPrice();
                
                BigDecimal demand = calculateDemand(company, time, currentPrice);
                Demand demandEntity = new Demand(company, time, demand);
                demandRepository.save(demandEntity);
                
                logger.info("Updated demand for {} at time {}: {} units (using latest price: ¤{}, avg competitor price: ¤{})", 
                    company.getName(), time, demand, currentPrice, 
                    calculateAverageCompetitorPriceUsingLatest(company, time));
            } else {
                logger.warn("No price data found for company: {}", company.getName());
            }
        }
        
        logger.info("Completed demand recalculation for ALL companies at time: {}", time);
    }

    public List<Demand> getDemandsByCompany(Company company) {
        return demandRepository.findByCompanyOrderByTimeAsc(company);
    }

    public List<Demand> getDemandsByTime(LocalTime time) {
        return demandRepository.findByTimeWithCompany(time);
    }
}