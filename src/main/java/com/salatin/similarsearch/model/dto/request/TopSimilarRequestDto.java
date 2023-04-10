package com.salatin.similarsearch.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class TopSimilarRequestDto {
    @Schema(
        description = "Allows to limit searching results",
        example = "10"
    )
    private int count;
    @Schema(
        description = "The question you want to find similar",
        example = "What books do you like to read?"
    )
    private String question;
}
