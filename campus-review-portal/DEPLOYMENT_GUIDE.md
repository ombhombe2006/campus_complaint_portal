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
- Java: https://www.oracle.com/java/technologies/javase-downloads.html
- Maven: https://maven.apache.org/download.cgi (extract to `C:\apache-maven`)
- XAMPP: https://www.apachefriends.org/download.html
- Tomcat: https://tomcat.apache.org/download-90.cgi (extract to `C:\apache-tomcat`)

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

# Build project
cd backend
mvn clean package

# Deploy to Tomcat
Copy-Item "target\campus-review-portal.war" "C:\apache-tomcat\webapps\"
Start-Process "C:\apache-tomcat\bin\startup.bat"

# Test: http://localhost:8080/campus-review-portal
```

## 🚨 Quick Fixes
**Maven not found:** `$env:PATH += ";C:\apache-maven\bin"`  
**Port 8080 busy:** `netstat -ano | findstr :8080` then `taskkill /PID [number] /F`  
**DB connection failed:** Check MySQL running, verify database name `campus_review_portal`  
**Emergency fallback:** `Start-Process "simple-demo\index.html"`

## 📋 Checklist
```
☐ Java installed
☐ Maven extracted & in PATH
☐ XAMPP installed & MySQL running
☐ Database created & schema imported
☐ Tomcat extracted
☐ Project built: mvn clean package
☐ WAR deployed to webapps/
☐ Tomcat started
☐ Application accessible: localhost:8080/campus-review-portal
☐ Login works: user1/user123, admin/admin123
```

**Strategy:** Always demo `simple-demo\index.html` first, then show full deployment if time permits.
