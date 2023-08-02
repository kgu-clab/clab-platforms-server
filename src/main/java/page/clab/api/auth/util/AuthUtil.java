package page.clab.api.auth.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
//import page.clab.api.type.dto.UserPrincipal;
import page.clab.api.type.entity.User;

public class AuthUtil {

//    public static User getAuthenticationInfo() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal)) {
//            return null;
//        }
//        return ((UserPrincipal) authentication.getPrincipal()).toUser();
//    }


    public static String getAuthenticationInfoUserId() {
//        return getAuthenticationInfo().getId();
        return "201912156";
    }

}