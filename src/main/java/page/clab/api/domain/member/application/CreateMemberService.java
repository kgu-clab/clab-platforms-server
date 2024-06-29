package page.clab.api.domain.member.application;

import page.clab.api.domain.member.dto.request.MemberRequestDto;

public interface CreateMemberService {
    String execute(MemberRequestDto requestDto);
}
