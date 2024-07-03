package page.clab.api.domain.award.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.award.application.port.in.AwardUpdateUseCase;
import page.clab.api.domain.award.application.port.out.LoadAwardPort;
import page.clab.api.domain.award.application.port.out.RegisterAwardPort;
import page.clab.api.domain.award.domain.Award;
import page.clab.api.domain.award.dto.request.AwardUpdateRequestDto;
import page.clab.api.domain.member.application.port.in.MemberInfoRetrievalUseCase;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class AwardUpdateService implements AwardUpdateUseCase {

    private final LoadAwardPort loadAwardPort;
    private final RegisterAwardPort registerAwardPort;
    private final MemberInfoRetrievalUseCase memberInfoRetrievalUseCase;
    private final ValidationService validationService;

    @Transactional
    @Override
    public Long update(Long awardId, AwardUpdateRequestDto requestDto) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMemberInfo = memberInfoRetrievalUseCase.getCurrentMemberDetailedInfo();
        Award award = loadAwardPort.findByIdOrThrow(awardId);
        award.validateAccessPermission(currentMemberInfo);
        award.update(requestDto);
        validationService.checkValid(award);
        return registerAwardPort.save(award).getId();
    }
}
