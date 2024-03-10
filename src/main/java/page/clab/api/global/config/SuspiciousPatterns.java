package page.clab.api.global.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.regex.Pattern;

@Setter
@Getter
@Configuration
public class SuspiciousPatterns {

    @Getter
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
            Pattern.compile(".*\\/(phpmyadmin|pma|admin|dbadmin|mysql|myadmin|phpMyAdmin).*"),

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
            Pattern.compile(".*\\/(\\.bash_history|\\.ssh|thumbs\\.db|desktop\\.ini|\\.Trashes|\\.Spotlight-V100|\\.vol).*"),
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

}
