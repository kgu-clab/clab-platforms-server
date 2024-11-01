package page.clab.api.global.auth.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import page.clab.api.global.auth.exception.AuthenticationInfoNotFoundException;

/**
 * AuthUtil은 인증 정보에 접근하기 위한 유틸리티 클래스로, 인증 정보 조회 및 사용자 인증 상태를 확인하는 메서드를 제공합니다.
 *
 * <p>이 클래스는 인증된 사용자의 정보를 가져오거나, 현재 인증 상태를 확인하는 데 사용됩니다.
 * 인증 정보가 없을 경우 예외를 발생시킵니다.</p>
 *
 * <p>주요 메서드:</p>
 * <ul>
 *     <li>{@link #getAuthenticationInfo()}: 현재 인증된 사용자의 정보를 반환</li>
 *     <li>{@link #getAuthenticationInfoMemberId()}: 인증된 사용자의 ID를 반환</li>
 *     <li>{@link #isUserUnAuthenticated(Authentication)}: 사용자가 인증되지 않았는지 확인</li>
 * </ul>
 *
 * @throws AuthenticationInfoNotFoundException 인증 정보가 없거나 유효하지 않을 경우 발생
 */
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

    public static Boolean isUserUnAuthenticated(Authentication authentication) {
        return (authentication == null || authentication.getAuthorities() == null || authentication.getAuthorities().isEmpty());
    }
}
