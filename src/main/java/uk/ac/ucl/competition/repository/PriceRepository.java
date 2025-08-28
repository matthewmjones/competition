package uk.ac.ucl.competition.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uk.ac.ucl.competition.entity.Price;
import uk.ac.ucl.competition.entity.Company;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface PriceRepository extends JpaRepository<Price, Integer> {
    
    List<Price> findByCompany(Company company);
    
    List<Price> findByCompanyId(Integer companyId);
    
    List<Price> findByTime(LocalTime time);
    
    List<Price> findByCompanyOrderByTimeAsc(Company company);
    
    List<Price> findByCompanyIdOrderByTimeAsc(Integer companyId);
    
    List<Price> findByCompanyIdOrderByTimeDesc(Integer companyId);
    
    @Query("SELECT p FROM Price p WHERE p.company.id = :companyId AND p.time BETWEEN :startTime AND :endTime ORDER BY p.time ASC")
    List<Price> findByCompanyIdAndTimeBetween(Integer companyId, LocalTime startTime, LocalTime endTime);
    
    @Query("SELECT p FROM Price p JOIN FETCH p.company WHERE p.time = :time")
    List<Price> findByTimeWithCompany(LocalTime time);
}