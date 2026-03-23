package com.example.examsystem.mapper;

import com.example.examsystem.dto.response.ExamResponse;
import com.example.examsystem.entity.Exam;
import com.example.examsystem.repository.projection.ExamProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;


/*
 * Name: Le Trung Duc - Nhom 4
 * Date: 18-03-2026
 * Function: Lấy dữ liệu để Mapping trả về cho client
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ExamMapper {

    ExamResponse mappingToExamResponse(Exam e);
    // mapping projection interface: bỏ get + chữ đầu không viết hoa
    @Mapping(source = "questionCount", target = "totalQuestion")
    ExamResponse toResponse(ExamProjection projection);

}
