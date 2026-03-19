package com.example.examsystem.repository;

import com.example.examsystem.entity.ExamPart;
import com.example.examsystem.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
/*
 * Name: Le Trung Duc - Nhom 4
 * Date: 18-03-2026
 * Function: Xư lý thao tác lấy dữ liệu ở DB
 */
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    /**
     * Dành cho V-ACT (GlobalTimingStrategy):
     * Lấy TOÀN BỘ câu hỏi và lựa chọn của một bài thi cùng lúc.
     */
    @Query("""
        SELECT DISTINCT q 
        FROM Question q 
        JOIN FETCH q.examPart ep 
        LEFT JOIN FETCH q.options 
        WHERE ep.exam.id = :examId 
        ORDER BY ep.orderIndex ASC, q.id ASC
    """)
    List<Question> findAllByExamIdWithOptions(@Param("examId") Long examId);

    /**
     * Dành cho HSA / TSA (PartBasedTimingStrategy):
     * Lấy câu hỏi và lựa chọn của MỘT PHẦN THI cụ thể.
     */
    @Query("""
        SELECT DISTINCT q 
        FROM Question q 
        LEFT JOIN FETCH q.options 
        WHERE q.examPart.id = :partId 
        ORDER BY q.id ASC
    """)
    List<Question> findAllByPartIdWithOptions(@Param("partId") Long partId);

    // Lấy tất cả câu hỏi thuộc tất cả các phần thi của 1 mã đề (examId)
    @Query("SELECT q FROM Question q WHERE q.examPart.exam.id = :examId")
    List<Question> findAllByExamId(@Param("examId") Long examId);
}