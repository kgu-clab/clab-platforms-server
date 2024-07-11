package page.clab.api.global.auth.application;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import page.clab.api.domain.memberManagement.member.application.port.out.RetrieveMemberPort;
import page.clab.api.domain.memberManagement.member.domain.Member;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final RetrieveMemberPort retrieveMemberPort;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return retrieveMemberPort.findById(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 멤버를 찾을 수 없습니다."));
    }

    private UserDetails createUserDetails(Member member) {
        return Member.createUserDetails(member);
    }

}
