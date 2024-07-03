package page.clab.api.domain.member.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.port.in.RetrieveMemberUseCase;
import page.clab.api.domain.member.application.port.in.RetrieveMyProfileUseCase;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.member.dto.response.MyProfileResponseDto;

@Service
@RequiredArgsConstructor
public class MyProfileRetrievalService implements RetrieveMyProfileUseCase {

    private final RetrieveMemberUseCase retrieveMemberUseCase;

    @Transactional(readOnly = true)
    @Override
    public MyProfileResponseDto retrieve() {
        Member currentMember = retrieveMemberUseCase.getCurrentMember();
        return MyProfileResponseDto.toDto(currentMember);
    }
}
