package page.clab.api.domain.member.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.port.in.MemberBirthdayRetrievalThisMonthUseCase;
import page.clab.api.domain.member.application.port.out.RetrieveMembersByBirthdayPort;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.member.dto.response.MemberBirthdayResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class MemberBirthdayRetrievalThisMonthService implements MemberBirthdayRetrievalThisMonthUseCase {

    private final RetrieveMembersByBirthdayPort retrieveMembersByBirthdayPort;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<MemberBirthdayResponseDto> retrieve(int month, Pageable pageable) {
        Page<Member> birthdayMembers = retrieveMembersByBirthdayPort.findBirthdaysThisMonth(month, pageable);
        return new PagedResponseDto<>(birthdayMembers.map(MemberBirthdayResponseDto::toDto));
    }
}
