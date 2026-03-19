package com.example.examsystem.repository.projection;
import com.example.examsystem.enums.ExamType;


/*
 * Name: Le Trung Duc - Nhom 4
 * Date: 18-03-2026
 * Function: Xư lý thao tác lấy dữ liệu ở DB
 */
public interface ExamProjection {
    Long getId();
    String getTitle();
    ExamType getExamType();
    Long getDifficulty();
    Double getTotalMaxScore();
    Long getTotalDuration();
    Long getQuestionCount();
}
