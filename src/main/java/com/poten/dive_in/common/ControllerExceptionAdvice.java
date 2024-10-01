package com.poten.dive_in.common;

import com.poten.dive_in.common.dto.CommonResponse;
import org.apache.coyote.BadRequestException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    // 유효성 검사 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException e) {

        Map<String,String> validationErrors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        error -> error.getDefaultMessage() == null ? "Unknown error" : error.getDefaultMessage()
                ));

        return new ResponseEntity<>(CommonResponse.error("유효성 검사 실패하였습니다.",validationErrors), HttpStatus.BAD_REQUEST);
    }


}
