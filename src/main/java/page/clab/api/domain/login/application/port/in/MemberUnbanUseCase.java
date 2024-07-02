package page.clab.api.domain.login.application.port.in;

import jakarta.servlet.http.HttpServletRequest;

public interface MemberUnbanUseCase {
    Long unban(HttpServletRequest request, String memberId);
}
