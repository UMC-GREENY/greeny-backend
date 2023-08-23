![GREENY 썸네일-001 (1)](https://github.com/UMC-GREENY/greeny-backend/assets/121410579/0e882d66-0e65-43d5-a4ca-6dea91c535d5)
# "에코 라이프를 실천해 나갈 여러분, 어서오세요 여러분과 동행하는 친환경 라이프 스타일 서비스 GREENY 입니다"

**친환경 라이프 서비스 GREENY는 지속 가능한 라이프스타일을 추구하는 사용자들을 위해 친환경 제품 정보, 생활 팁, 지속 가능한 쇼핑몰 추천 등을 제공하는 서비스입니다
지구를 위해 어떤 노력들을 해야하는지 잘 몰랐던 사람들이 에코 라이프를 실천해 나갈 수 있도록 하고 친환경적인 소비 습관과 환경을 생각하는 습관을 형성합니다
대부분의 사람들이 알고 있는 것처럼 우리나라뿐만 아니라 전 지구적으로 환경 오염 문제가 점점 심각해지고 있는 상황 속에서 앞장서고자 합니다**

## 어떤 사람들을 위한 서비스일까요?
- 환경 보호를 위해 뭔가 하고 싶지만 알고 있는 정보가 없어 친환경 라이프를 시작하는 데 진입 장벽이 있는
- 친환경 제품에 대한 정보가 부족한
- 친환경 스토어들을 한 눈에 보고 싶은
- 환경 보호를 위한 생활 팁들을 쉽게 알고 싶은

## 👊 주요 기능

👤 **회원**
- 회원가입, 탈퇴
- 로그인, 로그아웃
- 자동 로그인
- 소셜 로그인 (Kakao, Naver)
- 내가 찜한 스토어, 제품 조회
- 내가 작성한 게시글 조회

🌳 **에코 스토어**
- ALL, NEW, BEST 스토어 목록 및 상세 정보 조회
- 지역, 카테고리에 따른 필터링
- 인기순, 후기순 정렬
- 찜 하기/취소
- 리뷰 작성, 조회, 삭제

🌿 **에코 제품**
- ALL, NEW, BEST 제품 목록 및 상세 정보 조회
- 인기순, 후기순, 가격 높은 순, 가격 낮은 순 정렬
- 찜 하기/취소
- 리뷰 작성, 조회, 삭제

🍀 **커뮤니티**
- 게시글 작성, 수정, 조회, 삭제
- Best 게시글 조회
- 스토어, 제품에 대한 전체 리뷰 조회

👀 **기타**
- 도움말
- 생활 팁 정보 조회

## 🛠️ 개발 환경
|분류|내용|
|:---|:---|
|통합 개발 환경|IntelliJ|
|Java version|11.0.20-amzn|
|데이터베이스|AWS RDS (MySQL)|
|프레임워크|Spring boot v2.7.13|
|Project 빌드 관리 도구|Gradle|
|배포|Docker, AWS EC2 (Ubuntu)|
|CI/CD|Github Actions|
|기타|AWS S3|
|패키지 구조|DDD (Domain-Driven Design)|
|버전 관리|Git, Github|
|협업 Tool|Notion, Figma, Discord|

## ⚙️ System architecture
![System Architecture](https://github.com/UMC-GREENY/greeny-backend/assets/121410579/821fff55-d759-484a-9d02-f06868457302)

## 📃 ERD
![ERD](https://www.notion.so/image/https%3A%2F%2Fs3-us-west-2.amazonaws.com%2Fsecure.notion-static.com%2F0108cc5b-b067-4b09-b43c-b35a210d111d%2FUntitled.png?table=block&id=eddbfea0-2fa5-48ed-9072-eddc0ec50fad&spaceId=402a8ff4-3d6b-4096-b362-a6a4865edb8c&width=2000&userId=403f54d0-161a-443a-8ec6-6789d04c6e2c&cache=v2)

## 📗 API 명세서
[API Docs](https://secret-enquiry-5de.notion.site/API-705d147f145447fa90553dac2054e18b?pvs=4)

## 👥 Backend Convention
### Issues
`Ex. Title`
- 기능 개발, 오류 수정 등의 Issue 생성
- Issue number 확인 (#1, 2, 3..)
- 트러블 슈팅을 위해 오류에 대한 Issue를 생성하고, 해결 과정 문서화

### Branch
`Ex. Type/#Issue.number-Title`

`master` : 최종본

`env` : 환경 설정

`feat` : 이슈 별 기능 개발

`refactor` : 리팩토링

`fix` : 오류 수정

`test` : 테스트 코드 작성

### Commit Message
`Ex. [ Type ] Title`

`env` : 환경 설정

`feat` : 기능 개발

`refactor` : 리팩토링

`fix` : 오류 수정

`chore` : Gradle 설정, 기타 작업

`test` : 테스트 코드 작성

**커밋 단위는 세부 기능 기준!!**

### PR
`Ex. [#Issue.number] Type: Title`

**팀원들의 코드를 꼼꼼히 살펴보고 상세한 리뷰 작성!!**

## 😁 Team
|[미누/문민우](https://github.com/Minuooooo)|[모건/김태균](https://github.com/taegyuni)|[한/김지은](https://github.com/gol2580)|[시오/김현성](https://github.com/evgeniac10)|[준/이우성](https://github.com/dtd1614)|
|-----|-----|-----|-----|-----|
|![미누/문민우](https://avatars.githubusercontent.com/u/121410579?v=4)|![모건/김태균](https://avatars.githubusercontent.com/u/81752546?v=4)|![한/김지은](https://avatars.githubusercontent.com/u/86960201?v=4)|![시오/김현성](https://avatars.githubusercontent.com/u/122839143?v=4)|![준/이우성](https://avatars.githubusercontent.com/u/116648310?v=4)|
