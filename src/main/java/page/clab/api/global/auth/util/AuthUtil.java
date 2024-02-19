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
            throw new AuthenticationInfoNotFoundException("Authentication principal is not of type User.");
        }
    }

    public static String getAuthenticationInfoMemberId() {
        return getAuthenticationInfo().getUsername();
    }

}