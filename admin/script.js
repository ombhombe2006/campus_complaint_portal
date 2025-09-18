document.addEventListener('DOMContentLoaded', function() {
    const modal = document.getElementById('complaintModal');
    const closeModal = document.getElementById('closeModal');
    const closeModalBtn = document.getElementById('closeModalBtn');

    function openModal() {
        if (modal) {
            modal.classList.remove('hidden');
        }
    }

    function hideModal() {
        if (modal) {
            modal.classList.add('hidden');
        }
    }

    document.addEventListener('click', function(e) {

        if (e.target.classList.contains('js-view-btn')) {
            openModal();

        }
    });
    

    if (closeModal) {
        closeModal.addEventListener('click', hideModal);
    }
    if (closeModalBtn) {
        closeModalBtn.addEventListener('click', hideModal);
    }
    

    if (modal) {
        modal.addEventListener('click', function(e) {

            if (e.target === modal) {
                hideModal();
            }
        });
    }


    document.addEventListener('change', function(e) {

        if (e.target.tagName === 'SELECT' && e.target.closest('.complaint-item')) {
            console.log('Status updated for a complaint in the list.');
        }
    });
});