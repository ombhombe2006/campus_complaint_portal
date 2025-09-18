document.addEventListener('DOMContentLoaded', function () {
    const loginPage = document.getElementById('loginPage');
    const signupPage = document.getElementById('signupPage');
    const successMessage = document.getElementById('successMessage');

    const loginBtn = document.getElementById('loginBtn');
    const signupBtn = document.getElementById('signupBtn');

    const switchToSignup = document.getElementById('switchToSignup');
    const switchToLogin = document.getElementById('switchToLogin');
    
    const signupForm = document.getElementById('signupForm');
    const closeSuccessBtn = document.getElementById('closeSuccess');

    function showLoginPage() {
        loginPage.classList.remove('hidden');
        signupPage.classList.add('hidden');
        loginBtn.classList.add('active');
        signupBtn.classList.remove('active');
    }

    function showSignupPage() {
        signupPage.classList.remove('hidden');
        loginPage.classList.add('hidden');
        signupBtn.classList.add('active');
        loginBtn.classList.remove('active');
    }

    loginBtn.addEventListener('click', showLoginPage);
    signupBtn.addEventListener('click', showSignupPage);
    switchToSignup.addEventListener('click', showSignupPage);
    switchToLogin.addEventListener('click', showLoginPage);

    signupForm.addEventListener('submit', function(event) {
        event.preventDefault(); 
        successMessage.classList.remove('hidden');
    });

    closeSuccessBtn.addEventListener('click', function() {
        successMessage.classList.add('hidden');
        showLoginPage(); 
    });

    showLoginPage();
});