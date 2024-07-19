package page.clab.api.domain.auth.accountLockInfo.application.port.in;

import jakarta.servlet.http.HttpServletRequest;

public interface BanMemberUseCase {
    Long banMember(HttpServletRequest request, String memberId);
}
