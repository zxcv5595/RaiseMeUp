package com.zxcv5595.member.exception;

import static com.zxcv5595.member.type.ErrorCode.ACCESS_DENIED;
import static com.zxcv5595.member.type.ErrorCode.VALIDATION_FAILED;

import com.zxcv5595.member.dto.ErrorResponse;
import io.jsonwebtoken.MalformedJwtException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final int VALIDATION_FAILED_STATUS = VALIDATION_FAILED.getStatus().value();
    private static final int ACCESS_DENIED_STATUS = ACCESS_DENIED.getStatus().value();

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> customExceptionHandler(CustomException e) {
        log.error("'{}':'{}'", e.getErrorCode(), e.getErrorCode().getMessage());

        ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode(), e.getStatus(),
                e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        ErrorResponse errorResponse = new ErrorResponse(VALIDATION_FAILED,
                VALIDATION_FAILED_STATUS, errors.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException() {
        log.error("AccessDeniedException is occurred");

        ErrorResponse errorResponse = new ErrorResponse(ACCESS_DENIED,
                ACCESS_DENIED_STATUS, ACCESS_DENIED.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ErrorResponse> handleMalformedJwtException() {
        log.error("MalformedJwtException is occurred");

        ErrorResponse errorResponse = new ErrorResponse(ACCESS_DENIED,
                ACCESS_DENIED_STATUS, ACCESS_DENIED.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}

