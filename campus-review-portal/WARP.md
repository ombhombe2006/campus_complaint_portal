# WARP.md

This file provides guidance to WARP (warp.dev) when working with code in this repository.

## Project Overview

Campus Review Portal is a complete 3-tier web application for campus facility reviews and complaint management. Built using simple, proven technologies per project requirements:

- **Frontend**: HTML, CSS, JavaScript (vanilla - no frameworks)
- **Backend**: Java Servlets with JDBC (no frameworks - direct database access)
- **Database**: MySQL with straightforward schema design

## Architecture

### Backend Structure
```
backend/src/main/java/com/campus/
├── servlet/
│   ├── LoginServlet.java       # Dual authentication (user/admin)
│   ├── ReviewServlet.java      # CRUD operations for reviews
│   ├── ComplaintServlet.java   # CRUD operations for complaints
│   └── (other servlets)
└── util/
    ├── DatabaseConnection.java # MySQL JDBC connection management
    └── CORSFilter.java        # Cross-origin request handling
```

### Database Schema
```sql
users (id, username, password, full_name, email)
admins (id, username, password, full_name, email)  
facilities (id, name, category)
reviews (id, user_id, facility_id, title, content, rating, status)
complaints (id, user_id, facility_id, title, description, status, admin_response)
```

### Key Design Patterns
- **Servlet-based REST API**: Each servlet handles specific endpoints (`/api/login`, `/api/reviews/*`, etc.)
- **Session Management**: HttpSession for user authentication state
- **Dual Authentication**: Separate tables and logic for users vs admins
- **CORS Handling**: Custom filter for cross-origin requests
- **JSON Communication**: Simple JSON parsing/generation using json-simple library

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
# Build WAR file
cd backend
mvn clean package

# Deploys to: backend/target/campus-review-portal.war
# Deploy to Tomcat webapps/ directory
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

### Local Development Server
```bash
# Start Tomcat server (after deploying WAR)
# Access application at: http://localhost:8080/campus-review-portal/

# API endpoints available at:
# POST /api/login
# GET /api/reviews/recent
# GET /api/reviews (admin)
# POST /api/reviews
# GET/POST/PUT /api/complaints
```

## Configuration

### Database Configuration
Default configuration in `DatabaseConnection.java`:
- **URL**: `jdbc:mysql://localhost:3306/campus_review_portal`
- **Username**: `root`
- **Password**: `password`

Also configured in `web.xml` context parameters for consistency.

### Default Credentials
- **User**: username=`user1`, password=`user123`
- **Admin**: username=`admin`, password=`admin123`

## Key Implementation Details

### Authentication Flow
1. Frontend sends JSON to `/api/login` with `{username, password, loginType}`
2. LoginServlet queries appropriate table (users vs admins)
3. Creates HttpSession with user details
4. All subsequent requests check session for authentication

### API Design
- **REST-like endpoints**: Each servlet handles multiple HTTP methods
- **JSON communication**: Request/response bodies use JSON format
- **Session-based auth**: No tokens, uses traditional servlet sessions
- **Error handling**: Consistent JSON error responses with HTTP status codes

### Frontend-Backend Integration
- **AJAX calls**: JavaScript uses fetch() for API communication
- **Dynamic updates**: DOM manipulation updates UI based on API responses
- **Form handling**: JavaScript prevents default form submission, sends JSON instead
- **Responsive design**: CSS handles mobile/desktop layouts

### Security Considerations
- **Separate auth tables**: Users cannot access admin functions
- **Session validation**: Servlets check session before processing requests
- **SQL injection prevention**: Uses PreparedStatement throughout
- **Input validation**: Both frontend and backend validate form data

## Development Workflow

1. **Database changes**: Update `database/schema.sql`, reimport to MySQL
2. **Backend changes**: Edit Java files, run `mvn clean package`, redeploy WAR
3. **Frontend changes**: Edit HTML/CSS/JS files directly (no build step)
4. **Testing**: Use browser developer tools, check Tomcat logs, verify database state

## Troubleshooting

### Common Issues
- **Database connection failed**: Check MySQL running, database exists, correct credentials
- **404 on API calls**: Ensure Tomcat running, WAR deployed, correct URL paths
- **Login not working**: Verify database has sample users, check session cookies enabled
- **CORS errors**: Ensure frontend accessed through same port as backend

### Debugging
- **Backend logs**: Check Tomcat logs in `logs/catalina.out`
- **Database queries**: Use MySQL Workbench or phpMyAdmin to verify data
- **Frontend debugging**: Use browser console, Network tab for API calls
- **Session issues**: Check browser Application tab for session cookies