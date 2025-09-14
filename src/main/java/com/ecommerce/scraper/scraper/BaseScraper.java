package com.ecommerce.scraper.scraper;

import com.ecommerce.scraper.model.Product;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.util.List;
import java.util.Random;

public abstract class BaseScraper {
    protected Random random = new Random();
    
    protected WebDriver createDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
        return new ChromeDriver(options);
    }
    
    protected Document getDocument(String url) throws Exception {
        return Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                .timeout(10000)
                .get();
    }
    
    protected void randomDelay() {
        try {
            Thread.sleep(1000 + random.nextInt(2000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    public abstract List<Product> scrapeProducts(String searchQuery, int maxPages);
    protected abstract Product parseProduct(org.jsoup.nodes.Element element);
}