package page.clab.api.domain.award.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.award.application.port.in.RegisterAwardUseCase;
import page.clab.api.domain.award.application.port.out.RegisterAwardPort;
import page.clab.api.domain.award.domain.Award;
import page.clab.api.domain.award.dto.request.AwardRequestDto;
import page.clab.api.domain.member.application.port.in.RetrieveMemberUseCase;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class AwardRegisterService implements RegisterAwardUseCase {

    private final RetrieveMemberUseCase retrieveMemberUseCase;
    private final ValidationService validationService;
    private final RegisterAwardPort registerAwardPort;

    @Transactional
    @Override
    public Long registerAward(AwardRequestDto requestDto) {
        String currentMemberId = retrieveMemberUseCase.getCurrentMemberId();
        Award award = AwardRequestDto.toEntity(requestDto, currentMemberId);
        validationService.checkValid(award);
        return registerAwardPort.save(award).getId();
    }
}
