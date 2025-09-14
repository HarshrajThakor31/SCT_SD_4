package com.ecommerce.scraper.controller;

import com.ecommerce.scraper.dto.ScrapingJobRequest;
import com.ecommerce.scraper.model.Product;
import com.ecommerce.scraper.model.ScrapingJob;
import com.ecommerce.scraper.model.User;
import com.ecommerce.scraper.repository.ProductRepository;
import com.ecommerce.scraper.scraper.ScraperFactory;
import com.ecommerce.scraper.service.ExportService;
import com.ecommerce.scraper.service.ScrapingService;
import com.ecommerce.scraper.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ScrapingController {
    
    private final ScrapingService scrapingService;
    private final UserService userService;
    private final ProductRepository productRepository;
    private final ScraperFactory scraperFactory;
    private final ExportService exportService;
    
    public ScrapingController(ScrapingService scrapingService,
                             UserService userService,
                             ProductRepository productRepository,
                             ScraperFactory scraperFactory,
                             ExportService exportService) {
        this.scrapingService = scrapingService;
        this.userService = userService;
        this.productRepository = productRepository;
        this.scraperFactory = scraperFactory;
        this.exportService = exportService;
    }
    
    @GetMapping("/websites")
    public ResponseEntity<Map<String, List<String>>> getWebsites() {
        Map<String, List<String>> response = new HashMap<>();
        response.put("websites", scraperFactory.getAvailableWebsites());
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/scraping-jobs")
    public ResponseEntity<ScrapingJob> createJob(@Valid @RequestBody ScrapingJobRequest request,
                                                Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        ScrapingJob job = scrapingService.createJob(request.getName(), request.getWebsite(), 
                                                   request.getSearchQuery(), user);
        
        return ResponseEntity.ok(job);
    }
    
    @GetMapping("/scraping-jobs")
    public ResponseEntity<List<ScrapingJob>> getJobs(Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        List<ScrapingJob> jobs = scrapingService.getUserJobs(user.getId());
        return ResponseEntity.ok(jobs);
    }
    
    @GetMapping("/scraping-jobs/{id}")
    public ResponseEntity<ScrapingJob> getJob(@PathVariable Long id, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        ScrapingJob job = scrapingService.getJob(id, user.getId());
        if (job == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(job);
    }
    
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts(@RequestParam(required = false) Long jobId,
                                                    Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        List<Product> products;
        
        if (jobId != null) {
            products = productRepository.findByUserIdAndJobId(user.getId(), jobId);
        } else {
            products = productRepository.findByUserId(user.getId());
        }
        
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/export/{format}")
    public ResponseEntity<byte[]> exportData(@PathVariable String format,
                                           @RequestParam(required = false) Long jobId,
                                           Authentication authentication) {
        try {
            User user = userService.findByUsername(authentication.getName());
            List<Product> products;
            
            if (jobId != null) {
                products = productRepository.findByUserIdAndJobId(user.getId(), jobId);
            } else {
                products = productRepository.findByUserId(user.getId());
            }
            
            byte[] data;
            String filename;
            MediaType mediaType;
            
            switch (format.toLowerCase()) {
                case "csv":
                    data = exportService.exportToCsv(products);
                    filename = "products.csv";
                    mediaType = MediaType.TEXT_PLAIN;
                    break;
                case "json":
                    data = exportService.exportToJson(products);
                    filename = "products.json";
                    mediaType = MediaType.APPLICATION_JSON;
                    break;
                case "excel":
                    data = exportService.exportToExcel(products);
                    filename = "products.xlsx";
                    mediaType = MediaType.APPLICATION_OCTET_STREAM;
                    break;
                default:
                    return ResponseEntity.badRequest().build();
            }
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                    .contentType(mediaType)
                    .body(data);
                    
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/dashboard/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats(Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalJobs", scrapingService.getUserJobs(user.getId()).size());
        stats.put("totalProducts", productRepository.countByUserId(user.getId()));
        stats.put("completedJobs", scrapingService.getUserJobs(user.getId()).stream()
                .mapToLong(job -> job.getStatus() == ScrapingJob.JobStatus.COMPLETED ? 1 : 0).sum());
        
        long totalJobs = scrapingService.getUserJobs(user.getId()).size();
        long completedJobs = (Long) stats.get("completedJobs");
        double successRate = totalJobs > 0 ? (completedJobs * 100.0 / totalJobs) : 0;
        stats.put("successRate", successRate);
        
        return ResponseEntity.ok(stats);
    }
}