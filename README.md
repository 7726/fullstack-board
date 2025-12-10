# fullstack-board

## 📌 개요
CRUD, 검색, 인증/인가, 페이징, 예외 처리, 문서화 등
백엔드 핵심 기능을 모두 통합한 실전 게시판 API 프로젝트입니다.

## 🛠 주요 기능

### 🔐 인증/인가
- JWT 기반 로그인
- 회원가입 기능
- ROLE(USER/ADMIN) 기반 접근 제어
- 인증/인가 실패 시 JSON 응답 커스터마이징
- 본인 정보 조회 API (/members/me)

### 📝 게시판 기능 (Post)
- 게시글 등록 / 조회 / 수정 / 삭제
- 본인 확인 후 수정/삭제 가능
- Soft Delete(isDeleted) 적용
- Validation + 일관된 예외 응답 구조

### 🔎 검색 & 페이징
- QueryDSL 기반 키워드 검색
- 작성자 / 날짜 범위 / 복합 조건 검색
- Pageable 기반 페이징
- count 쿼리 정합성 보완
- CommonPageResponse(data + meta) 구조

### 🧾 문서화 & 기타
- Swagger(OpenAPI) 문서화
- 헬스 체크 (/health, /health/db)
- dev 환경 초기 데이터 스크립트 제공

## 🔍 학습 포인트
- JWT 인증/인가 + CRUD 조합의 전체 서비스 흐름 이해
- QueryDSL로 동적 조건/페이징 구현
- Soft Delete가 반영된 조회/검색
- 작성자 검증 로직 설계
- API 문서화와 실서비스 형태의 구조 구성

## ⚙️ 사용 기술
Java 17, Spring Boot 3.2, Spring Data JPA, QueryDSL, Spring Security, JWT, MySQL/H2, Swagger, Lombok
