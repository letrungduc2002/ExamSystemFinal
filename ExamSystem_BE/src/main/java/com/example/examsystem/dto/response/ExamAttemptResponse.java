package com.example.examsystem.dto.response;

import com.example.examsystem.enums.AttemptStatus;
import com.example.examsystem.enums.ExamType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

/*
 * Name: Le Trung Duc - Nhom 4
 * Date: 18-03-2026
 * Function: Thuc Hien Chuc Nang chuyen doi du lieu
 */

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExamAttemptResponse {
    Long attemptId;
    ExamType examType;
    AttemptStatus status;
    LocalDateTime startTime;
    Long currentPartId;
}