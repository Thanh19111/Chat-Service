package com.thanhpham.chat.exception;

import com.thanhpham.chat.dto.response.ErrorResponse;
import com.thanhpham.chat.enums.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse<Void>> handleGlobalException(Exception exception, WebRequest webRequest) {
        ErrorResponse<Void> res = ErrorResponse.<Void>builder()
                .apiPath(webRequest.getDescription(false))
                .errorTime(LocalDateTime.now())
                .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(exception.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponse<Void>> handleAppException(AppException exception, WebRequest webRequest) {
        ErrorCode errorCode = exception.getErrorCode();
        ErrorResponse<Void> res = ErrorResponse.<Void>builder()
                .apiPath(webRequest.getDescription(false))
                .errorCode(errorCode.getCode())
                .message(errorCode.getMessage())
                .errorTime(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }
}
