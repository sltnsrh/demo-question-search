package com.salatin.questionsearch.controller;

import com.salatin.questionsearch.exception.ApiExceptionObject;
import com.salatin.questionsearch.model.dto.request.TopSimilarRequestDto;
import com.salatin.questionsearch.model.dto.response.QuestionResponseDto;
import com.salatin.questionsearch.service.QuestionService;
import com.salatin.questionsearch.service.mapper.QuestionMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(
    name = "Question Controller",
    description = "Allows to make search through existing questions in a DB"
)
public class QuestionController {
    private final QuestionService questionService;
    private final QuestionMapper questionMapper;

    @Operation(
        summary = "Find top longest questions",
        description = "Returns a list of the top longest questions."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved the list of questions"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid count value. Count should be a positive integer.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiExceptionObject.class)
            )
        )
    })
    @GetMapping("/top-longest")
    public ResponseEntity<List<QuestionResponseDto>> findTopLongest(
        @Parameter(
            name = "count", description = "Number of questions to return",
            example = "5", required = true
        )
        @RequestParam @Positive int count) {

        List<QuestionResponseDto> result = questionService.findTopLongest(count).stream()
            .map(questionMapper::toDto)
            .toList();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(
        summary = "Find top most similar questions",
        description = "Returns a list of the top most similar questions "
            + "in order from most similar to least similar."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved the list of questions"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Bad request",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiExceptionObject.class)
            )
        )
    })
    @PostMapping("top-most-similar")
    public ResponseEntity<List<QuestionResponseDto>> findTopSimilar(
        @Parameter(
            description = "Request body containing the count and question to compare",
            name = "requestDto", required = true
        )
        @RequestBody TopSimilarRequestDto requestDto) {

        List<QuestionResponseDto> resultList = questionService
            .findTopSimilar(requestDto.getCount(), requestDto.getQuestion()).stream()
            .map(questionMapper::toDto)
            .toList();

        return new ResponseEntity<>(resultList,
            HttpStatus.OK);
    }
}
