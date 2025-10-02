# 🚀 QUICK REFERENCE - Deployment Options

## INSTANT ACCESS (0 Setup Time)
```powershell
# Open browser demo - works immediately
Start-Process "simple-demo/index.html"
```
**Credentials**: `user1/user123` or `admin/admin123`

## SPRING BOOT (2-5 Minutes Setup)
```bash
# Development mode
cd backend
mvn spring-boot:run

# Production mode
mvn clean package
java -jar target/campus-review-portal.jar
```
**URL**: http://localhost:8080

## SERVLET BACKUP (15-30 Minutes Setup)
```bash
# If Spring Boot fails
cd backend-servlet-backup
mvn clean package
# Deploy to Tomcat webapps/
```

## VALIDATION
```powershell
# Test all options
./validate-all-options.ps1
```

## EMERGENCY FALLBACK
- GitHub: https://github.com/ombhombe2006/campus_complaint_portal
- Always keep simple-demo/ ready!

## PRESENTATION STRATEGY
1. **START**: simple-demo (guaranteed success)
2. **SHOW**: Spring Boot if working
3. **BACKUP**: GitHub repo on any device