package com.ecommerce.scraper.service;

import com.ecommerce.scraper.model.Product;
import com.ecommerce.scraper.model.ScrapingJob;
import com.ecommerce.scraper.model.User;
import com.ecommerce.scraper.repository.ProductRepository;
import com.ecommerce.scraper.repository.ScrapingJobRepository;
import com.ecommerce.scraper.scraper.BaseScraper;
import com.ecommerce.scraper.scraper.ScraperFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScrapingService {
    
    private final ScrapingJobRepository jobRepository;
    private final ProductRepository productRepository;
    private final ScraperFactory scraperFactory;
    
    public ScrapingService(ScrapingJobRepository jobRepository, 
                          ProductRepository productRepository,
                          ScraperFactory scraperFactory) {
        this.jobRepository = jobRepository;
        this.productRepository = productRepository;
        this.scraperFactory = scraperFactory;
    }
    
    public ScrapingJob createJob(String name, String website, String searchQuery, User user) {
        ScrapingJob job = new ScrapingJob(name, website, searchQuery, user);
        job = jobRepository.save(job);
        
        // Execute immediately (synchronous for now)
        executeJobSync(job.getId());
        
        return jobRepository.findById(job.getId()).orElse(job);
    }
    
    public void executeJobSync(Long jobId) {
        try {
            Thread.sleep(1000); // Small delay to ensure job creation response is sent
            
            ScrapingJob job = jobRepository.findById(jobId).orElse(null);
            if (job == null) return;
            
            job.setStatus(ScrapingJob.JobStatus.RUNNING);
            jobRepository.save(job);
            
            // Use real scraper
            BaseScraper scraper = scraperFactory.getScraper(job.getWebsite());
            List<Product> products = scraper.scrapeProducts(job.getSearchQuery(), 2);
            
            for (Product product : products) {
                product.setJob(job);
                productRepository.save(product);
            }
            
            job.setStatus(ScrapingJob.JobStatus.COMPLETED);
            job.setTotalProducts(products.size());
            job.setScrapedProducts(products.size());
            job.setCompletedAt(LocalDateTime.now());
            jobRepository.save(job);
            
        } catch (Exception e) {
            ScrapingJob job = jobRepository.findById(jobId).orElse(null);
            if (job != null) {
                job.setStatus(ScrapingJob.JobStatus.FAILED);
                job.setErrorMessage(e.getMessage());
                jobRepository.save(job);
            }
        }
    }
    
    public List<ScrapingJob> getUserJobs(Long userId) {
        return jobRepository.findByUserId(userId);
    }
    
    public ScrapingJob getJob(Long jobId, Long userId) {
        return jobRepository.findById(jobId)
                .filter(job -> job.getUser().getId().equals(userId))
                .orElse(null);
    }
}