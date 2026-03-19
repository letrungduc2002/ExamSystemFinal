export function getExamIdFromUrl() {
    const params = new URLSearchParams(window.location.search);
    return params.get("examId");
}