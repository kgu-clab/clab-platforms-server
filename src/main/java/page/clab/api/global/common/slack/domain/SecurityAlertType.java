package page.clab.api.global.common.slack.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SecurityAlertType {

    ABNORMAL_ACCESS("비정상적인 접근", "Unexpected access pattern detected."),
    REPEATED_LOGIN_FAILURES("지속된 로그인 실패", "Multiple consecutive failed login attempts."),
    DUPLICATE_LOGIN("중복 로그인", "Duplicate login attempt."),
    UNAUTHORIZED_ACCESS("인가되지 않은 접근", "Unauthorized access attempt."),
    BLACKLISTED_IP_ADDED("블랙리스트 IP 등록", "IP address has been added to the blacklist."),
    BLACKLISTED_IP_REMOVED("블랙리스트 IP 해제", "IP address has been removed from the blacklist."),
    MEMBER_BANNED("멤버 밴 등록", "Member has been banned."),
    MEMBER_UNBANNED("멤버 밴 해제", "Member has been unbanned.");

    private final String title;
    private final String defaultMessage;

}
