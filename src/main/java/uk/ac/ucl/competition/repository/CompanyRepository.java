package uk.ac.ucl.competition.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uk.ac.ucl.competition.entity.Company;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
    
    Optional<Company> findByName(String name);
    
    Optional<Company> findByEmail(String email);
    
    Optional<Company> findByPhone(String phone);
    
    boolean existsByName(String name);
    
    boolean existsByEmail(String email);
    
    boolean existsByPhone(String phone);
    
    @Query("SELECT c FROM Company c LEFT JOIN FETCH c.prices WHERE c.id = :id")
    Optional<Company> findByIdWithPrices(Integer id);
}