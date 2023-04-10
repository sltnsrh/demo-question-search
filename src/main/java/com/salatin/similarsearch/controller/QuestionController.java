package com.salatin.similarsearch.controller;

import com.salatin.similarsearch.model.dto.request.TopSimilarRequestDto;
import com.salatin.similarsearch.model.dto.response.QuestionResponseDto;
import com.salatin.similarsearch.service.QuestionService;
import com.salatin.similarsearch.service.mapper.QuestionMapper;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/questions")
@RequiredArgsConstructor
@Validated
public class QuestionController {
    private final QuestionService questionService;
    private final QuestionMapper questionMapper;

    @GetMapping("/top-longest")
    public ResponseEntity<List<QuestionResponseDto>> findTopLongest(
        @RequestParam @Positive int count) {

        List<QuestionResponseDto> result = questionService.findTopLongest(count).stream()
            .map(questionMapper::toDto)
            .toList();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("top-most-similar")
    public ResponseEntity<List<QuestionResponseDto>> findTopSimilar(
        @RequestBody TopSimilarRequestDto requestDto) {

        List<QuestionResponseDto> resultList = questionService
            .findTopSimilar(requestDto.getCount(), requestDto.getQuestion()).stream()
            .map(questionMapper::toDto)
            .toList();

        return new ResponseEntity<>(resultList,
            HttpStatus.OK);
    }
}
