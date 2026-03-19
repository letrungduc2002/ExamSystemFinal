package com.example.examsystem.dto.response;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;


/*
 * Name: Le Trung Duc - Nhom 4
 * Date: 18-03-2026
 * Function: Thuc Hien Chuc Nang chuyen doi du lieu
 */


@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExamResponse {
    Long id;
    String examType;
    String title;
    Long difficulty;
    Double totalMaxScore;
    Long totalDuration;
    Long totalQuestion;
}
