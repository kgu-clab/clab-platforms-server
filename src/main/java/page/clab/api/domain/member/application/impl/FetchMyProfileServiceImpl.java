package page.clab.api.domain.member.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.FetchMyProfileService;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.member.dto.response.MyProfileResponseDto;

@Service
@RequiredArgsConstructor
public class FetchMyProfileServiceImpl implements FetchMyProfileService {

    private final MemberLookupService memberLookupService;

    @Transactional(readOnly = true)
    @Override
    public MyProfileResponseDto fetchMyProfile() {
        Member currentMember = memberLookupService.getCurrentMember();
        return MyProfileResponseDto.toDto(currentMember);
    }
}
