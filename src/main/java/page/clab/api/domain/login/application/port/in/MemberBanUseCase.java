package page.clab.api.domain.login.application.port.in;

import jakarta.servlet.http.HttpServletRequest;

public interface MemberBanUseCase {
    Long ban(HttpServletRequest request, String memberId);
}
