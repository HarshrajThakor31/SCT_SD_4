package com.ecommerce.scraper.service;

import com.ecommerce.scraper.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.util.List;

@Service
public class ExportService {
    
    public byte[] exportToCsv(List<Product> products) throws Exception {
        StringWriter stringWriter = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(stringWriter);
        
        // Header
        String[] header = {"Name", "Price", "Rating", "Reviews", "Website", "Category", "Brand", "Availability", "URL"};
        csvWriter.writeNext(header);
        
        // Data
        for (Product product : products) {
            String[] data = {
                product.getName(),
                product.getPrice() != null ? product.getPrice().toString() : "",
                product.getRating() != null ? product.getRating().toString() : "",
                product.getReviewsCount() != null ? product.getReviewsCount().toString() : "",
                product.getWebsite(),
                product.getCategory(),
                product.getBrand(),
                product.getAvailability(),
                product.getProductUrl()
            };
            csvWriter.writeNext(data);
        }
        
        csvWriter.close();
        return stringWriter.toString().getBytes();
    }
    
    public byte[] exportToJson(List<Product> products) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsBytes(products);
    }
    
    public byte[] exportToExcel(List<Product> products) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Products");
        
        // Header
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Name", "Price", "Rating", "Reviews", "Website", "Category", "Brand", "Availability", "URL"};
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }
        
        // Data
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            Row row = sheet.createRow(i + 1);
            
            row.createCell(0).setCellValue(product.getName());
            row.createCell(1).setCellValue(product.getPrice() != null ? product.getPrice() : 0);
            row.createCell(2).setCellValue(product.getRating() != null ? product.getRating() : 0);
            row.createCell(3).setCellValue(product.getReviewsCount() != null ? product.getReviewsCount() : 0);
            row.createCell(4).setCellValue(product.getWebsite());
            row.createCell(5).setCellValue(product.getCategory());
            row.createCell(6).setCellValue(product.getBrand());
            row.createCell(7).setCellValue(product.getAvailability());
            row.createCell(8).setCellValue(product.getProductUrl());
        }
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        
        return outputStream.toByteArray();
    }
}