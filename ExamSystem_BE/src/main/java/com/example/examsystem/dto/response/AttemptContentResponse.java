package com.example.examsystem.dto.response;

import com.example.examsystem.enums.ExamType;
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
public class AttemptContentResponse {
    private ExamType examType;
    private LocalDateTime startTime;
    private Long durationInMinutes;
    private List<QuestionResponse> questions;
}
