let currentModule = null;
function getPageName() {
    const params = new URLSearchParams(window.location.search);
    return params.get("page") || "news";
}


async function loadHtml(page) {
    const container = document.getElementById("page-content");
    try {
        const res = await fetch(`/pages/${page}.html`);
        if (!res.ok) throw new Error("Page not found");
        const html = await res.text();
        container.innerHTML = html;

    } catch (err) {
        console.error(err);
        container.innerHTML = `
            <div style="padding:40px;text-align:center;">
                <h2>Page not found</h2>
            </div>
        `;
    }
}


function loadCss(page) {
    const oldCss = document.getElementById("page-css");
    if (oldCss) oldCss.remove();
    const css = document.createElement("link");
    css.rel = "stylesheet";
    css.href = `/assets/css/pages/${page}.css`;
    css.id = "page-css";
    document.head.appendChild(css);
}

async function loadModule(page) {
    try {
        if (currentModule && currentModule.destroy) {
            currentModule.destroy();
        }
        const module = await import(`/assets/js/modules/${page}.js?t=${Date.now()}`);
        currentModule = module;
        if (module.init) {
            module.init();
        }
    } catch (error) {
        console.error("Module load error:", error);
    }
}

async function loadPage() {
    const page = getPageName();
    await loadHtml(page);
    loadCss(page);
    await loadModule(page);
    if (typeof setActiveSidebar === "function") {
        setActiveSidebar();
    }
}

window.addEventListener("popstate", loadPage);
document.addEventListener("DOMContentLoaded", loadPage);