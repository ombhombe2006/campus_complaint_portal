document.addEventListener('DOMContentLoaded', function() {
    const complaintForm = document.getElementById('complaintForm');
    const successModal = document.getElementById('successModal');
    const closeSuccessModalBtn = document.getElementById('closeSuccessModal');

    complaintForm.addEventListener('submit', function(e) {
        e.preventDefault();
        successModal.classList.remove('hidden');
    });

    closeSuccessModalBtn.addEventListener('click', function() {
        successModal.classList.add('hidden');
        complaintForm.reset();
    });
});