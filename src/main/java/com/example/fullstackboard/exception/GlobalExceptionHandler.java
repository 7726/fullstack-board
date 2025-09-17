package com.example.fullstackboard.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice  // 모든 컨트롤러에서 발생하는 예외 처리
public class GlobalExceptionHandler {

    // 유효성 검증 실패 (@Valid 실패 시)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e) {
        String requestId = UUID.randomUUID().toString();  // 요청 추적 ID

        // 필드별 에러 메시지 모음
        Map<String, String> fieldErrors = e.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        fe -> fe.getField(),  // 필드 명
                        fe -> fe.getDefaultMessage(),  // 에러 메시지
                        (a, b) -> a  // 같은 필드 중복 시 첫 번째 메시지 사용
                ));

        log.warn("[{}] Validation failed: {}", requestId, fieldErrors);

        return ResponseEntity
                .status(ErrorCode.VALIDATION_FAILED.status)  // 400 (VALIDATION_FAILED)
                .body(ErrorResponse.of(
                        ErrorCode.VALIDATION_FAILED,
                        ErrorCode.VALIDATION_FAILED.defaultMessage,
                        fieldErrors,
                        requestId
                ));
    }

    // 커스텀 BadRequest 예외 처리
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException e) {
        String requestId = UUID.randomUUID().toString();
        log.warn("[{}] Bad request: {}", requestId, e.getMessage());
        return ResponseEntity
                .status(ErrorCode.BAD_REQUEST.status)
                .body(ErrorResponse.of(ErrorCode.BAD_REQUEST, e.getMessage(), requestId));
    }

    // 잘못된 타입/파라미터 형식 (예: 숫자 처리에 문자열 넣은 경우)
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        String requestId = UUID.randomUUID().toString();
        log.warn("[{}] Bad request: {}", requestId, e.getMessage());
        return ResponseEntity
                .status(ErrorCode.BAD_REQUEST.status)
                .body(ErrorResponse.of(ErrorCode.NOT_FOUND, e.getMessage(), requestId));
    }

    // IllegalArgumentException 발생 시 -> BadRequest로 처리
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException e) {
        String requestId = UUID.randomUUID().toString();
        log.warn("[{}] Bad request: {}", requestId, e.getMessage());
        return ResponseEntity
                .status(ErrorCode.BAD_REQUEST.status)
                .body(ErrorResponse.of(ErrorCode.BAD_REQUEST, e.getMessage(), requestId));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        String requestId = UUID.randomUUID().toString();  // 요청 단위 추적 ID
        log.error("[{}] Unexpected error: {}", requestId, ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(ErrorCode.INTERNAL_ERROR, requestId));
    }

}