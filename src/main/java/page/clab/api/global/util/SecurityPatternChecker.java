package page.clab.api.global.util;

import java.util.List;
import java.util.regex.Pattern;

/**
 * SecurityPatternChecker 클래스는 보안과 관련된 의심스러운 URL 패턴을 관리하고 검사하는 역할을 합니다. 이 클래스는 시스템 파일, 데이터베이스 백업, 또는 민감한 설정 파일에 대한 무단
 * 접근을 시도하는 의심스러운 URL 경로를 사전에 정의된 패턴과 비교하여 확인할 수 있는 유틸리티 메소드를 제공합니다.
 */
public class SecurityPatternChecker {

    /**
     * 보안과 관련된 의심스러운 URL 경로를 나타내는 사전 정의된 패턴 목록입니다. 이 패턴들은 시스템 파일, 백업 파일, 설정 파일 및 일반적으로 사용되는 웹 쉘 스크립트(.php, .asp, .jsp
     * 등)에 대한 경로를 포함합니다. 해당 경로로의 무단 접근을 감지하는 데 사용됩니다.
     */
    private static final List<Pattern> suspiciousPatterns = List.of(
        // 일반적인 웹 쉘 및 백도어
        Pattern.compile(".*\\.php.*"),
        Pattern.compile(".*\\.asp.*"),
        Pattern.compile(".*\\.jsp.*"),
        Pattern.compile(".*\\.sh.*"),
        Pattern.compile(".*\\/cgi-bin\\/.*"),

        // 워드프레스 관련 경로
        Pattern.compile(".*\\/wp-(admin|login|config)\\.php.*"),
        Pattern.compile(".*\\/wp-content\\/.*"),

        // 관리자 페이지 및 도구들
        Pattern.compile(".*\\/(phpmyadmin|pma|dbadmin|mysql|myadmin|phpMyAdmin).*"),

        // 개발 관련 파일과 디렉토리
        Pattern.compile(".*\\/(\\.git|\\.svn|\\.hg|\\.env|\\.idea|\\.vscode|\\.vs|\\.DS_Store).*"),
        Pattern.compile(".*\\.(bak|config|yaml|yml|ini).*"),

        // 서버 및 시스템 파일
        Pattern.compile(".*\\/(etc\\/passwd|etc\\/shadow|etc\\/hosts|etc\\/group).*"),

        // 백업 파일
        Pattern.compile(".*\\.(sql|tar|gz|zip|rar|dump).*"),
        Pattern.compile(".*\\/(sqlbackup|backup|backups|bck).*"),

        // 서버 상태 및 정보
        Pattern.compile(".*\\/server-(status|info).*"),

        // IDE 설정 및 파일
        Pattern.compile(".*\\/(sftp-config|sftp-settings|\\.editorconfig|\\.project).*"),
        Pattern.compile(".*\\/(\\.npmrc|\\.dockerenv|Dockerfile|\\.kube|\\.yarn|\\.npm|\\.expo|\\.builddir).*"),

        // 특정 시스템 파일
        Pattern.compile(".*\\/(debug|trace\\.axd|config\\.json|settings\\.json|package\\.json|node_modules).*"),
        Pattern.compile(
            ".*\\/(\\.bash_history|\\.ssh|thumbs\\.db|desktop\\.ini|\\.Trashes|\\.Spotlight-V100|\\.vol).*"),
        Pattern.compile(".*\\/(config|logs|tmp|temp|conf|backups).*"),

        // 환경 설정 파일
        Pattern.compile(".*\\.env\\.local$"),
        Pattern.compile(".*\\.env\\.prod$"),
        Pattern.compile(".*\\.env\\.dev$"),

        // 빌드 디렉토리
        Pattern.compile(".*\\/dist\\/.*"),
        Pattern.compile(".*\\/build\\/.*"),

        // 기타 구성 파일
        Pattern.compile(".*\\.json$"),
        Pattern.compile(".*\\.toml$"),

        // 관리 도구 경로
        Pattern.compile(".*\\/cpanel.*"),
        Pattern.compile(".*\\/webmail.*"),

        // 서버 관리 도구
        Pattern.compile(".*\\/solr.*"),
        Pattern.compile(".*\\/adminer.*"),

        // 데이터베이스 관리 경로
        Pattern.compile(".*\\/db\\.php$"),
        Pattern.compile(".*\\/sqlmanager.*"),

        // 클라우드 스토리지 버킷
        Pattern.compile(".*\\/s3\\/.*"),
        Pattern.compile(".*\\/azure\\/.*"),

        // API 키, 비밀번호 파일
        Pattern.compile(".*\\/api_keys.*"),
        Pattern.compile(".*\\/passwords.*")
    );

    /**
     * 주어진 경로(path)가 의심스러운 패턴과 일치하는지 확인하는 메소드입니다. 이 메소드는 사전에 정의된 패턴 목록을 기반으로 경로를 검사하고, 해당 경로가 의심스러운 패턴과 일치하면 true를
     * 반환합니다.
     *
     * @param path 요청된 URL 경로
     * @return 경로가 의심스러운 패턴과 일치하는지 여부 (일치하면 true, 일치하지 않으면 false)
     */
    public static boolean matchesSuspiciousPattern(String path) {
        return suspiciousPatterns.stream()
            .anyMatch(pattern -> pattern.matcher(path).matches());
    }
}
