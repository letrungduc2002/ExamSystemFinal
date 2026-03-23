package com.example.examsystem.service;

import com.example.examsystem.dto.request.AnswerRequest;
import com.example.examsystem.dto.request.SubmitPartRequest;
import com.example.examsystem.dto.response.ExamResultMappingResponse;
import com.example.examsystem.dto.response.QuestionResultMapping;
import com.example.examsystem.dto.response.SubmitPartResponse;
import com.example.examsystem.entity.ExamAttempt;
import com.example.examsystem.entity.Option;
import com.example.examsystem.entity.Question;
import com.example.examsystem.entity.StudentResponse;
import com.example.examsystem.enums.AttemptStatus;
import com.example.examsystem.mapper.QuestionMapper;
import com.example.examsystem.pattern.factory.ExamStrategyFactory;
import com.example.examsystem.pattern.strategy.ExamExecutionStrategy;
import com.example.examsystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/*
 * Name: Le Trung Duc - Nhom 4
 * Date: 18-03-2026
 * Function: Lưu câu trả lời
 */
@Service
@RequiredArgsConstructor
public class AnswerService {
    private final StudentResponseRepository studentResponseRepository;
    private final ExamAttemptRepository attemptRepository;
    private final ExamRepository examRepository;
    private final UserRepository userRepository;
    private final ExamStrategyFactory strategyFactory;
    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;

    @Transactional
    public void saveStudentAnswersBatch(Long attemptId, List<AnswerRequest> answerRequests) {
        ExamAttempt attempt = attemptRepository.findById(attemptId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy lượt thi!"));
        if (attempt.getStatus() != AttemptStatus.IN_PROGRESS) {
            throw new IllegalStateException("Bài thi đã kết thúc, không thể lưu thêm đáp án!");
        }
        // 3. Lấy Strategy tương ứng
        ExamExecutionStrategy strategy = strategyFactory.getStrategy(attempt.getExam().getExamType());
        // 4. lưu từng đáp án
        for (AnswerRequest request : answerRequests) {
            if (request.getQuestionId() != null && request.getOptionId() != null) {
                strategy.saveAnswer(attempt, request.getQuestionId(), request.getOptionId());
            }
        }
    }
}
