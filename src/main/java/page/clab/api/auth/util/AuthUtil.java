package page.clab.api.auth.util;

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