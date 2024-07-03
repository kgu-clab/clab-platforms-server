package page.clab.api.domain.award.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.award.application.port.in.UpdateAwardUseCase;
import page.clab.api.domain.award.application.port.out.RegisterAwardPort;
import page.clab.api.domain.award.application.port.out.RetrieveAwardPort;
import page.clab.api.domain.award.domain.Award;
import page.clab.api.domain.award.dto.request.AwardUpdateRequestDto;
import page.clab.api.domain.member.application.port.in.RetrieveMemberInfoUseCase;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class AwardUpdateService implements UpdateAwardUseCase {

    private final RetrieveAwardPort retrieveAwardPort;
    private final RegisterAwardPort registerAwardPort;
    private final RetrieveMemberInfoUseCase retrieveMemberInfoUseCase;
    private final ValidationService validationService;

    @Transactional
    @Override
    public Long update(Long awardId, AwardUpdateRequestDto requestDto) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMemberInfo = retrieveMemberInfoUseCase.getCurrentMemberDetailedInfo();
        Award award = retrieveAwardPort.findByIdOrThrow(awardId);
        award.validateAccessPermission(currentMemberInfo);
        award.update(requestDto);
        validationService.checkValid(award);
        return registerAwardPort.save(award).getId();
    }
}
