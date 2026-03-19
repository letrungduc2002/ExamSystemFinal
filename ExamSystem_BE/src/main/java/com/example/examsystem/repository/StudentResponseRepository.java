package com.example.examsystem.repository;

import com.example.examsystem.entity.StudentResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
/*
 * Name: Le Trung Duc - Nhom 4
 * Date: 18-03-2026
 * Function: Xư lý thao tác lấy dữ liệu ở DB
 */
public interface StudentResponseRepository extends JpaRepository<StudentResponse, Long> {
    Optional<StudentResponse> findByAttemptIdAndQuestionId(Long attemptId, Long questionId);
    //Optional<StudentResponse> findByExamAttemptIdAndQuestionId(Long attemptId, Long questionId);
    List<StudentResponse> findByAttemptId(Long attemptId);
}
