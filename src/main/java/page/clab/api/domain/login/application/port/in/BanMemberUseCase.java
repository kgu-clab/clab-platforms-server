package page.clab.api.domain.login.application.port.in;

import jakarta.servlet.http.HttpServletRequest;

public interface BanMemberUseCase {
    Long banMember(HttpServletRequest request, String memberId);
}
