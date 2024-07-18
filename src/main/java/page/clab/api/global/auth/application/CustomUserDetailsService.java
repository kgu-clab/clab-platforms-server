package page.clab.api.global.auth.application;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import page.clab.api.domain.memberManagement.member.domain.Member;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return externalRetrieveMemberUseCase.findById(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 멤버를 찾을 수 없습니다."));
    }

    private UserDetails createUserDetails(Member member) {
        return Member.createUserDetails(member);
    }
}
