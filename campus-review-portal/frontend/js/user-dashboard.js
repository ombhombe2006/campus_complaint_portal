// User Dashboard JavaScript

document.addEventListener('DOMContentLoaded', function() {
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

// Load user's reviews
async function loadUserReviews() {
    try {
        const response = await fetch('http://localhost:8080/campus-review-portal/api/reviews/user', {
            method: 'GET',
            credentials: 'include'
        });
        
        const reviews = await response.json();
        const tableBody = document.getElementById('userReviewsTable');
        
        if (!Array.isArray(reviews)) {
            tableBody.innerHTML = '<tr><td colspan="5">No reviews found</td></tr>';
            return;
        }
        
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
        
    } catch (error) {
        console.error('Error loading reviews:', error);
        document.getElementById('userReviewsTable').innerHTML = 
            '<tr><td colspan="5">Error loading reviews</td></tr>';
    }
}

// Load user's complaints
async function loadUserComplaints() {
    try {
        const response = await fetch('http://localhost:8080/campus-review-portal/api/complaints/user', {
            method: 'GET',
            credentials: 'include'
        });
        
        const complaints = await response.json();
        const tableBody = document.getElementById('userComplaintsTable');
        
        if (!Array.isArray(complaints)) {
            tableBody.innerHTML = '<tr><td colspan="5">No complaints found</td></tr>';
            return;
        }
        
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
        
    } catch (error) {
        console.error('Error loading complaints:', error);
        document.getElementById('userComplaintsTable').innerHTML = 
            '<tr><td colspan="5">Error loading complaints</td></tr>';
    }
}

// Load facilities for dropdowns
async function loadFacilities() {
    try {
        const response = await fetch('http://localhost:8080/campus-review-portal/api/reviews/facilities');
        const facilities = await response.json();
        
        if (!Array.isArray(facilities)) {
            console.error('Failed to load facilities');
            return;
        }
        
        // Populate review facility dropdown
        const reviewSelect = document.getElementById('reviewFacility');
        const complaintSelect = document.getElementById('complaintFacility');
        
        // Clear existing options (except first one)
        reviewSelect.innerHTML = '<option value="">Select a facility</option>';
        complaintSelect.innerHTML = '<option value="">Select a facility</option>';
        
        facilities.forEach(facility => {
            const option1 = new Option(facility.name, facility.id);
            const option2 = new Option(facility.name, facility.id);
            reviewSelect.add(option1);
            complaintSelect.add(option2);
        });
        
    } catch (error) {
        console.error('Error loading facilities:', error);
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

// Handle review form submission
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
    
    try {
        const response = await fetch('http://localhost:8080/campus-review-portal/api/reviews', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            credentials: 'include',
            body: JSON.stringify(reviewData)
        });
        
        const result = await response.json();
        
        if (result.success) {
            showMessage('reviewMessage', 'Review submitted successfully!', 'success');
            event.target.reset();
            // Refresh reviews if on that section
            if (document.getElementById('reviewsSection').style.display !== 'none') {
                loadUserReviews();
            }
        } else {
            showMessage('reviewMessage', result.message || 'Failed to submit review', 'error');
        }
        
    } catch (error) {
        console.error('Error submitting review:', error);
        showMessage('reviewMessage', 'Connection error. Please try again.', 'error');
    }
}

// Handle complaint form submission
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
    
    try {
        const response = await fetch('http://localhost:8080/campus-review-portal/api/complaints', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            credentials: 'include',
            body: JSON.stringify(complaintData)
        });
        
        const result = await response.json();
        
        if (result.success) {
            showMessage('complaintMessage', 'Complaint submitted successfully!', 'success');
            event.target.reset();
            // Refresh complaints if on that section
            if (document.getElementById('complaintsSection').style.display !== 'none') {
                loadUserComplaints();
            }
        } else {
            showMessage('complaintMessage', result.message || 'Failed to submit complaint', 'error');
        }
        
    } catch (error) {
        console.error('Error submitting complaint:', error);
        showMessage('complaintMessage', 'Connection error. Please try again.', 'error');
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