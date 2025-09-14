package com.ecommerce.scraper.scraper;

import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.List;

@Component
public class ScraperFactory {
    
    private final AmazonScraper amazonScraper;
    private final EbayScraper ebayScraper;
    
    public ScraperFactory(AmazonScraper amazonScraper, EbayScraper ebayScraper) {
        this.amazonScraper = amazonScraper;
        this.ebayScraper = ebayScraper;
    }
    
    public BaseScraper getScraper(String website) {
        switch (website.toLowerCase()) {
            case "amazon":
                return amazonScraper;
            case "ebay":
                return ebayScraper;
            default:
                throw new IllegalArgumentException("Scraper for " + website + " not implemented");
        }
    }
    
    public List<String> getAvailableWebsites() {
        return Arrays.asList("amazon", "ebay");
    }
}