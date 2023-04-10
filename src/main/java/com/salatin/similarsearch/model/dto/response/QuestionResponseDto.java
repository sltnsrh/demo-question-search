package com.salatin.similarsearch.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class QuestionResponseDto {
    @Schema(description = "The question id", example = "10")
    private Long id;
    @Schema(description = "The text value of a question", example = "Do you prefer coffee or tea?")
    private String text;
}
