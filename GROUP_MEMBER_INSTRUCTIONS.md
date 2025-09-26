# Campus Review Portal - Group Member Instructions

## 🎯 For Demo Day - Everyone Has a Role!

### 📁 **Step 1: Get the Project Folder**
- Get the `campus-review-portal` folder from your teammate
- Copy it to your Desktop or any location
- That's it - no installation needed for basic demo!

---

## 👥 **Group Member Roles & Responsibilities**

### **Member 1: Database Expert** 🗄️
**Your Job**: Handle all database-related demo parts

**What You Need to Know**:
- Database name: `campus_review_portal`
- Tables: users, admins, facilities, reviews, complaints
- Login credentials:
  - User: user1/user123
  - Admin: admin/admin123

**Demo Tasks**:
1. Show database structure in MySQL Workbench/phpMyAdmin
2. Run these queries during demo:
   ```sql
   SELECT * FROM users;
   SELECT * FROM admins;
   SELECT * FROM facilities;
   SELECT * FROM reviews;
   SELECT * FROM complaints;
   ```
3. Explain the separate user/admin login system
4. Show sample data

---

### **Member 2: Frontend Designer** 🎨
**Your Job**: Showcase the user interface and design

**What You Need to Know**:
- Main files: `frontend/index.html`, `user-dashboard.html`, `admin-dashboard.html`
- Theme: Red and white campus colors
- Responsive design works on different screen sizes

**Demo Tasks**:
1. Open `frontend/index.html` in browser
2. Show the dual login interface (User vs Admin)
3. Navigate to both dashboards
4. Demonstrate responsive design (resize browser window)
5. Explain the red/white theme choice
6. Show different sections: reviews, complaints, facility selection

**Practice Lines**:
- "We chose red and white to match typical campus branding"
- "Notice the clean, professional interface suitable for both students and faculty"
- "The design is responsive and works on mobile devices too"

---

### **Member 3: Technical Lead** ⚙️
**Your Job**: Explain the technical architecture and backend

**What You Need to Know**:
- Tech Stack: HTML/CSS/JS frontend + Java backend + MySQL
- Architecture: 3-tier (Frontend → Backend → Database)
- Key files: servlets in `backend/src/main/java/com/campus/servlet/`

**Demo Tasks**:
1. Explain project structure and folders
2. Show the Java servlet code (LoginServlet.java)
3. Explain the JDBC database connection
4. Discuss the web.xml configuration
5. Talk about the dual authentication system

**Practice Lines**:
- "We used pure Java servlets without frameworks to keep it simple"
- "The database connection uses JDBC for direct MySQL access"
- "Separate login tables ensure user credentials can't access admin functions"

---

### **Member 4: Feature Demonstrator** 🚀
**Your Job**: Walk through user journeys and features

**What You Need to Know**:
- User features: Add reviews, submit complaints, track status
- Admin features: Moderate reviews, update complaints, manage facilities
- Campus facilities: Library, Turf, Washroom, Canteen, etc.

**Demo Tasks**:
1. **User Journey Demo**:
   - Click "User Login" button
   - Show user dashboard sections
   - Explain review submission process
   - Show complaint tracking

2. **Admin Journey Demo**:
   - Click "Admin Login" button  
   - Show admin dashboard
   - Explain review moderation
   - Show complaint status updates

**Practice Lines**:
- "Students and faculty can rate facilities from 1-5 stars"
- "Users can track their complaints from 'Pending' to 'Resolved'"
- "Admins can moderate content and respond to complaints"

---

## 🎪 **Demo Day Flow (5-minute presentation)**

1. **Introduction** (30 sec): "We built a Campus Review Portal with dual login system"
2. **Technical Overview** (60 sec): Member 3 explains architecture
3. **Database Demo** (60 sec): Member 1 shows data structure
4. **Frontend Demo** (90 sec): Member 2 shows design and navigation
5. **Feature Walkthrough** (90 sec): Member 4 demonstrates user/admin flows
6. **Conclusion** (30 sec): "This helps improve campus facilities through user feedback"

---

## 🔧 **If Time for Full Setup**

**Quick Setup on Demo Laptop**:
1. Install XAMPP (5 minutes)
2. Start Apache + MySQL
3. Import `database/schema.sql` in phpMyAdmin
4. Open `frontend/index.html`
5. Ready to demo! 

**If No Backend Setup**:
- Just show frontend HTML files
- Show database in MySQL separately
- Explain how they would connect

---

## 📝 **Key Points to Remember**

✅ **Dual Login**: Users and admins have separate authentication  
✅ **Campus Focused**: Reviews for library, turf, washrooms, etc.  
✅ **Red/White Theme**: Professional campus branding  
✅ **Simple Tech Stack**: No fancy frameworks - just HTML/CSS/JS + Java + MySQL  
✅ **Practical Solution**: Solves real campus facility feedback problems  

---

## 🆘 **Emergency Backup Plan**

If technology fails:
1. Show the folder structure
2. Open HTML files in notepad to show code
3. Show database schema in text format
4. Explain the concept and walk through mockups

**Remember**: The idea and execution matter more than perfect technical demo!

---

**Good luck team! 🚀 You've got this!**