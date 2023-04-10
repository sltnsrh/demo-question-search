package com.salatin.similarsearch.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TopSimilarRequestDto {
    private int count;
    private String question;
}
