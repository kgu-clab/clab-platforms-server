package page.clab.api.domain.login.application;

import jakarta.servlet.http.HttpServletRequest;

public interface BanMemberService {
    Long execute(HttpServletRequest request, String memberId);
}
