package com.salatin.similarsearch.service.mapper;

import com.salatin.similarsearch.model.Question;
import com.salatin.similarsearch.model.dto.response.QuestionResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

    QuestionResponseDto toDto(Question question);
}
