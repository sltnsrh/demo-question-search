package com.salatin.similarsearch.controller;

import com.salatin.similarsearch.model.Question;
import com.salatin.similarsearch.service.QuestionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/questions")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @GetMapping("/top-longest")
    public ResponseEntity<List<Question>> findTopLongest(@RequestParam int count) {

        return new ResponseEntity<>(questionService.findTopLongestQuestions(count), HttpStatus.OK);
    }
}
