function loadComponent(id, file, callback) {
    fetch(file)
        .then(res => res.text())
        .then(data => {
            document.getElementById(id).innerHTML = data;
            if (callback) callback();
        });
}

document.addEventListener("DOMContentLoaded", () => {
    loadComponent("header", "/components/header.html", initHeader);
    loadComponent("sidebar", "/components/sidebar.html", () => {
        initSidebarRouter();
        setActiveSidebar();
    });
    loadComponent("footer", "/components/footer.html");
});