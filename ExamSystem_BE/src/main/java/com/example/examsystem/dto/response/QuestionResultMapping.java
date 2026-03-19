package com.example.examsystem.dto.response;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


/*
 * Name: Le Trung Duc - Nhom 4
 * Date: 18-03-2026
 * Function: Thuc Hien Chuc Nang chuyen doi du lieu
 */


@Getter
@Setter
@Builder
public class QuestionResultMapping {
    private Long questionId;
    private String content;               // Nội dung câu hỏi
    private List<OptionResponse> options; // Danh sách A, B, C, D

    private Long selectedOptionId;        // Đáp án của bạn (null nếu học sinh bỏ trống)
    private Long correctOptionId;         // Đáp án đúng của hệ thống
    private boolean isCorrect;            // Cờ đánh dấu đúng/sai để Frontend tô màu (Xanh/Đỏ)
}


