package com.salatin.questionsearch.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiExceptionObject {
    @Schema(example = "Exception cause description.")
    private String message;
    @Schema(example = "BAD_REQUEST")
    private HttpStatus httpStatus;
    @Schema(example = "2023-04-10 12:36:05")
    private String timestamp;
}
