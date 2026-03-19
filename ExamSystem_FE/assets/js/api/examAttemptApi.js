const API_BASE = "http://localhost:8080/api/v1";
export async function createExamAttempt(examId) {
    const response = await fetch(`${API_BASE}/exams/${examId}/attempts`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        }
    });
    if (!response.ok) {
        throw new Error("Không thể tạo lượt làm bài");
    }
    return await response.json();
}

