package com.example.examsystem.controller;

import com.example.examsystem.dto.response.ApiResponse;
import com.example.examsystem.dto.response.ExamResponse;
import com.example.examsystem.dto.response.ExamSummaryResponse;
import com.example.examsystem.service.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;



/*
 * Name: Le Trung Duc - Nhom 4
 * Date: 18-03-2026
 * Function: Gui Api lay ra bai thi hien thi cho Student
 */




@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ExamController {

    // Hiển thị giao diện bài thi
    private final ExamService examService;

    @GetMapping("/api/exams")
    public ApiResponse<Page<ExamResponse>> getExams(
            @RequestParam(name = "title", required = false) String examName,
            @RequestParam(name = "type", required = false) String examType,
            @RequestParam(name = "difficult", required = false) Long difficult,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        ApiResponse<Page<ExamResponse>> apiResponse = ApiResponse.<Page<ExamResponse>>builder()
                .code(200)
                .message("Success")
                .data(examService.findExam(examName, examType, difficult, pageable))
                .build();
        return apiResponse;
    }

    @GetMapping("/{examId}/summary")
    public ApiResponse<ExamSummaryResponse> getExamSummary(@PathVariable("examId") Long examId) {
        ApiResponse<ExamSummaryResponse> apiResponse = ApiResponse.<ExamSummaryResponse>builder()
                .code(200)
                .message("Success")
                .data(examService.getExamSummary(examId))
                .build();
        return apiResponse;
    }
}
