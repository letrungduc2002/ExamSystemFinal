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


    @Transactional
    public SubmitPartResponse submitPart(Long attemptId, SubmitPartRequest request) {
        ExamAttempt attempt = attemptRepository.findById(attemptId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy lượt thi!"));
        if (attempt.getStatus() != AttemptStatus.IN_PROGRESS) {
            throw new IllegalStateException("Bài thi đã kết thúc, không thể nộp phần thi!");
        }

        ExamExecutionStrategy strategy = strategyFactory.getStrategy(attempt.getExam().getExamType());
        ExamAttempt updatedAttempt = strategy.submitPart(attempt, request.getCurrentPartId());
        attemptRepository.save(updatedAttempt);
        Long nextPartId = (updatedAttempt.getCurrentPart() != null) ? updatedAttempt.getCurrentPart().getId() : null;
        boolean isFinished = (nextPartId == null);
        return SubmitPartResponse.builder()
                .isFinished(isFinished)
                .nextPartId(nextPartId)
                .build();
    }
    
    @Transactional
    public ExamResultMappingResponse submitExam(Long attemptId) {
        ExamAttempt attempt = attemptRepository.findById(attemptId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy lượt thi!"));

        if (attempt.getStatus() == AttemptStatus.COMPLETED) {
            throw new IllegalStateException("Bài thi này đã được nộp và chấm điểm!");
        }
        Long examId = attempt.getExam().getId();
        List<Question> allQuestions = questionRepository.findAllByExamId(examId);

        // 3. Lấy toàn bộ bài làm của học sinh
        List<StudentResponse> studentResponses = studentResponseRepository.findByAttemptId(attemptId);

        // Chuyển List thành Map<QuestionId, SelectedOptionId>
        Map<Long, Long> userAnswersMap = studentResponses.stream()
                .collect(Collectors.toMap(
                        res -> res.getQuestion().getId(),
                        res -> res.getSelectedOption().getId()
                ));

        int correctCount = 0;
        List<QuestionResultMapping> details = new ArrayList<>();

        // 4. THUẬT TOÁN CHẤM ĐIỂM
        for (Question question : allQuestions) {
            Long correctOptionId = question.getOptions().stream()
                    .filter(Option::getIsCorrect)
                    .map(Option::getId)
                    .findFirst()
                    .orElse(null);

            Long selectedOptionId = userAnswersMap.get(question.getId());

            boolean isCorrect = (selectedOptionId != null && selectedOptionId.equals(correctOptionId));
            if (isCorrect) {
                correctCount++;
            }

            details.add(QuestionResultMapping.builder()
                    .questionId(question.getId())
                    .content(question.getContent())
                    .options(questionMapper.toQuestionResponseMapping(question.getOptions()))
                    .selectedOptionId(selectedOptionId)
                    .correctOptionId(correctOptionId)
                    .isCorrect(isCorrect)
                    .build());
        }
        attempt.setStatus(AttemptStatus.COMPLETED);
        attempt.setSubmitTime(LocalDateTime.now());
        double score = (double) correctCount / allQuestions.size() * 10.0;
        score = Math.round(score * 100.0) / 100.0;
        attempt.setTotalScore(score);
        attemptRepository.save(attempt);
        return ExamResultMappingResponse.builder()
                .score(score)
                .correctAnswers(correctCount)
                .totalQuestions(allQuestions.size())
                .submitTime(attempt.getSubmitTime())
                .details(details)
                .build();
    }
}
