package page.clab.api.domain.member.application;

import page.clab.api.domain.member.dto.request.MemberRequestDto;

public interface MemberRegisterService {
    String register(MemberRequestDto requestDto);
}
