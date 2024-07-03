package page.clab.api.domain.member.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.port.in.MemberLookupUseCase;
import page.clab.api.domain.member.application.port.in.MyProfileRetrievalUseCase;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.member.dto.response.MyProfileResponseDto;

@Service
@RequiredArgsConstructor
public class MyProfileRetrievalService implements MyProfileRetrievalUseCase {

    private final MemberLookupUseCase memberLookupUseCase;

    @Transactional(readOnly = true)
    @Override
    public MyProfileResponseDto retrieve() {
        Member currentMember = memberLookupUseCase.getCurrentMember();
        return MyProfileResponseDto.toDto(currentMember);
    }
}
