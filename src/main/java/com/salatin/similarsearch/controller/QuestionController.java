package com.salatin.similarsearch.controller;

import com.salatin.similarsearch.model.Question;
import com.salatin.similarsearch.model.dto.request.TopSimilarRequestDto;
import com.salatin.similarsearch.service.QuestionService;
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

    @GetMapping("/top-longest")
    public ResponseEntity<List<Question>> findTopLongest(
        @RequestParam @Positive int count) {

        return new ResponseEntity<>(questionService.findTopLongest(count), HttpStatus.OK);
    }

    @PostMapping("top-most-similar")
    public ResponseEntity<List<Question>> findTopSimilar(
        @RequestBody TopSimilarRequestDto requestDto) {

        return new ResponseEntity<>(
            questionService.findTopSimilar(requestDto.getCount(), requestDto.getQuestion()),
            HttpStatus.OK);
    }
}
