# 세투연 행사 운영 플랫폼 API 흐름

## 시연 순서

1. ADMIN 로그인
2. 행사 생성
3. 행사 상태를 OPEN으로 변경
4. MEMBER 로그인
5. 행사 신청
6. STAFF 로그인
7. 신청자 목록 조회
8. 신청 승인 또는 반려
9. 출석 코드 발급
10. MEMBER 로그인
11. 출석 코드 입력
12. 피드백 작성
13. ADMIN 로그인
14. 대시보드 조회

## 샘플 계정

서버 시작 시 `DemoDataLoader`가 아래 계정을 자동 생성한다.

| 역할 | 이메일 | 비밀번호 |
| --- | --- | --- |
| ADMIN | admin@setuyeon.kr | password123! |
| STAFF | staff@setuyeon.kr | password123! |
| MEMBER | member@setuyeon.kr | password123! |

## 요청 흐름 설명

발표에서는 아래 흐름으로 설명하면 된다.

```text
HTTP 요청
-> Controller: URL과 요청 JSON을 받음
-> Service: 권한과 비즈니스 규칙을 검사함
-> Repository: DB 저장/조회 실행
-> PostgreSQL: 실제 데이터 저장
-> Repository
-> Service
-> Controller
-> JSON 응답
```

## 핵심 API

### 로그인

```http
POST /auth/login
Content-Type: application/json

{
  "email": "admin@setuyeon.kr",
  "password": "password123!"
}
```

응답의 `token` 값을 이후 요청의 `Authorization` 헤더에 넣는다.

```http
Authorization: Bearer <token>
```

### 행사 생성

```http
POST /events
Authorization: Bearer <ADMIN_TOKEN>
Content-Type: application/json

{
  "title": "모의투자대회 설명회",
  "description": "세투연 모의투자대회 참가 방법과 리서치 제출 방식을 안내합니다.",
  "location": "세종대학교 학생회관 세미나실",
  "eventDateTime": "2026-07-03T19:00:00",
  "capacity": 50
}
```

### 행사 모집 시작

```http
PATCH /events/1/status
Authorization: Bearer <ADMIN_TOKEN>
Content-Type: application/json

{
  "status": "OPEN"
}
```

### 행사 신청

```http
POST /events/1/applications
Authorization: Bearer <MEMBER_TOKEN>
```

규칙:

- 행사가 `OPEN`일 때만 신청 가능
- 같은 회원은 같은 행사에 중복 신청 불가
- 일반 부원 `MEMBER`만 신청 가능

### 신청자 조회

```http
GET /events/1/applications
Authorization: Bearer <STAFF_TOKEN>
```

### 승인

```http
PATCH /applications/1/approve
Authorization: Bearer <STAFF_TOKEN>
```

규칙:

- `PENDING` 신청만 승인 가능
- 승인되면 상태가 `APPROVED`로 변경됨

### 출석 코드 발급

```http
POST /events/1/attendance-code
Authorization: Bearer <STAFF_TOKEN>
```

응답의 `attendanceCode`를 회원 출석 요청에 사용한다.

### 출석

```http
POST /events/1/attendance
Authorization: Bearer <MEMBER_TOKEN>
Content-Type: application/json

{
  "code": "123456"
}
```

규칙:

- 승인된 `APPROVED` 신청자만 출석 가능
- 출석 코드가 일치하고 만료되지 않아야 함
- 같은 행사에 중복 출석 불가

### 피드백

```http
POST /events/1/feedback
Authorization: Bearer <MEMBER_TOKEN>
Content-Type: application/json

{
  "rating": 5,
  "content": "기업분석 사례가 실제 투자 의사결정과 연결되어 유익했습니다."
}
```

규칙:

- 승인된 신청자만 피드백 작성 가능
- 평점은 1점부터 5점까지
- 같은 행사에 중복 피드백 불가

### 대시보드

```http
GET /dashboard
Authorization: Bearer <ADMIN_TOKEN>
```

조회 항목:

- 전체 행사 수
- 모집중 행사 수
- 전체 신청 수
- 승인/반려 신청 수
- 출석 수
- 피드백 수
- 행사별 신청/승인/출석/피드백 수

## 발표용 핵심 설명

```text
이 서비스는 행사 중심 플랫폼이기 때문에 Event를 중심 Entity로 두었습니다.
회원은 행사에 신청하고, 운영진은 신청을 승인 또는 반려합니다.
출석과 피드백은 승인된 신청자만 가능하도록 Service 계층에서 검증했습니다.
Controller는 요청을 받고, Service는 업무 규칙을 검사하며, Repository는 PostgreSQL과 데이터를 주고받습니다.
```
