# 🚀 Campus Review Portal - Deployment Guide

## ⚡ INSTANT DEMO (2 minutes)
```powershell
cd "campus-review-portal"
Start-Process "simple-demo\index.html"
# Credentials: user1/user123 or admin/admin123
```
✅ **Always works - perfect for presentations!**

## 🔧 Full Deployment (Fresh Laptop)

**Prerequisites Check:**
```powershell
java --version  # Install Java 11+ if needed
```

### 1. Install Software
**Download & Install:**
- Java 11+: https://www.oracle.com/java/technologies/javase-downloads.html
- Maven: https://maven.apache.org/download.cgi (extract to `C:\apache-maven`)
- XAMPP: https://www.apachefriends.org/download.html
- ~~Tomcat: Not needed! Spring Boot includes embedded server~~

### 2. Setup Database
```powershell
# Start XAMPP → MySQL
# Open http://localhost/phpmyadmin
# Create database: campus_review_portal
# Import: database\schema.sql
```

### 3. Build & Deploy
```powershell
# Add Maven to PATH
$env:PATH += ";C:\apache-maven\bin"

# Build Spring Boot project
cd backend
mvn clean package

# Run Spring Boot JAR (no Tomcat needed!)
java -jar target\campus-review-portal.jar

# Test: http://localhost:8080/campus-review-portal
# Spring Boot runs on embedded server
```

## 🚨 Quick Fixes
**Maven not found:** `$env:PATH += ";C:\apache-maven\bin"`  
**Port 8080 busy:** `netstat -ano | findstr :8080` then `taskkill /PID [number] /F`  
**DB connection failed:** Check MySQL running, verify database name `campus_review_portal`  
**Emergency fallback:** `Start-Process "simple-demo\index.html"`

## 📋 Checklist
```
☐ Java 11+ installed
☐ Maven extracted & in PATH
☐ XAMPP installed & MySQL running
☐ Database created & schema imported
☐ Project built: mvn clean package
☐ Spring Boot JAR running: java -jar target\campus-review-portal.jar
☐ Application accessible: localhost:8080/campus-review-portal
☐ Login works: user1/user123, admin/admin123
```

**Strategy:** Always demo `simple-demo\index.html` first, then show full deployment if time permits.
