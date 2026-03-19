function initSidebarRouter() {
    const menuItems = document.querySelectorAll(".menu-item");
    menuItems.forEach(item => {
        item.addEventListener("click", function(e) {
            // chặn hành vi reload của thẻ <a>
            e.preventDefault();
            const url = new URL(this.href);
            const page = url.searchParams.get("page");
            // Thay đổi URL nhưng không reload trang
            history.pushState({}, "", "?page=" + page);
            loadPage();
        });
    });
}



function setActiveSidebar() {
    const params = new URLSearchParams(window.location.search);
    const currentPage = params.get("page") || "news";
    const menuItems = document.querySelectorAll(".menu-item");
    menuItems.forEach(item => {
        const url = new URL(item.href);
        const page = url.searchParams.get("page");
        if (page === currentPage) {
            item.classList.add("active");
        } else {
            item.classList.remove("active");
        }
    });
}