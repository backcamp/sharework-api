package com.sharework.global;

import com.sharework.response.model.ErrorResponse;
import com.sharework.response.model.meta.BasicMeta;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e) {

        BasicMeta meta = new BasicMeta(false, e.getMessage());
        return ResponseEntity.ok(new ErrorResponse(meta));
    }
}
