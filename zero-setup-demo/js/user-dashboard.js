// User Dashboard JavaScript

document.addEventListener('DOMContentLoaded', function() {
    // Add demo banner
    addDemoBanner();
    
    // Check authentication
    if (!checkAuth()) {
        return;
    }
    
    const user = getCurrentUser();
    if (user.userType !== 'user') {
        alert('Access denied. User login required.');
        window.location.href = 'index.html';
        return;
    }
    
    // Set welcome message
    document.getElementById('welcomeUser').textContent = `Welcome, ${user.fullName}!`;
    
    // Load initial data
    loadUserReviews();
    loadFacilities();
    
    // Set up form event listeners
    setupFormHandlers();
});

// Show different sections
function showSection(sectionName) {
    // Hide all sections
    const sections = document.querySelectorAll('.content-section');
    sections.forEach(section => section.style.display = 'none');
    
    // Remove active class from all nav links
    const navLinks = document.querySelectorAll('.sidebar a');
    navLinks.forEach(link => link.classList.remove('active'));
    
    // Show selected section
    const targetSection = document.getElementById(sectionName + 'Section');
    if (targetSection) {
        targetSection.style.display = 'block';
    }
    
    // Add active class to clicked nav link
    event.target.classList.add('active');
    
    // Load section-specific data
    switch(sectionName) {
        case 'reviews':
            loadUserReviews();
            break;
        case 'complaints':
            loadUserComplaints();
            break;
        case 'addReview':
            loadFacilities();
            break;
        case 'addComplaint':
            loadFacilities();
            break;
    }
}

// Load user's reviews - DEMO VERSION WITH MOCK DATA
async function loadUserReviews() {
    // Mock user reviews data
    const reviews = [
        {
            facilityName: "Main Library",
            title: "Great Study Environment",
            rating: 5,
            status: "APPROVED",
            createdAt: "2024-09-25T10:30:00"
        },
        {
            facilityName: "Sports Turf", 
            title: "Good for Football",
            rating: 4,
            status: "PENDING",
            createdAt: "2024-09-24T15:20:00"
        },
        {
            facilityName: "Canteen",
            title: "Decent Food Quality", 
            rating: 3,
            status: "APPROVED",
            createdAt: "2024-09-23T12:15:00"
        }
    ];
    
    const tableBody = document.getElementById('userReviewsTable');
    tableBody.innerHTML = '';
    
    if (reviews.length === 0) {
        tableBody.innerHTML = '<tr><td colspan="5">No reviews submitted yet</td></tr>';
        return;
    }
    
    reviews.forEach(review => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${review.facilityName}</td>
            <td>${review.title}</td>
            <td>${'★'.repeat(review.rating)}${'☆'.repeat(5 - review.rating)}</td>
            <td><span class="status ${review.status.toLowerCase()}">${review.status}</span></td>
            <td>${formatDate(review.createdAt)}</td>
        `;
        tableBody.appendChild(row);
    });
}

// Load user's complaints - DEMO VERSION WITH MOCK DATA
async function loadUserComplaints() {
    // Mock user complaints data
    const complaints = [
        {
            facilityName: "Boys Washroom Block A",
            title: "Broken Tap",
            status: "IN_PROGRESS",
            createdAt: "2024-09-24T09:15:00",
            adminResponse: "We are working on fixing this issue. Expected completion by end of week."
        },
        {
            facilityName: "Parking Area",
            title: "Insufficient Lighting", 
            status: "PENDING",
            createdAt: "2024-09-23T16:45:00",
            adminResponse: null
        },
        {
            facilityName: "Computer Lab",
            title: "Slow Internet Connection",
            status: "RESOLVED", 
            createdAt: "2024-09-20T11:30:00",
            adminResponse: "Internet speed has been upgraded. Please test and confirm."
        }
    ];
    
    const tableBody = document.getElementById('userComplaintsTable');
    tableBody.innerHTML = '';
    
    if (complaints.length === 0) {
        tableBody.innerHTML = '<tr><td colspan="5">No complaints submitted yet</td></tr>';
        return;
    }
    
    complaints.forEach(complaint => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${complaint.facilityName}</td>
            <td>${complaint.title}</td>
            <td><span class="status ${complaint.status.toLowerCase()}">${complaint.status}</span></td>
            <td>${formatDate(complaint.createdAt)}</td>
            <td>${complaint.adminResponse || 'No response yet'}</td>
        `;
        tableBody.appendChild(row);
    });
}

// Load facilities for dropdowns - DEMO VERSION WITH MOCK DATA
async function loadFacilities() {
    // Mock facilities data
    const facilities = [
        { id: 1, name: "Main Library" },
        { id: 2, name: "Sports Turf" },
        { id: 3, name: "Boys Washroom Block A" },
        { id: 4, name: "Girls Washroom Block A" },
        { id: 5, name: "Canteen" },
        { id: 6, name: "Computer Lab" },
        { id: 7, name: "Auditorium" },
        { id: 8, name: "Parking Area" },
        { id: 9, name: "Gym" }
    ];
    
    // Populate review facility dropdown
    const reviewSelect = document.getElementById('reviewFacility');
    const complaintSelect = document.getElementById('complaintFacility');
    
    if (reviewSelect) {
        reviewSelect.innerHTML = '<option value="">Select a facility</option>';
        facilities.forEach(facility => {
            const option = new Option(facility.name, facility.id);
            reviewSelect.add(option);
        });
    }
    
    if (complaintSelect) {
        complaintSelect.innerHTML = '<option value="">Select a facility</option>';
        facilities.forEach(facility => {
            const option = new Option(facility.name, facility.id);
            complaintSelect.add(option);
        });
    }
}

// Set up form event handlers
function setupFormHandlers() {
    // Review form handler
    const reviewForm = document.getElementById('reviewForm');
    if (reviewForm) {
        reviewForm.addEventListener('submit', handleReviewSubmit);
    }
    
    // Complaint form handler
    const complaintForm = document.getElementById('complaintForm');
    if (complaintForm) {
        complaintForm.addEventListener('submit', handleComplaintSubmit);
    }
}

// Handle review form submission - DEMO VERSION WITH MOCK RESPONSE
async function handleReviewSubmit(event) {
    event.preventDefault();
    
    const formData = new FormData(event.target);
    const reviewData = {
        facilityId: formData.get('facility'),
        title: formData.get('title'),
        content: formData.get('content'),
        rating: formData.get('rating')
    };
    
    // Validate data
    if (!reviewData.facilityId || !reviewData.title || !reviewData.content || !reviewData.rating) {
        showMessage('reviewMessage', 'Please fill in all fields', 'error');
        return;
    }
    
    // Mock successful submission (no API call)
    const result = { success: true };
    
    showMessage('reviewMessage', 'Review submitted successfully!', 'success');
    event.target.reset();
    
    // Refresh reviews if on that section
    if (document.getElementById('reviewsSection').style.display !== 'none') {
        loadUserReviews();
    }
}

// Handle complaint form submission - DEMO VERSION WITH MOCK RESPONSE
async function handleComplaintSubmit(event) {
    event.preventDefault();
    
    const formData = new FormData(event.target);
    const complaintData = {
        facilityId: formData.get('facility'),
        title: formData.get('title'),
        description: formData.get('description')
    };
    
    // Validate data
    if (!complaintData.facilityId || !complaintData.title || !complaintData.description) {
        showMessage('complaintMessage', 'Please fill in all fields', 'error');
        return;
    }
    
    // Mock successful submission (no API call)
    const result = { success: true };
    
    showMessage('complaintMessage', 'Complaint submitted successfully!', 'success');
    event.target.reset();
    
    // Refresh complaints if on that section
    if (document.getElementById('complaintsSection').style.display !== 'none') {
        loadUserComplaints();
    }
}

// Show message to user
function showMessage(elementId, message, type) {
    const messageElement = document.getElementById(elementId);
    messageElement.textContent = message;
    messageElement.style.color = type === 'success' ? '#065f46' : '#dc2626';
    
    // Clear message after 5 seconds
    setTimeout(() => {
        messageElement.textContent = '';
    }, 5000);
}

// Format date for display
function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString() + ' ' + date.toLocaleTimeString();
}

// Add demo mode banner
function addDemoBanner() {
    const banner = document.createElement('div');
    banner.style.cssText = `
        background: #fef3c7;
        border: 2px solid #f59e0b;
        color: #92400e;
        padding: 10px;
        text-align: center;
        font-weight: bold;
        position: fixed;
        top: 0;
        left: 0;
        right: 0;
        z-index: 1000;
        box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    };
    
    // Adjust body padding to account for banner
    document.body.style.paddingTop = '50px';
}
