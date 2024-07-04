package page.clab.api.domain.login.application.port.in;

import jakarta.servlet.http.HttpServletRequest;

public interface UnbanMemberUseCase {
    Long unbanMember(HttpServletRequest request, String memberId);
}
