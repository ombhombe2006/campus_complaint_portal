// Admin Dashboard JavaScript

document.addEventListener('DOMContentLoaded', function() {
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

// Load all reviews for admin management
async function loadAllReviews() {
    try {
        const response = await fetch('http://localhost:8080/campus-review-portal/api/reviews', {
            method: 'GET',
            credentials: 'include'
        });
        
        const reviews = await response.json();
        const tableBody = document.getElementById('adminReviewsTable');
        
        if (!Array.isArray(reviews)) {
            tableBody.innerHTML = '<tr><td colspan="7">Failed to load reviews</td></tr>';
            return;
        }
        
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
        
    } catch (error) {
        console.error('Error loading reviews:', error);
        document.getElementById('adminReviewsTable').innerHTML = 
            '<tr><td colspan="7">Error loading reviews</td></tr>';
    }
}

// Load all complaints for admin management
async function loadAllComplaints() {
    try {
        const response = await fetch('http://localhost:8080/campus-review-portal/api/complaints', {
            method: 'GET',
            credentials: 'include'
        });
        
        const complaints = await response.json();
        const tableBody = document.getElementById('adminComplaintsTable');
        
        if (!Array.isArray(complaints)) {
            tableBody.innerHTML = '<tr><td colspan="6">Failed to load complaints</td></tr>';
            return;
        }
        
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
        
    } catch (error) {
        console.error('Error loading complaints:', error);
        document.getElementById('adminComplaintsTable').innerHTML = 
            '<tr><td colspan="6">Error loading complaints</td></tr>';
    }
}

// Load facilities with statistics
async function loadFacilitiesWithStats() {
    try {
        // This is a simplified version - in a full implementation, 
        // you'd create an endpoint that returns facility stats
        const response = await fetch('http://localhost:8080/campus-review-portal/api/reviews/facilities');
        const facilities = await response.json();
        
        if (!Array.isArray(facilities)) {
            document.getElementById('facilitiesTable').innerHTML = 
                '<tr><td colspan="4">Failed to load facilities</td></tr>';
            return;
        }
        
        const tableBody = document.getElementById('facilitiesTable');
        tableBody.innerHTML = '';
        
        facilities.forEach(facility => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${facility.name}</td>
                <td>${facility.category}</td>
                <td>-</td>
                <td>-</td>
            `;
            tableBody.appendChild(row);
        });
        
    } catch (error) {
        console.error('Error loading facilities:', error);
        document.getElementById('facilitiesTable').innerHTML = 
            '<tr><td colspan="4">Error loading facilities</td></tr>';
    }
}

// Update review status
async function updateReviewStatus(reviewId, status) {
    try {
        const response = await fetch('http://localhost:8080/campus-review-portal/api/reviews', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            credentials: 'include',
            body: JSON.stringify({
                reviewId: reviewId,
                status: status
            })
        });
        
        const result = await response.json();
        
        if (result.success) {
            alert(`Review ${status.toLowerCase()} successfully!`);
            loadAllReviews(); // Refresh the list
        } else {
            alert('Failed to update review: ' + result.message);
        }
        
    } catch (error) {
        console.error('Error updating review:', error);
        alert('Connection error. Please try again.');
    }
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

// Handle facility form submission
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
    
    try {
        // Note: You'd need to implement this endpoint
        const response = await fetch('http://localhost:8080/campus-review-portal/api/facilities', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            credentials: 'include',
            body: JSON.stringify(facilityData)
        });
        
        const result = await response.json();
        
        if (result.success) {
            showMessage('facilityMessage', 'Facility added successfully!', 'success');
            event.target.reset();
            loadFacilitiesWithStats(); // Refresh the list
        } else {
            showMessage('facilityMessage', result.message || 'Failed to add facility', 'error');
        }
        
    } catch (error) {
        console.error('Error adding facility:', error);
        showMessage('facilityMessage', 'Connection error. Please try again.', 'error');
    }
}

// Handle complaint update
async function handleComplaintUpdate(event) {
    event.preventDefault();
    
    const complaintId = document.getElementById('complaintId').value;
    const status = document.getElementById('complaintStatus').value;
    const adminResponse = document.getElementById('adminResponse').value;
    
    try {
        const response = await fetch('http://localhost:8080/campus-review-portal/api/complaints', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            credentials: 'include',
            body: JSON.stringify({
                complaintId: complaintId,
                status: status,
                adminResponse: adminResponse
            })
        });
        
        const result = await response.json();
        
        if (result.success) {
            alert('Complaint updated successfully!');
            closeResponseModal();
            loadAllComplaints(); // Refresh the list
        } else {
            alert('Failed to update complaint: ' + result.message);
        }
        
    } catch (error) {
        console.error('Error updating complaint:', error);
        alert('Connection error. Please try again.');
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