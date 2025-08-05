# Mople - 모두의 플리

[![codecov](https://codecov.io/gh/codeit-part4/sb01-mople-team2/branch/main/graph/badge.svg)](https://codecov.io/gh/codeit-part4/sb01-mople-team2)

> 대규모 트래픽이 예상되는 글로벌 콘텐츠 평점 및 큐레이션 플랫폼

🔗 **배포 링크**: [https://guileless-malasada-55630c.netlify.app/](https://guileless-malasada-55630c.netlify.app/)

## 📌 소개

- **모두의 플리**는 사용자가 영화, 드라마, 스포츠 등 다양한 콘텐츠를 **수집·평가·큐레이팅·공유**하고,
  **팔로우 및 실시간 DM(WebSocket)** 을 통해 소셜 경험을 확장할 수 있는 플랫폼입니다.  
  콘텐츠는 외부 **Open API**와 **Spring Batch**를 통해 자동 수집되며,
  사용자는 개인의 취향에 맞게 **플레이리스트를 만들고 공유하거나 구독**할 수 있습니다.  
  **실시간 같이 보기**, **DM 기능**이 제공되며,
  **알림은 SSE 기반**으로 제공되어 콘텐츠와 소통 모두를 실시간으로 즐길 수 있습니다.  
  **로그인, 권한, 소셜 연동, 계정 잠금** 등의 사용자 관리는 **JWT 인증과 보안 정책**을
  기반으로 안정적으로 처리됩니다.

---

## 🚀 기술 스택

### 💻 언어

- Java 17
- Typescript

### 🧩 백엔드

- Spring Boot
- Spring Security
- Spring Batch
- Spring Data JPA
- QueryDSL

### 🖥 프론트엔드

- React
- Vite

### 🗄 데이터베이스

- PostgreSQL

### ⚙ 빌드 & CI/CD

- Gradle
- GitHub Actions (CI: 테스트 & 커버리지 / CD: ECR → ECS 배포)
- Docker (멀티 스테이지 빌드, platform 대응)

### ☁ 클라우드 & 인프라

- AWS EC2 기반 ECS (서비스 별 Task 정의)
- Amazon ECR
- Amazon RDS
- Route 53 + ACM (HTTPS)
- Nginx (Reverse Proxy)
- CloudWatch Logs

### 📈 모니터링 & 문서화

- Spring Actuator
- Swagger (SpringDoc OpenAPI)

### 🧪 테스트 & 품질 관리

- JUnit5
- Mockito
- Codecov (커버리지 리포팅)

### 🛠 기타 도구

- GitHub
- Notion (기획 & 협업)

---

## 🧱 아키텍처

<img width="2170" height="1116" alt="rds-diagram" src="https://github.com/user-attachments/assets/07adbe23-6470-4f37-a785-b0eb4514a093" />


---

## 📂 ERD

<img width="3624" height="5166" alt="batch_job_execution" src="https://github.com/user-attachments/assets/4c954cf4-bece-4ec6-be74-65dcbf74a915" />

---

## 📄 협업 및 기여

- GitHub Issues & PR을 통한 피드백
- Notion을 통한 일정 공유

---

## 🏁 향후 개선 방향

- 콘텐츠 추천 알고리즘 고도화 (선형 회귀 + 피드백 반영)
- WebSocket, SSE 등 다중 서버로 확장 및 로드밸런싱 적용
- Spring Batch 실패 처리 추가
- Kafka 기반 이벤트 아키텍처 적용
- ElasticSearch 기반 콘텐츠 검색 기능 강화

---

## 🙌 팀원 소개

| 이름 | 역할 | 담당 기능 | GitHub |
|------|------|------------|--------|
| 이승주 | 팀장 / 백엔드 | 콘텐츠 데이터 관리, 실시간 같이 보기 기능 | [@leesj092](https://github.com/leesj092) |
| 오하람 | 백엔드 / 프론트엔드 | 사용자 관리 | [@haram0111](https://github.com/haram0111) |
| 신은섭 | 백엔드 | 팔로우 및 DM(WebSocket), 인프라 구성 | [@eunseobb](https://github.com/eunseobb) |
| 김도일 | 백엔드 | 알림 기능 (SSE 기반) | [@kbkg11](https://github.com/kbkg11) |
| 장태준 | 백엔드 / 프론트엔드 | 콘텐츠 평가 및 큐레이팅 기능 | [@janghoosa](https://github.com/janghoosa) |
