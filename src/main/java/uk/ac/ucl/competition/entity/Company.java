package uk.ac.ucl.competition.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name = "company")
public class Company {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NotBlank
    @Size(max = 20)
    @Column(name = "name", nullable = false, unique = true, length = 20)
    private String name;
    
    @Email
    @Size(max = 100)
    @Column(name = "email", unique = true, length = 100)
    private String email;
    
    @Size(max = 15)
    @Column(name = "phone", unique = true, length = 15)
    private String phone;
    
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Price> prices;
    
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Demand> demands;
    
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Profit> profits;
    
    public Company() {}
    
    public Company(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public List<Price> getPrices() {
        return prices;
    }
    
    public void setPrices(List<Price> prices) {
        this.prices = prices;
    }
    
    public List<Demand> getDemands() {
        return demands;
    }
    
    public void setDemands(List<Demand> demands) {
        this.demands = demands;
    }
    
    public List<Profit> getProfits() {
        return profits;
    }
    
    public void setProfits(List<Profit> profits) {
        this.profits = profits;
    }
}