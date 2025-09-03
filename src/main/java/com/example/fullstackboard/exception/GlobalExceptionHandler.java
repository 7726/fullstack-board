package com.example.fullstackboard.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice  // 모든 컨트롤러에서 발생하는 예외 처리
public class GlobalExceptionHandler {

    // 유효성 검증 실패 (@Valid 실패 시)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e) {
        // 필드별 에러 메시지 모음
        Map<String, String> fieldErrors = e.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        fe -> fe.getField(),  // 필드 명
                        fe -> fe.getDefaultMessage(),  // 에러 메시지
                        (a, b) -> a  // 같은 필드 중복 시 첫 번째 메시지 사용
                ));
        return ResponseEntity
                .status(ErrorCode.VALIDATION_FAILED.status)  // 400 (VALIDATION_FAILED)
                .body(ErrorResponse.of(ErrorCode.VALIDATION_FAILED, ErrorCode.VALIDATION_FAILED.defaultMessage, fieldErrors));
    }

    // 커스텀 BadRequest 예외 처리
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException e) {
        return ResponseEntity
                .status(ErrorCode.BAD_REQUEST.status)
                .body(ErrorResponse.of(ErrorCode.BAD_REQUEST, e.getMessage()));
    }

    // 잘못된 타입/파라미터 형식 (예: 숫자 처리에 문자열 넣은 경우)
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        return ResponseEntity
                .status(ErrorCode.BAD_REQUEST.status)
                .body(ErrorResponse.of(ErrorCode.BAD_REQUEST, "파라미터 형식이 올바르지 않습니다."));
    }

    // IllegalArgumentException 발생 시 -> BadRequest로 처리
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity
                .status(ErrorCode.BAD_REQUEST.status)
                .body(ErrorResponse.of(ErrorCode.BAD_REQUEST, e.getMessage()));
    }
}