package com.example.examsystem.pattern.strategy.impl;

import com.example.examsystem.entity.ExamAttempt;
import com.example.examsystem.entity.Question;
import com.example.examsystem.entity.StudentResponse;
import com.example.examsystem.enums.AttemptStatus;
import com.example.examsystem.enums.ExamType;
import com.example.examsystem.repository.OptionRepository;
import com.example.examsystem.repository.QuestionRepository;
import com.example.examsystem.repository.StudentResponseRepository;
import com.example.examsystem.pattern.strategy.ExamExecutionStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/*
 * Name: Le Trung Duc - Nhom 4
 * Date: 18-03-2026
 * Function: xử lý riêng cho loại đề thi truyền thống, thi một mạch từ đầu đến cuối (V_ACT)
 */


@Component
@RequiredArgsConstructor
public class GlobalTimingStrategy implements ExamExecutionStrategy {

    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;
    private final StudentResponseRepository responseRepository;

    @Override
    public List<ExamType> getSupportedTypes() {
        return Collections.singletonList(ExamType.V_ACT);
    }

    @Override
    @Transactional
    public ExamAttempt startExam(ExamAttempt attempt) {
        attempt.setStartTime(LocalDateTime.now());
        attempt.setStatus(AttemptStatus.IN_PROGRESS);
        return attempt;
    }

    // Lấy toàn bộ câu hỏi của bài thi cùng một lúc
    @Override
    public List<Question> getQuestions(ExamAttempt attempt) {
        return questionRepository.findAllByExamIdWithOptions(attempt.getExam().getId());
    }

    // Trả về tổng thời gian của cả Exam
    @Override
    public Long getDuration(ExamAttempt attempt) {
        return attempt.getExam().getTotalDuration();
    }

    @Override
    @Transactional
    public StudentResponse saveAnswer(ExamAttempt attempt, Long questionId, Long optionId) {
        if (attempt.getStatus() != AttemptStatus.IN_PROGRESS) {
            throw new IllegalStateException("Bài thi đã kết thúc, không thể lưu đáp án!");
        }
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy câu hỏi"));
        StudentResponse response = responseRepository
                .findByAttemptIdAndQuestionId(attempt.getId(), question.getId())
                .orElse(new StudentResponse());
        response.setAttempt(attempt);
        response.setQuestion(question);
        response.setSelectedOption(optionId != null ? optionRepository.getReferenceById(optionId) : null);
        return responseRepository.save(response);
    }

    @Override
    @Transactional
    public ExamAttempt submitPart(ExamAttempt attempt, Long currentPartId) {
        return attempt;
    }

    @Override
    @Transactional
    public ExamAttempt submitExam(ExamAttempt attempt) {
        attempt.setSubmitTime(LocalDateTime.now());
        attempt.setStatus(AttemptStatus.COMPLETED);
        return attempt;
    }
}