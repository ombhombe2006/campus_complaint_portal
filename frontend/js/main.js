// Campus Review Portal - Main JavaScript

let currentLoginType = '';

// Load reviews on page load
document.addEventListener('DOMContentLoaded', function() {
    loadRecentReviews();
});

// Open login modal
function openLogin(type) {
    currentLoginType = type;
    document.getElementById('loginTitle').textContent = type === 'admin' ? 'Admin Login' : 'User Login';
    document.getElementById('loginModal').style.display = 'block';
    document.getElementById('loginMessage').textContent = '';
    
    // Clear form
    document.getElementById('loginForm').reset();
}

// Close login modal
function closeLogin() {
    document.getElementById('loginModal').style.display = 'none';
    currentLoginType = '';
}

// Handle login form submission
document.getElementById('loginForm').addEventListener('submit', async function(e) {
    e.preventDefault();
    
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    
    try {
        const response = await fetch('http://localhost:8080/api/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                username: username,
                password: password,
                loginType: currentLoginType
            })
        });
        
        const result = await response.json();
        
        if (result.success) {
            // Store user data
            sessionStorage.setItem('userType', currentLoginType);
            sessionStorage.setItem('username', username);
            sessionStorage.setItem('userId', result.userId);
            sessionStorage.setItem('fullName', result.fullName);
            
            // Redirect to appropriate dashboard
            if (currentLoginType === 'admin') {
                window.location.href = 'admin-dashboard.html';
            } else {
                window.location.href = 'user-dashboard.html';
            }
        } else {
            document.getElementById('loginMessage').textContent = result.message;
        }
    } catch (error) {
        document.getElementById('loginMessage').textContent = 'Connection error. Please try again.';
        console.error('Login error:', error);
    }
});

// Load recent reviews
async function loadRecentReviews() {
    try {
        const response = await fetch('http://localhost:8080/api/reviews/recent');
        const reviews = await response.json();
        
        const reviewsGrid = document.getElementById('reviewsGrid');
        reviewsGrid.innerHTML = '';
        
        if (reviews.length === 0) {
            reviewsGrid.innerHTML = '<p>No reviews available yet.</p>';
            return;
        }
        
        reviews.forEach(review => {
            const reviewCard = document.createElement('div');
            reviewCard.className = 'review-card';
            reviewCard.innerHTML = `
                <div class="review-title">${review.title}</div>
                <div class="review-facility">Facility: ${review.facilityName}</div>
                <div class="rating">Rating: ${'★'.repeat(review.rating)}${'☆'.repeat(5 - review.rating)}</div>
                <div class="review-content">${review.content}</div>
            `;
            reviewsGrid.appendChild(reviewCard);
        });
    } catch (error) {
        console.error('Error loading reviews:', error);
        document.getElementById('reviewsGrid').innerHTML = '<p>Error loading reviews.</p>';
    }
}

// Utility functions for other pages
function logout() {
    sessionStorage.clear();
    window.location.href = 'index.html';
}

function checkAuth() {
    const userType = sessionStorage.getItem('userType');
    if (!userType) {
        window.location.href = 'index.html';
        return false;
    }
    return true;
}

function getCurrentUser() {
    return {
        userType: sessionStorage.getItem('userType'),
        username: sessionStorage.getItem('username'),
        userId: sessionStorage.getItem('userId'),
        fullName: sessionStorage.getItem('fullName')
    };
}