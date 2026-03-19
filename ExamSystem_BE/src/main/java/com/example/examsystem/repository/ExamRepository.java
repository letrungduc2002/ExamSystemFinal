package com.example.examsystem.repository;
import com.example.examsystem.entity.Exam;
import com.example.examsystem.enums.ExamType;
import com.example.examsystem.repository.projection.ExamPartProjection;
import com.example.examsystem.repository.projection.ExamProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public interface ExamRepository extends JpaRepository<Exam, Long> {
    Page<Exam> findAll(Pageable pageable);

    @Query("""
            SELECT 
                e.id as id,
                e.title as title,
                e.examType as examType,
                e.difficulty as difficulty,
                e.totalMaxScore as totalMaxScore,
                e.totalDuration as totalDuration,
                COUNT(q.id) as questionCount
            FROM Exam e
            LEFT JOIN e.examParts ep
            LEFT JOIN ep.questions q 
            WHERE (:examName IS NULL OR e.title like concat ('%', :examName , '%')) 
            and (:examType IS NULL OR e.examType =:examType) and (:difficult IS NULL OR e.difficulty =:difficult)
            GROUP BY e.id, e.title, e.examType, e.difficulty, e.totalMaxScore, e.totalDuration
            """)
    Page<ExamProjection> findExam( @Param("examName") String examName,
                        @Param("examType") ExamType examType,
                        @Param("difficult") Long difficult, Pageable pageable);


    @Query("""
    SELECT 
        e.title AS examTitle,
        ep.id AS partId,
        ep.name AS partName,
        ep.duration AS duration,
        COUNT(q.id) AS totalQuestions
    FROM Exam e 
    JOIN e.examParts ep 
    LEFT JOIN ep.questions q 
    WHERE e.id = :examId 
    GROUP BY e.id, e.title, ep.id, ep.name, ep.duration, ep.orderIndex 
    ORDER BY ep.orderIndex ASC
""")
    List<ExamPartProjection> getExamSummaryByExamId(@Param("examId") Long examId);
}
