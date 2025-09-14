package com.ecommerce.scraper.repository;

import com.ecommerce.scraper.model.ScrapingJob;
import com.ecommerce.scraper.model.ScrapingJob.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ScrapingJobRepository extends JpaRepository<ScrapingJob, Long> {
    List<ScrapingJob> findByUserId(Long userId);
    List<ScrapingJob> findByUserIdAndStatus(Long userId, JobStatus status);
    Long countByUserId(Long userId);
    Long countByUserIdAndStatus(Long userId, JobStatus status);
}