package page.clab.api.domain.auth.accountLockInfo.application.port.in;

import jakarta.servlet.http.HttpServletRequest;

public interface UnbanMemberUseCase {

    Long unbanMember(HttpServletRequest request, String memberId);
}
