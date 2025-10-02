# Campus Review Portal - Warp AI Assistant Configuration

## Project Overview

Campus Review Portal is a complete web application for campus facility reviews and complaint management built with:

- **Frontend**: HTML, CSS, JavaScript (vanilla - no frameworks)
- **Backend**: Spring Boot with Spring Data JPA
- **Database**: MySQL with JPA entity mapping
- **Build Tool**: Maven
- **Server**: Spring Boot embedded Tomcat

## Architecture

### Backend Structure
```
backend/src/main/java/com/campus/
├── CampusReviewPortalApplication.java  # Spring Boot main class
├── controller/                         # REST Controllers
│   ├── LoginController.java            # Authentication endpoints
│   └── ReviewController.java           # Review CRUD operations
├── entity/                             # JPA Entities
│   ├── User.java, Admin.java
│   ├── Facility.java
│   └── Review.java, Complaint.java
├── repository/                         # Spring Data JPA
│   ├── UserRepository.java
│   └── ReviewRepository.java (etc.)
├── dto/                                # Data Transfer Objects
│   └── LoginRequest.java, ApiResponse.java
└── config/
    └── CorsConfig.java                 # Cross-origin configuration
```

### Database Schema
```sql
users (id, username, password, full_name, email)
admins (id, username, password, full_name, email)  
facilities (id, name, category)
reviews (id, user_id, facility_id, title, content, rating, status)
complaints (id, user_id, facility_id, title, description, status, admin_response)
```

## Key Development Rules
- **Frontend**: Must use only HTML, CSS, and JavaScript - no frameworks or libraries
- **Backend**: Must use Spring Boot with JPA - no plain JDBC or servlets
- **Database**: MySQL only
- **Architecture**: REST API with Spring controllers, JPA repositories
- **Authentication**: Session-based authentication with separate User and Admin entities
- **CORS**: Configured in Spring for cross-origin requests

## Build & Development Commands

### Database Setup
```bash
# Start MySQL (if using XAMPP)
# Import database/schema.sql into MySQL
# Creates database: campus_review_portal
# Default credentials: root/password (configured in DatabaseConnection.java)
```

### Backend Build & Deploy
```bash
# Run development server
cd backend
mvn spring-boot:run

# Or build JAR and run
mvn clean package
java -jar target/campus-review-portal.jar

# Backend runs on http://localhost:8080
```

### Frontend Development
```bash
# Frontend is static HTML/CSS/JS - no build process
# Open frontend/index.html directly or serve through web server
# For full functionality, backend must be running on port 8080
# Note: images/ folder exists but is currently empty
```

### Testing
```bash
# Run tests
cd backend
mvn test

# Test database connection
# Use DatabaseConnection.testConnection() method
```

### API Endpoints
```bash
# Spring Boot runs on http://localhost:8080
# Frontend should make requests to backend API

# Available endpoints:
# POST /api/login
# POST /api/logout  
# GET /api/session
# GET /api/reviews/recent
# GET /api/reviews (admin)
# POST /api/reviews
# PUT /api/reviews (admin)
# GET /api/reviews/facilities
```

## Configuration

### Database Configuration
Spring Boot configuration in `application.properties`:
- **URL**: `jdbc:mysql://localhost:3306/campus_review_portal`
- **Username**: `root`
- **Password**: `password`
- **JPA**: Hibernate auto-configuration enabled

### Default Credentials
- **User**: username=`user1`, password=`user123`
- **Admin**: username=`admin`, password=`admin123`

## Key Implementation Details

### Authentication Flow
1. Frontend sends JSON to `/api/login` with `{username, password, loginType}`
2. LoginController queries appropriate repository (UserRepository vs AdminRepository)
3. Creates HttpSession with user details
4. All subsequent requests check session for authentication

### API Design
- **RESTful endpoints**: Spring controllers with proper HTTP method mapping
- **JSON communication**: Request/response bodies use JSON format
- **Session-based auth**: HttpSession for user state management
- **Error handling**: Consistent ApiResponse wrapper with HTTP status codes

### Frontend-Backend Integration
- **AJAX calls**: JavaScript uses fetch() for API communication
- **Dynamic updates**: DOM manipulation updates UI based on API responses
- **Form handling**: JavaScript prevents default form submission, sends JSON instead
- **Responsive design**: CSS handles mobile/desktop layouts

### Security Considerations
- **Separate auth entities**: Users cannot access admin functions
- **Session validation**: Controllers check session before processing requests
- **SQL injection prevention**: JPA/Hibernate handles parameterized queries
- **Input validation**: Bean validation annotations and frontend validation

## Development Workflow

1. **Database changes**: Update `database/schema.sql`, restart Spring Boot app
2. **Backend changes**: Edit Java files, Spring Boot auto-restarts in dev mode
3. **Frontend changes**: Edit HTML/CSS/JS files directly (no build step)
4. **Testing**: Use browser developer tools, check Spring Boot logs, verify database state

## Troubleshooting

### Common Issues
- **Database connection failed**: Check MySQL running, database exists, correct credentials in application.properties
- **404 on API calls**: Ensure Spring Boot app running on port 8080, correct URL paths
- **Login not working**: Verify database has sample users, check session cookies enabled
- **CORS errors**: Check CorsConfig.java allows frontend origin

### Debugging
- **Backend logs**: Check Spring Boot console output or application logs
- **Database queries**: Use MySQL Workbench or phpMyAdmin to verify data
- **Frontend debugging**: Use browser console, Network tab for API calls
- **Session issues**: Check browser Application tab for session cookies
