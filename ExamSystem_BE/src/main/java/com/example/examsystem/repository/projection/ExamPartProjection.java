package com.example.examsystem.repository.projection;


/*
 * Name: Le Trung Duc - Nhom 4
 * Date: 18-03-2026
 * Function: Xư lý thao tác lấy dữ liệu ở DB
 */
public interface ExamPartProjection {
    String getExamTitle();
    Long getPartId();
    String getPartName();
    Integer getDuration();
    Long getTotalQuestions();

    Long getId();
    Integer getOrderIndex();
}
