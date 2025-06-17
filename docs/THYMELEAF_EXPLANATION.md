# Thymeleaf 사용 설명

## 1. Thymeleaf란?

Thymeleaf는 Java 서버 사이드 템플릿 엔진입니다. HTML을 그대로 유지하면서 동적 뷰를 만들 수 있는 특징이 있어, 내추럴 템플릿이라고도 합니다.

## 2. 프로젝트에서의 Thymeleaf 사용

### 2.1 기본 설정

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org"></html>
```

### 2.2 레이아웃 템플릿

```html
<!-- layout.html -->
<!DOCTYPE html>
<html
  xmlns:th="http://www.thymeleaf.org"
  xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"
>
  <head>
    <title>게시판</title>
  </head>
  <body>
    <div layout:fragment="content">
      <!-- 각 페이지의 내용이 여기에 들어감 -->
    </div>
  </body>
</html>

<!-- 페이지에서 사용 -->
<html layout:decorate="~{layout/layout}">
  <div layout:fragment="content">
    <!-- 페이지 내용 -->
  </div>
</html>
```

## 3. 주요 문법

### 3.1 텍스트 출력

```html
<!-- 텍스트 출력 -->
<span th:text="${message}">기본 메시지</span>

<!-- 이스케이프 없이 출력 -->
<div th:utext="${htmlContent}">HTML 내용</div>

<!-- 객체의 속성 접근 -->
<span th:text="${board.title}">제목</span>
```

### 3.2 반복문

```html
<tr th:each="board : ${boards}">
    <td th:text="${board.id}">1</td>
    <td th:text="${board.title}">제목</td>
    <!-- 상태 변수 사용 -->
    <tr th:each="board, stat : ${boards}">
        <td th:text="${stat.count}">1</td>
        <td th:text="${stat.even}">짝수 여부</td>
    </tr>
</tr>
```

### 3.3 조건문

```html
<!-- if -->
<div th:if="${message}" class="alert alert-success">
  <span th:text="${message}">메시지</span>
</div>

<!-- unless (if의 반대) -->
<div th:unless="${#lists.isEmpty(boards)}">게시글이 있습니다.</div>

<!-- switch -->
<div th:switch="${user.role}">
  <p th:case="'admin'">관리자</p>
  <p th:case="'user'">일반 사용자</p>
  <p th:case="*">권한 없음</p>
</div>
```

### 3.4 링크 처리

```html
<!-- 기본 URL -->
<a th:href="@{/board/list}">목록</a>

<!-- 파라미터 추가 -->
<a th:href="@{/board/{id}(id=${board.id})}">상세보기</a>

<!-- 여러 파라미터 -->
<a th:href="@{/board/search(keyword=${keyword},page=${page})}">검색</a>
```

### 3.5 폼 처리

```html
<!-- 폼 객체 바인딩 -->
<form th:action="@{/board/write}" th:object="${boardForm}" method="post">
  <!-- 필드 바인딩 -->
  <input type="text" th:field="*{title}" />

  <!-- 에러 표시 -->
  <div th:if="${#fields.hasErrors('title')}" th:errors="*{title}">
    제목 오류
  </div>
</form>
```

## 4. 유틸리티 객체

### 4.1 날짜 포맷팅

```html
<!-- 기본 날짜 포맷 -->
<span th:text="${#temporals.format(board.createdAt, 'yyyy-MM-dd HH:mm')}">
  2024-01-01 12:00
</span>

<!-- 현재 날짜와 비교 -->
<span th:text="${#temporals.day(board.createdAt)}">1</span>
```

### 4.2 숫자 포맷팅

```html
<!-- 숫자 포맷 -->
<span th:text="${#numbers.formatInteger(board.viewCount, 3, 'COMMA')}">
  1,234
</span>

<!-- 소수점 포맷 -->
<span th:text="${#numbers.formatDecimal(price, 1, 2)}"> 12.34 </span>
```

### 4.3 문자열 처리

```html
<!-- 문자열 연결 -->
<span th:text="${'Welcome ' + user.name}">Welcome User</span>

<!-- 문자열 자르기 -->
<span th:text="${#strings.substring(board.content, 0, 100)}">
  내용 일부...
</span>
```

## 5. 보안 통합

### 5.1 Spring Security 통합

```html
<!-- 인증 여부 확인 -->
<div th:if="${#authorization.expression('isAuthenticated()')}">
  로그인된 사용자만 보임
</div>

<!-- 권한 확인 -->
<div th:if="${#authorization.expression('hasRole(''ROLE_ADMIN'')')}">
  관리자만 보임
</div>

<!-- 현재 사용자 정보 -->
<span th:text="${#authentication.name}">사용자명</span>
```

## 6. 모범 사례

### 6.1 템플릿 설계 원칙

1. 레이아웃 템플릿 활용
2. 반복되는 코드는 fragment로 분리
3. 적절한 에러 처리
4. 국제화(i18n) 고려

### 6.2 성능 최적화

1. 캐시 활용
2. 불필요한 중복 연산 제거
3. 적절한 fragment 캐싱
4. 정적 리소스 최적화
