package com.ecommerce.scraper.dto;

import jakarta.validation.constraints.NotBlank;

public class ScrapingJobRequest {
    @NotBlank
    private String name;
    
    @NotBlank
    private String website;
    
    @NotBlank
    private String searchQuery;
    
    public ScrapingJobRequest() {}
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }
    
    public String getSearchQuery() { return searchQuery; }
    public void setSearchQuery(String searchQuery) { this.searchQuery = searchQuery; }
}