package com.ecommerce.scraper.scraper;

import com.ecommerce.scraper.model.Product;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class EbayScraper extends BaseScraper {
    
    @Override
    public List<Product> scrapeProducts(String searchQuery, int maxPages) {
        List<Product> products = new ArrayList<>();
        
        for (int page = 1; page <= maxPages; page++) {
            try {
                String url = String.format("https://www.ebay.com/sch/i.html?_nkw=%s&_pgn=%d", 
                                         searchQuery.replace(" ", "+"), page);
                
                Document doc = getDocument(url);
                Elements productElements = doc.select(".s-item__wrapper");
                
                for (Element element : productElements) {
                    Product product = parseProduct(element);
                    if (product != null) {
                        products.add(product);
                    }
                }
                
                randomDelay();
                
            } catch (Exception e) {
                System.err.println("Error scraping eBay page " + page + ": " + e.getMessage());
            }
        }
        
        return products;
    }
    
    @Override
    protected Product parseProduct(Element element) {
        try {
            Product product = new Product();
            
            // Name
            Element nameElement = element.selectFirst(".s-item__title");
            if (nameElement != null) {
                product.setName(nameElement.text().trim());
            }
            
            // Price
            Element priceElement = element.selectFirst(".s-item__price");
            if (priceElement != null) {
                String priceText = priceElement.text().replaceAll("[^\\d.]", "");
                Pattern pattern = Pattern.compile("(\\d+\\.?\\d*)");
                Matcher matcher = pattern.matcher(priceText);
                if (matcher.find()) {
                    product.setPrice(Double.parseDouble(matcher.group(1)));
                }
            }
            
            // Rating (eBay doesn't always show ratings)
            product.setRating(0.0);
            product.setReviewsCount(0);
            
            // Image URL
            Element imgElement = element.selectFirst("img");
            if (imgElement != null) {
                product.setImageUrl(imgElement.attr("src"));
            }
            
            // Product URL
            Element linkElement = element.selectFirst(".s-item__link");
            if (linkElement != null) {
                product.setProductUrl(linkElement.attr("href"));
            }
            
            product.setWebsite("eBay");
            product.setCategory("General");
            product.setBrand("N/A");
            product.setAvailability("Available");
            
            return product;
            
        } catch (Exception e) {
            System.err.println("Error parsing eBay product: " + e.getMessage());
            return null;
        }
    }
}