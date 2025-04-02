# DoitMoney Backend

DoitMoney Backend는 "나만의 AI가계부" 애플리케이션의 서버 사이드로, 사용자의 재무 관리 데이터를 처리하고 제공하는 RESTful API를 제공합니다. 이 프로젝트는 Spring Boot와 Gradle을 기반으로 하며, 사용자 인증, 거래 및 계좌 관리, 고정 지출 처리, 뉴스 캐싱 등 다양한 기능을 지원합니다.

---

## 1. 개요

DoitMoney Backend는 개인 재무관리 애플리케이션을 위한 핵심 기능들을 제공합니다. 주요 기능은 다음과 같습니다.

- **사용자 관리**:  
  - 회원가입 및 로그인 기능 (비밀번호 암호화, 중복 이메일 체크)
- **거래 관리**:  
  - 사용자별 거래 내역 입력, 수정, 삭제 (복합 기본키를 이용한 Transaction CRUD)
- **계좌 관리**:  
  - 사용자의 은행, 카드, 현금 계좌 등록 및 조회
- **고정 지출 관리**:  
  - 정기 지출(월세, 구독 서비스 등)을 일정에 맞춰 자동 처리
- **뉴스 캐싱**:  
  - 금융 관련 뉴스 데이터를 SERP API를 통해 조회 및 DB에 저장
- **보안**:  
  - Spring Security를 사용하여 CORS, CSRF 설정 및 URL별 접근 제어
- **API 문서화**:  
  - Swagger(OpenAPI)를 통한 API 문서 제공

---

## 2. 기술 스택

- **Spring Boot 3.x**
- **Spring Data JPA**
- **Spring Security**
- **Spring Scheduler**
- **Swagger/OpenAPI (springdoc)**
- **MariaDB** (데이터베이스)
- **Gradle** (빌드 도구)
- **Lombok** (보일러플레이트 코드 감소)
- **Jsoup** (HTML 파싱)

---

## 3. 프로젝트 구조

```plaintext
doitmoney-backend/
├── src/
│   ├── main/
│   │   ├── java/com/doitmoney/backend/
│   │   │   ├── config/           # CORS, Security, RestTemplate, Swagger 설정
│   │   │   ├── controller/       # REST 컨트롤러 (User, Account, Transaction, FixedExpense, News)
│   │   │   ├── dto/              # DTO 클래스 (LoginRequest, SerpApiResponse 등)
│   │   │   ├── entity/           # JPA 엔티티 (User, Account, Transaction, FixedExpense, NewsArticleEntity)
│   │   │   ├── repository/       # Spring Data JPA Repository 인터페이스
│   │   │   ├── security/         # Custom UserDetails 구현 등 보안 관련 클래스
│   │   │   └── service/          # 비즈니스 로직 (계좌, 거래, 고정 지출, 뉴스, 사용자 관리 등)
│   │   └── resources/
│   │       └── application.properties   # DB, 서버 포트, SERP API 키 등 설정
│   └── test/                 # 단위 및 통합 테스트
└── build.gradle              # Gradle 빌드 구성 파일
```


⸻

## 4. 사전 요구 사항
	•	Java 17 이상
	•	MariaDB 데이터베이스 서버
	•	Gradle (또는 제공된 Gradle Wrapper 사용)

## 5. 주요 API 엔드포인트
	•	User API
	•	POST /users/register : 회원가입 (비밀번호 암호화 및 중복 이메일 체크)
	•	POST /users/login : 로그인 (이메일과 비밀번호를 통한 인증)
	•	Account API
	•	POST /accounts/{userId} : 사용자 계좌 등록
	•	GET /accounts/{userId} : 사용자 계좌 목록 조회
	•	Transaction API
	•	GET /transactions/{userId} : 사용자 거래 내역 조회
	•	POST /transactions/{userId} : 거래 내역 추가
	•	GET /transactions : (예시) 로그인한 사용자의 거래 내역 조회
	•	Fixed Expense API
	•	POST /fixed-expenses/{userId} : 고정 지출 등록
	•	GET /fixed-expenses/{userId} : 사용자 고정 지출 조회
	•	News API
	•	GET /news/today : 오늘의 금융 뉴스 반환 (DB 캐시)
	•	GET /news/force-update : 뉴스 업데이트 강제 실행

⸻

## 6. Swagger API 문서

API 문서는 Swagger를 통해 제공됩니다. 서버 실행 후 다음 URL에서 확인할 수 있습니다.

[http://localhost:5009/swagger-ui/index.html](http://doitmoney.kro.kr/api/swagger-ui/index.html)

⸻

## 7. 문의 및 연락
	•	고재형 (202045044)
	•	이준환 (202045066)
	•	Email: junans0boi@gmail.com
