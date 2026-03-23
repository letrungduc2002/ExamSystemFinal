package com.example.examsystem.service;

import com.example.examsystem.dto.request.AnswerRequest;
import com.example.examsystem.dto.request.SubmitPartRequest;
import com.example.examsystem.dto.response.*;
import com.example.examsystem.entity.*;
import com.example.examsystem.enums.AttemptStatus;
import com.example.examsystem.mapper.QuestionMapper;
import com.example.examsystem.pattern.factory.ExamStrategyFactory;
import com.example.examsystem.repository.*;
import com.example.examsystem.pattern.strategy.ExamExecutionStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;



/*
 * Name: Le Trung Duc - Nhom 4
 * Date: 18-03-2026
 * Function: Thuc Hien Chuc Nang Xu Ly Qua Trinh Khoi Tao, Luu Bai Thi, Va Tinh Toan Ket Qua
 */


@Service
@RequiredArgsConstructor
public class ExamAttemptService {

    private final StudentResponseRepository studentResponseRepository;
    private final ExamAttemptRepository attemptRepository;
    private final ExamRepository examRepository;
    private final UserRepository userRepository;
    private final ExamStrategyFactory strategyFactory;
    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;


    @Transactional
    public ExamAttemptResponse startExam(Long examId, Long studentId) {
        Optional<ExamAttempt> existingAttempt = attemptRepository
                .findFirstByUserIdAndExamIdAndStatus(studentId, examId, AttemptStatus.IN_PROGRESS);
        if (existingAttempt.isPresent()) {
            ExamAttempt attempt = existingAttempt.get();
            return ExamAttemptResponse.builder()
                    .attemptId(attempt.getId())
                    .examType(attempt.getExam().getExamType())
                    .status(attempt.getStatus())
                    .startTime(attempt.getStartTime())
                    .currentPartId(attempt.getCurrentPart() != null ? attempt.getCurrentPart().getId() : null)
                    .build();
        }
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bài thi!"));
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy học sinh!"));
        ExamAttempt newAttempt = new ExamAttempt();
        newAttempt.setExam(exam);
        newAttempt.setUser(student);
        // 3. Gọi Factory để lấy đúng Chiến lược
        ExamExecutionStrategy strategy = strategyFactory.getStrategy(exam.getExamType());
        // 4. thiết lập thời gian và phần thi đầu tiên
        newAttempt = strategy.startExam(newAttempt);
        ExamAttempt savedAttempt = attemptRepository.save(newAttempt);
        return ExamAttemptResponse.builder()
                .attemptId(savedAttempt.getId())
                .examType(exam.getExamType())
                .status(savedAttempt.getStatus())
                .startTime(savedAttempt.getStartTime())
                .currentPartId(savedAttempt.getCurrentPart() != null ? savedAttempt.getCurrentPart().getId() : null)
                .build();
    }

    @Transactional(readOnly = true)
    public AttemptContentResponse getQuestionsForAttempt(Long attemptId) {
        ExamAttempt attempt = attemptRepository.findById(attemptId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy lượt thi!"));
        // Lấy đúng Strategy dựa vào loại bài thi
        ExamExecutionStrategy strategy = strategyFactory.getStrategy(attempt.getExam().getExamType());
        // Strategy tự quyết định kéo 1 phần hay toàn bộ bài
        List<Question> rawQuestions = strategy.getQuestions(attempt);
        Long duration = strategy.getDuration(attempt);
        return AttemptContentResponse.builder()
                .examType(attempt.getExam().getExamType())
                .startTime(attempt.getStartTime())
                .durationInMinutes(duration)
                .questions(questionMapper.toQuestionResponseList(rawQuestions))
                .build();
    }

}