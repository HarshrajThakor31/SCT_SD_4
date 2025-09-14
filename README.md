# E-commerce Product Scraper (Java Spring Boot)

A full-stack web application for scraping product information from e-commerce websites with a modern React frontend and Java Spring Boot backend.

## Features

### Core Functionality
- **Multi-site Scraping**: Support for Amazon, eBay, and extensible to other sites
- **Product Data Extraction**: Names, prices, ratings, reviews, images, URLs
- **Structured Data Storage**: H2 database with JPA/Hibernate
- **Multiple Export Formats**: CSV, JSON, Excel

### User Management
- **Authentication**: JWT-based login/registration system
- **User Isolation**: Each user's data is separate and secure
- **Session Management**: Secure token-based authentication

### Dashboard & Analytics
- **Real-time Dashboard**: Job statistics and progress tracking
- **Data Visualization**: Charts showing scraping results
- **Job Management**: Create, monitor, and manage scraping jobs

### Production Features
- **Rate Limiting**: Respectful scraping with delays
- **Error Handling**: Comprehensive error management
- **Background Processing**: Async job execution
- **Containerization**: Docker support for easy deployment

## Quick Start

### Prerequisites
- Java 17+
- Maven 3.6+
- Node.js 16+
- Chrome browser (for Selenium)

### Backend Setup
```bash
# Build and run the Java application
mvn clean install
mvn spring-boot:run
```

### Frontend Setup
```bash
# Navigate to frontend directory
cd frontend

# Install dependencies
npm install

# Start development server
npm start
```

### Using Windows Batch Script
```bash
# Run both backend and frontend
run.bat
```

### Using Docker
```bash
# Build and run with Docker Compose
docker-compose -f docker-compose.java.yml up --build
```

## API Endpoints

### Authentication
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login

### Scraping
- `GET /api/websites` - Get available websites
- `POST /api/scraping-jobs` - Create new scraping job
- `GET /api/scraping-jobs` - List user's jobs
- `GET /api/scraping-jobs/{id}` - Get specific job details

### Data Management
- `GET /api/products` - Get scraped products
- `GET /api/export/{format}` - Export data (CSV/JSON/Excel)
- `GET /api/dashboard/stats` - Dashboard statistics

## Architecture

### Backend (Spring Boot)
- **Models**: JPA entities with User, Product, ScrapingJob
- **Authentication**: JWT tokens with BCrypt password hashing
- **Scrapers**: Modular scraper architecture with factory pattern
- **Background Tasks**: Async job processing with @Async
- **Database**: H2 in-memory database for development

### Frontend (React)
- **Components**: Modular React components with hooks
- **State Management**: Local state with API integration
- **UI/UX**: Tailwind CSS for responsive design
- **Data Visualization**: Recharts for analytics

### Database Schema
```sql
Users: id, username, email, password, active, created_at
Products: id, name, price, rating, reviews_count, image_url, product_url, description, category, brand, availability, website, scraped_at, job_id
ScrapingJobs: id, name, website, search_query, status, total_products, scraped_products, created_at, completed_at, error_message, user_id
```

## Technology Stack

### Backend
- **Java 17** - Programming language
- **Spring Boot 3.2** - Application framework
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Data persistence
- **H2 Database** - In-memory database
- **Selenium WebDriver** - Web scraping
- **JSoup** - HTML parsing
- **JWT** - Token-based authentication
- **Apache POI** - Excel export
- **OpenCSV** - CSV export

### Frontend
- **React 18** - UI framework
- **Axios** - HTTP client
- **Tailwind CSS** - Styling
- **Recharts** - Data visualization

## Configuration

### Application Properties
```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:h2:file:./data/ecommerce_scraper
    driver-class-name: org.h2.Driver
    username: sa
    password: 

jwt:
  secret: your-secret-key-change-in-production
  expiration: 86400000
```

## Usage Examples

### Creating a Scraping Job
1. Login to the application
2. Navigate to "New Job" tab
3. Enter job name, select website, and search query
4. Click "Start Scraping"
5. Monitor progress in Dashboard

### Exporting Data
1. Go to "Products" tab
2. Select export format (CSV, JSON, Excel)
3. Click export button
4. File downloads automatically

## Development

### Running Tests
```bash
mvn test
```

### Building for Production
```bash
mvn clean package
java -jar target/scraper-1.0.0.jar
```

### Adding New Scrapers
1. Extend `BaseScraper` class
2. Implement `scrapeProducts()` and `parseProduct()` methods
3. Register in `ScraperFactory`

## Deployment

### Production Checklist
- [ ] Change JWT secret in production
- [ ] Use PostgreSQL instead of H2
- [ ] Configure proper logging
- [ ] Set up reverse proxy (Nginx)
- [ ] Set up SSL certificates
- [ ] Configure monitoring

### Docker Deployment
```bash
# Production deployment
docker-compose -f docker-compose.java.yml up -d
```

## Legal Considerations
- Respect robots.txt files
- Implement rate limiting
- Follow website terms of service
- Use scraped data responsibly

## Contributing
1. Fork the repository
2. Create feature branch
3. Add tests for new functionality
4. Submit pull request

## License
MIT License - see LICENSE file for details