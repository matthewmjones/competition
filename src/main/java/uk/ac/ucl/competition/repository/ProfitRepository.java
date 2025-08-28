package uk.ac.ucl.competition.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uk.ac.ucl.competition.entity.Profit;
import uk.ac.ucl.competition.entity.Company;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface ProfitRepository extends JpaRepository<Profit, Integer> {
    
    List<Profit> findByCompany(Company company);
    
    List<Profit> findByCompanyId(Integer companyId);
    
    List<Profit> findByTime(LocalTime time);
    
    List<Profit> findByCompanyOrderByTimeAsc(Company company);
    
    List<Profit> findByCompanyIdOrderByTimeAsc(Integer companyId);
    
    @Query("SELECT p FROM Profit p WHERE p.company.id = :companyId AND p.time BETWEEN :startTime AND :endTime ORDER BY p.time ASC")
    List<Profit> findByCompanyIdAndTimeBetween(Integer companyId, LocalTime startTime, LocalTime endTime);
    
    @Query("SELECT p FROM Profit p JOIN FETCH p.company WHERE p.time = :time")
    List<Profit> findByTimeWithCompany(LocalTime time);
    
    @Query("DELETE FROM Profit p WHERE p.company.id = :companyId AND p.time = :time")
    void deleteByCompanyIdAndTime(Integer companyId, LocalTime time);
}