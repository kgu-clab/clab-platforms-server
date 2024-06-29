package page.clab.api.domain.login.application;

import jakarta.servlet.http.HttpServletRequest;

public interface MemberUnbanService {
    Long unban(HttpServletRequest request, String memberId);
}
