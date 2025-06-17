# JPA 사용 설명

## 1. JPA란?

JPA(Java Persistence API)는 자바 ORM(Object-Relational Mapping) 기술의 표준입니다. 객체와 관계형 데이터베이스를 매핑하여 개발자가 SQL을 직접 작성하지 않고도 데이터베이스를 사용할 수 있게 해줍니다.

## 2. 프로젝트에서의 JPA 사용

### 2.1 엔티티 매핑

```java
@Entity
@Table(name = "boards")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    // ... 기타 필드
}
```

주요 어노테이션 설명:

- `@Entity`: JPA 엔티티 클래스임을 나타냄
- `@Table`: 매핑할 테이블 지정
- `@Id`: 기본키 지정
- `@GeneratedValue`: 기본키 생성 전략 설정
- `@Column`: 컬럼 매핑 및 속성 지정

### 2.2 레포지토리 인터페이스

```java
public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByTitleContaining(String title);
    List<Board> findByAuthor(String author);
}
```

JpaRepository 제공 기본 메서드:

- `save()`: 저장 및 수정
- `findById()`: ID로 조회
- `findAll()`: 전체 조회
- `delete()`: 삭제
- `count()`: 개수 조회

### 2.3 쿼리 메서드

메서드 이름으로 쿼리 생성:

- `findBy필드명`: 정확한 매칭
- `findBy필드명Containing`: LIKE 검색
- `findBy필드명Between`: 범위 검색
- `findBy필드명OrderBy필드명Desc`: 정렬

### 2.4 @Query 어노테이션

복잡한 쿼리의 경우 직접 JPQL 작성:

```java
@Query("UPDATE Board b SET b.viewCount = b.viewCount + 1 WHERE b.id = :id")
void increaseViewCount(@Param("id") Long id);
```

## 3. JPA 설정

### 3.1 application.properties 설정

```properties
# JPA 설정
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

설정 설명:

- `show-sql`: SQL 로그 출력
- `ddl-auto`: 테이블 자동 생성 전략
  - `create`: 매번 테이블 새로 생성
  - `update`: 변경사항만 반영
  - `validate`: 검증만 수행
  - `none`: 아무 작업도 하지 않음
- `format_sql`: SQL 로그 포맷팅
- `dialect`: 데이터베이스 방언 설정

## 4. 트랜잭션 관리

### 4.1 @Transactional 사용

```java
@Service
@Transactional(readOnly = true)
public class BoardService {
    @Transactional
    public void createBoard(BoardRequestDto dto) {
        // 저장 로직
    }

    // 읽기 전용 메서드
    public BoardResponseDto getBoard(Long id) {
        // 조회 로직
    }
}
```

트랜잭션 속성:

- `readOnly`: 읽기 전용 트랜잭션 (성능 최적화)
- `rollbackFor`: 특정 예외 발생 시 롤백
- `timeout`: 트랜잭션 타임아웃 설정
- `propagation`: 트랜잭션 전파 방식 설정

## 5. 성능 최적화

### 5.1 N+1 문제 해결

연관 관계 조회 시 발생하는 N+1 문제 해결:

```java
@Query("SELECT b FROM Board b JOIN FETCH b.comments")
List<Board> findAllWithComments();
```

### 5.2 페이징 처리

```java
public interface BoardRepository extends JpaRepository<Board, Long> {
    Page<Board> findAll(Pageable pageable);
}

// 서비스에서 사용
Page<Board> boards = boardRepository.findAll(
    PageRequest.of(0, 10, Sort.by("createdAt").descending())
);
```

## 6. 모범 사례

### 6.1 엔티티 설계 원칙

1. 엔티티는 꼭 필요한 필드만 포함
2. 가능한 한 기본 생성자는 protected로 설정
3. Getter는 열어두고 Setter는 필요한 경우만 사용
4. 연관관계는 필요한 경우만 설정

### 6.2 레이어 아키텍처

1. 엔티티는 도메인 계층에서만 사용
2. API 응답은 DTO 사용
3. 서비스 계층에서 트랜잭션 관리
4. 레포지토리는 인터페이스로 설계
