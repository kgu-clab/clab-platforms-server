# 기여 지침
C-Lab Platforms Server 프로젝트에 관심을 가져주셔서 감사합니다. 커뮤니티의 모든 구성원들이 효과적으로 기여할 수 있도록 다음의 절차와 규칙을 준수하여 주시기 바랍니다.

- [한국어](CONTRIBUTING.md)
- [English](CONTRIBUTING_EN.md)

## 시작하기
1. 프로젝트 저장소를 개인 계정으로 포크합니다.
2. 포크한 저장소를 로컬 시스템으로 클론합니다: `git clone https://github.com/[your-username]/clab-platforms-server.git`.
3. 새로운 작업 브랜치를 생성합니다: `git checkout -b [new branch name]`.

## 작업 규칙
- 작업할 최소 기능 단위로 이슈를 생성합니다.
- 목표 이슈, 명세 이슈, 작업 이슈의 순서로 이슈를 구성합니다.
- 이슈 번호를 참조하여 `feat/#이슈번호` 형식의 브랜치를 만듭니다.
- 다음과 같은 커밋 메시지 형식을 사용합니다:
    - **feat**: 새로운 기능 추가.
        - 예시: `feat(기능): [기능 설명]`
    - **refactor**: 새로운 기능이나 버그 수정 없이 코드 리팩토링.
        - 예시: `refactor(모듈): [리팩토링 설명]`
    - **fix**: 버그 수정.
        - 예시: `fix(이슈): [버그 수정 설명]`
    - **hotfix**: 긴급하게 적용해야 하는 즉시 수정.
        - 예시: `hotfix(이슈): [긴급 수정 설명]`
    - **chore**: 일반적인 작업 및 유지보수.
        - 예시: `chore(작업): [작업 설명]`

## 풀 리퀘스트 (Pull Requests)
- 간결하고 명확한 PR 제목을 작성합니다.
- PR 설명에 변경된 내용을 상세히 기술합니다. 
- 관련된 이슈 번호를 참조하고, 필요한 경우 리뷰어를 태그합니다.

## 코드 리뷰
- 메인테이너 또는 프로젝트 관리자가 리뷰를 진행합니다. 수정이 필요한 경우 피드백을 제공합니다. 
- 피드백에 따른 수정 사항이 있을 경우, 해당 브랜치에 추가 커밋을 통해 수정합니다.

## 병합 (Merging)
- PR이 승인되고 모든 체크가 완료되면, 메인테이너에 의해 `develop` 브랜치로 병합됩니다.

프로젝트에 기여해 주셔서 감사드리며, 여러분의 기여가 이 커뮤니티의 발전에 큰 도움이 될 것입니다.
