# 게시판 프로젝트 구조

## 1. 프로젝트 개요

Spring Boot와 JPA를 사용한 게시판 프로젝트입니다.

### 1.1 사용 기술

- Spring Boot 3.2.3
- Spring Data JPA
- Spring Security
- Thymeleaf
- MySQL
- Gradle
- Java 17

### 1.2 개발 환경

- IDE: Eclipse/IntelliJ
- 데이터베이스: MySQL 8.0
- JDK: Java 17

## 2. 프로젝트 구조

```
board_jpa
├── src/main/java/com/example/board_jpa
│   ├── config/                 # 설정 클래스
│   │   └── SecurityConfig.java # Spring Security 설정
│   ├── controller/            # 컨트롤러 클래스
│   │   ├── BoardController.java    # REST API 컨트롤러
│   │   ├── BoardWebController.java # 웹 페이지 컨트롤러
│   │   └── MemberController.java   # 회원 관련 컨트롤러
│   ├── domain/               # 엔티티 클래스
│   │   ├── Board.java       # 게시글 엔티티
│   │   └── Member.java      # 회원 엔티티
│   ├── dto/                 # DTO 클래스
│   │   ├── BoardRequestDto.java    # 게시글 요청 DTO
│   │   ├── BoardResponseDto.java   # 게시글 응답 DTO
│   │   ├── MemberJoinRequestDto.java  # 회원가입 DTO
│   │   └── MemberLoginRequestDto.java # 로그인 DTO
│   ├── repository/          # 레포지토리 인터페이스
│   │   ├── BoardRepository.java  # 게시글 레포지토리
│   │   └── MemberRepository.java # 회원 레포지토리
│   └── service/            # 서비스 클래스
│       ├── BoardService.java  # 게시글 서비스
│       └── MemberService.java # 회원 서비스
└── src/main/resources
    ├── application.properties # 애플리케이션 설정
    └── templates/            # Thymeleaf 템플릿
        ├── layout/          # 공통 레이아웃
        │   └── layout.html
        ├── board/          # 게시판 페이지
        │   ├── list.html   # 목록 페이지
        │   ├── write.html  # 작성 페이지
        │   ├── detail.html # 상세 페이지
        │   └── edit.html   # 수정 페이지
        └── member/         # 회원 페이지
            ├── login.html  # 로그인 페이지
            └── join.html   # 회원가입 페이지
```

## 3. 주요 기능 설명

### 3.1 게시판 기능

1. **게시글 CRUD**

   - 목록 조회: `GET /`
   - 상세 조회: `GET /board/{id}`
   - 작성: `POST /board/write`
   - 수정: `PUT /board/edit/{id}`
   - 삭제: `DELETE /board/delete/{id}`

2. **게시글 속성**
   - 제목 (title)
   - 내용 (content)
   - 작성자 (author)
   - 조회수 (viewCount)
   - 작성일시 (createdAt)
   - 수정일시 (updatedAt)

### 3.2 회원 기능

1. **회원 관리**

   - 회원가입: `POST /join`
   - 로그인: `POST /login`
   - 로그아웃: `GET /logout`

2. **회원 속성**
   - 아이디 (username)
   - 비밀번호 (password)
   - 이름 (name)
   - 이메일 (email)
   - 가입일시 (createdAt)
   - 역할 (role)

## 4. 데이터베이스 구조

### 4.1 boards 테이블

```sql
CREATE TABLE boards (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    content TEXT NOT NULL,
    author VARCHAR(50) NOT NULL,
    view_count INT DEFAULT 0,
    created_at DATETIME,
    updated_at DATETIME
);
```

### 4.2 members 테이블

```sql
CREATE TABLE members (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    created_at DATETIME,
    role VARCHAR(20)
);
```

## 5. 보안 설정

### 5.1 Spring Security 설정

- 비밀번호 암호화: BCrypt 알고리즘 사용
- CSRF 보호: 비활성화 (API 사용을 위해)
- 공개 URL: /, /join, /login, /css/**, /js/**
- 인증 필요 URL: 그 외 모든 URL
- 커스텀 로그인 페이지: /login
- 로그아웃 후 이동: /

## 6. 실행 방법

1. **데이터베이스 준비**

   ```sql
   CREATE DATABASE board;
   ```

2. **애플리케이션 설정**

   - application.properties 파일에서 데이터베이스 연결 정보 확인

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/board
   spring.datasource.username=root
   spring.datasource.password=1234
   ```

3. **빌드 및 실행**

   ```bash
   ./gradlew clean build
   java -jar build/libs/board_jpa-0.0.1-SNAPSHOT.jar
   ```

4. **접속**
   - 웹 브라우저에서 http://localhost:8080 접속
