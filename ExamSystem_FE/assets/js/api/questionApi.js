// Tạo API lấy danh sách câu hỏi
const API_BASE = "http://localhost:8080/api/v1";

export async function getQuestionsByAttempt(attemptId) {

    const res = await fetch(
        `${API_BASE}/exams/attempts/${attemptId}/questions`
    );

    if (!res.ok) {
        throw new Error("Không tải được câu hỏi");
    }

    return res.json();
}

