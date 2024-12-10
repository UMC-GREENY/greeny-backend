## 서버는 비용 문제로 인해 2023.08.24 이후 비활성화 상태입니다.
<br>

## 개요
친환경적인 삶을 추구할 수 있도록 정보를 제공하는 플랫폼 REST API 서버입니다.
<br>
기초적인 인증, CRUD 등의 기능을 구현하여 서버의 전체적인 구조 및 DB와의 안정적인 연동에 대해 학습합니다.
<br>
주제에 맞게 이슈를 생성하고, 적극적인 코드 리뷰를 통해 팀원들 간 활발한 소통이 이루어지는 것을 중요시합니다.
<br></br>

## 기술
Java, Spring-boot, Security, JPA, MySQL, Docker, EC2, RDS, S3, GitHub Actions
<br></br>

## 시스템 아키텍처
<img width="510" alt="System Architecture" src="https://github.com/user-attachments/assets/41bb5aa2-3922-4a73-b770-4c4f5dc1f53d">
<br></br>

## ERD
<img width="1192" alt="erd" src="https://github.com/user-attachments/assets/0a3449f5-dd07-44ea-9bf5-ec8cf70a9ede">
<br></br>

## CI/CD
반복되는 과정을 자동화하여 애플리케이션에 변경 사항이 발생하면 자동으로 배포합니다.
1. 빌드를 완료하고 도커 이미지를 생성한 뒤 Docker Hub에 저장
2. EC2 서버에 접속하여 실행 중인 모든 컨테이너 중단 및 애플리케이션 이미지 삭제
3. Docker Hub에서 이미지를 가져오고 모든 컨테이너 실행
<br></br>

## 컨벤션
### 코드 리뷰
- 인당 두 명의 리뷰자 선정
- 의도에 대해 질문, 개선이 필요해 보이면 피드백
- 종료되면 PR 전송자가 직접 병합
<br>

### 이슈
- 기능 구현, 트러블 슈팅 등을 주제로 생성
- 이슈 번호 확인
<br>

### 브랜치: Type/#Issue.number-Title
- master: 최종본
- env: 환경 설정
- feat: 기능 구현
- refactor: 리팩토링
- fix: 오류 수정
<br>

### 커밋: [Type] Title
- env: 설정
- feat: 기능 구현
- refactor: 리팩토링
- fix: 오류 수정
- chore: gradle 설정, 기타 작업
- test: 테스트 코드
<br>

### PR: [#Issue.number] Type: Title
- 구체적인 내용 작성
<br>

## 😁 Team
|[미누/문민우](https://github.com/Minuooooo)|[모건/김태균](https://github.com/taegyuni)|[한/김지은](https://github.com/gol2580)|[시오/김현성](https://github.com/evgeniac10)|[준/이우성](https://github.com/dtd1614)|
|-----|-----|-----|-----|-----|
|![미누/문민우](https://avatars.githubusercontent.com/u/121410579?v=4)|![모건/김태균](https://avatars.githubusercontent.com/u/81752546?v=4)|![한/김지은](https://avatars.githubusercontent.com/u/86960201?v=4)|![시오/김현성](https://avatars.githubusercontent.com/u/122839143?v=4)|![준/이우성](https://avatars.githubusercontent.com/u/116648310?v=4)|
