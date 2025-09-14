package com.ecommerce.scraper.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "scraping_jobs")
public class ScrapingJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String website;
    private String searchQuery;
    
    @Enumerated(EnumType.STRING)
    private JobStatus status = JobStatus.PENDING;
    
    private Integer totalProducts = 0;
    private Integer scrapedProducts = 0;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime completedAt;
    
    @Column(columnDefinition = "TEXT")
    private String errorMessage;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
    
    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Product> products;
    
    public enum JobStatus {
        PENDING, RUNNING, COMPLETED, FAILED
    }
    
    // Constructors
    public ScrapingJob() {}
    
    public ScrapingJob(String name, String website, String searchQuery, User user) {
        this.name = name;
        this.website = website;
        this.searchQuery = searchQuery;
        this.user = user;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }
    
    public String getSearchQuery() { return searchQuery; }
    public void setSearchQuery(String searchQuery) { this.searchQuery = searchQuery; }
    
    public JobStatus getStatus() { return status; }
    public void setStatus(JobStatus status) { this.status = status; }
    
    public Integer getTotalProducts() { return totalProducts; }
    public void setTotalProducts(Integer totalProducts) { this.totalProducts = totalProducts; }
    
    public Integer getScrapedProducts() { return scrapedProducts; }
    public void setScrapedProducts(Integer scrapedProducts) { this.scrapedProducts = scrapedProducts; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }
}