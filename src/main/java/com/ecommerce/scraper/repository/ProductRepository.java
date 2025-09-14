package com.ecommerce.scraper.repository;

import com.ecommerce.scraper.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByJobId(Long jobId);
    
    @Query("SELECT p FROM Product p JOIN p.job j WHERE j.user.id = :userId")
    List<Product> findByUserId(@Param("userId") Long userId);
    
    @Query("SELECT p FROM Product p JOIN p.job j WHERE j.user.id = :userId AND p.job.id = :jobId")
    List<Product> findByUserIdAndJobId(@Param("userId") Long userId, @Param("jobId") Long jobId);
    
    @Query("SELECT COUNT(p) FROM Product p JOIN p.job j WHERE j.user.id = :userId")
    Long countByUserId(@Param("userId") Long userId);
}