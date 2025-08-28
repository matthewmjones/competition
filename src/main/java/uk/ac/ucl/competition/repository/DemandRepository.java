package uk.ac.ucl.competition.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uk.ac.ucl.competition.entity.Demand;
import uk.ac.ucl.competition.entity.Company;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface DemandRepository extends JpaRepository<Demand, Integer> {
    
    List<Demand> findByCompany(Company company);
    
    List<Demand> findByCompanyId(Integer companyId);
    
    List<Demand> findByTime(LocalTime time);
    
    List<Demand> findByCompanyOrderByTimeAsc(Company company);
    
    List<Demand> findByCompanyIdOrderByTimeAsc(Integer companyId);
    
    List<Demand> findByCompanyIdOrderByTimeDesc(Integer companyId);
    
    @Query("SELECT d FROM Demand d WHERE d.company.id = :companyId AND d.time BETWEEN :startTime AND :endTime ORDER BY d.time ASC")
    List<Demand> findByCompanyIdAndTimeBetween(Integer companyId, LocalTime startTime, LocalTime endTime);
    
    @Query("SELECT d FROM Demand d JOIN FETCH d.company WHERE d.time = :time")
    List<Demand> findByTimeWithCompany(LocalTime time);
    
    @Query("DELETE FROM Demand d WHERE d.company.id = :companyId AND d.time = :time")
    void deleteByCompanyIdAndTime(Integer companyId, LocalTime time);
}