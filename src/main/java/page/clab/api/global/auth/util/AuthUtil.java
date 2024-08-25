package page.clab.api.global.auth.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import page.clab.api.global.auth.exception.AuthenticationInfoNotFoundException;

public class AuthUtil {

    public static User getAuthenticationInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new AuthenticationInfoNotFoundException();
        }
        if (authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        } else {
            throw new AuthenticationInfoNotFoundException("인증 정보가 존재하지 않습니다.");
        }
    }

    public static String getAuthenticationInfoMemberId() {
        return getAuthenticationInfo().getUsername();
    }

    public static Boolean isUserUnAuthenticated (Authentication authentication) {
        return (authentication == null || authentication.getAuthorities() == null || authentication.getAuthorities().isEmpty());
    }
}
