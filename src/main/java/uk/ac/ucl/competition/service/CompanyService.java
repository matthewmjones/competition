package uk.ac.ucl.competition.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ucl.competition.entity.Company;
import uk.ac.ucl.competition.entity.Price;
import uk.ac.ucl.competition.entity.Demand;
import uk.ac.ucl.competition.entity.Profit;
import uk.ac.ucl.competition.repository.CompanyRepository;
import uk.ac.ucl.competition.repository.PriceRepository;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CompanyService {
    
    private final CompanyRepository companyRepository;
    private final PriceRepository priceRepository;
    private final DemandService demandService;
    private final ProfitService profitService;
    
    @Autowired
    public CompanyService(CompanyRepository companyRepository, PriceRepository priceRepository, DemandService demandService, ProfitService profitService) {
        this.companyRepository = companyRepository;
        this.priceRepository = priceRepository;
        this.demandService = demandService;
        this.profitService = profitService;
    }
    
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }
    
    public Optional<Company> getCompanyById(Integer id) {
        return companyRepository.findById(id);
    }
    
    public Optional<Company> getCompanyByName(String name) {
        return companyRepository.findByName(name);
    }
    
    public Company saveCompany(Company company) {
        return companyRepository.save(company);
    }
    
    public Company createCompany(String name, String email, String phone) {
        Company company = new Company(name, email, phone);
        return companyRepository.save(company);
    }
    
    public void deleteCompany(Integer id) {
        companyRepository.deleteById(id);
    }
    
    public boolean existsByName(String name) {
        return companyRepository.existsByName(name);
    }
    
    public boolean existsByEmail(String email) {
        return companyRepository.existsByEmail(email);
    }
    
    public boolean existsByPhone(String phone) {
        return companyRepository.existsByPhone(phone);
    }
    
    public List<Price> getPricesForCompany(Integer companyId) {
        return priceRepository.findByCompanyIdOrderByTimeAsc(companyId);
    }
    
    public Price addPriceToCompany(Integer companyId, LocalTime time, BigDecimal price) {
        Optional<Company> company = companyRepository.findById(companyId);
        if (company.isPresent()) {
            Price newPrice = new Price(company.get(), time, price);
            Price savedPrice = priceRepository.save(newPrice);
            
            // Recalculate demands for ALL companies at the current time
            // (using each company's latest price from the database)
            demandService.recalculateAllDemandsAfterPriceChange(time);
            
            // Recalculate profits for ALL companies at the current time
            // (using each company's latest price and current demand)
            profitService.recalculateAllProfitsAfterPriceChange(time);
            
            return savedPrice;
        }
        throw new IllegalArgumentException("Company not found with id: " + companyId);
    }
    
    public List<Price> getAllPricesAtTime(LocalTime time) {
        return priceRepository.findByTimeWithCompany(time);
    }
    
    public Optional<Company> getCompanyWithPrices(Integer id) {
        return companyRepository.findByIdWithPrices(id);
    }
    
    public List<Demand> getDemandsForCompany(Integer companyId) {
        Optional<Company> company = companyRepository.findById(companyId);
        if (company.isPresent()) {
            return demandService.getDemandsByCompany(company.get());
        }
        throw new IllegalArgumentException("Company not found with id: " + companyId);
    }
    
    public List<Demand> getAllDemandsAtTime(LocalTime time) {
        return demandService.getDemandsByTime(time);
    }
    
    public List<Profit> getProfitsForCompany(Integer companyId) {
        Optional<Company> company = companyRepository.findById(companyId);
        if (company.isPresent()) {
            return profitService.getProfitsByCompany(company.get());
        }
        throw new IllegalArgumentException("Company not found with id: " + companyId);
    }
    
    public List<Profit> getAllProfitsAtTime(LocalTime time) {
        return profitService.getProfitsByTime(time);
    }
}