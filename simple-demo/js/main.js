// Campus Review Portal - Main JavaScript

let currentLoginType = '';

// Load reviews on page load
document.addEventListener('DOMContentLoaded', function() {
    addDemoBanner();
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

// Handle login form submission - DEMO VERSION WITH MOCK DATA
document.getElementById('loginForm').addEventListener('submit', async function(e) {
    e.preventDefault();
    
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    
    // Mock authentication - no backend required
    let result = { success: false, message: 'Invalid username or password' };
    
    // Check demo credentials
    if (currentLoginType === 'user' && username === 'user1' && password === 'user123') {
        result = {
            success: true,
            userId: 1,
            fullName: 'John Doe',
            userType: 'user'
        };
    } else if (currentLoginType === 'admin' && username === 'admin' && password === 'admin123') {
        result = {
            success: true,
            userId: 1,
            fullName: 'System Admin',
            userType: 'admin'
        };
    }
    
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
});

// Load recent reviews - DEMO VERSION WITH MOCK DATA
async function loadRecentReviews() {
    // Mock reviews data - no backend required
    const reviews = [
        {
            title: "Excellent Library Facilities",
            facilityName: "Main Library",
            rating: 5,
            content: "Very quiet study environment with great book collection. Highly recommended!"
        },
        {
            title: "Good Sports Turf", 
            facilityName: "Sports Turf",
            rating: 4,
            content: "Well maintained ground. Perfect for football and cricket matches."
        },
        {
            title: "Clean Cafeteria",
            facilityName: "Canteen", 
            rating: 4,
            content: "Good food quality and reasonable prices. Could use more seating."
        },
        {
            title: "Modern Computer Lab",
            facilityName: "Computer Lab",
            rating: 5, 
            content: "Latest computers with high speed internet. Great for programming classes."
        }
    ];
    
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
    banner.innerHTML = '🎯 DEMO MODE - Using Mock Data (No Backend Required)';
    document.body.insertBefore(banner, document.body.firstChild);
    
    // Adjust body padding to account for banner
    document.body.style.paddingTop = '50px';
}
