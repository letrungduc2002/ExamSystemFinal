package com.example.examsystem.mapper;

import com.example.examsystem.dto.response.OptionResponse;
import com.example.examsystem.dto.response.QuestionResponse;
import com.example.examsystem.dto.response.QuestionResultMapping;
import com.example.examsystem.entity.Option;
import com.example.examsystem.entity.Question;
import org.mapstruct.Mapper;
import java.util.List;


/*
 * Name: Le Trung Duc - Nhom 4
 * Date: 18-03-2026
 * Function: Lấy dữ liệu để Mapping trả về cho client
 */
@Mapper(componentModel = "spring")
public interface QuestionMapper {
    QuestionResponse toQuestionResponse(Question question);
    List<QuestionResponse> toQuestionResponseList(List<Question> questions);
    List<OptionResponse> toQuestionResponseMapping(List<Option> questions);
    OptionResponse toOptionResponse(Option option);
}