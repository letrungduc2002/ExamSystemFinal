package com.example.examsystem.pattern.strategy;
import com.example.examsystem.entity.ExamAttempt;
import com.example.examsystem.entity.Question;
import com.example.examsystem.entity.StudentResponse;
import com.example.examsystem.enums.ExamType;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


/*
 * Name: Le Trung Duc - Nhom 4
 * Date: 18-03-2026
 * Function: định nghĩa toàn bộ các hành vi chung xử lý từng
 *           quá trình làm đề thi (HSA, TSA, V_ACT) của học sinh.
 */


@Repository
public interface ExamExecutionStrategy {
    List<ExamType> getSupportedTypes();
    ExamAttempt startExam(ExamAttempt attempt);

    // Hàm lấy danh sách câu hỏi theo Strategy
    List<Question> getQuestions(ExamAttempt attempt);

    // Lấy thời gian làm bài (tính bằng phút) tùy theo loại đề
    Long getDuration(ExamAttempt attempt);


    StudentResponse saveAnswer(ExamAttempt attempt, Long questionId, Long optionId);
    ExamAttempt submitPart(ExamAttempt attempt, Long currentPartId);
    ExamAttempt submitExam(ExamAttempt attempt);
}