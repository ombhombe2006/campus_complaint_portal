# 🏫 Campus Review Portal

A complete web application for campus facility reviews and complaint management system built with Spring Boot.

## 🚀 Quick Start Options

### Option 1: Zero Setup Demo (Instant)
**Perfect for presentations - works immediately**

1. Navigate to `zero-setup-demo/` folder
2. Open `index.html` in any web browser
3. Use demo credentials:
   - **User**: `user1` / `user123`
   - **Admin**: `admin` / `admin123`

✅ **Zero dependencies** - works on any computer instantly!

### Option 2: Full Spring Boot Application 
**Complete working application with real backend**

Requires: MySQL + Java + Maven (see setup below)

## 🌟 Features

### For Users (Students & Faculty):
- ✅ **Review Campus Facilities**: Rate library, turf, washrooms, canteen, labs, gym, etc. (1-5 stars)
- ✅ **Submit Complaints**: Report issues with campus facilities
- ✅ **Track Status**: Monitor progress from "Pending" → "In Progress" → "Resolved"
- ✅ **Personal History**: View all previous reviews and complaints

### For Administrators:
- ✅ **Moderate Reviews**: Approve, reject, or manage user-submitted reviews
- ✅ **Handle Complaints**: Update status, add official responses
- ✅ **Manage Facilities**: Add new campus facilities to the system
- ✅ **Monitor Activity**: Overview of all user feedback and complaints

## 🏗️ Tech Stack

- **Frontend**: HTML5, CSS3, JavaScript (vanilla - no frameworks)
- **Backend**: Spring Boot with JPA/Hibernate (no plain servlets)
- **Database**: MySQL with JPA entity mapping
- **Build Tool**: Maven
- **Server**: Spring Boot embedded Tomcat
- **Theme**: Red and white color scheme

## 📁 Project Structure

```
campus-review-portal/
├── zero-setup-demo/       # 🎯 Zero dependency demo (instant)
│   ├── index.html         # Demo homepage
│   ├── user-dashboard.html
│   ├── admin-dashboard.html
│   ├── css/
│   └── js/                # Mock data simulation
├── frontend/              # 🚀 Full frontend (for Spring Boot)
│   ├── index.html
│   ├── css/style.css
│   └── js/                # Real API integration
├── backend/               # ☕ Spring Boot application
│   ├── src/main/java/
│   │   └── com/campus/
│   │       ├── controller/ # REST controllers
│   │       ├── entity/     # JPA entities
│   │       └── repository/ # Data repositories
│   └── pom.xml            # Maven configuration
├── database/
│   └── schema.sql         # 🗄️ MySQL database setup
└── backups/              # 📚 Archive & backup files
```

## 🎯 Demo Credentials

**User Login:**
- Username: `user1`
- Password: `user123`

**Admin Login:**
- Username: `admin`
- Password: `admin123`

## ⚡ Full Setup Instructions

### Prerequisites
- Java 8+ (✅ Works with Java 11+)
- MySQL Server
- Maven (for building)

### Database Setup
1. Start MySQL Server
2. Import `database/schema.sql`
3. Creates database: `campus_review_portal`
4. Default connection: `root/password` (change in `application.properties` if needed)

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

### Access Application
- **Zero Setup Demo**: Open `zero-setup-demo/index.html`
- **Full Application**: http://localhost:8080 (Spring Boot)

## 🔧 Development

- **Database changes**: Update `database/schema.sql`, restart Spring Boot app
- **Backend changes**: Edit Java files, Spring Boot auto-restarts in dev mode
- **Frontend changes**: Edit HTML/CSS/JS files directly
- **Testing**: Use browser dev tools, check Spring Boot logs, verify database

## 🎨 Design Highlights

- **Campus Branding**: Professional red and white color scheme
- **Responsive Layout**: Works on desktop, tablet, and mobile
- **Clean UI**: Intuitive navigation and dashboard layouts
- **Dual Authentication**: Separate user and admin login systems

## 💡 Use Cases

**Simple Demo Version:**
- Quick presentations without technical setup
- UI/UX showcasing to stakeholders
- Design validation and feedback collection

**Full Implementation:**
- Academic project demonstrations
- Real campus deployment
- Complete backend functionality showcase

## 🚀 Deployment for Presentations

**For Immediate Demo:**
```powershell
# 2-minute setup - works immediately
Start-Process "simple-demo\index.html"
# Credentials: user1/user123 or admin/admin123
```

**For Full Backend Demo:**
```powershell
# 15-minute setup - Spring Boot makes it easier!
# See DEPLOYMENT_GUIDE.md for step-by-step guide
```

**Files:**
- `DEPLOYMENT_GUIDE.md` - Complete deployment guide for fresh laptops
- `test-deployment.ps1` - Pre-presentation validation script
- `simple-demo/` - Instant working demo (always ready!)
