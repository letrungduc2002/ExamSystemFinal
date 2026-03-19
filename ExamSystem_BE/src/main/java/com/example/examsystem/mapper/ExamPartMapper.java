package com.example.examsystem.mapper;

import com.example.examsystem.dto.response.PartSummaryDTO;
import com.example.examsystem.entity.ExamPart;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;


/*
 * Name: Le Trung Duc - Nhom 4
 * Date: 18-03-2026
 * Function: Lấy dữ liệu để Mapping trả về cho client
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ExamPartMapper {


}
