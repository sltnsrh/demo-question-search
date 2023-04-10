package com.salatin.similarsearch.model.dto.request;

import lombok.Getter;

@Getter
public class TopSimilarRequestDto {
    private int count;
    private String question;
}
