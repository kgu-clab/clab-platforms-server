package page.clab.api.domain.member.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.FetchBirthdaysThisMonthService;
import page.clab.api.domain.member.dao.MemberRepository;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.member.dto.response.MemberBirthdayResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class FetchBirthdaysThisMonthServiceImpl implements FetchBirthdaysThisMonthService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<MemberBirthdayResponseDto> fetchBirthdaysThisMonth(int month, Pageable pageable) {
        Page<Member> birthdayMembers = memberRepository.findBirthdaysThisMonth(month, pageable);
        return new PagedResponseDto<>(birthdayMembers.map(MemberBirthdayResponseDto::toDto));
    }
}
