package com.salatin.questionsearch.service.mapper;

import com.salatin.questionsearch.model.Question;
import com.salatin.questionsearch.model.dto.response.QuestionResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

    QuestionResponseDto toDto(Question question);
}
