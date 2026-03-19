const BASE_URL = "http://localhost:8080/api";
export async function fetchExams(page = 0, size = 10) {
    try {
        const response = await fetch(
            `${BASE_URL}/exams?page=${page}&size=${size}`
        );
        if (!response.ok) {
            throw new Error("Failed to fetch exams");
        }
        const result = await response.json();
        return result.data;
    } catch (error) {
        console.error("Exam API error:", error);
        throw error;
    }
}