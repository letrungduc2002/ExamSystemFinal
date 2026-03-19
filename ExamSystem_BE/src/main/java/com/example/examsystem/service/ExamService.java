package com.example.examsystem.service;

import com.example.examsystem.dto.response.ExamResponse;
import com.example.examsystem.dto.response.ExamSummaryResponse;
import com.example.examsystem.dto.response.PartSummaryDTO;
import com.example.examsystem.enums.ExamType;
import com.example.examsystem.mapper.ExamMapper;
import com.example.examsystem.repository.ExamRepository;
import com.example.examsystem.repository.projection.ExamPartProjection;
import com.example.examsystem.repository.projection.ExamProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/*
 * Name: Le Trung Duc - Nhom 4
 * Date: 18-03-2026
 * Function: Thuc Hien Chuc Nang Xu Ly Qua Trinh Lay Ra Bai Thi Hien Thi Cho Student
 */


@Service
@RequiredArgsConstructor
public class ExamService {
    private final ExamRepository examRepository;
    private final ExamMapper examMapper;

    public Page<ExamResponse> findExam(String examName, String examType, Long difficult,
                                       Pageable pageable) {
        ExamType typeEnum = null;
        if (examType != null && !examType.trim().isEmpty()) {
            typeEnum = ExamType.valueOf(examType.toUpperCase());
        }
        Page<ExamProjection> findExam = examRepository.findExam(examName, typeEnum, difficult, pageable);
        List<ExamResponse> examResult = new ArrayList<>();

        for (ExamProjection e : findExam.getContent()) {
            ExamResponse convert = examMapper.toResponse(e);
            examResult.add(convert);
        }
        return new PageImpl<>(examResult, findExam.getPageable(), findExam.getTotalElements());
    }

    public ExamSummaryResponse getExamSummary(Long examId) {
        List<ExamPartProjection> flatResults = examRepository.getExamSummaryByExamId(examId);
        if (flatResults.isEmpty()) {
            throw new RuntimeException("Không tìm thấy bài thi hoặc chưa có phần thi!");
        }
        String examTitle = flatResults.get(0).getExamTitle();
        List<PartSummaryDTO> parts = flatResults.stream()
                .map(item -> new PartSummaryDTO(
                        item.getPartId(),
                        item.getPartName(),
                        item.getDuration(),
                        item.getTotalQuestions()
                ))
                .toList();
        return new ExamSummaryResponse(examTitle, parts);
    }
}
