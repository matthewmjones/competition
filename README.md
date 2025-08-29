# Economic Competition Simulation

A real-time web application that demonstrates competitive market economics through interactive price-setting simulation. Companies compete by adjusting prices while observing how market forces affect demand and profitability in real-time.

![Java](https://img.shields.io/badge/Java-21%20LTS-orange?style=flat-square&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.5-green?style=flat-square&logo=spring)
![Maven](https://img.shields.io/badge/Maven-3-blue?style=flat-square&logo=apache-maven)

## Overview

This educational application simulates competitive market dynamics where multiple companies set prices and immediately see the effects on demand and profit. Built with Spring Boot and featuring real-time updates, it provides an interactive way to understand microeconomic principles.

## Features

### Economic Simulation
- **Dynamic Pricing**: Set prices between $1.00 - $20.00
- **Automatic Calculations**: Real-time demand and profit computation
- **Market Competition**: Pricing decisions affect all competitors
- **Economic Formulas**:
  - Demand: `100 - 5p + 2q` (where p = own price, q = competitor average)
  - Profit: `(price - 2) × demand` (fixed cost: $2/unit)

### Interactive Interface
- **Real-time Updates**: Live charts with Server-Sent Events
- **Multiple Views**: Company overview and individual dashboards
- **Mobile Responsive**: Works on all devices
- **Data Visualization**: D3.js charts for trends and comparisons

### Management Tools
- Create and manage companies
- Historical price tracking
- Administrative panel with data reset options

## Technology Stack

- **Backend**: Spring Boot 3.5.5, Java 21 LTS
- **Frontend**: Thymeleaf, D3.js
- **Database**: MySQL 8.0+ (H2 for development)
- **Build**: Maven 3
- **Testing**: JUnit 5

## Getting Started

### Prerequisites
- Java 21 LTS or later
- MySQL 8.0+ (for production) or H2 (for development)

### Installation

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd competition
   ```

2. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

3. Open http://localhost:8080 in your browser

The application starts with sample data for immediate exploration.

### Development

```bash
# Run tests
./mvnw test

# Build project
./mvnw clean package

# Run with different port
./mvnw spring-boot:run -Dserver.port=8081
```

## Usage

### Basic Workflow
1. **Create Companies**: Add companies with unique names
2. **Set Prices**: Enter prices between $1.00-$20.00
3. **Observe Results**: Watch real-time demand and profit changes
4. **Analyze Competition**: Compare performance across companies

### Key Pages
- **Homepage** (`/`): Introduction and getting started guide
- **Dashboard** (`/dashboard`): Overview of all companies
- **Company View** (`/dashboard/company/{name}`): Individual company analytics
- **Admin Panel** (`/admin`): Data management and reset tools

### Economic Strategy
- Lower prices increase demand but reduce profit margins
- Higher prices decrease demand but increase profit margins
- Competitor pricing directly affects your demand
- Optimal pricing typically falls between $8-12 depending on competition

## API Reference

### Web Pages
- `/` - Homepage with introduction
- `/dashboard` - Main dashboard
- `/dashboard/company/{name}` - Individual company view
- `/company_input` - Add company form
- `/price_input` - Set prices form
- `/admin` - Administrative panel

### REST Endpoints
- `GET /api/chart-data/all` - All companies data
- `GET /api/chart-data/company/{name}` - Specific company data
- `GET /api/chart-updates` - SSE endpoint for real-time updates

## Educational Value

This application demonstrates key concepts in:
- **Microeconomics**: Price competition and market dynamics
- **Software Architecture**: Spring Boot MVC pattern
- **Real-time Systems**: Server-Sent Events implementation
- **Data Visualization**: Interactive charts with D3.js

## Contributing

Contributions are welcome! Areas for improvement:
- Additional economic models
- Enhanced visualizations
- Performance optimizations
- Test coverage
- Documentation

## License

Educational use only. Please refer to your institution's guidelines for academic use.

## Troubleshooting

**Port already in use:**
```bash
./mvnw spring-boot:run -Dserver.port=8081
```

**Java version check:**
```bash
java --version  # Ensure Java 21+
```

**Memory issues:**
```bash
export MAVEN_OPTS="-Xmx1024m"
./mvnw spring-boot:run
```