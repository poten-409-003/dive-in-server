package com.poten.dive_in.controller;

import com.poten.dive_in.dto.CommonResponse;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@ControllerAdvice
public class ControllerExceptionAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse<Object>> handleException(Exception e){
        return new ResponseEntity<>(CommonResponse.error(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<CommonResponse<Object>> handleException(BadRequestException e){
        return new ResponseEntity<>(CommonResponse.error(e.getMessage()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<CommonResponse<Object>> handleException(RuntimeException e){
        return new ResponseEntity<>(CommonResponse.error(e.getMessage()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    public ResponseEntity<CommonResponse<Object>> handleException(HttpClientErrorException.Unauthorized e){
        return new ResponseEntity<>(CommonResponse.error(e.getMessage()),HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(HttpClientErrorException.Forbidden.class)
    public ResponseEntity<CommonResponse<Object>> handleException(HttpClientErrorException.Forbidden e){
        return new ResponseEntity<>(CommonResponse.error(e.getMessage()),HttpStatus.FORBIDDEN);
    }


}
