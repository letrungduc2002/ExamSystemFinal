package com.example.examsystem.dto.response;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/*
 * Name: Le Trung Duc - Nhom 4
 * Date: 18-03-2026
 * Function: Thuc Hien Chuc Nang chuyen doi du lieu
 */


@Getter
@Setter
@Builder
public class ExamResultMappingResponse {
    private double score;                 // Điểm tổng (ví dụ: thang điểm 10)
    private int correctAnswers;           // Số câu đúng
    private int totalQuestions;           // Tổng số câu hỏi
    private LocalDateTime submitTime;     // Thời gian nộp bài

    private List<QuestionResultMapping> details; // Danh sách chi tiết để vẽ bảng
}
