package page.clab.api.domain.login.application;

import jakarta.servlet.http.HttpServletRequest;

public interface MemberUnbanUseCase {
    Long unban(HttpServletRequest request, String memberId);
}
