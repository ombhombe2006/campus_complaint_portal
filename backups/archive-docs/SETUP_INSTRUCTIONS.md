# Campus Review Portal - Setup Instructions

## 📋 Requirements
- MySQL Server (XAMPP recommended for easy setup)
- Java 8 or higher
- Web browser (Chrome/Firefox/Edge)
- Web server (Tomcat or XAMPP)

## 🚀 Quick Setup (For Demo on Any Computer)

### Option 1: Using XAMPP (Recommended for Demo)
1. **Install XAMPP** from https://www.apachefriends.org/
2. **Start Apache and MySQL** in XAMPP Control Panel
3. **Import Database**:
   - Open http://localhost/phpmyadmin
   - Create new database: `campus_review_portal`
   - Import the `database/schema.sql` file
4. **Test Frontend**: Open `frontend/index.html` in browser

### Option 2: Standalone MySQL Setup
1. **Install MySQL Server**
2. **Run Database Script**:
   ```sql
   -- Copy and paste contents of database/schema.sql into MySQL Workbench
   ```
3. **Test Frontend**: Open `frontend/index.html` in browser

## 📁 Project Structure
```
campus-review-portal/
├── frontend/                    # HTML/CSS/JS files (ready to run)
│   ├── index.html              # Main homepage
│   ├── user-dashboard.html     # User interface
│   ├── admin-dashboard.html    # Admin interface
│   ├── css/style.css          # Red/White theme styling
│   └── js/main.js             # JavaScript functionality
├── backend/                    # Java backend (for full functionality)
├── database/                   # Database setup
│   └── schema.sql             # MySQL database script
└── SETUP_INSTRUCTIONS.md      # This file
```

## 🔑 Login Credentials

### User Login:
- Username: `user1`
- Password: `user123`

### Admin Login:
- Username: `admin`
- Password: `admin123`

## 🎯 Demo Features

### For User Login:
- ✅ View personal reviews and complaints
- ✅ Add new reviews for campus facilities
- ✅ Submit complaints
- ✅ Track complaint status

### For Admin Login:
- ✅ Manage all reviews (approve/reject)
- ✅ Handle complaints (update status, add responses)
- ✅ Add new facilities
- ✅ Monitor system activity

## 🌐 Frontend-Only Demo (No Backend Required)

**For quick demo without backend setup:**

1. Open `frontend/index.html` in browser
2. Show the dual login interface
3. Demonstrate the red/white theme
4. Navigate through dashboard mockups
5. Show responsive design

**Note**: Login functionality requires backend server. For demo purposes, you can show the UI design and explain the workflow.

## 🔧 Full Backend Setup (If Time Permits)

### Prerequisites:
- Apache Tomcat 9.x
- MySQL JDBC Driver
- Java SDK

### Steps:
1. **Deploy WAR file** to Tomcat webapps
2. **Start Tomcat server**
3. **Configure database connection** in web.xml
4. **Access application** at http://localhost:8080/campus-review-portal

## 🎨 Project Highlights

- **Dual Login System**: Separate authentication for users and admins
- **Red & White Theme**: Consistent campus branding
- **Responsive Design**: Works on laptops, tablets, and phones
- **Review System**: Rate facilities (library, turf, washroom, etc.)
- **Complaint Management**: Track issues from submission to resolution
- **Clean Architecture**: HTML/CSS/JS frontend + Java backend + MySQL

## 🚨 Troubleshooting

### Database Connection Issues:
- Check MySQL server is running
- Verify database name: `campus_review_portal`
- Check credentials in backend configuration

### Frontend Not Loading:
- Open `index.html` directly in browser
- Check browser console for JavaScript errors
- Ensure all CSS/JS files are in correct folders

## 📱 Demo Script

1. **Homepage**: Show dual login options
2. **User Journey**: Login → Add Review → Submit Complaint
3. **Admin Journey**: Login → Moderate Reviews → Update Complaints
4. **Responsive Design**: Show on different screen sizes
5. **Database**: Show actual data in MySQL

---

**Tech Stack**: HTML5, CSS3, JavaScript, Java Servlets, MySQL
**Project Level**: Mini-project suitable for academic demonstration