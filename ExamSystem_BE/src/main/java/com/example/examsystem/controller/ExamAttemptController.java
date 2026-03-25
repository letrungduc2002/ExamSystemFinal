package com.example.examsystem.controller;
import com.example.examsystem.dto.request.AnswerRequest;
import com.example.examsystem.dto.request.SubmitPartRequest;
import com.example.examsystem.dto.response.*;
import com.example.examsystem.service.ExamAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/*
 * Name: Le Trung Duc - Nhom 4
 * Date: 18-03-2026
 * Function: Gui Api thuc hien qua trinh Khoi Tao, Luu Bai Thi, Va Tinh Toan Ket Qua
 */
@RestController
@RequestMapping("/api/v1/exams")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ExamAttemptController {

    private final ExamAttemptService attemptService;
    /**
     * API: Khởi tạo lượt làm bài cho một Đề thi
     * POST /api/v1/exams/{examId}/attempts
     */
    @PostMapping("/{examId}/attempts")
    public ApiResponse<ExamAttemptResponse> startExam(@PathVariable("examId") Long examId) {
        Long currentStudentId = 1L;
        ApiResponse<ExamAttemptResponse> apiResponse = ApiResponse.<ExamAttemptResponse>builder()
                .code(200)
                .message("Success")
                .data(attemptService.createAttempt(examId, currentStudentId))
                .build();
        return apiResponse;
    }

    /**
     * API: Hiển thị câu hỏi đề thi
     * GET /api/v1/exams/attempts/{attemptId}/questions
     */
    @GetMapping("/attempts/{attemptId}/questions")
    public ApiResponse<AttemptContentResponse> getQuestionsForAttempt(@PathVariable("attemptId") Long attemptId) {
        // Gọi Service lấy danh sách câu hỏi
        ApiResponse<AttemptContentResponse> response = ApiResponse.<AttemptContentResponse>builder()
                .code(200)
                .message("Success")
                .data(attemptService.getQuestionsForAttempt(attemptId))
                .build();
        return response;
    }

    /**
     * API: Auto-save lưu đồng loạt đáp án của học sinh
     * POST /api/v1/exams/attempts/{attemptId}/answers
     */
    @PostMapping("/attempts/{attemptId}/answers")
    public ApiResponse<String> autoSave(
            @PathVariable("attemptId") Long attemptId,
            @RequestBody List<AnswerRequest> requests) { // Đổi thành List

        attemptService.autoSaveAnswer(attemptId, requests);
        ApiResponse<String> response = ApiResponse.<String>builder()
                .code(200)
                .message("Đã đồng bộ thành công " + requests.size() + " đáp án")
                .data(null)
                .build();
        return response;
    }

    /**
     * API: Nộp phần thi hiện tại và chuyển sang phần tiếp theo
     * POST /api/v1/exams/attempts/{attemptId}/submit-part
     */
    @PostMapping("/attempts/{attemptId}/submit-part")
    public ApiResponse<SubmitPartResponse> submitPart(
            @PathVariable("attemptId") Long attemptId,
            @RequestBody SubmitPartRequest request) {

        ApiResponse<SubmitPartResponse> response = ApiResponse.<SubmitPartResponse>builder()
                .code(200)
                .message("Đã nộp toàn bộ bài thi thành công! Chuyển phần thi thành công!")
                .data(attemptService.submitPart(attemptId, request))
                .build();
        return response;
    }

    /**
     * API: Nộp toàn bộ bài thi và tính điểm
     * POST /api/v1/exams/attempts/{attemptId}/submit
     */
    @PostMapping("/attempts/{attemptId}/submit")
    public ApiResponse<ExamResultMappingResponse> submitExam(
                        @PathVariable("attemptId") Long attemptId) {

        ApiResponse<ExamResultMappingResponse> response = ApiResponse.<ExamResultMappingResponse>builder()
                .code(200)
                .message("Nộp bài và chấm điểm thành công!")
                .data(attemptService.submitAndGetScore(attemptId))
                .build();
        return response;
    }
}
