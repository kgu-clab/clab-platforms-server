package page.clab.api.domain.login.application;

import jakarta.servlet.http.HttpServletRequest;

public interface UnbanMemberService {
    Long unbanMemberById(HttpServletRequest request, String memberId);
}
