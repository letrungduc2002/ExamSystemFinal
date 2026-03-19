import { createExamAttempt } from "../api/examAttemptApi.js";
import { getExamIdFromUrl } from "../utils/url.js";
import { getQuestionsByAttempt } from "../api/questionApi.js";

let examTimerInterval = null;

const quizState = {

    examId: null,
    attemptId: null,

    startTime: null,
    duration: null,

    questions: [],
    currentIndex: 0,

    answers: {},
    flagged: {},

    examType: null, // HSA / TSA / V_ACT
    partBreaks: [],

    currentPart: 1,

    lastSavedHash: null
};

let autoSaveInterval = null;
let breakInterval = null;

async function initQuiz() {

    bindActions();

    try {

        const examId = getExamIdFromUrl();
        if (!examId) {
            alert("Không tìm thấy examId");
            return;
        }

        quizState.examId = examId;

        const attemptRes = await createExamAttempt(examId);

        quizState.attemptId = attemptRes.data.attemptId;

        console.log("Attempt:", quizState.attemptId);

        await loadQuestions();

    } catch (err) {

        console.error(err);
        alert("Không thể bắt đầu bài thi");

    }
}

async function loadQuestions() {

    const result = await getQuestionsByAttempt(quizState.attemptId);
    const data = result.data;

    quizState.startTime = data.startTime;
    quizState.duration = data.durationInMinutes;
    quizState.questions = data.questions;
    quizState.examType = data.examType;
    quizState.partBreaks = data.partBreaks || [];

    renderNavigator();
    renderQuestion();

    startTimer();
    restoreAnswers();
    startAutoSave();
}

function startTimer() {
    if (examTimerInterval) {
        clearInterval(examTimerInterval);
    }
    console.log("Timer started");
    const timerEl = document.getElementById("timer");
    const start = new Date(quizState.startTime).getTime();
    const end = start + quizState.duration * 60 * 1000;
    examTimerInterval = setInterval(() => {
        const now = Date.now();
        const remaining = end - now;
        if (remaining <= 0) {
            clearInterval(examTimerInterval);
            autoSubmit();
            return;
        }
        const minutes = Math.floor(remaining / 60000);
        const seconds = Math.floor((remaining % 60000) / 1000);
        timerEl.innerText =
            `${minutes}:${seconds.toString().padStart(2, "0")}`;

    }, 1000);
}

function renderNavigator() {

    const nav = document.getElementById("questionNavigator");

    nav.innerHTML = quizState.questions.map((q, i) => {

        let cls = "q-nav";

        if (i === quizState.currentIndex) cls += " active";
        if (quizState.answers[q.id]) cls += " answered";
        if (quizState.flagged[q.id]) cls += " flagged";

        return `
            <div class="${cls}" data-index="${i}">
                ${i + 1}
            </div>
        `;

    }).join("");

    document.querySelectorAll(".q-nav").forEach(el => {

        el.onclick = () => {

            quizState.currentIndex = parseInt(el.dataset.index);

            renderQuestion();
            renderNavigator();

        }

    });
}

function renderQuestion() {

    const q = quizState.questions[quizState.currentIndex];

    const container = document.getElementById("questionContainer");

    container.innerHTML = `
        <h3>Câu ${quizState.currentIndex + 1}</h3>
        <p>${q.content}</p>

        ${q.options.map((o, index) => `

            <div class="option-card
                ${quizState.answers[q.id] == o.id ? "selected" : ""}"

                data-option="${o.id}">

                ${String.fromCharCode(65 + index)}. ${o.content}

            </div>

        `).join("")}
    `;

    document.querySelectorAll(".option-card").forEach(el => {

        el.onclick = () => {

            const optionId = el.dataset.option;

            quizState.answers[q.id] = optionId;

            saveToLocal();

            renderQuestion();
            renderNavigator();
            updateProgress();

        };

    });

    updateProgress();
}

function saveToLocal() {

    const key = `exam_attempt_${quizState.attemptId}`;

    localStorage.setItem(
        key,
        JSON.stringify(quizState.answers)
    );
}

function restoreAnswers() {

    const key = `exam_attempt_${quizState.attemptId}`;

    const saved = localStorage.getItem(key);

    if (!saved) return;

    quizState.answers = JSON.parse(saved);
}

function startAutoSave() {

    autoSaveInterval = setInterval(() => {

        saveAnswers();

    }, 30000);
}

function buildAnswerPayload() {

    return Object.entries(quizState.answers)
        .map(([questionId, optionId]) => ({

            questionId: Number(questionId),
            optionId: Number(optionId)

        }));
}

async function saveAnswers() {

    const payload = buildAnswerPayload();

    const hash = JSON.stringify(payload);

    if (hash === quizState.lastSavedHash) return;

    quizState.lastSavedHash = hash;

    try {

        const backendUrl = `http://localhost:8080/api/v1/exams/attempts/${quizState.attemptId}/answers`;

        await fetch(backendUrl, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(payload)
        });

        console.log("Auto saved");

    } catch (err) {

        console.error("Save failed", err);

    }
}

async function submitPart() {

    try {
        const backendUrl = `http://localhost:8080/api/v1/exams/attempts/${quizState.attemptId}/submit-part`;
        await fetch(backendUrl, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                currentPartId: quizState.currentPart
            })
        }
        );

        console.log("Part submitted:", quizState.currentPart);

    } catch (err) {

        console.error("Submit part failed", err);

    }
}

function updateProgress() {

    const el = document.getElementById("questionProgress");

    el.innerText =
        `Câu ${quizState.currentIndex + 1} / ${quizState.questions.length}`;
}

function bindActions() {

    document.getElementById("nextBtn").onclick = () => {

        if (quizState.currentIndex < quizState.questions.length - 1) {

            quizState.currentIndex++;

            checkBreak();

            renderQuestion();
            renderNavigator();
            updateProgress();

        }
    };

    document.getElementById("prevBtn").onclick = () => {

        if (quizState.currentIndex > 0) {

            quizState.currentIndex--;

            renderQuestion();
            renderNavigator();
            updateProgress();

        }
    };

    document.getElementById("flagBtn").onclick = () => {

        const q = quizState.questions[quizState.currentIndex];

        quizState.flagged[q.id] = !quizState.flagged[q.id];

        renderNavigator();
    };

    document.getElementById("skipBreakBtn").onclick = () => {

        if (breakInterval) {
            clearInterval(breakInterval);
        }

        closeBreakPopup();
    };

    document.getElementById("submitBtn").onclick = () => {

        if (confirm("Bạn chắc chắn muốn nộp bài?")) {

            submitExam();

        }

    };
}

function checkBreak() {

    if (quizState.examType === "V_ACT") return;

    if (quizState.partBreaks.includes(quizState.currentIndex)) {

        openBreakPopup(quizState.currentPart);

    }
}

function openBreakPopup(partIndex) {

    const popup = document.getElementById("breakPopup");

    const title = document.getElementById("breakTitle");

    title.innerText = `Bạn đã hoàn thành Phần ${partIndex}`;

    popup.classList.remove("hidden");

    startBreakTimer();
}

function startBreakTimer() {

    const timerEl = document.getElementById("breakTimer");

    let remaining = 300;

    breakInterval = setInterval(() => {

        remaining--;

        const min = Math.floor(remaining / 60);
        const sec = remaining % 60;

        timerEl.innerText =
            `${min}:${sec.toString().padStart(2, "0")}`;

        if (remaining <= 0) {

            clearInterval(breakInterval);

            closeBreakPopup();

        }

    }, 1000);
}

async function closeBreakPopup() {
    document.getElementById("breakPopup")
        .classList.add("hidden");
    // ❗ chỉ reset với HSA và TSA
    if (quizState.examType === "HSA" || quizState.examType === "TSA") {
        clearLocalAnswers();
    }
    quizState.currentPart++;
    await loadNextPart();
}


async function autoSubmit() {

    await saveAnswers();

    await submitPart();

    if (quizState.currentPart < 3 && quizState.examType !== "V_ACT") {

        openBreakPopup(quizState.currentPart);

        return;
    }

    finishExam();
}

async function submitExam() {
    await saveAnswers();
    await submitPart();
    // nếu chưa phải part cuối
    if (quizState.currentPart < 3 && quizState.examType !== "V_ACT") {
        openBreakPopup(quizState.currentPart);
        return;
    }
    finishExam();
}

async function finishExam() {

    if (autoSaveInterval) {
        clearInterval(autoSaveInterval);
    }

    clearLocalAnswers();

    try {

        const res = await fetch(
            `http://localhost:8080/api/v1/exams/attempts/${quizState.attemptId}/submit`,
            {
                method: "POST"
            }
        );

        const result = await res.json();

        // lưu kết quả tạm
        sessionStorage.setItem(
            "exam_result",
            JSON.stringify(result.data)
        );

        // chuyển sang trang score
        window.location.href = "/score.html";

    } catch (err) {

        console.error("Submit exam failed", err);
        alert("Không thể lấy kết quả bài thi");

    }
}

document.addEventListener("DOMContentLoaded", initQuiz);

window.addEventListener("beforeunload", () => {

    saveAnswers();

});


async function loadNextPart() {

    try {

        const res = await fetch(
            `http://localhost:8080/api/v1/exams/attempts/${quizState.attemptId}/questions`
        );

        const data = await res.json();

        quizState.questions = data.data.questions;
        quizState.partBreaks = data.data.partBreaks || [];
        quizState.duration = data.data.durationInMinutes;

        quizState.currentIndex = 0;
        quizState.answers = {};
        quizState.flagged = {};

        renderNavigator();
        renderQuestion();

        startTimer();

    } catch (err) {
        console.error("Load next part failed", err);
    }
}

function clearLocalAnswers() {
    const key = `exam_attempt_${quizState.attemptId}`;
    localStorage.removeItem(key);
    quizState.answers = {};
}