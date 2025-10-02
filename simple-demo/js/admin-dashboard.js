// Admin Dashboard JavaScript

document.addEventListener('DOMContentLoaded', function() {
    // Add demo banner
    addDemoBanner();
    
    // Check authentication
    if (!checkAuth()) {
        return;
    }
    
    const user = getCurrentUser();
    if (user.userType !== 'admin') {
        alert('Access denied. Admin login required.');
        window.location.href = 'index.html';
        return;
    }
    
    // Set welcome message
    document.getElementById('welcomeAdmin').textContent = `Welcome, ${user.fullName}!`;
    
    // Load initial data
    loadAllReviews();
    
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
            loadAllReviews();
            break;
        case 'complaints':
            loadAllComplaints();
            break;
        case 'facilities':
            loadFacilitiesWithStats();
            break;
    }
}

// Load all reviews for admin management - DEMO VERSION WITH MOCK DATA
async function loadAllReviews() {
    // Mock admin reviews data
    const reviews = [
        {
            id: 1,
            userName: "John Doe",
            facilityName: "Main Library",
            title: "Great Study Environment",
            rating: 5,
            status: "PENDING",
            createdAt: "2024-09-25T10:30:00"
        },
        {
            id: 2,
            userName: "Jane Smith",
            facilityName: "Sports Turf", 
            title: "Good for Football",
            rating: 4,
            status: "APPROVED",
            createdAt: "2024-09-24T15:20:00"
        },
        {
            id: 3,
            userName: "Mike Wilson",
            facilityName: "Canteen",
            title: "Decent Food Quality", 
            rating: 3,
            status: "PENDING",
            createdAt: "2024-09-23T12:15:00"
        },
        {
            id: 4,
            userName: "Sarah Johnson",
            facilityName: "Computer Lab",
            title: "Modern Equipment",
            rating: 5,
            status: "REJECTED",
            createdAt: "2024-09-22T09:45:00"
        }
    ];
    
    const tableBody = document.getElementById('adminReviewsTable');
    tableBody.innerHTML = '';
    
    if (reviews.length === 0) {
        tableBody.innerHTML = '<tr><td colspan="7">No reviews found</td></tr>';
        return;
    }
    
    reviews.forEach(review => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${review.userName}</td>
            <td>${review.facilityName}</td>
            <td>${review.title}</td>
            <td>${'★'.repeat(review.rating)}${'☆'.repeat(5 - review.rating)}</td>
            <td><span class="status ${review.status.toLowerCase()}">${review.status}</span></td>
            <td>${formatDate(review.createdAt)}</td>
            <td>
                <button class="btn" onclick="updateReviewStatus(${review.id}, 'APPROVED')" 
                        ${review.status === 'APPROVED' ? 'disabled' : ''}>Approve</button>
                <button class="btn" onclick="updateReviewStatus(${review.id}, 'REJECTED')" 
                        ${review.status === 'REJECTED' ? 'disabled' : ''}
                        style="background: #dc2626; margin-left: 5px;">Reject</button>
            </td>
        `;
        tableBody.appendChild(row);
    });
}

// Load all complaints for admin management - DEMO VERSION WITH MOCK DATA
async function loadAllComplaints() {
    // Mock admin complaints data
    const complaints = [
        {
            id: 1,
            userName: "John Doe",
            facilityName: "Boys Washroom Block A",
            title: "Broken Tap",
            status: "IN_PROGRESS",
            createdAt: "2024-09-24T09:15:00",
            adminResponse: "We are working on fixing this issue."
        },
        {
            id: 2,
            userName: "Sarah Wilson",
            facilityName: "Parking Area",
            title: "Insufficient Lighting", 
            status: "PENDING",
            createdAt: "2024-09-23T16:45:00",
            adminResponse: ""
        },
        {
            id: 3,
            userName: "Mike Johnson",
            facilityName: "Computer Lab",
            title: "Slow Internet Connection",
            status: "RESOLVED", 
            createdAt: "2024-09-20T11:30:00",
            adminResponse: "Internet speed has been upgraded. Please test and confirm."
        },
        {
            id: 4,
            userName: "Jane Smith",
            facilityName: "Canteen",
            title: "Food Quality Issues",
            status: "PENDING",
            createdAt: "2024-09-19T13:20:00",
            adminResponse: ""
        }
    ];
    
    const tableBody = document.getElementById('adminComplaintsTable');
    tableBody.innerHTML = '';
    
    if (complaints.length === 0) {
        tableBody.innerHTML = '<tr><td colspan="6">No complaints found</td></tr>';
        return;
    }
    
    complaints.forEach(complaint => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${complaint.userName}</td>
            <td>${complaint.facilityName}</td>
            <td>${complaint.title}</td>
            <td><span class="status ${complaint.status.toLowerCase()}">${complaint.status}</span></td>
            <td>${formatDate(complaint.createdAt)}</td>
            <td>
                <button class="btn" onclick="openComplaintModal(${complaint.id}, '${complaint.status}', '${complaint.adminResponse || ''}')">
                    Update
                </button>
            </td>
        `;
        tableBody.appendChild(row);
    });
}

// Load facilities with statistics - DEMO VERSION WITH MOCK DATA
async function loadFacilitiesWithStats() {
    // Mock facilities data with stats
    const facilities = [
        { name: "Main Library", category: "LIBRARY", reviewCount: 15, avgRating: 4.2 },
        { name: "Sports Turf", category: "TURF", reviewCount: 8, avgRating: 3.8 },
        { name: "Boys Washroom Block A", category: "WASHROOM", reviewCount: 3, avgRating: 2.7 },
        { name: "Girls Washroom Block A", category: "WASHROOM", reviewCount: 5, avgRating: 3.4 },
        { name: "Canteen", category: "CANTEEN", reviewCount: 12, avgRating: 3.9 },
        { name: "Computer Lab", category: "LAB", reviewCount: 10, avgRating: 4.5 },
        { name: "Auditorium", category: "AUDITORIUM", reviewCount: 6, avgRating: 4.0 },
        { name: "Parking Area", category: "PARKING", reviewCount: 4, avgRating: 2.5 },
        { name: "Gym", category: "GYM", reviewCount: 7, avgRating: 4.1 }
    ];
    
    const tableBody = document.getElementById('facilitiesTable');
    tableBody.innerHTML = '';
    
    facilities.forEach(facility => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${facility.name}</td>
            <td>${facility.category}</td>
            <td>${facility.reviewCount}</td>
            <td>${facility.avgRating.toFixed(1)} ★</td>
        `;
        tableBody.appendChild(row);
    });
}

// Update review status - DEMO VERSION WITH MOCK RESPONSE
async function updateReviewStatus(reviewId, status) {
    // Mock successful update (no API call)
    alert(`Review ${status.toLowerCase()} successfully! (Demo Mode)`);
    loadAllReviews(); // Refresh the list with mock data
}

// Open complaint modal for updating
function openComplaintModal(complaintId, currentStatus, currentResponse) {
    document.getElementById('complaintId').value = complaintId;
    document.getElementById('complaintStatus').value = currentStatus;
    document.getElementById('adminResponse').value = currentResponse;
    document.getElementById('responseModal').style.display = 'block';
}

// Close complaint modal
function closeResponseModal() {
    document.getElementById('responseModal').style.display = 'none';
}

// Set up form event handlers
function setupFormHandlers() {
    // Facility form handler
    const facilityForm = document.getElementById('facilityForm');
    if (facilityForm) {
        facilityForm.addEventListener('submit', handleFacilitySubmit);
    }
    
    // Complaint response form handler
    const responseForm = document.getElementById('responseForm');
    if (responseForm) {
        responseForm.addEventListener('submit', handleComplaintUpdate);
    }
}

// Handle facility form submission - DEMO VERSION WITH MOCK RESPONSE
async function handleFacilitySubmit(event) {
    event.preventDefault();
    
    const formData = new FormData(event.target);
    const facilityData = {
        name: formData.get('name'),
        category: formData.get('category')
    };
    
    // Validate data
    if (!facilityData.name || !facilityData.category) {
        showMessage('facilityMessage', 'Please fill in all fields', 'error');
        return;
    }
    
    // Mock successful submission (no API call)
    showMessage('facilityMessage', 'Facility added successfully! (Demo Mode)', 'success');
    event.target.reset();
    loadFacilitiesWithStats(); // Refresh the list
}

// Handle complaint update - DEMO VERSION WITH MOCK RESPONSE
async function handleComplaintUpdate(event) {
    event.preventDefault();
    
    const complaintId = document.getElementById('complaintId').value;
    const status = document.getElementById('complaintStatus').value;
    const adminResponse = document.getElementById('adminResponse').value;
    
    // Mock successful update (no API call)
    alert('Complaint updated successfully! (Demo Mode)');
    closeResponseModal();
    loadAllComplaints(); // Refresh the list with mock data
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
    `;
    banner.innerHTML = '🛡️ ADMIN DEMO MODE - Using Mock Data (No Backend Required)';
    document.body.insertBefore(banner, document.body.firstChild);
    
    // Adjust body padding to account for banner
    document.body.style.paddingTop = '50px';
}
