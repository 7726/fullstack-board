package com.example.fullstackboard.exception;

import java.time.LocalDateTime;
import java.util.Map;

// 에러가 발생했을 때 클라이언트로 전달되는 응답 형식을 정의한 클래스
public record ErrorResponse (
        String code,  // 에러 코드 문자열
        String message,  // 사용자 메시지
        Map<String, String> errors,  // 필드 단위의 에러 정보 에러 (ex: { "email": "이메일 형식이 잘못되었습니다." })
        LocalDateTime timestamp,  // 발생 시각
        String requestId  // 요청 추적 ID
) {
    // 모든 에러 정보 직접 넣는 경우
    public static ErrorResponse of(ErrorCode code, String message, Map<String, String> errors, String requestId) {
        return new ErrorResponse(code.name(), message, errors, LocalDateTime.now(), requestId);
    }

    // 필드 에러 없는 경우
    public static ErrorResponse of(ErrorCode code, String message, String requestId) {
        return of(code, message, null, requestId);
    }

    // 메시지도 기본 메시지를 쓰는 경우
    public static ErrorResponse of(ErrorCode code, String requestId) {
        return of(code, code.defaultMessage, null, requestId);
    }
}
