package com.example.examsystem.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;


/*
 * Name: Le Trung Duc - Nhom 4
 * Date: 18-03-2026
 * Function: Thuc Hien Chuc Nang Nhan Du Lieu Tu Client
 */

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExamRequest {
    String examType;
    Long difficulty;
}
