package com.example.examsystem.pattern.strategy.impl;

import com.example.examsystem.entity.*;
import com.example.examsystem.enums.AttemptStatus;
import com.example.examsystem.enums.ExamType;
import com.example.examsystem.repository.ExamPartRepository;
import com.example.examsystem.repository.OptionRepository;
import com.example.examsystem.repository.QuestionRepository;
import com.example.examsystem.repository.StudentResponseRepository;
import com.example.examsystem.repository.projection.ExamPartProjection;
import com.example.examsystem.pattern.strategy.ExamExecutionStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


/*
 * Name: Le Trung Duc - Nhom 4
 * Date: 18-03-2026
 * Function: xử lý chung loại đề thi chia thành nhiều phần độc lập,
 *           tính giờ riêng biệt cho từng phần (HSA, TSA)
 *
 */


@Component
@RequiredArgsConstructor
public class PartBasedTimingStrategy implements ExamExecutionStrategy {

    private final ExamPartRepository partRepository;
    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;
    private final StudentResponseRepository responseRepository;

    @Override
    public List<ExamType> getSupportedTypes() {
        return Arrays.asList(ExamType.HSA, ExamType.TSA);
    }

    @Override
    @Transactional
    public ExamAttempt startExam(ExamAttempt attempt) {
        attempt.setStartTime(LocalDateTime.now());
        attempt.setStatus(AttemptStatus.IN_PROGRESS);

        ExamPartProjection firstPartInfo = partRepository.getFirstPartInfo(attempt.getExam().getId())
                .orElseThrow(() -> new RuntimeException("Bài thi chưa có cấu trúc phần thi!"));
        attempt.setCurrentPart(partRepository.getReferenceById(firstPartInfo.getId()));
        return attempt;
    }



    @Override
    public List<Question> getQuestions(ExamAttempt attempt) {
        if (attempt.getCurrentPart() == null) {
            throw new IllegalStateException("Lượt thi này chưa có phần thi hiện tại.");
        }
        return questionRepository.findAllByPartIdWithOptions(attempt.getCurrentPart().getId());
    }

    // Xử lý lấy ra time theo từng loại đề
    @Override
    public Long getDuration(ExamAttempt attempt) {
        if (attempt.getCurrentPart() == null) {
            throw new IllegalStateException("Lượt thi này chưa có phần thi hiện tại.");
        }
        // Trả về thời gian của riêng Part đang làm
        return attempt.getCurrentPart().getDuration();
    }


    @Override
    @Transactional
    public StudentResponse saveAnswer(ExamAttempt attempt, Long questionId, Long optionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy câu hỏi!"));
        Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đáp án!"));

        // 3. Logic UPSERT
        StudentResponse response = responseRepository
                .findByAttemptIdAndQuestionId(attempt.getId(), questionId)
                .orElse(new StudentResponse()); // Nếu chưa có thì tạo mới

        response.setAttempt(attempt);
        response.setQuestion(question);
        response.setSelectedOption(option);

        return responseRepository.save(response);
    }

    @Override
    @Transactional
    public ExamAttempt submitPart(ExamAttempt attempt, Long currentPartId) {
        if (!attempt.getCurrentPart().getId().equals(currentPartId)) {
            throw new IllegalStateException("Thao tác không hợp lệ: Sai ID phần thi hiện tại.");
        }
        Integer currentOrder = attempt.getCurrentPart().getOrderIndex();
        Optional<ExamPartProjection> nextPartInfo = partRepository.getNextPartInfo(attempt.getExam().getId(), currentOrder);
        if (nextPartInfo.isPresent()) {
            attempt.setCurrentPart(partRepository.getReferenceById(nextPartInfo.get().getId()));
        } else {
            attempt.setCurrentPart(null);
        }

        return attempt;
    }

    @Override
    public ExamAttempt submitExam(ExamAttempt attempt) {
        attempt.setSubmitTime(LocalDateTime.now());
        attempt.setStatus(AttemptStatus.COMPLETED);
        attempt.setCurrentPart(null);
        return attempt;
    }

    // Logic dùng chung để UPSERT
    private StudentResponse upsertStudentResponse(ExamAttempt attempt, Question question, Long optionId) {
        StudentResponse response = responseRepository
                .findByAttemptIdAndQuestionId(attempt.getId(), question.getId())
                .orElse(new StudentResponse());
        response.setAttempt(attempt);
        response.setQuestion(question);
        response.setSelectedOption(optionId != null ? optionRepository.getReferenceById(optionId) : null);
        return responseRepository.save(response);
    }
}