package com.example.examsystem.repository;

import com.example.examsystem.entity.ExamPart;
import com.example.examsystem.repository.projection.ExamPartProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/*
 * Name: Le Trung Duc - Nhom 4
 * Date: 18-03-2026
 * Function: Xư lý thao tác lấy dữ liệu ở DB
 */
@Repository
public interface ExamPartRepository extends JpaRepository<ExamPart, Long> {

    // Lấy thông tin phần thi đầu tiên
    @Query("SELECT p.id AS id, p.orderIndex AS orderIndex FROM ExamPart p WHERE p.exam.id = :examId ORDER BY p.orderIndex ASC LIMIT 1")
    Optional<ExamPartProjection> getFirstPartInfo(@Param("examId") Long examId);

    // Lấy thông tin phần thi tiếp theo
    @Query("SELECT p.id AS id, p.orderIndex AS orderIndex FROM ExamPart p WHERE p.exam.id = :examId AND p.orderIndex > :currentOrder ORDER BY p.orderIndex ASC LIMIT 1")
    Optional<ExamPartProjection> getNextPartInfo(@Param("examId") Long examId, @Param("currentOrder") Integer currentOrder);
}
