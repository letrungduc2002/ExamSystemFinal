function initHeader() {
    const profileBtn = document.getElementById('profileBtn');
    const dropdown = document.getElementById('dropdownMenu');
    const profile = document.getElementById('profile');
    if (profileBtn) {
        profileBtn.addEventListener('click', function (e) {
            e.stopPropagation();
            dropdown.classList.toggle('show');
        });
    }

    document.addEventListener('click', function (e) {
        if (profile && !profile.contains(e.target)) {
            dropdown.classList.remove('show');
        }
    });

    if (dropdown) {
        dropdown.addEventListener('click', function (e) {
            e.stopPropagation();
        });
    }
}