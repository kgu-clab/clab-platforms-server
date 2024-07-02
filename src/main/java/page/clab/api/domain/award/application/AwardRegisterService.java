package page.clab.api.domain.award.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.award.application.port.in.AwardRegisterUseCase;
import page.clab.api.domain.award.application.port.out.RegisterAwardPort;
import page.clab.api.domain.award.domain.Award;
import page.clab.api.domain.award.dto.request.AwardRequestDto;
import page.clab.api.domain.member.application.MemberLookupUseCase;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class AwardRegisterService implements AwardRegisterUseCase {

    private final MemberLookupUseCase memberLookupUseCase;
    private final ValidationService validationService;
    private final RegisterAwardPort registerAwardPort;

    @Transactional
    @Override
    public Long register(AwardRequestDto requestDto) {
        String currentMemberId = memberLookupUseCase.getCurrentMemberId();
        Award award = AwardRequestDto.toEntity(requestDto, currentMemberId);
        validationService.checkValid(award);
        return registerAwardPort.save(award).getId();
    }
}
