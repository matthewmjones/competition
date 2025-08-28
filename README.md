# Competition Dashboard

A real-time economic simulation web application that models competitive market scenarios where companies compete by setting prices, with automatic demand and profit calculations based on economic formulas.

![Java](https://img.shields.io/badge/Java-21%20LTS-orange?style=flat-square&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.5-green?style=flat-square&logo=spring)
![Maven](https://img.shields.io/badge/Maven-3-blue?style=flat-square&logo=apache-maven)
![License](https://img.shields.io/badge/License-Educational-lightgrey?style=flat-square)

## 🎯 Overview

This educational web application demonstrates economic competition theory through an interactive simulation. Companies can set prices and observe how market forces affect demand and profitability in real-time. The application features live data visualization with automatic recalculation of market metrics when any company adjusts their pricing strategy.

## ✨ Key Features

### 🏢 Company Management
- Add companies with basic information (name, email, phone)
- View individual company dashboards with detailed analytics
- Track pricing strategies across multiple companies

### 📊 Real-time Economic Simulation
- **Dynamic Pricing**: Companies can set prices between $1.00 - $20.00
- **Automatic Demand Calculation**: `demand = 100 - 5p + 2q`
  - `p` = company's own price
  - `q` = average price of all competitors
- **Profit Calculation**: `profit = (price - 2) × demand`
  - Fixed cost per unit: $2.00

### 📈 Interactive Dashboards
- **Main Dashboard**: Overview of all companies with profit bar charts and price/demand trends
- **Company Dashboards**: Individual views with dual-axis charts and integrated price input
- **Real-time Updates**: Server-Sent Events (SSE) for live chart refresh across all connected browsers
- **Mobile Responsive**: Optimized layouts for mobile and tablet devices

### 🔧 Administrative Tools
- Data reset functionality (with safety confirmations)
- Database statistics monitoring
- Bulk data management for educational scenarios

## 🚀 Technology Stack

- **Backend**: Spring Boot 3.5.5, Java 21 LTS
- **Frontend**: Thymeleaf templates, D3.js for visualizations
- **Database**: MySQL 8.0+
- **Real-time Communication**: Server-Sent Events (SSE)
- **Build Tool**: Maven 3
- **Testing**: JUnit 5

## 🏗️ Architecture

```
src/
├── main/java/uk/ac/ucl/competition/
│   ├── entity/          # JPA entities (Company, Price, Demand, Profit)
│   ├── repository/      # Spring Data repositories
│   ├── service/         # Business logic and economic calculations
│   └── controller/      # REST controllers and SSE endpoints
└── resources/
    ├── templates/       # Thymeleaf HTML templates
    ├── static/         # CSS, JS, and static assets
    └── data.sql        # Sample data for demonstration
```

## 🚀 Getting Started

### Prerequisites
- Java 21 LTS or later
- Maven 3 (or use included Maven wrapper)
- MySQL Server 8.0+ running locally
- `MYSQL_SERVER_PWD` environment variable set with your MySQL root password

### Installation & Running

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd competition
   ```

2. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

3. **Access the application**
   - Open your browser to `http://localhost:8080`
   - The application starts with sample data pre-loaded

### Development Commands

```bash
# Run tests
./mvnw test

# Build the project
./mvnw clean package

# Run specific test class
./mvnw test -Dtest=CompetitionApplicationTests

# Build for production
./mvnw clean package -Pproduction
```

## 🎮 Usage Guide

### 1. Company Management
- Navigate to "Add Company" to create new companies
- Each company needs a unique name (up to 20 characters)
- Email and phone are optional fields

### 2. Setting Prices
- Use "Add Price" to set prices for existing companies
- Prices must be between $1.00 and $20.00
- Price changes immediately trigger recalculation for all companies

### 3. Viewing Analytics
- **Main Dashboard** (`/dashboard`): See all companies at once
- **Company Dashboard** (`/dashboard/company/{name}`): Detailed individual view
- Charts update automatically when any company changes prices

### 4. Understanding the Economics
- **Lower prices** = higher demand but lower profit margins
- **Higher prices** = lower demand but higher profit margins
- **Competitor pricing** affects your demand through the economic formula
- **Sweet spot** typically exists around $8-12 depending on competition

## 📊 Data Model

### Core Entities
- **Company**: Business information and identity
- **Price**: Historical pricing data with timestamps
- **Demand**: Calculated demand values based on market conditions
- **Profit**: Derived profit calculations from price and demand

### Economic Relationships
- All companies' latest prices determine demand for each company
- Demand directly influences profit calculation
- Market equilibrium emerges from competitive pricing decisions

## 🔧 Configuration

### Database
- Uses H2 in-memory database (data resets on restart)
- H2 Console available at `/h2-console` in development
- JDBC URL: `jdbc:h2:mem:testdb`

### Application Properties
Key configurations in `application.properties`:
- Server port: 8080
- H2 console: enabled
- JPA: hibernate DDL auto-create
- Session storage: JDBC

## 🧪 Testing

The application includes comprehensive tests:
```bash
# Run all tests
./mvnw test

# Run with coverage
./mvnw test jacoco:report
```

## 🛣️ API Endpoints

### Web Interface
- `/` - Redirect to dashboard
- `/dashboard` - Main dashboard
- `/dashboard/company/{name}` - Individual company dashboard
- `/company_input` - Add new company form
- `/price_input` - Add price data form
- `/admin` - Administrative panel

### REST API
- `GET /api/chart-data/all` - All companies' chart data
- `GET /api/chart-data/company/{name}` - Specific company data
- `GET /api/chart-updates` - SSE endpoint for real-time updates

### Administrative
- `POST /admin/reset/all` - Reset all data (requires confirmation)
- `POST /admin/reset/data-only` - Reset data, keep companies

## 🎓 Educational Value

This application demonstrates:
- **Microeconomic principles**: Price competition and market dynamics
- **Real-time data visualization**: D3.js charts with live updates
- **Spring Boot architecture**: Full-stack web development patterns
- **Database relationships**: JPA entities and repository patterns
- **Responsive design**: Mobile-first web development

## 🤝 Contributing

This is an educational project. Contributions are welcome for:
- Additional economic models
- Enhanced visualizations
- Performance improvements
- Testing coverage
- Documentation improvements

## 📝 License

This project is designed for educational purposes. Please refer to your institution's guidelines for academic use.

## 🐛 Troubleshooting

### Common Issues

1. **Port 8080 already in use**
   ```bash
   ./mvnw spring-boot:run -Dserver.port=8081
   ```

2. **Java version issues**
   ```bash
   java --version  # Ensure Java 21+
   ```

3. **Memory issues**
   ```bash
   export MAVEN_OPTS="-Xmx1024m"
   ./mvnw spring-boot:run
   ```

### Development Tips
- Use Spring Boot DevTools for hot reloading
- Check browser console for SSE connection issues
- Monitor H2 console for database state
- Use browser developer tools to debug chart rendering

## 📞 Support

For questions about this educational software:
1. Check the troubleshooting section above
2. Review the application logs for error details
3. Ensure all prerequisites are installed correctly
4. Verify browser compatibility (modern browsers required for D3.js)

---

**Built with ❤️ for economic education and Spring Boot learning**