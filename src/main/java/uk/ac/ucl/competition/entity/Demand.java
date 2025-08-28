package uk.ac.ucl.competition.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalTime;

@Entity
@Table(name = "demand")
public class Demand {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    @NotNull
    private Company company;
    
    @Column(name = "time")
    private LocalTime time;
    
    @Column(name = "demand", precision = 10, scale = 2)
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal demand;
    
    public Demand() {}
    
    public Demand(Company company, LocalTime time, BigDecimal demand) {
        this.company = company;
        this.time = time;
        this.demand = demand;
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Company getCompany() {
        return company;
    }
    
    public void setCompany(Company company) {
        this.company = company;
    }
    
    public LocalTime getTime() {
        return time;
    }
    
    public void setTime(LocalTime time) {
        this.time = time;
    }
    
    public BigDecimal getDemand() {
        return demand;
    }
    
    public void setDemand(BigDecimal demand) {
        this.demand = demand;
    }
    
}