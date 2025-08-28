package uk.ac.ucl.competition.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalTime;

@Entity
@Table(name = "price")
public class Price {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    @NotNull
    private Company company;
    
    @Column(name = "time")
    private LocalTime time;
    
    @Column(name = "price", precision = 10, scale = 2)
    @DecimalMin(value = "1.0", inclusive = true, message = "Price must be at least ¤1.00")
    @DecimalMax(value = "20.0", inclusive = true, message = "Price must not exceed ¤20.00")
    @NotNull(message = "Price is required")
    private BigDecimal price;
    
    public Price() {}
    
    public Price(Company company, LocalTime time, BigDecimal price) {
        this.company = company;
        this.time = time;
        this.price = price;
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
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}