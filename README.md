# C-Lab Page Server

[![Spring Boot Gradle CI](https://github.com/KGU-C-Lab/clab-server/actions/workflows/spring-boot-gradle-ci.yml/badge.svg)](https://github.com/KGU-C-Lab/clab-server/actions/workflows/spring-boot-gradle-ci.yml)

경기대학교 개발보안동아리 C-Lab의 공식 백엔드 시스템입니다.

해당 프로젝트는 C-Lab의 활동을 지원하고, 회원들 간의 소통을 원활하게 하기 위해 개발되었습니다.

## Contributors

<a href="https://github.com/KGU-C-Lab/clab-server/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=KGU-C-Lab/clab-server" />
</a>

## Tech Stack

- **Spring Boot**: 웹 및 애플리케이션 개발을 위한 프레임워크.
- **Spring Security**: 인증 및 권한 부여를 위한 보안 프레임워크.
- **Spring Data JPA**: 데이터 접근 계층을 위한 JPA.
- **QueryDSL**: 복잡한 쿼리 작성을 단순화.
- **PostgreSQL**: 주 데이터베이스로 사용.
- **Redis**: 캐싱 및 JWT 관리를 위해 사용.
- **Thymeleaf**: 메일 전송을 위한 템플릿 엔진.
- **IPInfo**: IP 주소 기반 위치 정보 조회.
- **Google Authenticator**: 2단계 인증을 위한 라이브러리.
- **Slack API**: 각종 보안 알림을 위해 사용.
- **Swagger**: API 문서 자동화.

## Project Structure

### Domain
각 도메인은 독립적인 기능을 담당하는 클래스들로 구성됩니다.

각 도메인 폴더 내부에는 다음과 같은 하위 구조를 가집니다:

- `api`: REST API를 통해 외부 요청을 처리합니다.
- `application`: 핵심 비즈니스 로직을 수행합니다.
- `dao`: 데이터베이스의 CRUD 작업을 담당합니다.
- `domain`: 도메인 객체를 정의합니다.
- `dto`: 계층 간 데이터 교환을 위한 객체를 정의합니다.
- `exception`: 도메인별 예외 상황을 처리합니다.

### Global
`global` 폴더에는 프로젝트 전반에 걸쳐 공통적으로 사용되는 클래스들이 포함됩니다.

- `auth`: 인증 관련 기능을 수행합니다.
- `common`: 여러 도메인에서 공통적으로 사용되는 유틸리티 클래스를 제공합니다.
- `config`: 프로젝트의 설정 관련 클래스를 모아둡니다.
- `exception`: 전역적인 예외 처리 로직을 담당합니다.
- `handler`: 예외 및 특정 상황에 대한 핸들러를 정의합니다.
- `util`: 일반적인 유틸리티 기능을 제공합니다.
- `validation`: 유효성 검증을 위한 클래스를 포함합니다.

## Database Schema

![absent](https://github.com/KGU-C-Lab/clab-server/assets/85067003/4b8e66ab-f7fc-49b7-85a0-cb27b32a0436)

## License

이 프로젝트는 GNU 일반 공중 사용 허가서(GPL) v3.0에 따라 라이선스가 부여됩니다.

## Contributing

본 프로젝트에 기여하고자 하시는 분은 다음의 가이드라인을 따라주세요.

[CONTRIBUTING.md](CONTRIBUTING.md)
