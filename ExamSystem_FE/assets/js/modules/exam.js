// Chuyển đổi Grid/List
const gridBtn = document.getElementById('gridView');
const listBtn = document.getElementById('listView');
const examContainer = document.getElementById('examContainer');
if (gridBtn) {
    gridBtn.addEventListener('click', function () {
        gridBtn.classList.add('active');
        listBtn.classList.remove('active');
        examContainer.classList.remove('list-view');
    });
}
if (listBtn) {
    listBtn.addEventListener('click', function () {
        listBtn.classList.add('active');
        gridBtn.classList.remove('active');
        examContainer.classList.add('list-view');
    });
}
window.addEventListener('resize', function () {
    if (window.innerWidth > 768) {
        sidebar.classList.remove('open');
    }
});




const API_URL = "http://localhost:8080/api/exams";
let currentPage = 0;
const pageSize = 5;
let filters = {
    title: "",
    type: "",
    difficult: ""
};

// ============================
// INIT
// ============================

export function init() {
    setupSearch();
    setupFilterUI();
    loadExams();
}

// ============================
// LOAD DATA
// ============================

async function loadExams() {
    const params = new URLSearchParams({
        page: currentPage,
        size: pageSize
    });
    Object.entries(filters).forEach(([key, value]) => {
        if (value) params.append(key, value);
    });
    const res = await fetch(`${API_URL}?${params}`);
    const result = await res.json();
    const pageData = result.data;
    renderExamCards(
        pageData.content,
        document.getElementById("examContainer"),
        document.getElementById("emptyState")
    );
    renderPagination(pageData);
}

// ============================
// PAGINATION
// ============================

function renderPagination(pageData) {
    const pagination = document.getElementById("pagination");
    if (!pagination) return;
    const { number: page, totalPages } = pageData;

    let html = `
        <div class="page-item ${page === 0 ? 'disabled' : ''}" data-page="${page - 1}">
            <i class="fas fa-chevron-left"></i>
        </div>
    `;
    for (let i = 0; i < totalPages; i++) {
        html += `
            <div class="page-item ${i === page ? 'active' : ''}" data-page="${i}">
                ${i + 1}
            </div>
        `;
    }
    html += `
        <div class="page-item ${page === totalPages - 1 ? 'disabled' : ''}" data-page="${page + 1}">
            <i class="fas fa-chevron-right"></i>
        </div>
    `;
    pagination.innerHTML = html;

    pagination.querySelectorAll(".page-item").forEach(item => {
        item.onclick = () => {
            if (item.classList.contains("disabled")) return;
            const page = item.dataset.page;
            if (page === undefined) return;
            currentPage = Number(page);
            loadExams();
        };
    });
}

// ============================
// RENDER EXAM
// ============================

function renderExamCards(exams, container, emptyState) {
    if (!exams?.length) {
        container.innerHTML = "";
        emptyState.style.display = "block";
        return;
    }

    emptyState.style.display = "none";

    container.innerHTML = exams.map(exam => {
        const difficulty = getDifficulty(exam.difficulty);
        return `
        <div class="exam-card">
            <div class="card-header">
                <div class="org-icon">
                    <i class="fas fa-university"></i>
                </div>
                <span class="badge hot">Hot</span>
            </div>
            <div class="card-body">
                <h3>${exam.title}</h3>
                <div class="tags">
                    <span class="tag exam-type">${exam.examType}</span>
                    <span class="tag difficulty-${difficulty.class}">
                        ${difficulty.text}
                    </span>
                </div>
                <div class="meta-info">
                    <span><i class="far fa-clock"></i> ${exam.totalDuration} phút</span>
                    <span><i class="fas fa-question-circle"></i> ${exam.totalQuestion} câu</span>
                    <span><i class="fas fa-star"></i> ${exam.totalMaxScore}</span>
                </div>

            </div>
            <div class="card-footer">
               <button class="btn-start" data-id="${exam.id}">
                   Bắt đầu thi <i class="fas fa-arrow-right"></i>
               </button>
            </div>
            

        </div>
        `;
    }).join("");

    container.querySelectorAll(".btn-start").forEach(btn => {

        btn.onclick = () => {

            const examId = btn.dataset.id;

            openSummaryPopup(examId);

        };

    });
}

// ============================
// SEARCH
// ============================

function setupSearch() {
    const searchInput = document.getElementById("searchInput");
    searchInput?.addEventListener("keypress", e => {
        if (e.key !== "Enter") return;
        filters.title = searchInput.value.trim();
        currentPage = 0;
        loadExams();
    });
}

// ============================
// FILTER
// ============================

function setupFilterUI() {
    const filterBtn = document.getElementById("filterBtn");
    const filterMenu = document.getElementById("filterMenu");
    filterBtn?.addEventListener("click", e => {
        e.stopPropagation();
        filterMenu.classList.toggle("show");
    });

    document.addEventListener("click", e => {
        if (!filterMenu.contains(e.target) && !filterBtn.contains(e.target)) {
            filterMenu.classList.remove("show");
        }
    });

    document.getElementById("applyFilter")?.addEventListener("click", () => {
        const typeChecked = document.querySelector(
            '#filterMenu input[id="v_act"]:checked, #filterMenu input[id="hsa"]:checked, #filterMenu input[id="tsa"]:checked'
        );
        filters.type = typeChecked ? typeChecked.id.toUpperCase() : "";
        const difficultChecked = document.querySelector(
            '#filterMenu input[id="easy"]:checked, #filterMenu input[id="medium"]:checked, #filterMenu input[id="hard"]:checked'
        );
        const difficultMap = {
            easy: 1,
            medium: 2,
            hard: 3
        };
        filters.difficult = difficultChecked ? difficultMap[difficultChecked.id] : "";
        currentPage = 0;
        filterMenu.classList.remove("show");
        loadExams();
    });

    document.getElementById("resetFilter")?.addEventListener("click", () => {
        document
            .querySelectorAll('#filterMenu input[type="checkbox"]')
            .forEach(cb => cb.checked = false);
        filters = {
            title: "",
            type: "",
            difficult: ""
        };
        currentPage = 0;
        loadExams();
    });
}

// ============================
// DIFFICULTY MAP
// ============================

function getDifficulty(level) {
    const map = {
        1: { text: "Dễ", class: "easy" },
        2: { text: "Trung bình", class: "medium" },
        3: { text: "Khó", class: "hard" }
    };
    return map[level] || { text: "Không rõ", class: "medium" };
}

// Mở popup xem cấu hình sơ bộ bài thi
let currentExamId = null;

async function openSummaryPopup(examId) {
    currentExamId = examId;
    const popup = document.getElementById("examSummaryPopup");
    const title = document.getElementById("examSummaryTitle");
    const parts = document.getElementById("examParts");

    popup.classList.add("show");
    title.innerText = "Đang tải...";
    parts.innerHTML = `
        <div class="skeleton"></div>
        <div class="skeleton"></div>
        <div class="skeleton"></div>
    `;
    const res = await fetch(`http://localhost:8080/${examId}/summary`);
    const result = await res.json();
    const data = result.data;
    title.innerText = data.title;
    parts.innerHTML = data.parts.map(p => `
        <div class="part-item">
            <span>${p.name}</span>
            <span>${p.totalQuestions} câu • ${p.duration} phút</span>
        </div>
    `).join("");
}

// ĐÓng popup
document.getElementById("closePopup")?.addEventListener("click", () => {
    document.getElementById("examSummaryPopup").classList.remove("show");
    document.getElementById("confirmExamRule").checked = false;
});

document.getElementById("startExamBtn")?.addEventListener("click", () => {
    const confirm = document.getElementById("confirmExamRule");
    if (!confirm.checked) {
        alert("Bạn cần xác nhận đã đọc cấu trúc đề trước khi bắt đầu.");
        return;
    }
    // chuyển sang trang quiz
    window.location.href = `/quiz.html?examId=${currentExamId}`;
});