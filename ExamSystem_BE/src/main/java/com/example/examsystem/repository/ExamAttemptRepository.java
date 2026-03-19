package com.example.examsystem.repository;

import com.example.examsystem.entity.ExamAttempt;
import com.example.examsystem.enums.AttemptStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/*
 * Name: Le Trung Duc - Nhom 4
 * Date: 18-03-2026
 * Function: Xư lý thao tác lấy dữ liệu ở DB
 */
public interface ExamAttemptRepository extends JpaRepository<ExamAttempt, Long> {
    Optional<ExamAttempt> findFirstByUserIdAndExamIdAndStatus(Long userId, Long examId, AttemptStatus status);
}
